package cn.nubia.entity;

import java.io.Serializable;

/**
 * Created by WJ on 2015/9/7.
 */
public class ExamItem extends Item implements Serializable {
    private int mCourseIndex;
    private String mStartTime;
    private String mEndTime;
    private String mLocale;
    private int mExamCredits;
    private long mRecordModifyTime;

    public long getRecordModifyTime() {
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(long mRecordModifyTime) {
        this.mRecordModifyTime = mRecordModifyTime;
    }


    public int getCourseIndex() {
        return mCourseIndex;
    }

    public void setCourseIndex(int mCourseIndex) {
        this.mCourseIndex = mCourseIndex;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        mLocale = locale;
    }

    public int getExamCredits() {
        return mExamCredits;
    }

    public void setExamCredits(int mExamCredits) {
        this.mExamCredits = mExamCredits;
    }

}
