package cn.nubia.util.jsonprocessor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CheckRecordItem;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/14 19:05
 */
public class CheckRecordAssembler implements IAssemblerGenerics<CheckRecordItem> {
    @Override
    public List<CheckRecordItem> assemble(JSONArray jsonArray) {
        List<CheckRecordItem> itemList = new ArrayList<>();

        for(int i =0;i<jsonArray.length();i++){
            JSONObject jsonObject ;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                itemList.add(makeCheckRecord(jsonObject));
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
            Log.e("qiubing",String.valueOf(itemList.size()));
        }

        return itemList;
    }

    @Override
    public CheckRecordItem assemble(JSONObject jsonObject) {
        try {
            return makeCheckRecord(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CheckRecordItem makeCheckRecord(JSONObject jsonObject) throws JSONException {
        CheckRecordItem check = new CheckRecordItem();
        check.setmLessonName(jsonObject.getString("lesson_name"));
        check.setmCheckTime(jsonObject.getLong("check_time"));
        return check;
    }
}
