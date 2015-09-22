package cn.nubia.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.nubia.db.DbUtil;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.Item;
import cn.nubia.entity.LessonItem;

/**
 * Created by WJ on 2015/9/8.
 * 内存中维护课程信息的list更新类
 */
public class UpdateClassListHelper {
    final private static String TAG = "UpdateClassListHelper";
    /**
     * 更新所有的课程类型数据，包括课程，课时更新
     * */
    public static void updateAllClassData(JSONArray jsonArray, List<CourseItem> courseList,String tableName) throws JSONException {
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
                    Log.e("test",""+item.getIndex());
                    break;
                default:
                    break;
            }
            UpdateClassListHelper.updateDataByClassType(type, item, courseList,tableName);
        }
//        binarySort(courseList);
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
        lessonItem.setCheckUsers(jsonObjectDetail.getInt("check_users"));
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
        courseItem.setLessones((short) jsonObjectDetail.getInt("lessones"));
        courseItem.setHasExam(jsonObjectDetail.getBoolean("has_exam"));
        courseItem.setRecordModifyTime(jsonObjectDetail.getLong("course_record_modify_time"));
        if (courseType.equals("senior")){
            courseItem.setEnrollCredits(jsonObjectDetail.has("share")?(short)jsonObjectDetail.getInt("enroll_credits"):20);
        }else if (courseType.equals("share")){
            courseItem.setShareType(jsonObjectDetail.has("share")?(short)jsonObjectDetail.getInt("course_level"):0);
        }
        return courseItem;
    }

    private static ExamItem makeExam(String operater,JSONObject jsonObjectDetail) throws JSONException{
        ExamItem examItem = new ExamItem();
        examItem.setOperator(operater);
//        examItem.setCourseIndex(jsonObjectDetail.getInt("course_index"));
        examItem.setIndex(jsonObjectDetail.getInt("exam_index"));
        examItem.setLocale(jsonObjectDetail.getString("locale"));
        examItem.setStartTime(jsonObjectDetail.getLong("start_time"));
        examItem.setEndTime(jsonObjectDetail.getLong("end_time"));
        examItem.setExamCredits(jsonObjectDetail.getInt("exam_credits"));
        examItem.setName(jsonObjectDetail.getString("exam_name"));
        examItem.setErollUsers(jsonObjectDetail.getInt("users"));
        examItem.setDescription(jsonObjectDetail.getString("exam_description"));
        return examItem;
    }
    /**
     * 更新所有的考试类型数据
     * */
    public static void updateAllExamData(JSONArray jsonArray,List<ExamItem> examList) throws JSONException {
        int len = jsonArray.length();
        ExamItem item ;
        for(int i = 0;i < len; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String operate = jsonObject.getString("operate");
            JSONObject jsonObjectDetail = jsonObject.getJSONObject("detail");
            item = makeExam(operate,jsonObjectDetail);
            updateExamItem(operate, item, examList);
        }
    }

    /**
     * 根据类型更新数据
     * @param classType 课程类型
     * */
    private static void updateDataByClassType(String classType,Item item, List<CourseItem> list,String tableName){
        switch (classType){
            case "course":
            case "share":
            case "senior":
                if (item instanceof CourseItem){
                    updateCourseItem(item.getOperator(), (CourseItem) item, list, tableName);
                }
                break;
            case "lesson":
                if (item instanceof LessonItem){
                    int index = binarySearch(list, ((LessonItem) item).getCourseIndex());
                    if (index >= 0) {
                        CourseItem courseItem = list.get(index);
                        updateLessonItem(item.getOperator(), (LessonItem) item, courseItem.getLessonList());
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * @// TODO: 2015/9/8  在List中更新单个数据
     * binarySearch(list,item),如果没有查找到数据，则返回(-(插入点) - 1)。插入点即第一个大于此键的元素索引
     * @param operate 操作类型
     * @param item  课程内容
     * */
    private static void updateCourseItem(String operate,CourseItem item, List<CourseItem> list,String tableName){
        int listIndex = binarySearch(list, item, false);
        switch (operate){
            case "insert":
                if(listIndex > -1 || item.getName().equals("null"))
                {
                    Log.e(TAG,"插入重复课程！或Name为null");
                    return;
                }else {
                    /***插入数据库**/
                    DbUtil.getInstance(null).insertCourseItem(item, tableName);
                    //如果不存在，返回负值
                    list.add(-(listIndex + 1), item);
                }
                break;
            case "update":
                if (listIndex >= 0){
                     list.set(listIndex, item);
                    /***插入数据库中字段**/
                    DbUtil.getInstance(null).updateCourseItem(item, tableName);
                }
                break;
            case "delete":
                if (listIndex >= 0){
                    list.remove(listIndex);
                    DbUtil.getInstance(null).deleteCourseItem(item, tableName);
                }
                break;
            default:
                break;
        }
    }

    private static void updateLessonItem(String operate,LessonItem item, List<LessonItem> list){
        int listIndex = binarySearch(list, item, true);
        switch (operate){
            case "insert":
                if(listIndex > -1|| item.getIndex() == 0 || item.getName().equals("null"))
                {
                    Log.e(TAG, "插入重复课时！或Name为null或值为零");
                    return;
                }else {
                    //如果不存在，返回负值
                    list.add(-(listIndex+1),item);
                    DbUtil.getInstance(null).insertLessonItem(item);
                }
                break;
            case "update":
                if (listIndex >= 0){
                    list.set(listIndex,item);
                    DbUtil.getInstance(null).updateLessonItem(item);
                }
                break;
            case "delete":
                if (listIndex >= 0){
                    list.remove(listIndex);
                    DbUtil.getInstance(null).deleteLessonItem(item);
                }
                break;
            default:
                break;
        }
    }

    private static void updateExamItem(String operate,ExamItem item, List<ExamItem> list){
        int listIndex = binarySearch(list, item, false);
        switch (operate){
            case "insert":
                if(listIndex > -1 || item.getName().equals("null"))
                {
                    Log.e(TAG,"插入重复课时！或Name为null");
                    return;
                }else {
                    //如果不存在，返回负值
                    list.add(-(listIndex + 1), item);
                    DbUtil.getInstance(null).insertExamItem(item);
                }
                break;
            case "update":
                if (listIndex >= 0){
                    list.set(listIndex, item);
                    DbUtil.getInstance(null).updateExamItem(item);
                }
                break;
            case "delete":
                if (listIndex >= 0){
                    list.remove(listIndex);
                    DbUtil.getInstance(null).deleteExamItem(item);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据课程索引查找，降序排列
     * */
    private static int binarySearch(List<? extends Item> list,int index){
        Item item = new CourseItem();
        item.setIndex(index);
        return binarySearch(list, item, false);
    }

    private static int binarySearch(List<? extends Item> list,Item item, final boolean isAsc){
        //二分查找课程该Item对应的记录，
        return Collections.binarySearch(list, item, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                if (lhs.getIndex() == rhs.getIndex())
                    return 0;
                else if(isAsc){
                    return lhs.getIndex() > rhs.getIndex() ? 1 : -1;
                }else
                    return lhs.getIndex() > rhs.getIndex() ? -1 : 1;
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