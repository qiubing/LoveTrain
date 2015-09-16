package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class LessonJudgement extends Paramable{
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
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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
        params.add("ContentApplicability", String.valueOf(mContentApplicability));
        params.add("ContentRationality", String.valueOf(mContentRationality));
        params.add("Discussion", String.valueOf(mDiscussion));
        params.add("TimeRationality", String.valueOf(mTimeRationality));
        params.add("ExpressionAbility", String.valueOf(mExpressionAbility));
        params.add("Communication", String.valueOf(mCommunication));
        params.add("Organization", String.valueOf(mOrganization));
        params.add("ComprehensiveEvaluation", mComprehensiveEvaluation);
        params.add("Suggestion", mSuggestion);
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
