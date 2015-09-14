package cn.nubia.service;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class PswModifyHandler extends JsonHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.d("jiangyu", "pswModifyHandler success");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA);
        String resultStr = factoryGenerics.getResult(response);
//        factoryGenerics.getUpdate(str,new ArrayList<CourseItem>());
        Log.d("jiangyu", resultStr);
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        Log.d("jiangyu", "pswModifyHandler failure");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA);
        String resultStr = factoryGenerics.getResult(errorResponse);
//        factoryGenerics.getUpdate(str,new ArrayList<CourseItem>());

    }
}
