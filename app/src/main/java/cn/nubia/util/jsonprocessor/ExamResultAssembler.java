package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.ExamResultItem;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 14:43
 */
public class ExamResultAssembler implements IAssemblerGenerics<ExamResultItem> {

    @Override
    public List<ExamResultItem> assemble(JSONArray jsonArray) {
        List<ExamResultItem> mResultList = new ArrayList<ExamResultItem>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                mResultList.add(makeExamResult(obj));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return mResultList;
    }

    private ExamResultItem makeExamResult(JSONObject jsonObject) throws JSONException {
        ExamResultItem result = new ExamResultItem();
        result.setmLessonName(jsonObject.getString("lesson_name"));
        result.setmExamScore(jsonObject.getDouble("exam_score"));
        result.setmLessonIndex(jsonObject.getInt("lesson_index"));
        return result;
    }
}
