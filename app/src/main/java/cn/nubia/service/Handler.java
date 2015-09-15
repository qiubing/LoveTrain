package cn.nubia.service;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by JiangYu on 2015/9/14.
 */
public abstract class Handler extends JsonHttpResponseHandler {
    public abstract void setActivityInter(ActivityInter inter);
}
