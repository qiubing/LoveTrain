package cn.nubia.entity;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class LessonJudgement {
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
}
