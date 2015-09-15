package cn.nubia.service;

import com.loopj.android.http.JsonHttpResponseHandler;

import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by JiangYu on 2015/9/14.
 */
public abstract class Handler extends JsonHttpResponseHandler {
    public abstract void setActivityInter(ActivityInter inter);
    public abstract void setOperateType(CommunicateService.OperateType type);
    public abstract void initEntityFactory(EntityFactoryGenerics factoryGenerics);
}
