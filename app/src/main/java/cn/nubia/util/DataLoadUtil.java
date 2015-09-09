package cn.nubia.util;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by WJ on 2015/9/9.
 *
 */
public class DataLoadUtil {
    private static LoadViewUtil mLoadViewUtil;
    public static void setLoadViewUtil(LoadViewUtil loadViewUtil){
        mLoadViewUtil = loadViewUtil;
    }
    /**
     *  Get方式请求课程类数据，课程、课时、考试
     * */
    public static void queryClassInfoDataforGet(String url){
        AsyncHttpHelper.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                updateAllData(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADFAILURE);
            }
        });
    }

    /**
     *  Post方式请求课程类数据，课程、课时、考试
     * */
    public static void queryClassInfoDataforPost(String url,RequestParams requestParams){
        AsyncHttpHelper.post(url,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                /**
                 * 在这里修理我
                 * */
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                /**
                 * 在这里修理我
                 * */
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADFAILURE);
            }
        });
    }
}
