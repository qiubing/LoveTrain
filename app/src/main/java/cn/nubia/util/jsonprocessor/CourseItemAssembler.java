package cn.nubia.util.jsonprocessor;

import android.util.Log;

import java.util.List;

import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.UpdateClassListHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class CourseItemAssembler implements AssemblerGenerics<CourseItem> {

    @Override
    public boolean assemble(JSONArray jsonArray, List<CourseItem> list) {
        try {
            int arrayIndex = 0;
            JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
            while (jsonObject != null){
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "course":
                    case "share":
                    case "senior":
                        UpdateClassListHelper.updateDatabyClassType(
                                "course",
                                makeCourse(jsonObject.getJSONObject("detail")),
                                list);
                        break;
                    case "lesson":
                        UpdateClassListHelper.updateDatabyClassType(
                                "lesson",
                                makeLesson(jsonObject.getJSONObject("detail")),
                                list);
                        break;
                }
                jsonObject = jsonArray.getJSONObject(++arrayIndex);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    public CourseItem makeCourse(JSONObject jsonObject) throws JSONException {
        CourseItem item = new CourseItem();

        item.setType(jsonObject.getString("type")) ;
        item.setOperator(jsonObject.getString("operator"));
        item.setIndex(jsonObject.getInt("courseindex"));
        item.setDescription(jsonObject.getString("description"));
        item.setName(jsonObject.getString("name"));

        item.setLessones((short) jsonObject.getInt("lesson"));
        item.setCourseStatus((short) jsonObject.getInt("status"));
        item.setHasExam((short) jsonObject.getInt("hasExam"));
        item.setRecordModifyTime(jsonObject.getLong("recordModifyTime"));
        if(item.getType().equals("sharecourse"))
            item.setShareType((short) jsonObject.getInt(""));
        if(item.getType().equals("senior"))
            item.setCourseCredits(jsonObject.getInt("credits"));
//        item.setJudgeScore((float)jsonObject.getDouble("judgescore"));
//        item.setLessonList(List<LessonItem> mLessonList);

        return item;
    }

    public LessonItem makeLesson(JSONObject jsonObject) throws JSONException {
        LessonItem item = new LessonItem();

        item.setType(jsonObject.getString("type")) ;
        item.setOperator(jsonObject.getString("operator"));
        item.setIndex(jsonObject.getInt("lessonindex"));
        item.setDescription(jsonObject.getString("description"));
        item.setName(jsonObject.getString("name"));

        item.setCourseIndex(jsonObject.getInt("course")) ;
        item.setTeacherID(jsonObject.getString("teacherID"));
        item.setTeacherName(jsonObject.getString("teacherName"));
        item.setStartTime(jsonObject.getString("startTime")) ;
        item.setEndTime(jsonObject.getString("endTime"));
        item.setLocation(jsonObject.getString("location"));
        item.setCheckCredits(jsonObject.getInt("checkcredits"));
        item.setTeacherCredits(jsonObject.getInt("teachercredits"));
        item.setJudgeScore((float) jsonObject.getDouble("judgescore"));
        item.setRecordModifyTime(jsonObject.getLong("recordModifyTime")) ;
//        item.setLessonIndex(jsonObject.getInt("lessonindex"));
//        item.setLessonName(jsonObject.getString("lessonname"));
//        item.setLessonTheme(jsonObject.getString("lessontheme"));

        return item;
    }
}
