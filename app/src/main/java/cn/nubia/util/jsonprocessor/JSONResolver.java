package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class JSONResolver {

    /**当JSON字符串解析成功并且返回数据的code值为0（表示成功时）将该数据中的data部分
     * 作为JSON数组返回;否则返回null*/
    public static JSONArray readArray(String JSONString){
        try {
            JSONObject jsonObject =  new JSONObject(JSONString);
            if (jsonObject.getString("code").equals("0"))
                return jsonObject.getJSONArray("data");
            else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**当JSON字符串解析成功并且返回数据的code值为0（表示成功时）将该数据中的data部分
     * 作为字符串返回;否则返回null*/
    public static String readString(String JSONString){
        try {
            JSONObject jsonObject =  new JSONObject(JSONString);
            if (jsonObject.getString("code").equals("0"))
                return jsonObject.getString("data");
            else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
