package cn.nubia.service;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.nubia.entity.CourseItem;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class AwardCreditsHandler extends JsonHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.d("jiangyu", "awardCreditsHandler success");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA);
//        String resultStr = factoryGenerics.getResult(response);
        factoryGenerics.getUpdate(response,new ArrayList<CourseItem>());
        Log.d("jiangyu", str);
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        Log.d("jiangyu", "awardCreditsHandler failure");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA);
        String resultStr = factoryGenerics.getResult(errorResponse);
//        factoryGenerics.getUpdate(response,new ArrayList<CourseItem>());

    }
}
