package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.LessonJudgementMsg;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class LessonJudgementAssembler implements IAssemblerGenerics<LessonJudgementMsg> {

    @Override
    public List<LessonJudgementMsg> assemble(JSONArray jsonArray) {
        try {
            List<LessonJudgementMsg> itemList = new ArrayList<LessonJudgementMsg>();
            for(int arrayIndex=0;arrayIndex<jsonArray.length();arrayIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "judgement":
                        itemList.add(makeLessonJudge(jsonObject.getJSONObject("detail")));
                }
            }
            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LessonJudgementMsg makeLessonJudge(JSONObject jsonObject) throws JSONException {
        LessonJudgementMsg item = new LessonJudgementMsg();

        item.setLessonIndex(jsonObject.getInt("lesson_index"));
        item.setUserID(jsonObject.getString("user_id"));
        item.setUserName(jsonObject.getString("user_name"));

        item.setContentApplicability((float) jsonObject.getDouble("content_applicability"));
        item.setContentRationality((float) jsonObject.getDouble("content_rationality"));
        item.setDiscussion((float) jsonObject.getDouble("discussion"));
        item.setTimeRationality((float) jsonObject.getDouble("time_rationality"));
        item.setContentUnderstanding((float) jsonObject.getDouble("content_understanding"));
        item.setExpressionAbility((float) jsonObject.getDouble("expression_ability"));
        item.setCommunication((float) jsonObject.getDouble("communication"));
        item.setOrganization((float) jsonObject.getDouble("organization"));
        item.setComprehensiveEvaluation(jsonObject.getString("comprehensive_evaluation"));
        item.setSuggestion(jsonObject.getString("suggestion"));

        item.setRecordModifyTime(jsonObject.getLong("record_modify_time"));

        return item;
    }


}
