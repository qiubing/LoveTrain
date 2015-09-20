package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Created by JiangYu on 2015/9/19.
 */
public class ExamMsg extends Paramable {
    private int mExamIndex;

    public int getExamIndex(){
        return mExamIndex;
    }

    public void setExamIndex(int examIndex){
        mExamIndex = examIndex;
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
        params.add("exam_index", String.valueOf(mExamIndex));
        return params;
    }

    @Override
    protected RequestParams toDeleteParams(RequestParams params) {
        return null;
    }
}
