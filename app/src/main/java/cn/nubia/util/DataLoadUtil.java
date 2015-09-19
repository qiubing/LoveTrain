package cn.nubia.util;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import cn.nubia.entity.Constant;


import cn.nubia.entity.Constant;


import cn.nubia.entity.Constant;

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
                mLoadViewUtil.setNetworkFailedViewVisible(false);

//                updateAllData(response);
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                mLoadViewUtil.setNetworkFailedViewVisible(true);
                mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
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
                 mLoadViewUtil.showShortToast("很抱歉\\n数据加载失败啦");
//               mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADFAILURE);
            }
        });
    }
}
