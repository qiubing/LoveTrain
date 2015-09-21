package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.ShareCourseMsg;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 17:03
 */
public class ShareCourseAssembler implements IAssemblerGenerics<ShareCourseMsg> {
    @Override
    public List<ShareCourseMsg> assemble(JSONArray jsonArray) {
        List<ShareCourseMsg> mCourseList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                mCourseList.add(makeShareCourse(obj));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return mCourseList;
    }
    private ShareCourseMsg makeShareCourse(JSONObject jsonObject) throws JSONException {
        ShareCourseMsg course = new ShareCourseMsg();
        course.setCourseName(jsonObject.getString("course_name"));
        course.setCourseIndex(jsonObject.getInt("course_index"));
        course.setCourseDescription(jsonObject.getString("course_description"));
        course.setCourseLevel(jsonObject.getInt("course_level"));
        course.setLocale(jsonObject.getString("locale"));
        course.setStartTime(jsonObject.getLong("start_time"));
        course.setEndTime(jsonObject.getLong("end_time"));
        return course;
    }
}
