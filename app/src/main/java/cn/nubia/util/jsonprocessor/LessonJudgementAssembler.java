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
            int arrayIndex = 0;
            JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
            while (jsonObject != null){
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "judgement":
                        itemList.add(makeLessonJudge(jsonObject.getJSONObject("detail")));
                }
                jsonObject = jsonArray.getJSONObject(++arrayIndex);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LessonJudgement makeLessonJudge(JSONObject jsonObject) throws JSONException {
        LessonJudgement item = new LessonJudgement();

        item.setContentApplicability((float) jsonObject.getDouble("contentapplicability"));
        item.setContentRationality((float) jsonObject.getDouble("contentrationality"));
        item.setDiscussion((float) jsonObject.getDouble("discussion"));
        item.setTimeRationality((float) jsonObject.getDouble("timerationality"));
        item.setContentUnderstanding((float) jsonObject.getDouble("contentunderstanding"));
        item.setExpressionAbility((float) jsonObject.getDouble("expressionability"));
        item.setCommunication((float) jsonObject.getDouble("communication"));
        item.setOrganization((float) jsonObject.getDouble("Organization"));
        item.setComprehensiveEvaluation(jsonObject.getString("comprehensiveevaluation"));
        item.setSuggestion(jsonObject.getString("suggestion"));

        return item;
    }


}
