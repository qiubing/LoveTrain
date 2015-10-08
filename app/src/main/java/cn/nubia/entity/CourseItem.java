package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class CourseItem extends Item{
    public final static String STATUS = "course_status";
    public final static String HAS_EXAM = "hasExam";
    public final static String COURSE_INDEX = "class_index";
    public final static String CREDITS = "course_credits";
    public final static String RECORD_MODIFY_TIME = "course_record_modify_time";
    public final static String LESSONES_COUNT = "lessones";
    public final static String SHARE_TYPE = "share_type";

    private short mLessones;
    private boolean mHasExam;
    private short mCourseStatus;
//    private float mJudgeScore;
    private int mCourseCredits;
    private long mRecordModifyTime;
    private short mShareType;

    public short getEnrollCredits() {
        return mEnrollCredits;
    }

    public void setEnrollCredits(short mEnrollCredits) {
        this.mEnrollCredits = mEnrollCredits;
    }

    private short mEnrollCredits;
    private List<LessonItem> mLessonList;

    public List<LessonItem> getLessonList() {
        return mLessonList;
    }

    public void setLessonList(List<LessonItem> mLessonList) {
        this.mLessonList = mLessonList;
    }

    public short getShareType() {
        return mShareType;
    }

    public void setShareType(short mShareType) {
        this.mShareType = mShareType;
    }

    public short getLessones() {
        return mLessones;
    }

    public void setLessones(short lessones) {
        this.mLessones = lessones;
    }

    public short getCourseStatus() {
        return mCourseStatus;
    }

    public void setCourseStatus(short courseStatus) {
        this.mCourseStatus = courseStatus;
    }

    public boolean hasExam() {
        return mHasExam;
    }

    public void setHasExam(boolean hasExam) {
        this.mHasExam = hasExam;
    }

//    public float getJudgeScore() {
//        return mJudgeScore;
//    }
//
//    public void setJudgeScore(float judgeScore) {
//        this.mJudgeScore = judgeScore;
//    }

    public int getCourseCredits() {
        return mCourseCredits;
    }

    public void setCourseCredits(int courseCredits) {
        this.mCourseCredits = courseCredits;
    }

    public long getRecordModifyTime() {
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(long recordModifyTime) {
        mRecordModifyTime = recordModifyTime;
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
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if (o != null && o.getClass() == CourseItem.class){
            CourseItem courseItem = (CourseItem) o;
            if(this.getIndex() == courseItem.getIndex())
                return true;
        }
        return false;
    }
}
