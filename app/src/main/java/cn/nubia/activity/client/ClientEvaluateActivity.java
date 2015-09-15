package cn.nubia.activity.client;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.EvaluateAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.LessonJudgement;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by 胡立 on 2015/9/14.
 */
public class ClientEvaluateActivity  extends Activity {

    private RefreshLayout mRefreshLayout;
    private ExpandableListView mExpandableListView;
    private ErrorHintView mErrorHintView;
    private TextView barTxt;
    public static int VIEW_LIST = 1;
    /** 显示断网 **/
    public static int VIEW_WIFIFAILUER = 2;
    /** 显示加载数据失败 **/
    public static int VIEW_LOADFAILURE = 3;
    public static int VIEW_LOADING = 4;
    private static final String URL_PATH = "http://love-train-dev.nubia.cn/";
    private List<LessonJudgement> mList = new ArrayList<>();
    EvaluateAdapter mEvaluateAdapter;

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
        mErrorHintView = (ErrorHintView) findViewById(R.id.evaluate_hintView);
        mExpandableListView = (ExpandableListView) findViewById(R.id.evaluate_expandableListView);
        barTxt = (TextView) findViewById(R.id.sub_page_title);
        barTxt.setText("我的课程评价");
        initBeforeData();
        initEvents();
    }

    protected void initBeforeData() {
        mEvaluateAdapter = new  EvaluateAdapter(ClientEvaluateActivity.this);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mEvaluateAdapter);
        showLoading(VIEW_LOADING);
        loadData("/my/find_course_judge.do");
    }

    public void loadData(String page) {
        String url = URL_PATH + page;
        AsyncHttpHelper.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int code, Header[] headers,
                                  byte[] responseBody) {
                try {
                    if (responseBody != null && responseBody.length > 0) {
                        JSONObject obj = new JSONObject(new String(responseBody));
                        EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(
                                EntityFactoryGenerics.ItemType.LESSONJUDGEMENT, obj);
                        List<LessonJudgement> list = (List<LessonJudgement>) factoryGenerics.get();
                        Log.i("huhu", "onSuccess");
                        Log.i("huhu", list.toString());
                        showLoading(VIEW_LIST);

                        /*JSONArray array = obj.getJSONArray("items");
                        List<GameDetailItem> list = JsonUtils.getInstance(
                                GameDetailItem.class, array);*/

                        if (list != null && list.size() > 0) {
                            mList.clear();
                            mList.addAll(list);
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        showLoading(VIEW_LOADFAILURE);
                        Log.i("huhu", "VIEW_LOADFAILURE");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showLoading(VIEW_LOADFAILURE);
                    Log.i("huhu", "VIEW_LOADFAILURE");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                Log.i("huhu", "onFailure");

                String qusiba = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[],\"data\":[{\"type\":\"judgement\",\"detail\":{\"ContentApplicability\":1.5,\"ContentRationality\":2.5,\"Discussion\":3.5,\"TimeRationality\":4,\"ContentUnderstanding\":5,\"ExpressionAbility\":0,\"Communication\":3.5,\"Organization\":2,\"ComprehensiveEvaluation\":\"去死吧我平衡你\",\"Suggestion\":\"我不去\"}},{\"type\":\"judgement\",\"detail\":{\"ContentApplicability\":1.5,\"ContentRationality\":2.5,\"Discussion\":3.5,\"TimeRationality\":4,\"ContentUnderstanding\":5,\"ExpressionAbility\":0,\"Communication\":3.5,\"Organization\":2,\"ComprehensiveEvaluation\":\"去死吧fgh我平衡你\",\"Suggestion\":\"我不gfhgfh去\"}},{\"type\":\"judgement\",\"detail\":{\"ContentApplicability\":1.5,\"ContentRationality\":2.5,\"Discussion\":3.5,\"TimeRationality\":4,\"ContentUnderstanding\":5,\"ExpressionAbility\":0,\"Communication\":3.5,\"Organization\":2,\"ComprehensiveEvaluation\":\"去死把你我阿克苏阿克苏健康阿卡拉速度进空间卡数据爱看书的话你\",\"Suggestion\":\"我大汉口路号码不换了不去\"}}]}";

                try {
                    if (qusiba != null) {
                        JSONObject obj = new JSONObject(qusiba);
                        EntityFactoryGenerics factoryGenerics = new EntityFactoryGenerics(
                                EntityFactoryGenerics.ItemType.LESSONJUDGEMENT, obj);
                        List<LessonJudgement> list = (List<LessonJudgement>) factoryGenerics.get();
                        showLoading(VIEW_LIST);

                        /*JSONArray array = obj.getJSONArray("items");
                        List<GameDetailItem> list = JsonUtils.getInstance(
                                GameDetailItem.class, array);*/

                        if (list != null && list.size() > 0) {
                            mList.clear();
                            mList.addAll(list);
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        showLoading(VIEW_LOADFAILURE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showLoading(VIEW_LOADFAILURE);
                }

                //这行代码最后要加上
                //showLoading(VIEW_WIFIFAILUER);
            }
        });
    }




    protected void initEvents() {
        // TODO Auto-generated method stub
        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientEvaluateActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新数据
                        loadData("/my/find_course_judge.do");
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        //不需要上滑到底部更新
        /*// 加载监听器
        refreshLayout.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad() {
                showShortToast("加载更多");
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(27);
                        refreshLayout.setLoading(false);
                    }
                }, 1500);
            }
        });*/
        //不需要绑定监听器
        /*mExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                //showSpotAd();
                Bundle b = new Bundle();
                b.putSerializable("data", adapter.getItem(arg2));
                startActivity(GameDetailActivity.class, b);
            }
        });*/
    }

    /**
     * 等待
     *
     * @param i
     */
    private void showLoading(int i) {
        mErrorHintView.setVisibility(View.GONE);
        mExpandableListView.setVisibility(View.GONE);
        switch (i) {
            case 1:
                mErrorHintView.hideLoading();
                mExpandableListView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mErrorHintView.hideLoading();
                mErrorHintView.netError(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(VIEW_LOADING);
                        loadData("/my/find_course_judge.do");
                    }
                });
                break;
            case 3:
                mErrorHintView.hideLoading();
                mErrorHintView.loadFailure(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(VIEW_LOADING);
                        loadData("/my/find_course_judge.do");
                    }
                });
                break;
            case 4:
                mErrorHintView.loadingData();
                break;
        }
    }

    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }

}
