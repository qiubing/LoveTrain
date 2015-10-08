package cn.nubia.activity.admin;

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
import cn.nubia.adapter.SignUpManageAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.SignUpItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;

/**
 * Created by hexiao on 2015/9/11.
 */
public class AdminSignUpManageActivity extends Activity {
    private static final String TAG = "SignUpManage";
    private ArrayList<SignUpItem> mSignUpList;
    private CourseItem mCourseItem;
    private SignUpManageAdapter mSignUpAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_manage);
        initEvents();
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
        loadData();
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    private void initEvents(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.admin_signin_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("报名管理");

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_signin);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_signin);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_signin);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        mSignUpList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.admin_signIn_manage_listView);
        mSignUpAdapter = new SignUpManageAdapter(mSignUpList, AdminSignUpManageActivity.this);
        mListView.setAdapter(mSignUpAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AdminSignUpManageActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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

        Intent intent = getIntent();
        mCourseItem = (CourseItem) intent.getSerializableExtra("CourseItem");
        /***构造请求参数*/
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("course_index", mCourseItem.getIndex());
        Log.e(TAG, "params: " + params.toString());
        String signUpInfoUrl = Constant.BASE_URL + "enroll/list_enroll_users.do";
        AsyncHttpHelper.post(signUpInfoUrl, params, jsonHttpResponseHandler);
    }

    /**请求课程数据服务器数据的Handler*/
    private final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                Log.e(TAG,"onSuccess: " + response.toString());
                if(response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null){
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
            Toast.makeText(AdminSignUpManageActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_LONG).show();
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<SignUpItem>> {

        @Override
        protected List<SignUpItem> doInBackground(JSONArray... params) {
            List<SignUpItem> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    itemList.add(makeUpSignUpItem(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<SignUpItem> signUpItems) {
            super.onPostExecute(signUpItems);
            mSignUpList.clear();
            if (signUpItems != null && signUpItems.size() != 0) {
                mSignUpList.addAll(signUpItems);
            }
            mSignUpAdapter.notifyDataSetChanged();
            //Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度*/
        }
    }

    private SignUpItem makeUpSignUpItem(JSONObject object) throws JSONException {
        SignUpItem item = new SignUpItem();
        item.setCourseID(mCourseItem.getIndex());
        item.setUserName(object.getString("user_name"));
        item.setUserID(object.getString("user_id"));
        item.setIsEnroll(object.getBoolean("is_enroll"));
        return item;
    }

    public void back(View view) {
        this.finish();
    }
}
