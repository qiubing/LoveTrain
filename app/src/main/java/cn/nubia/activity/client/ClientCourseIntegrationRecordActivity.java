package cn.nubia.activity.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.ClientCourseIntegrationAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseIntegrationItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Author: qiubing
 * Date: 2015/9/2 9:32
 */
public class ClientCourseIntegrationRecordActivity extends Activity {
    private static final String TAG = "CourseIntegrationRecord";
    private TextView mScoreText;
    private ListView mListView;
    private  List<CourseIntegrationItem> mIntegrationList;
    private ClientCourseIntegrationAdapter mAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_integration_record);
        initEvents();
        loadData();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_course_integration_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        mScoreText = (TextView) findViewById(R.id.show_total_course_integration);
        text.setText("课程积分记录");

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_integration);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_integration);
        RelativeLayout networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_integration);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        mIntegrationList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.course_integration_detail);
        mAdapter = new ClientCourseIntegrationAdapter(mIntegrationList, ClientCourseIntegrationRecordActivity.this);
        mListView.setAdapter(mAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientCourseIntegrationRecordActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    private final MyJsonHttpResponseHandler mClientCourseIntegrationHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e(TAG, "onSuccess: " + response.toString());
                if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                }else {
                    Log.e(TAG, "VIEW_LOADFAILURE");
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                Log.e(TAG, "VIEW_LOADFAILURE");
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(ClientCourseIntegrationRecordActivity.this, "请求失败", Toast.LENGTH_LONG).show();
        }
    };

    private int getTotalScore(List<CourseIntegrationItem> list) {
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getmCheckCredits();
        }
        return total;
    }

    private void loadData(){
        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "credit/find_lesson_credits.do";
        AsyncHttpHelper.post(url, params, mClientCourseIntegrationHandler);
    }


    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<CourseIntegrationItem>> {

        @Override
        protected List<CourseIntegrationItem> doInBackground(JSONArray... params) {
            List<CourseIntegrationItem> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    itemList.add(makeCourseIntegration(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<CourseIntegrationItem> courseIntegrationItems) {
            mIntegrationList.clear();
            if (courseIntegrationItems!= null && courseIntegrationItems.size() != 0) {
                mIntegrationList.addAll(courseIntegrationItems);
            }
            mAdapter.notifyDataSetChanged();
            Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
            mScoreText.setVisibility(View.VISIBLE);
            mScoreText.setText("截止到当前，您的积分总分为" + getTotalScore(mIntegrationList) + "分");
        }
    }

    private CourseIntegrationItem makeCourseIntegration(JSONObject jsonObject) throws JSONException {
        CourseIntegrationItem integration = new CourseIntegrationItem();
        integration.setmLessonName(jsonObject.getString("lesson_name"));
        integration.setmAcquireTime(jsonObject.getLong("achieve_time"));
        integration.setmCheckCredits(jsonObject.getInt("credits"));
        integration.setmCause(jsonObject.getString("cause"));
        integration.setmLessonIndex(jsonObject.getInt("lesson_index"));
        return integration;
    }



    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
