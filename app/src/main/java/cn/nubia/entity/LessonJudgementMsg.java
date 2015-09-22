package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class LessonJudgementMsg extends Paramable{
    private float mContentApplicability;
    private float mContentRationality;
    private float mDiscussion;
    private float mTimeRationality;
    private float mContentUnderstanding;
    private float mExpressionAbility;
    private float mCommunication;
    private float mOrganization;
    private String mComprehensiveEvaluation;
    private String mSuggestion;

    private int mLessonIndex;
    private String mUserID;
    private String mUserName;

    private long mRecordModifyTime;


    public int getLessonIndex(){return mLessonIndex;}

    public void setLessonIndex(int lessonIndex){
        this.mLessonIndex = lessonIndex;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getUserID(){return mUserID;}

    public void setUserID(String userID){
        mUserID = userID;
    }

    public long getRecordModifyTime(){
        return mRecordModifyTime;
    }

    public void setRecordModifyTime(long recordModifyTime){
        mRecordModifyTime = recordModifyTime;
    }

    public float getContentApplicability() {
        return mContentApplicability;
    }

    public void setContentApplicability(float contentApplicability) {
        this.mContentApplicability = contentApplicability;
    }

    public float getContentRationality() {
        return mContentRationality;
    }

    public void setContentRationality(float contentRationality) {
        this.mContentRationality = contentRationality;
    }

    public float getDiscussion() {
        return mDiscussion;
    }

    public void setDiscussion(float discussion) {
        this.mDiscussion = discussion;
    }

    public float getTimeRationality() {
        return mTimeRationality;
    }

    public void setTimeRationality(float timeRationality) {
        this.mTimeRationality = timeRationality;
    }

    public float getContentUnderstanding() {
        return mContentUnderstanding;
    }

    public void setContentUnderstanding(float contentUnderstanding) {
        this.mContentUnderstanding = contentUnderstanding;
    }

    public float getExpressionAbility() {
        return mExpressionAbility;
    }

    public void setExpressionAbility(float expressionAbility) {
        this.mExpressionAbility = expressionAbility;
    }

    public float getCommunication() {
        return mCommunication;
    }

    public void setCommunication(float communication) {
        this.mCommunication = communication;
    }

    public float getOrganization() {
        return mOrganization;
    }

    public void setOrganization(float organization) {
        this.mOrganization = organization;
    }

    public String getComprehensiveEvaluation() {
        return mComprehensiveEvaluation;
    }

    public void setComprehensiveEvaluation(String comprehensiveEvaluation) {
        this.mComprehensiveEvaluation = comprehensiveEvaluation;
    }

    public String getSuggestion() {
        return mSuggestion;
    }

    public void setSuggestion(String suggestion) {
        this.mSuggestion = suggestion;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("lesson_index", String.valueOf(mLessonIndex));
        params.add("content_applicability", String.valueOf(mContentApplicability));
        params.add("content_rationality", String.valueOf(mContentRationality));
        params.add("discussion", String.valueOf(mDiscussion));
        params.add("time_rationality", String.valueOf(mTimeRationality));
        params.add("content_understanding", String.valueOf(mContentUnderstanding));
        params.add("expression_ability", String.valueOf(mExpressionAbility));
        params.add("communication", String.valueOf(mCommunication));
        params.add("organization", String.valueOf(mOrganization));
        params.add("comprehensive_evaluation", mComprehensiveEvaluation);
        params.add("suggestion", mSuggestion);
        params.add("user_id",Constant.user.getUserID());

        return params;
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
