package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.ExamItem;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class ExamItemAssembler implements IAssemblerGenerics<ExamItem> {

    @Override
    public List<ExamItem> assemble(JSONArray jsonArray) {
        try {
            List<ExamItem> itemList = new ArrayList<ExamItem>();
            for(int arrayIndex=0;arrayIndex<jsonArray.length();arrayIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "exam":
                        itemList.add(makeExam(jsonObject.getJSONObject("detail")));
                }
            }
            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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
//        item.setStartTime(jsonObject.getString("startTime")) ;
//        item.setEndTime(jsonObject.getString("endTime"));
        item.setLocale(jsonObject.getString("location"));
        item.setExamCredits(jsonObject.getInt("examcredits"));

        return item;
    }
}
