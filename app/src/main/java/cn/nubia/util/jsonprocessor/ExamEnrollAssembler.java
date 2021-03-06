package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.ExamScoreMsg;

/**
 * Created by JiangYu on 2015/9/19.
 */
public class ExamEnrollAssembler implements IAssemblerGenerics<ExamScoreMsg> {

    @Override
    public List<ExamScoreMsg> assemble(JSONArray jsonArray) {
        try {
            List<ExamScoreMsg> itemList = new ArrayList<ExamScoreMsg>();
            for(int arrayIndex=0;arrayIndex<jsonArray.length();arrayIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
                itemList.add(makeExamScoreMsg(jsonObject.getJSONObject("detail")));
            }
            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ExamScoreMsg assemble(JSONObject jsonObject) {
        try {
            return makeExamScoreMsg(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ExamScoreMsg makeExamScoreMsg(JSONObject jsonObject) throws JSONException {
        ExamScoreMsg item = new ExamScoreMsg();

        item.setUserName(jsonObject.getString("user_name"));
        item.setUserID(jsonObject.getString("user_id"));
        item.setExamScore((float) jsonObject.getDouble("exam_score"));
        item.setExamIndex(jsonObject.getInt("exam_index"));

        return item;
    }
}
