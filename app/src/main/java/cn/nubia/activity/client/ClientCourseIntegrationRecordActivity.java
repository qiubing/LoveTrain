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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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
import cn.nubia.adapter.ClientCourseIntegrationAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseIntegrationItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;


/**
 * Author: qiubing
 * Date: 2015/9/2 9:32
 */
public class ClientCourseIntegrationRecordActivity extends Activity {
    private static final String TAG = "CourseIntegrationRecord";
    private TextView mScoreText;
    private  List<CourseIntegrationItem> mIntegrationList;
    private ClientCourseIntegrationAdapter mAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_integration_record);
        initEvents();
        loadData();
    }

    private void initEvents() {
//        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_course_integration_title);
//        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        TextView text = (TextView) findViewById(R.id.sub_page_title);
        text.setText("课程积分记录");

//        RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.main_layout);
//        mScoreText = (TextView) findViewById(R.id.show_total_course_integration);
        mScoreText = (TextView)findViewById(R.id.title_text);


        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_integration);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_integration);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_integration);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        mIntegrationList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.course_integration_detail);
        mAdapter = new ClientCourseIntegrationAdapter(mIntegrationList, ClientCourseIntegrationRecordActivity.this);
        mListView.setAdapter(mAdapter);

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });


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

    private final JsonHttpResponseHandler mClientCourseIntegrationHandler = new JsonHttpResponseHandler() {

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
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
            } catch (JSONException e) {
                Log.e(TAG, "VIEW_LOADFAILURE");
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
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
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);

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
//            mScoreText.setVisibility(View.VISIBLE);
//            mScoreText.setText("截止到当前，您的积分总分为" + getTotalScore(mIntegrationList) + "分");
            mScoreText.setText(String.valueOf(getTotalScore(mIntegrationList)));
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
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
