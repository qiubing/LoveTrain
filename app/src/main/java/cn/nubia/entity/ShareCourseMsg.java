package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/15.
 */
public class ShareCourseMsg extends Paramable{
    private int mCourseIndex;
    private String mCourseName;
    private String mUserId;
    private String mUserName;
    private String mCourseDescription;
    private String mLocale;
    private int mCourseLevel;
    private long mStartTime;
    private long mEndTime;

    public ShareCourseMsg(){

    }

    public ShareCourseMsg(TechnologyShareCourseItem item){
        mCourseIndex = item.getmCourseIndex();
        mCourseName = item.getmCourseName();
        mUserId = item.getmUserId();
        mUserName = item.getmUserName();
        mCourseDescription = item.getmCourseDescription();
        mLocale = item.getmLocation();
        mCourseLevel = item.getmCourseLevel();
        mStartTime = item.getmStartTime();
        mEndTime = item.getmEndTime();
    }
    public String getUserId(){
        return mUserId;
    }

    public void setUserId(String userId){
        mUserId = userId;
    }

    public String getUserName(){
        return mUserName;
    }

    public void setUserName(String userName){
        mUserName = userName;
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

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public int getCourseLevel() {
        return mCourseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        this.mCourseLevel = courseLevel;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("course_name", mCourseName);
        params.add("course_description", mCourseDescription);
        params.add("course_level", String.valueOf(mCourseLevel));
        params.add("locale", mLocale);
        params.add("start_time", String.valueOf(mStartTime));
        params.add("end_time", String.valueOf(mEndTime));
        params.add("user_id",Constant.user.getUserID());

        return params;
    }
    @Override
    protected RequestParams toUpdateParams(RequestParams params) {
        params.add("course_index", String.valueOf(mCourseIndex));
        params.add("course_name", mCourseName);
        params.add("course_description", mCourseDescription);
        params.add("course_level", String.valueOf(mCourseLevel));
        params.add("locale", mLocale);
        params.add("start_time", String.valueOf(mStartTime));
        params.add("end_time", String.valueOf(mEndTime));
        params.add("user_name",mUserName);

        return params;
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
