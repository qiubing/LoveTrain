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
        int len = jsonArray.length();
        Item item = null;
        for(int i = 0;i < len; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            String operater = jsonObject.getString("operate");
            JSONObject jsonObjectDetail = jsonObject.getJSONObject("detail");
            switch (type){
                case "course":
                case "senior":
                case "share":
                    item = makeCourse(type,operater,jsonObjectDetail);
                    break;
                case "lesson":
                    item = makeLesson(type,operater,jsonObjectDetail);
                    break;
                default:
                    break;
            }
            UpdateClassListHelper.updateDataByClassType(type, item, courseList);
        }
        binarySort(courseList);
    }

    private static LessonItem makeLesson(String type,String operater,JSONObject jsonObjectDetail) throws JSONException {
        LessonItem lessonItem = new LessonItem();
        lessonItem.setType(type);
        lessonItem.setOperator(operater);
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
        return lessonItem;
    }

    private static CourseItem makeCourse(String courseType,String operater,JSONObject jsonObjectDetail) throws JSONException {
        List<LessonItem> lessonList = new ArrayList<>();
        CourseItem courseItem = new CourseItem();
        courseItem.setLessonList(lessonList);
        courseItem.setType(courseType);
        courseItem.setOperator(operater);
        courseItem.setIndex(jsonObjectDetail.getInt("course_index"));
        courseItem.setName(jsonObjectDetail.getString("course_name"));
        courseItem.setDescription(jsonObjectDetail.getString("course_description"));
        courseItem.setLessones((short) jsonObjectDetail.getInt("lessons"));
        courseItem.setHasExam((short) jsonObjectDetail.getInt("has_exam"));
        courseItem.setRecordModifyTime(jsonObjectDetail.getLong("course_record_modify_time"));
        if (courseType.equals("senior")){
            courseItem.setEnrollCredits(jsonObjectDetail.getInt("enroll_credits"));
        }else if (courseType.equals("share")){
            courseItem.setShareType((short)jsonObjectDetail.getInt("course_level"));
        }
        return courseItem;
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


    /**
     * 根据类型更新数据
     * @param classType 课程类型
     * */
    public static void updateDataByClassType(String classType,Item item, List<CourseItem> list){
        switch (classType){
            case "course":
            case "share":
            case "senior":
                if (item instanceof CourseItem){
                    updateCourseItem(item.getOperator(), (CourseItem) item, list);
                }
                break;
            case "lesson":
                if (item instanceof LessonItem){
                    int index = binarySearch(list,((LessonItem) item).getCourseIndex());
                    CourseItem courseItem = list.get(index);
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
