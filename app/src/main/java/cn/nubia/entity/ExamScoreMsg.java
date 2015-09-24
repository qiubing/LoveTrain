package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/19.
 */
public class ExamScoreMsg extends Paramable {
    private int mExamIndex;
    private String mUserID;
    private String mUserName;
    private float mExamScore;

    public int getExamIndex() {
        return mExamIndex;
    }

    public void setExamIndex(int examIndex) {
        mExamIndex = examIndex;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getUserName(){
        return mUserName;
    }

    public void setUserName(String userName){
        mUserName = userName;
    }

    public float getExamScore() {
        return mExamScore;
    }

    public void setExamScore(float examScore) {
        mExamScore = examScore;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("exam_index", String.valueOf(mExamIndex));
        params.add("user_id", mUserID);
        params.add("exam_score", String.valueOf(mExamScore));
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            //目标为空引用，并不指向对象
            return false;
        }else if(this == obj) {
            //引用同一个对象
            return true;
        }else if(getClass() != obj.getClass() ) {
            //目标与本对象不属于同一个类型
            return false;
        }else{
            ExamScoreMsg other = (ExamScoreMsg)obj;
            if(other.mExamIndex !=this.mExamIndex) {
                return false;
            }else if (!other.mUserID.equals(this.mUserID)) {
                return false;
            }else if(!other.mUserName.equals(this.mUserName)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int  hashCode() {
       return 0;
    }
}
