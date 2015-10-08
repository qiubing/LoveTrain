package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.ClientExamScoreAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamResultItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;

/**
 * Author: qiubing
 * Date: 2015/9/6 9:19
 */

public class ClientExamScoreActivity extends Activity {
    private static final String TAG = "ExamScore";
    private List<ExamResultItem> mResultList;
    private ClientExamScoreAdapter mAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam_score);
        initEvents();
        loadData();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_exam_score_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("考试成绩");

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_exam);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_exam);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_exam);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        mResultList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.exam_score_detail);
        mAdapter = new ClientExamScoreAdapter(mResultList, ClientExamScoreActivity.this);
        mListView.setAdapter(mAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientExamScoreActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private final JsonHttpResponseHandler mClientExamScoreHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
                    Log.e(TAG, "onSuccess :" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                }else {
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };

    private void loadData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "exam/my_exam_list.do";
        AsyncHttpHelper.post(url, params, mClientExamScoreHandler);
    }

    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<ExamResultItem>> {

        @Override
        protected List<ExamResultItem> doInBackground(JSONArray... params) {
            List<ExamResultItem> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    JSONObject detail = obj.getJSONObject("detail");
                    itemList.add(makeExamResult(detail));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ExamResultItem> examResultItems) {
            mResultList.clear();
            if (examResultItems != null && examResultItems.size() != 0) {
                mResultList.addAll(examResultItems);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private ExamResultItem makeExamResult(JSONObject jsonObject) throws JSONException {
        ExamResultItem result = new ExamResultItem();
        result.setmLessonName(jsonObject.getString("lesson_name"));
        result.setmExamScore(jsonObject.getDouble("lesson_score"));
        result.setmLessonIndex(jsonObject.getInt("lesson_index"));
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }



    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
