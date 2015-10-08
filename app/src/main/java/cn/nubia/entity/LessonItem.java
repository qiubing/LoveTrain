package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by WJ on 2015/9/6.
 */
public class LessonItem extends Item{
    public final static String COURSE_INDEX = "class_index";
    public final static String LESSON_INDEX = "lesson_index";
    public final static String JUDGE_SCORE = "judge_score";
    public final static String TEACHER_ID = "teacher_id";
    public final static String TEACHER_NAME = "teacher_name";
    public final static String START_TIME = "start_time";
    public final static String CHECK_USERS = "check_users";
    public final static String END_TIME = "end_time";
    public final static String LOCALE = "locale";
    public final static String CHECK_CREDITS = "check_credits";
    public final static String TEACHER_CREDITS = "teacher_credits";
    public final static String RECORD_MODIFY_TIME = "lesson_record_modify_time";
    public final static String IS_JUDGED = "is_judged";

    private int mCourseIndex;
    private String mTeacherID;
    private String mTeacherName;
    private long mStartTime;
    private long mEndTime;
    private String mLocale;
    private int mCheckUsers;  // 签到人数
    private int mCheckCredits;  // 签到积分
    private int mTeacherCredits;//讲师上课积分
    private double mJudgeScore;//该课程讲师评价综合得分
    private long mRecordModifyTime;

    public boolean isIsJudged() {
        return mIsJudged;
    }

    public void setIsJudged(boolean mIsJudged) {
        this.mIsJudged = mIsJudged;
    }

    private boolean mIsJudged;


    public int getCheckUsers() {
        return mCheckUsers;
    }

    public void setCheckUsers(int mCheckUsers) {
        this.mCheckUsers = mCheckUsers;
    }

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
