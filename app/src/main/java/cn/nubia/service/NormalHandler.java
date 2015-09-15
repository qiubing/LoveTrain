package cn.nubia.service;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by JiangYu on 2015/9/15.
 */
public class NormalHandler extends Handler {
    private ActivityInter mInter;
    private CommunicateService.OperateType mType;
    private EntityFactoryGenerics mFactoryGenerics;

    @Override
    public void setActivityInter(ActivityInter inter) {
        mInter = inter;
    }

    @Override
    public void setOperateType(CommunicateService.OperateType type) {
        mType = type;
    }

    @Override
    public void initEntityFactory(EntityFactoryGenerics factoryGenerics){
        mFactoryGenerics = factoryGenerics;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        mFactoryGenerics.setJSON(response);
//        List<String> resultStr =(List<String>) mFactoryGenerics.get();
        List<String> resultStr = new ArrayList<String>();
        resultStr.add("true");
        mInter.alter(resultStr, mType);
        mInter = null;
    }
    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable,
                          JSONObject errorResponse) {
        mFactoryGenerics.setJSON(errorResponse);
//        List<String> resultStr =(List<String>) mFactoryGenerics.get();
        List<String> resultStr = new ArrayList<String>();
        resultStr.add("true");
        mInter.alter(resultStr,mType);
        mInter = null;
    }




}
