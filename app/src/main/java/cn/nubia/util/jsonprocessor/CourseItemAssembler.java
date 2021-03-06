package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CourseItem;
import cn.nubia.entity.Item;
import cn.nubia.entity.LessonItem;


public class CourseItemAssembler implements IAssemblerGenerics<Item> {

    @Override
    public List<Item> assemble(JSONArray jsonArray) {
        try {
            List<Item> itemList = new ArrayList<>();
            for(int arrayIndex=0;arrayIndex<jsonArray.length();arrayIndex++){
                JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "course":
                    case "share":
                    case "senior":
                        itemList.add(makeCourse(jsonObject.getJSONObject("detail")));
                        break;
                    case "lesson":
                        itemList.add(makeLesson(jsonObject.getJSONObject("detail")));
                        break;
                }
            }

            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Item assemble(JSONObject jsonObject) {
        try {
            String objectType = jsonObject.getString("type");
            switch (objectType) {
                case "course":
                case "share":
                case "senior":
                    return makeCourse(jsonObject.getJSONObject("detail"));
                case "lesson":
                    return makeLesson(jsonObject.getJSONObject("detail"));
                default:
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private CourseItem makeCourse(JSONObject jsonObject) throws JSONException {
        CourseItem item = new CourseItem();

        item.setType(jsonObject.getString("type")) ;
        item.setOperator(jsonObject.getString("operator"));
        item.setIndex(jsonObject.getInt("courseindex"));
        item.setDescription(jsonObject.getString("description"));
        item.setName(jsonObject.getString("name"));

        item.setLessones((short) jsonObject.getInt("lesson"));
        item.setCourseStatus((short) jsonObject.getInt("status"));
        item.setHasExam( jsonObject.getBoolean("hasExam"));
        item.setRecordModifyTime(jsonObject.getLong("recordModifyTime"));
        if(item.getType().equals("sharecourse"))
            item.setShareType((short) jsonObject.getInt(""));
        if(item.getType().equals("senior"))
            item.setCourseCredits(jsonObject.getInt("credits"));
//        item.setJudgeScore((float)jsonObject.getDouble("judgescore"));
//        item.setLessonList(List<LessonItem> mLessonList);

        return item;
    }

    private LessonItem makeLesson(JSONObject jsonObject) throws JSONException {
        LessonItem item = new LessonItem();

        item.setType(jsonObject.getString("type")) ;
        item.setOperator(jsonObject.getString("operator"));
        item.setIndex(jsonObject.getInt("lessonindex"));
        item.setDescription(jsonObject.getString("description"));
        item.setName(jsonObject.getString("name"));

        item.setCourseIndex(jsonObject.getInt("course")) ;
        item.setTeacherID(jsonObject.getString("teacherID"));
        item.setTeacherName(jsonObject.getString("teacherName"));
        item.setStartTime(jsonObject.getLong("startTime")) ;
        item.setEndTime(jsonObject.getLong("endTime"));
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
