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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.ClientExamScoreAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamResultItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Author: qiubing
 * Date: 2015/9/6 9:19
 */

public class ClientExamScoreActivity extends Activity {
    private static final String TAG = "ExamScore";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam_score);
        initEvents();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_exam_score_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("考试成绩");
        mListView = (ListView) findViewById(R.id.exam_score_detail);

        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "exam/my_exam_list.do";
        AsyncHttpHelper.post(url, params, mClientExamScoreHandler);
    }

    private final MyJsonHttpResponseHandler mClientExamScoreHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0) {
                    EntityFactoryGenerics factoryGenerics =
                            new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.EXAMRESULT, response);
                    Log.e(TAG, "onSuccess :" + response.toString());
                    List<ExamResultItem> mResultList = (List<ExamResultItem>) factoryGenerics.get();
                    ClientExamScoreAdapter mAdapter = new ClientExamScoreAdapter(mResultList, ClientExamScoreActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e(TAG, TAG + ":onFailure");
        }
    };


    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
