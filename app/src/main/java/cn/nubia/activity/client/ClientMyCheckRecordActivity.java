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
import cn.nubia.adapter.ClientCheckRecordAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.CheckRecordItem;
import cn.nubia.entity.Constant;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;


/**
 * ClassName:
 * Description: 签到记录类
 * Author: qiubing
 * Date: 2015/9/2 9:26
 */
public class ClientMyCheckRecordActivity extends Activity {
    private static final String TAG = "MyCheckRecord";
    private List<CheckRecordItem> mCheckList;//签到记录表
    private ClientCheckRecordAdapter mAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checked_record);
        initEvents();
        loadData();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_check_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的签到记录");

        mCheckList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.check_detail);
        mAdapter = new ClientCheckRecordAdapter(mCheckList, ClientMyCheckRecordActivity.this);
        mListView.setAdapter(mAdapter);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_mycheck);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_mycheck);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_mycheck);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

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
                Toast.makeText(ClientMyCheckRecordActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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

    private final JsonHttpResponseHandler mCheckRecordHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
                    Log.e(TAG, "onSuccess:" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                } else {
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(ClientMyCheckRecordActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
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

    /**
     * 加载我的签到记录
     */
    private void loadData() {
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);

        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "user/find_check_record.do";
        AsyncHttpHelper.post(url, params, mCheckRecordHandler);
    }

    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<CheckRecordItem>> {

        @Override
        protected List<CheckRecordItem> doInBackground(JSONArray... params) {
            List<CheckRecordItem> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    itemList.add(makeCheckRecord(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<CheckRecordItem> checkRecordItems) {
            super.onPostExecute(checkRecordItems);
            mCheckList.clear();
            if (checkRecordItems != null && checkRecordItems.size() != 0) {
                mCheckList.addAll(checkRecordItems);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private CheckRecordItem makeCheckRecord(JSONObject jsonObject) throws JSONException {
        CheckRecordItem check = new CheckRecordItem();
        check.setmLessonName(jsonObject.getString("lesson_name"));
        check.setmCheckTime(jsonObject.getLong("check_time"));
        return check;
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
     * <p/>
     * param view
     */
    public void back(View view) {
        this.finish();
    }

}
