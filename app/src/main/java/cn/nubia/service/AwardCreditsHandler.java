package cn.nubia.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CourseItem;
import cn.nubia.entity.Item;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class AwardCreditsHandler extends JsonHttpResponseHandler {
    private Context mContext;
    public AwardCreditsHandler(Context context){
        mContext = context;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.d("jiangyu", "awardCreditsHandler success");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,response);
        List<String> resultStr =(List<String>) factoryGenerics.get();
//        EntityFactoryGenerics factoryGenerics =
//                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.COURSE,response);
//        List<Item> resultList = (List<Item>)factoryGenerics.get();
        Log.d("jiangyu", str);
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        Log.d("jiangyu", "awardCreditsHandler failure");
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,errorResponse);
//        String resultStr = factoryGenerics.getResult(errorResponse);
//        factoryGenerics.getUpdate(response,new ArrayList<CourseItem>());

        /**通过广播通知相应activity执行页面更新动作*/
        Intent broadcastIntent = new Intent("cn.nubia.lovetrain.broadcastreceiver.CREDITSAWARD");
        broadcastIntent.putExtra("operateResult",true);
        mContext.sendBroadcast(broadcastIntent);
    }
}
