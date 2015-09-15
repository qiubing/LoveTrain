package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.LessonJudgement;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class LessonJudgementAssembler implements IAssemblerGenerics<LessonJudgement> {

    @Override
    public List<LessonJudgement> assemble(JSONArray jsonArray) {
        try {
            List<LessonJudgement> itemList = new ArrayList<LessonJudgement>();
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

    public LessonJudgement makeLessonJudge(JSONObject jsonObject) throws JSONException {
        LessonJudgement item = new LessonJudgement();

        item.setContentApplicability((float) jsonObject.getDouble("ContentApplicability"));
        item.setContentRationality((float) jsonObject.getDouble("ContentRationality"));
        item.setDiscussion((float) jsonObject.getDouble("Discussion"));
        item.setTimeRationality((float) jsonObject.getDouble("TimeRationality"));
        item.setContentUnderstanding((float) jsonObject.getDouble("ContentUnderstanding"));
        item.setExpressionAbility((float) jsonObject.getDouble("ExpressionAbility"));
        item.setCommunication((float) jsonObject.getDouble("Communication"));
        item.setOrganization((float) jsonObject.getDouble("Organization"));
        item.setComprehensiveEvaluation(jsonObject.getString("ComprehensiveEvaluation"));
        item.setSuggestion(jsonObject.getString("Suggestion"));

        return item;
    }


}
