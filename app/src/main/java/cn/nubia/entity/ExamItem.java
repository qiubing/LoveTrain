package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;

/**
 * Created by WJ on 2015/9/7.
 */
public class ExamItem extends Item{
    public final static String EXAM_INDEX = "exam_index";
    public final static String COURSE_INDEX = "class_index";
    public final static String START_TIME = "start_time";
    public final static String END_TIME = "end_time";
    public final static String LOCALE = "locale";
    public final static String EXAM_CREDITS = "exam_credits";
    public final static String ENROLL_USERS = "users";


    private int mCourseIndex;
    private long mStartTime;
    private long mEndTime;
    private String mLocale;
    private int mExamCredits;
    private long mRecordModifyTime;
    private int mErollUsers;  //报名人数

    public int getErollUsers() {
        return mErollUsers;
    }

    public void setErollUsers(int erollUsers) {
        this.mErollUsers = erollUsers;
    }

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

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long mEndTime) {
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

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if (o != null && o.getClass() == ExamItem.class){
            ExamItem examItem = (ExamItem) o;
            if(this.getIndex() == examItem.getIndex())
                return true;
        }
        return false;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toUpdateParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toQueryParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toDeleteParams(RequestParams params) {
        return null;
    }
}
