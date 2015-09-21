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

import com.loopj.android.http.AsyncHttpResponseHandler;

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
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by 胡立 on 2015/9/14.
 */
public class ClientEvaluateActivity  extends Activity {

    private ExpandableListView mExpandableListView;
    //private ErrorHintView mErrorHintView;
    private TextView barTxt;
    /*public static int VIEW_LIST = 1;
    *//** 显示断网 **//*
    public static int VIEW_WIFIFAILUER = 2;
    *//** 显示加载数据失败 **//*
    public static int VIEW_LOADFAILURE = 3;
    public static int VIEW_LOADING = 4;*/
    private static final String URL = Constant.BASE_URL + "/my/find_lesson_judge.do";
    private List<LessonJudgementMsg> mList = new ArrayList<>();
    EvaluateAdapter mEvaluateAdapter;
    private GestureDetector gestureDetector;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;


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

        initBeforeData();
        initEvents();
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

    public void loadData() {
        AsyncHttpHelper.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int code, Header[] headers,
                                  byte[] responseBody) {
                try {
                    if (responseBody != null && responseBody.length > 0) {
                        JSONObject obj = new JSONObject(new String(responseBody));
                        EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(
                                EntityFactoryGenerics.ItemType.LESSONJUDGEMENT, obj);
                        List<LessonJudgementMsg> list = (List<LessonJudgementMsg>) factoryGenerics.get();
                        Log.i("huhu", "onSuccess");
                        Log.i("huhu", list.toString());

                        /*JSONArray array = obj.getJSONArray("items");
                        List<GameDetailItem> list = JsonUtils.getInstance(
                                GameDetailItem.class, array);*/

                        if (list != null && list.size() > 0) {
                            mList.clear();
                            mList.addAll(list);
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        Log.i("huhu", "VIEW_LOADFAILURE");
                        loadingFailedRelativeLayout.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                    Log.i("huhu", "VIEW_LOADFAILURE");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                Log.i("huhu", "onFailure");
                networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            }
        });
    }




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
