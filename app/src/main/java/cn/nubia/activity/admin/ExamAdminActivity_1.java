package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
import cn.nubia.adapter.ExamAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.ExamItem;
import cn.nubia.util.AsyncHttpUtil;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ExamAdminActivity_1 extends Activity {

    private ListView mAllExamListView;
    private ExamAdapter mExamAdapter;
    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private List<ExamItem> mExamList = new ArrayList<>();
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
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mErrorHintView = (ErrorHintView) findViewById(R.id.hintView);
    }

    protected void initEvents() {
        mExamAdapter = new ExamAdapter(mExamList,this);
        mAllExamListView.setAdapter(mExamAdapter);
        mAllExamListView.setOnItemClickListener(new ExamListOnItemClickListener());

        /*for Debug  第一次加载数据*/
        showLoading(4);
        Message msg = hand.obtainMessage();
        msg.what = 1;
        hand.sendMessage(msg);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新最新数据
                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        // 加载监听器
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                showShortToast("加载更多");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载历史数据
                        loadData();
                        mRefreshLayout.setLoading(false);
                    }
                }, 1500);
            }
        });
    }

    /*For debug*/
    private void loadData(){
        Message msg = hand.obtainMessage();
        msg.what = 2;
        hand.sendMessage(msg);
    }

    public void loadData(int page) {
        String url = "test" + page;

        AsyncHttpUtil.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Message msg = hand.obtainMessage();
                msg.what = 2;
                hand.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showLoading(2);
            }
        });
    }

    Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            List<ExamItem> examList = new ArrayList<>();
            /*For DEBUG  Need add data*/
            if(msg.what == 1)
            {
                for (int i = 10; i<30;i++){
                    ExamItem examItem = new ExamItem();
                    examItem.setIndex(i);
                    examItem.setName("Java基础");
                    examItem.setLocale(String.valueOf(i) + "室");
                    examItem.setStartTime("7月8号9点10分");
                    examItem.setEndTime("7月8号10点10分");
                    examList.add(0, examItem);
                }
                mExamList.addAll(examList);
                showLoading(1);
            }
            if(msg.what == 2)
            {
                for (int i = 40; i<50;i++){
                    ExamItem examItem = new ExamItem();
                    examItem.setIndex(i);
                    examItem.setName("Android基础");
                    examItem.setLocale(String.valueOf(i));
                    examItem.setStartTime("7月8号9点10分");
                    examItem.setEndTime("7月8号10点10分");
                    examList.add(0, examItem);
                }
                mExamList.addAll(examList);
                showLoading(1);
            }
            Collections.sort(mExamList, new Comparator<ExamItem>() {
                @Override
                public int compare(ExamItem lhs, ExamItem rhs) {
                    if(lhs.getIndex() == rhs.getIndex())
                        return 0;
                    else
                        return lhs.getIndex()<= rhs.getIndex()?1 : -1;
                }
            });
            mExamAdapter.notifyDataSetChanged();
        }
    };

    private class ExamListOnItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(ExamAdminActivity_1.this, EmptyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ExamInfo",mExamList.get(arg2-1));
            intent.putExtra("value",bundle);
            startActivity(intent);
        }
    }

    /**
     * 等待
     *
     * @param i
     */
    private void showLoading(int i) {
        mErrorHintView.setVisibility(View.GONE);
        mAllExamListView.setVisibility(View.GONE);
        switch (i) {
            case 1:
                mErrorHintView.hideLoading();
                mAllExamListView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mErrorHintView.hideLoading();
                mErrorHintView.netError(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(4);
                        loadData(1);
                    }
                });
                break;
            case 3:
                mErrorHintView.hideLoading();
                mErrorHintView.loadFailure(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(4);
                        loadData(1);
                    }
                });
                break;
            case 4:
                mErrorHintView.loadingData();
                break;
        }
    }

    /**
     * 短暂显示Toast提示(来自String) *
     */
    public void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
