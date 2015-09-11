package cn.nubia.util.jsonprocessor;

import java.util.List;

import cn.nubia.entity.ExamItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class ExamItemAssembler implements AssemblerGenerics<ExamItem> {

    @Override
    public boolean assemble(JSONArray jsonArray, List<ExamItem> examList) {
        try {
            int arrayIndex = 0;
            JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
            while (jsonObject != null){
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "exam":
                        examList.add(makeExam(jsonObject.getJSONObject("detail")));
                }
                jsonObject = jsonArray.getJSONObject(++arrayIndex);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ExamItem makeExam(JSONObject jsonObject) throws JSONException {
        ExamItem item = new ExamItem();

        item.setType(jsonObject.getString("type")) ;
        item.setOperator(jsonObject.getString("operator"));
        item.setIndex(jsonObject.getInt("examindex"));
        item.setDescription(jsonObject.getString("description"));
        item.setName(jsonObject.getString("examname"));

        item.setRecordModifyTime(jsonObject.getLong("recordModifyTime"));
        item.setCourseIndex(jsonObject.getInt("courseindex"));
        item.setStartTime(jsonObject.getString("startTime")) ;
        item.setEndTime(jsonObject.getString("endTime"));
        item.setLocale(jsonObject.getString("location"));
        item.setExamCredits(jsonObject.getInt("examcredits"));

        return item;
    }


}
