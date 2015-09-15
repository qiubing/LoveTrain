package cn.nubia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class CourseItem extends Item implements Serializable{
//    public static String TYPE = "type";
    public static String SHARETYPE = "share_type";
    public static String STATUS = "course_status";
    public static String HASEXAM = "hasExam";
    public static String CREDITS = "course_credits";
    public static String RECORD_MODIFY_TIME = "course_record_modify_time";
//    public static String COURSE_INDEX = "course_index";

    private short mLessones;
    private short mCourseStatus;
    private boolean mHasExam;
//    private float mJudgeScore;
    private int mCourseCredits;
    private short mIsDelete;
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

}
