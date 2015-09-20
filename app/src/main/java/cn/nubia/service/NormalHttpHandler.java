package cn.nubia.service;

import android.util.Log;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/15.
 */
public class NormalHttpHandler extends HttpHandler {
    private AsyncGetResult mAsynGetResult;

    public void setAsyncGetResult(AsyncGetResult asyncGetResult){
        mAsynGetResult = asyncGetResult;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        mAsynGetResult.setJSON(response);
        Log.e("jiangyu", "onSuccess : " + response);
        new Thread(mAsynGetResult).start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,Throwable throwable,
                          JSONObject errorResponse) {

        Log.e("jiangyu", "onFailure : ");
    }
}
