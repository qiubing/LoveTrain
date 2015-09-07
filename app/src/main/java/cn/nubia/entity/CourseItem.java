package cn.nubia.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class CourseItem implements Serializable{
    public static String NAME = "course_name";
    public static String DESCRIPTION = "course_description";
    public static String TYPE = "type";
    public static String SHARETYPE = "share_type";
    public static String STATUS = "course_status";
    public static String HASEXAM = "hasExam";
    public static String CREDITS = "course_credits";
    public static String RECORD_MODIFY_TIME = "course_record_modify_time";

    public static String COURSE_INDEX = "course_index";
    private int mCourseIndex;
    private String mCourseName;
    private String mCourseDescription;
    private short mType;
    private short mLessones;
    private short mCourseStatus;
    private short mHasExam;
//    private float mJudgeScore;
    private int mCourseCredits;
    private short mIsDelete;
    private long mRecordModifyTime;
    private short mShareType;

    public short getShareType() {
        return mShareType;
    }

    public void setShareType(short mShareType) {
        this.mShareType = mShareType;
    }

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

    public short getType() {
        return mType;
    }

    public void setType(short type) {
        this.mType = type;
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

    public short hasExam() {
        return mHasExam;
    }

    public void setHasExam(short hasExam) {
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

    public short isDelete() {
        return mIsDelete;
    }

    public void setIsDelete(short isDelete) {
        this.mIsDelete = isDelete;
    }

    public long getRecordModifyTime() {
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(long recordModifyTime) {
        mRecordModifyTime = recordModifyTime;
    }
}
