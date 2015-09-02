package cn.nubia.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class Course implements Serializable{
    private int mCourseIndex;
    private String mCourseName;
    private String mCourseDescription;
    private short mType;
    private int mLessones;
    private short mCourseStatus;
    private boolean mHasExam;
    private float mJudgeScore;
    private int mCourseCredits;
    private boolean mIsDelete;
    private Timestamp mRecordModifyTime;

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

    public int getLessones() {
        return mLessones;
    }

    public void setLessones(int lessones) {
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

    public float getJudgeScore() {
        return mJudgeScore;
    }

    public void setJudgeScore(float judgeScore) {
        this.mJudgeScore = judgeScore;
    }

    public int getCourseCredits() {
        return mCourseCredits;
    }

    public void setCourseCredits(int courseCredits) {
        this.mCourseCredits = courseCredits;
    }

    public boolean isDelete() {
        return mIsDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.mIsDelete = isDelete;
    }

    public Timestamp getRecordModifyTime() {
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(Timestamp recordModifyTime) {
        mRecordModifyTime = recordModifyTime;
    }
}
