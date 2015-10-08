package cn.nubia.activity.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.adapter.EvaluateAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonJudgementMsg;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;
import cn.nubia.util.jsonprocessor.LessonJudgementAssembler;

/**
 * Created by 胡立 on 2015/9/14.
 */
@SuppressWarnings("deprecation")
public class ClientEvaluateActivity  extends Activity {

    private ExpandableListView mExpandableListView;
    private static final String URL = Constant.BASE_URL + "my/find_lesson_judge.do";
    private final List<LessonJudgementMsg> mList = new ArrayList<>();
    private EvaluateAdapter mEvaluateAdapter;
    private GestureDetector gestureDetector;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private String lession_index_ID;


    private final Handler handler = new Handler() {
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
        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                Log.i("huhu", "ClientEvaluateActivity onClick");
                startActivity(intent);
            }
        });


        lession_index_ID = getIntent().getExtras().getString("lession_index_ID");
        mExpandableListView = (ExpandableListView) findViewById(R.id.evaluate_expandableListView);
        TextView barTxt = (TextView) findViewById(R.id.sub_page_title);
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
        /*mExpandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //return false;
                return  gestureDetector.onTouchEvent(event);
            }
        });
*/
        initEvents();
        initBeforeData();
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    private  void initBeforeData() {
        mEvaluateAdapter = new  EvaluateAdapter(ClientEvaluateActivity.this);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mEvaluateAdapter);
        //showLoading(VIEW_LOADING);
        loadData();
    }
    private void loadData(){
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
//        requestParams.add("device_id", "MXJSDLJFJFSFS");
//        requestParams.add("request_time","1445545456456");
//        requestParams.add("apk_version","1");
//        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.add("record_modify_time_course", "1435125456111");

        String stringArray []  = lession_index_ID.split(",");
        requestParams.add("lesson_index", stringArray[0]);
        requestParams.add("user_id", stringArray[1]);
        Log.i("huhu", ",,,," + requestParams.toString());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
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
                            EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(LessonJudgementAssembler.class);
                            factoryGenerics.setJSON(response);
                            Map<String,?> res = factoryGenerics.getResponse();
                            @SuppressWarnings("unchecked")
                            List<LessonJudgementMsg> list = (List<LessonJudgementMsg>) res.get("detail");
//                            EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(
//                                    EntityFactoryGenerics.ItemType.LESSONJUDGEMENT, response);
//                            List<LessonJudgementMsg> list = (List<LessonJudgementMsg>) factoryGenerics.get();
                            if (list != null && list.size() > 0) {
                                mList.clear();
                                mList.addAll(list);
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }.start();
                }else {
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
                Log.i("huhu", "VIEW_LOADFAILURE111");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Log.i("huhu", "onFailure");
        }
    };

    private  void initEvents() {
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
