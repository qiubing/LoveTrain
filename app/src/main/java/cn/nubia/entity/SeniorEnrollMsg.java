package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/24.
 */
public class SeniorEnrollMsg extends Paramable {
    private String mUserID;
    private int mCourseIndex;

    public String getUserID(){
        return mUserID;
    }

    public void setUserID(String userID){
        mUserID = userID;
    }

    public int getCourseIndex(){
        return mCourseIndex;
    }

    public void setCourseIndex(int courseIndex){
        mCourseIndex = courseIndex;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("course_index", String.valueOf(mCourseIndex));
        params.add("user_id", mUserID);

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
