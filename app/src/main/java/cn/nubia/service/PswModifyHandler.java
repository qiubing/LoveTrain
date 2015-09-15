package cn.nubia.service;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class PswModifyHandler extends Handler{
    private ActivityInter mInter;

    @Override
    public void setActivityInter(ActivityInter inter) {
        mInter = inter;
    }
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,response);
//        List<String> resultStr =(List<String>) factoryGenerics.get();
        List<String> resultStr = new ArrayList<String>();
        resultStr.add("true");
        mInter.alter(resultStr,CommunicateService.OperateType.UPDATE);
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        String str = "{code:0,data:[{type:course},{type:lesson}]}";
        EntityFactoryGenerics factoryGenerics =
                new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SIMPLEDATA,errorResponse);
//        List<String> resultStr =(List<String>) factoryGenerics.get();
        List<String> resultStr = new ArrayList<String>();
        resultStr.add("true");
        mInter.alter(resultStr,CommunicateService.OperateType.UPDATE);
    }
}
