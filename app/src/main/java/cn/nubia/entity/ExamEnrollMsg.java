package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/22.
 */
public class ExamEnrollMsg extends Paramable {
    private String mUserID;
    private int mExamIndex;

    public String getUserID(){
        return mUserID;
    }

    public void setUserID(String userID){
        mUserID = userID;
    }

    public void setExamIndex(int examIndex){
        mExamIndex = examIndex;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("exam_index", String.valueOf(mExamIndex));
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
