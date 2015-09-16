package cn.nubia.activity.client;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.ClientCheckRecordAdapter;
import cn.nubia.entity.CheckRecordItem;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * @ClassName:
 * @Description: TODO
 * @Author: qiubing
 * @Date: 2015/9/2 9:26
 */
public class ClientMyCheckRecordActivity extends Activity {
    private static final String TAG = "MyCheckRecord";
    private ClientCheckRecordAdapter mCheckAdapter;
    private List<CheckRecordItem> mCheckList;//签到记录表
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checked_record);
        initEvents();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_check_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的签到记录");
        mCheckList = new ArrayList<CheckRecordItem>();
        mListView = (ListView) findViewById(R.id.check_detail);
        //请求参数
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("user_id", "001");
        RequestParams request = Utils.toParams(param);
        String url = Constant.BASE_URL ;
        AsyncHttpHelper.post(url, request, mCheckRecordHandler);
    }

    AsyncHttpResponseHandler mCheckRecordHandler = new AsyncHttpResponseHandler(){

        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            Log.e(TAG,"onSuccess");
            String str = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"lesson_name\":\"Java基础一\",\"lesson_id\":\"1\",\"check_time\":1442261016111}," +
                    "{\"lesson_name\":\"Android开发一\",\"lesson_id\":\"2\",\"check_time\":1442261016144}," +
                    "{\"lesson_name\":\"OO思想\",\"lesson_id\":\"3\",\"check_time\":1442261016188}]}";
            try {
                JSONObject result = new JSONObject(str);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.CHECKRECORD, result);
                int code = factoryGenerics.getCode();
                if (code == 0) {
                    mCheckList = (List<CheckRecordItem>) factoryGenerics.get();
                    mCheckAdapter = new ClientCheckRecordAdapter(mCheckList, ClientMyCheckRecordActivity.this);
                    mListView.setAdapter(mCheckAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.e(TAG,"onFailure");
        }
    };

    /*JsonHttpResponseHandler mCheckRecordHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.v("bing", "success");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.v("bing", "failure");
            String str = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"lesson_name\":\"Java基础一\",\"lesson_id\":\"1\",\"check_time\":1442261016111}," +
                    "{\"lesson_name\":\"Android开发一\",\"lesson_id\":\"2\",\"check_time\":1442261016144}," +
                    "{\"lesson_name\":\"OO思想\",\"lesson_id\":\"3\",\"check_time\":1442261016188}]}";
            try {
                JSONObject result = new JSONObject(str);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.CHECKRECORD, result);
                int code = factoryGenerics.getCode();
                if (code == 0) {
                    mCheckList = (List<CheckRecordItem>) factoryGenerics.get();
                    mCheckAdapter = new ClientCheckRecordAdapter(mCheckList, ClientMyCheckRecordActivity.this);
                    mListView = (ListView) findViewById(R.id.check_detail);
                    mListView.setAdapter(mCheckAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };*/

    /**
     * 返回箭头绑定事件，即退出该页面
     *
     * @param view
     */
    public void back(View view) {
        this.finish();
    }
}
