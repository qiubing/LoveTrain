package cn.nubia.entity;

import java.io.Serializable;

/**
 * Created by WJ on 2015/9/6.
 */
public class LessonItem extends Item implements Serializable {
//    public static String LESSON_INDEX = "lesson_index";
    public static String COURSE_INDEX = "course_index";
//    public static String LESSON_NAME = "lesson_name";
    public static String JUDGE_SCORE = "judge_score";
    public static String TEACHER_ID = "teacher_id";
    public static String TEACHER_NAME = "lesson_theme";
    public static String START_TIME = "start_time";
    public static String END_TIME = "end_time";
    public static String LOCALE = "locale";
    public static String CHECK_CREDITS = "check_credits";
    public static String TEACHER_CREDITS = "teacher_credits";
//    public static String LESSON_THEME = "lesson_theme";

//    private int mLessonIndex;
    private String mLessonName;
    private int mCourseIndex;
    private String mTeacherID;
    private String mTeacherName;
//    private String mLessonTheme;
    private long mStartTime;
    private long mEndTime;
    private String mLocale;
    private int mCheckCredits;  // 签到积分
    private int mTeacherCredits;//讲师上课积分
    private double mJudgeScore;//该课程讲师评价综合得分
    private long mRecordModifyTime;

    public long getRecordModifyTime() {
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(long mRecordModifyTime) {
        this.mRecordModifyTime = mRecordModifyTime;
    }

    public String getTeacherName() {
        return mTeacherName;
    }

    public void setTeacherName(String mTeacherName) {
        this.mTeacherName = mTeacherName;
    }

//    public int getLessonIndex() {
//        return mLessonIndex;
////    }
//
//    public void setLessonIndex(int mLessonIndex) {
//        this.mLessonIndex = mLessonIndex;
//    }
//
    public String getLessonName() {
        return mLessonName;
    }
//
//    public void setLessonName(String mLessonName) {
//        this.mLessonName = mLessonName;
//    }

    public int getCourseIndex() {
        return mCourseIndex;
    }

    public void setCourseIndex(int mCourseIndex) {
        this.mCourseIndex = mCourseIndex;
    }

    public String getTeacherID() {
        return mTeacherID;
    }

    public void setTeacherID(String mUserID) {
        this.mTeacherID = mUserID;
    }
//
//    public String getLessonTheme() {
//        return mLessonTheme;
//    }
//
//    public void setLessonTheme(String mLessonTheme) {
//        this.mLessonTheme = mLessonTheme;
//    }

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

    public String getLocation() {
        return mLocale;
    }

    public void setLocation(String mLocation) {
        this.mLocale = mLocation;
    }

    public int getCheckCredits() {
        return mCheckCredits;
    }

    public void setCheckCredits(int mCheckCredits) {
        this.mCheckCredits = mCheckCredits;
    }

    public int getTeacherCredits() {
        return mTeacherCredits;
    }

    public void setTeacherCredits(int mTeacherCredits) {
        this.mTeacherCredits = mTeacherCredits;
    }

    public double getJudgeScore() {
        return mJudgeScore;
    }

    public void setJudgeScore(double mJudgeScore) {
        this.mJudgeScore = mJudgeScore;
    }
}
