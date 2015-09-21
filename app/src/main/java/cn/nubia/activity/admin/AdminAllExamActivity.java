package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
import cn.nubia.adapter.ExamAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by WJ on 2015/9/7.
 *
 * 已废弃
 */
public class AdminAllExamActivity extends Activity{
    private ListView mAllExamListView;
    private List<ExamItem> mExamList;
    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;
    private static  final String URL = Constant.BASE_URL + "exam/find_all.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_all_exam);
        initView();
        initEvents();
    }

    public void initView(){
        mAllExamListView = (ListView) findViewById(R.id.admin_all_exam_list);
    }

    protected void initEvents() {
        mExamList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(this, mAllExamListView, hand);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());
        ExamAdapter mExamAdapter = new ExamAdapter(mExamList, this);
        mAllExamListView.setAdapter(mExamAdapter);
        mAllExamListView.setOnItemClickListener(new ExamListOnItemClickListener());

        /*for Debug  模拟第一次加载数据*/
//        mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADING);
     /*   Message msg = hand.obtainMessage();
        msg.what = 1;
        hand.sendMessage(msg);*/
        loadData("1111232211111");
        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新最新数据
//                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
//                        loadData();
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
                    }
                }, 1500);
            }
        });

        // 加载监听器
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mLoadViewUtil.showShortToast("加载更多");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载历史数据
//                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
//                        loadData();
                        mRefreshLayout.setLoading(false);
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_FOOTER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
                    }
                }, 1500);
            }
        });
    }

    void loadData(String updateTime){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id","MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("course_record_modify_time",updateTime);
        Log.e("exam loadData", URL);
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("exam onSuccess", statusCode + "statusCode");
            Log.e("exam onSuccess", statusCode+response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("exam onFailure", statusCode + "statusCode");
            Log.e("exam onFailure", statusCode +throwable.toString());
        }
    };

    Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            /*For DEBUG*/
            if(msg.what == 1)
            {
                for (int i = 0; i<10;i++){
                    ExamItem examItem = new ExamItem();
                    examItem.setName("Java基础");
                    examItem.setLocale("C-3室");
//                    examItem.setStartTime("7月8号9点10分");
//                    examItem.setStartTime("7月8号10点10分");
                    mExamList.add(0,examItem);
                }
            }
        }
    };

    private class ExamListOnItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(AdminAllExamActivity.this, EmptyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ExamInfo",mExamList.get(arg2));
            intent.putExtra("value",bundle);
            startActivity(intent);
        }
    }
}
