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
        if(response!=null) {
            mAsynGetResult.setJSON(response);
            Log.e("jiangyu", "success " + response.toString());
            mAsynGetResult.run();
        }
//        new Thread(mAsynGetResult).start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,Throwable throwable,
                          JSONObject errorResponse) {
//        new Thread(mAsynGetResult).start();
        /**没有连接网络成功也要返回一个信息，已解除界面的按钮锁定*/
        Log.e("jiangyu", "failure");
        mAsynGetResult.run();
    }
}
