package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.Paramable;

/**
 * Created by JiangYu on 2015/9/10.
 */
class JSONResolver {
    private final static String UNHANDLE_ERR = "未知错误";
    /**获得服务器端返回的JSON对象的code值*/
    public static int readCode(JSONObject jsonObject){
        try {
            return jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**获得服务器端返回的JSON对象的message值*/
    public static String readMessage(JSONObject jsonObject){
        try {
            return jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return UNHANDLE_ERR;
        }
    }

    /**已数组方式获得服务器返回的JSON对象中包含的实体*/
    public static List<? extends Paramable> readAsList(JSONObject jsonObject,IAssemblerGenerics assembler){
        JSONArray array = jsonObject.optJSONArray("data");
        if(array!=null) {
            //noinspection unchecked
            return assembler.assemble(array);
        }else {
            JSONObject object = jsonObject.optJSONObject("data");
            if(object!=null){
                List<Paramable> list = new ArrayList<Paramable>();
                list.add(assembler.assemble(object));
                return list;
            }
        }
        return null;
    }

    /**获得服务器返回的JSON对象中包含的操作结果*/
    public static List<String> readOperateReult(JSONObject jsonObject){
        try {
            List<String> result = new ArrayList<>();
            result.add(jsonObject.getString("data"));
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
