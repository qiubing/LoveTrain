package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CourseIntegrationItem;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 11:10
 */
public class CourseIntegrationAssembler implements IAssemblerGenerics<CourseIntegrationItem> {
    @Override
    public List<CourseIntegrationItem> assemble(JSONArray jsonArray) {
        List<CourseIntegrationItem> mIntegartionList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                mIntegartionList.add(makeCourseIntegration(obj));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return mIntegartionList;
    }

    private CourseIntegrationItem makeCourseIntegration(JSONObject jsonObject) throws JSONException {
        CourseIntegrationItem integration = new CourseIntegrationItem();
        integration.setmLessonName(jsonObject.getString("lesson_name"));
        integration.setmAcquireTime(jsonObject.getLong("achieve_time"));
        integration.setmCheckCredits(jsonObject.getInt("check_credits"));
        integration.setmCause(jsonObject.getString("cause"));
        integration.setmLessonIndex(jsonObject.getInt("lesson_index"));
        return integration;
    }
}
