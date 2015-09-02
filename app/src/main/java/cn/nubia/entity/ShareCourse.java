package cn.nubia.entity;

import java.util.Date;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class ShareCourse extends Course {
    private short mCourseLevel;
    private String mLocale;
    private Date mStartTime;

    public short getCourseLevel() {
        return mCourseLevel;
    }

    public void setCourseLevel(short courseLevel) {
        this.mCourseLevel = courseLevel;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        this.mStartTime = startTime;
    }
}
