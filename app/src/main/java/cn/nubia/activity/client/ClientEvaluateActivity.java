package cn.nubia.activity.client;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.EvaluateAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonJudgementMsg;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by 胡立 on 2015/9/14.
 */
public class ClientEvaluateActivity  extends Activity {

    private ExpandableListView mExpandableListView;
    private TextView barTxt;
    private static final String URL = Constant.BASE_URL + "/my/find_lesson_judge.do";
    private List<LessonJudgementMsg> mList = new ArrayList<>();
    EvaluateAdapter mEvaluateAdapter;
    private GestureDetector gestureDetector;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private String lession_index_ID;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mEvaluateAdapter.updateData(mList);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);


        lession_index_ID = getIntent().getExtras().getString("lession_index_ID");
        Log.i("huhu", "get lession_index_ID" + lession_index_ID);

        //mErrorHintView = (ErrorHintView) findViewById(R.id.evaluate_hintView);
        mExpandableListView = (ExpandableListView) findViewById(R.id.evaluate_expandableListView);
        barTxt = (TextView) findViewById(R.id.sub_page_title);
        barTxt.setText("我的课程评价");

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

        initEvents();
        initBeforeData();
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    protected void initBeforeData() {
        mEvaluateAdapter = new  EvaluateAdapter(ClientEvaluateActivity.this);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mEvaluateAdapter);
        //showLoading(VIEW_LOADING);
        loadData();
    }
    private void loadData(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");

        String stringArray []  = lession_index_ID.split(",");
        requestParams.add("lession_index", stringArray[0]);
        requestParams.add("user_id", stringArray[1]);


        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
            Log.i("huhu", "addExam" + "EvaluateonSuccess");
            try {
                int code = response.getInt("code");
               // boolean result = response.getBoolean("result");
                //boolean isOk = response.getBoolean("data");
//                JSONArray jsonArray = response.getJSONArray("data");
//                JSONObject obj = (JSONObject)jsonArray;
                Log.i("huhu", "Evaluate" + code + "," + response);

                if(code == 0 && response != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(
                                    EntityFactoryGenerics.ItemType.LESSONJUDGEMENT, response);
                            List<LessonJudgementMsg> list = (List<LessonJudgementMsg>) factoryGenerics.get();
                            if (list != null && list.size() > 0) {
                                mList.clear();
                                mList.addAll(list);
                                handler.sendEmptyMessage(0);
                                Log.i("huhu", "Evaluate list" + list);
                            }
                        }
                    }.start();
                }else {
                    Log.i("huhu", "VIEW_LOADFAILURE");
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
                Log.i("huhu", "VIEW_LOADFAILURE");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Log.i("huhu", "onFailure");
        }
    };

    protected void initEvents() {
        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientEvaluateActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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

    public void back(View view) {
        this.finish();
    }

}
