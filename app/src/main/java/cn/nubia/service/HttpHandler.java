package cn.nubia.service;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by JiangYu on 2015/9/14.
 */
public abstract class HttpHandler extends JsonHttpResponseHandler {
    public abstract void setAsyncGetResult(AsyncGetResult asyncGetResult);

}
