package cn.nubia.entity;

import java.io.Serializable;

/**
 * @Description: 技术分享课程，包含讲师姓名和工号
 * @Author: qiubing
 * @Date: 2015/9/15 19:01
 */
public class TechnologyShareCourseItem implements Serializable{
    private int mCourseIndex;
    private String mCourseName;
    private String mUserId;
    private String mUserName;
    private String mLocation;
    private long mStartTime;
    private long mEndTime;
    private String mCourseDescription;
    private int mCourseLevel;

    public int getmCourseIndex() {
        return mCourseIndex;
    }

    public void setmCourseIndex(int mCourseIndex) {
        this.mCourseIndex = mCourseIndex;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public void setmCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmCourseDescription() {
        return mCourseDescription;
    }

    public void setmCourseDescription(String mCourseDescription) {
        this.mCourseDescription = mCourseDescription;
    }

    public int getmCourseLevel() {
        return mCourseLevel;
    }

    public void setmCourseLevel(int mCourseLevel) {
        this.mCourseLevel = mCourseLevel;
    }
}
