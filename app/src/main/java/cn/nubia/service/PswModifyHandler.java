package cn.nubia.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

import cn.nubia.entity.PswModifyMsg;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class PswModifyHandler extends JsonHttpResponseHandler {
    private Context mContext;
    public PswModifyHandler(Context context){
        mContext = context;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.d("jiangyu", "pswModifyHandler success");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,response);
        List<String> resultStr =(List<String>) factoryGenerics.get();
        Log.d("jiangyu", resultStr.get(0));
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        Log.d("jiangyu", "pswModifyHandler failure");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,errorResponse);
        List<String> resultStr =(List<String>) factoryGenerics.get();

    }
}
