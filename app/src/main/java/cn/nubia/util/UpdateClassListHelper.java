package cn.nubia.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.Item;
import cn.nubia.entity.LessonItem;

/**
 * Created by WJ on 2015/9/8.
 * 内存中维护课程信息的list更新类
 */
public class UpdateClassListHelper {
    final private static String TAG = "DataLoadUtil";

    /**
     * 更新所有的课程类型数据，包括课程，课时更新
     * */
    public static void updateAllClassData(JSONArray jsonArray, List<CourseItem> courseList) throws JSONException {
        Log.e("updateAllClassData","updateAllClassData");
        int len = jsonArray.length();
        Log.e("updateAllClassData","updateAllClassData"+len);
        for(int i = 0;i < len; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String type = jsonObject.getString("type");

            JSONObject jsonObjectDetail = jsonObject.getJSONObject("detail");

            Log.e("updateAllClassData",jsonObjectDetail.toString());
            switch (type){
                case "course":
                    Log.e("updateAllClassData", type);
                    List<LessonItem> lessonList = new ArrayList<>();
                    CourseItem courseItem = new CourseItem();
                    courseItem.setLessonList(lessonList);
                    courseItem.setType(type);
                    courseItem.setOperator(jsonObject.getString("operate"));
                    courseItem.setIndex(jsonObjectDetail.getInt("course_index"));
                    courseItem.setName(jsonObjectDetail.getString("course_name"));
                    courseItem.setDescription(jsonObjectDetail.getString("course_description"));
                    courseItem.setLessones((short) jsonObjectDetail.getInt("lessons"));
                    courseItem.setHasExam((short) jsonObjectDetail.getInt("has_exam"));
                    courseItem.setRecordModifyTime(jsonObjectDetail.getLong("course_record_modify_time"));
                    UpdateClassListHelper.updateDatabyClassType(type, courseItem, courseList);
                    Log.e("updateAllClassData", type);
                    break;
                case "lesson":
                    Log.e("updateAllClassData", type);
                    LessonItem lessonItem = new LessonItem();
                    lessonItem.setType(type);
                    lessonItem.setOperator(jsonObject.getString("operate"));
                    lessonItem.setIndex(jsonObjectDetail.getInt("lesson_index"));
                    lessonItem.setName(jsonObjectDetail.getString("lesson_name"));
                    lessonItem.setDescription(jsonObjectDetail.getString("lesson_description"));
                    lessonItem.setCourseIndex(jsonObjectDetail.getInt("course_index"));
                    lessonItem.setTeacherID(jsonObjectDetail.getString("user_id"));
                    lessonItem.setTeacherName(jsonObjectDetail.getString("teacher_name"));
                    lessonItem.setJudgeScore(jsonObjectDetail.getDouble("judge_score"));
                    lessonItem.setStartTime(jsonObjectDetail.getLong("start_time"));
                    lessonItem.setEndTime(jsonObjectDetail.getLong("start_time"));
                    lessonItem.setTeacherCredits(jsonObjectDetail.getInt("teacher_credits"));
                    lessonItem.setCheckCredits(jsonObjectDetail.getInt("check_credits"));
                    lessonItem.setRecordModifyTime(jsonObjectDetail.getLong("lesson_record_modify_time"));
                    UpdateClassListHelper.updateDatabyClassType(type, lessonItem, courseList);
                    break;
            }
        }
        binarySort(courseList);

    }

    /**
     * 更新所有的考试类型数据
     * */
    public static void updateAllExamData(JSONObject jsonObject,List<ExamItem> examList){
        /**修理我*/
//        parseJson(jsonObject);
        ExamItem examItem = new ExamItem();
        updateExamItem(examItem.getOperator(),examItem,examList);
        binarySort(examList);
    }

/*
    *//**
     * 更新所有的数据
     * *//*
    public void updateAllData(JSONObject jsonObject){

    }*/

    /**
     * 根据类型更新数据
     * @param classType 课程类型
     * */
    public static void updateDatabyClassType(String classType,Item item, List<CourseItem> list){
        Log.e("updateDatabyClassType",classType);
        switch (classType){
            case "course":
                if (item instanceof CourseItem){
                    updateCourseItem(item.getOperator(), (CourseItem) item, list);
                }
                break;
            case "lesson":
                if (item instanceof LessonItem){
                    int index = binarySearch(list,((LessonItem) item).getCourseIndex());
                    CourseItem courseItem = list.get(index);
                    Log.e("updateDatabyClassType",""+index);
                    if (index >= 0) {

                        updateLessonItem(item.getOperator(), (LessonItem) item, courseItem.getLessonList());
                    }
                }
                break;
            default:
                break;
        }
        Log.e("updateDatabyClassType",list.toString());
    }

    /**
     * @// TODO: 2015/9/8  在List中更新单个数据
     * @param operate 操作类型
     * @param item  课程内容
     * */
    private static void updateCourseItem(String operate,CourseItem item, List<CourseItem> list){
        Log.e("updateCourseItem",list.toString());
        int listIndex = binarySearch(list,item);
        switch (operate){
            case "insert":
                if(listIndex >= 0)
                {
                    Log.e(TAG,"插入重复课程！");
                    break;
                }
                Log.e("updateCourseItem",""+item.getName()+(-(listIndex+1)));

                  //如果不存在，返回负值
                list.add(-(listIndex+1),item);
                break;
            case "update":
                if (listIndex >= 0)
                    list.set(listIndex, item);
                break;
            case "delete":
                if (listIndex >= 0)
                    list.remove(listIndex);
                break;
            default:
                break;
        }
    }

    private static void updateLessonItem(String operate,LessonItem item, List<LessonItem> list){
        Log.e("updateLessonItem list",list.toString());
        int listIndex = binarySearch(list,item);
        switch (operate){
            case "insert":
                if(listIndex >= 0)
                {
                    Log.e(TAG,"插入重复课时！");
                    break;
                }
                //如果不存在，返回负值
                list.add(-(listIndex+1),item);
                break;
            case "update":
                if (listIndex >= 0)
                    list.set(listIndex,item);
                break;
            case "delete":
                if (listIndex >= 0)
                    list.remove(listIndex);
                break;
            default:
                break;
        }
    }

    private static void updateExamItem(String operate,ExamItem item, List<ExamItem> list){
        int listIndex = binarySearch(list,item);
        switch (operate){
            case "insert":
                if(listIndex >= 0)
                {
                    Log.e(TAG,"插入重复课时！");
                    break;
                }
                //如果不存在，返回负值
                list.add(-(listIndex+1),item);
                break;
            case "update":
                if (listIndex >= 0)
                    list.set(listIndex,item);
                break;
            case "delete":
                if (listIndex >= 0)
                    list.remove(listIndex);
                break;
            default:
                break;
        }
    }

    /**
     * 根据课程索引查找
     * */
    public static int binarySearch(List<? extends Item> list,int index){
        Log.e("binarySearch",""+index+list.toString());
        Item item = new CourseItem();
        item.setIndex(index);
        return binarySearch(list, item);
    }

    public static int binarySearch(List<? extends Item> list,Item item){
        Log.e("binarySearch list",list.toString());
        //二分查找课程该Item对应的记录，
        return Collections.binarySearch(list, item, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                Log.e("binarySearch","lhs"+lhs.toString()+"rhs"+rhs.toString());
                if (lhs.getIndex() == rhs.getIndex())
                    return 0;
                else
                    return lhs.getIndex() > rhs.getIndex() ? 1 : -1;
            }
        });
    }

    /**
     * 排序
     * */
    public static void binarySort(List<? extends Item> list){
        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                if (lhs.getIndex() == rhs.getIndex())
                    return 0;
                else
                    return lhs.getIndex() <= rhs.getIndex() ? 1 : -1;
            }
        });
    }

    /**
     * 对每一项排序，包括课程中的课时
     * */
    public static void binarySortforAll(List<CourseItem> list){
        for(CourseItem item : list){
            Collections.sort(item.getLessonList(), new Comparator<Item>() {
                @Override
                public int compare(Item lhs, Item rhs) {
                    if (lhs.getIndex() == rhs.getIndex())
                        return 0;
                    else
                        return lhs.getIndex() <= rhs.getIndex() ? 1 : -1;
                }
            });
        }
        //排序
        Collections.sort(list, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                if (lhs.getIndex() == rhs.getIndex())
                    return 0;
                else
                    return lhs.getIndex() <= rhs.getIndex() ? 1 : -1;
            }
        });
    }

}
