package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/15.
 */
public class ShareCourseItem extends Paramable{
    private int mCourseIndex;
    private String mCourseName;
    private String mCourseDescription;
    private String mLocale;
    private int mCourseLevel;
    private long mStartTime;
    private long mEndTime;

    public int getCourseIndex() {
        return mCourseIndex;
    }

    public void setCourseIndex(int courseIndex) {
        this.mCourseIndex = courseIndex;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        this.mCourseName = courseName;
    }

    public String getCourseDescription() {
        return mCourseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.mCourseDescription = courseDescription;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public int getCourseLevel() {
        return mCourseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        this.mCourseLevel = courseLevel;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    @Override
    public RequestParams toInsertParams() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("course_name",mCourseName);
        param.put("course_description",mCourseDescription);
        param.put("type",String.valueOf(2));
        param.put("course_level",String.valueOf(mCourseLevel));
        param.put("locale",mLocale);
        param.put("start_time",String.valueOf(mStartTime));
        param.put("end_time",String.valueOf(mEndTime));

        return new RequestParams(param);
    }
    @Override
    public RequestParams toUpdateParams() {
        return null;
    }
    @Override
    public RequestParams toQueryParams() {
        return null;
    }
    @Override
    public RequestParams toDeleteParams() {
        return null;
    }
}
