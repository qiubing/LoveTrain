package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by JiangYu on 2015/10/22.
 */
public class ExamScoreListMsg extends Paramable {
    private List<ExamScoreMsg> mExamScoreList;

    public void setExamScoreList(List<ExamScoreMsg> examScoreList){
        mExamScoreList = examScoreList;
    }

    public List<ExamScoreMsg> getExamScoreList(){
        return mExamScoreList;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        if(mExamScoreList.size()>0){
            String examIndex = null;
            String userId = "";
            String examScore = "";

            for(ExamScoreMsg msg : mExamScoreList){
                examIndex = String.valueOf(msg.getExamIndex());
                if(!userId.equals("")){
                    userId += ",";
                }
                userId += msg.getUserID();
                if(!examScore.equals("")){
                    examScore += ",";
                }
                examScore += msg.getExamScore();
            }

            params.add("exam_index", examIndex);
            params.add("user_id",userId);
            params.add("exam_score", examScore);
            return params;
        }
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
