package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.ClientCourseIntegrationAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseIntegrationItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Author: qiubing
 * Date: 2015/9/2 9:32
 */
public class ClientCourseIntegrationRecordActivity extends Activity {
    private static final String TAG = "CourseIntegrationRecord";
    private TextView mScoreText;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_integration_record);
        initEvents();
    }

    private void initEvents(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_course_integration_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        mScoreText = (TextView) findViewById(R.id.show_total_course_integration);
        text.setText("课程积分记录");
        mListView = (ListView) findViewById(R.id.course_integration_detail);

        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id",Constant.user.getUserID());
        String url = Constant.BASE_URL + "credit/find_lesson_credits.do";
        AsyncHttpHelper.post(url,params,mClientCourseIntegrationHandler);
    }

    private MyJsonHttpResponseHandler mClientCourseIntegrationHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e(TAG, "onSuccess: " + response.toString());
            EntityFactoryGenerics factoryGenerics =
                    new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.COURSEINTEGRATION, response);
            int code = factoryGenerics.getCode();
            //成功返回
            if (code == 0){
                List<CourseIntegrationItem> mIntegrationList = (List<CourseIntegrationItem>) factoryGenerics.get();
                ClientCourseIntegrationAdapter mAdapter = new ClientCourseIntegrationAdapter(mIntegrationList, ClientCourseIntegrationRecordActivity.this);
                mListView.setAdapter(mAdapter);
                Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                mScoreText.setVisibility(View.VISIBLE);
                mScoreText.setText("截止到当前，您的积分总分为" + getTotalScore(mIntegrationList) + "分");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e(TAG, "onFailure:" + throwable.toString());
        }
    };
    /*AsyncHttpResponseHandler mClientCourseIntegrationHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            Log.e(TAG,"onSuccess");
            String jsonStr = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"lesson_name\":\"Java基础一\",\"lesson_index\":\"1\",\"check_credits\":\"30\",\"cause\":\"正常\",\"acquire_time\":1302122566546}," +
                    "{\"lesson_name\":\"Android讲义一\",\"lesson_index\":\"2\",\"check_credits\":\"40\",\"cause\":\"分享\",\"acquire_time\":1302122566546}," +
                    "{\"lesson_name\":\"概要需求分析\",\"lesson_index\":\"3\",\"check_credits\":\"50\",\"cause\":\"高级\",\"acquire_time\":1302122566546}]}";
            try {
                JSONObject result = new JSONObject(jsonStr);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.COURSEINTEGRATION, result);
                int code = factoryGenerics.getCode();
                //成功返回
                if (code == 0){
                    mIntegrationList = (List<CourseIntegrationItem>) factoryGenerics.get();
                    mAdapter = new ClientCourseIntegrationAdapter(mIntegrationList,ClientCourseIntegrationRecordActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                    mScoreText.setText("截止到当前，您的积分总分为" + getTotalScore(mIntegrationList) + "分");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.e(TAG, "onFailure");
        }
    };*/

    private int getTotalScore(List<CourseIntegrationItem> list){
        int total = 0;
        for (int i = 0; i < list.size(); i++){
            total += list.get(i).getmCheckCredits();
        }
        return total;
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view){
        this.finish();
    }
}
