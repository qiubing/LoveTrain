package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.admin.AdminExamDetailActivity;
import cn.nubia.adapter.ExamAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.Item;
import cn.nubia.interfaces.OnTabActivityResultListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.db.DbUtil;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ExamAddTabActivity extends Activity implements OnTabActivityResultListener{
    private ListView mAllExamListView;
    private ExamAdapter mExamAdapter;
    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;
    private List<ExamItem> mExamList;

    private static final String URL = Constant.BASE_URL + "course/get_all_exams.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_all_exam);
        initView();
        initEvents();
    }

    private void initView(){
        mAllExamListView = (ListView) findViewById(R.id.admin_all_exam_list);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
    }

    private void initEvents() {
        mExamList = new ArrayList<>();

        mLoadViewUtil = new LoadViewUtil(this, mAllExamListView, null);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());
        mExamAdapter = new ExamAdapter(mExamList,this);
        mAllExamListView.setAdapter(mExamAdapter);
        mAllExamListView.setOnItemClickListener(new ExamListOnItemClickListener());

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新最新数据
                        loadData();
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
                        loadData();
                        mRefreshLayout.setLoading(false);
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_FOOTER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
                    }
                }, 1500);
            }
        });

        AsyncLoadDBTask dbTask = new AsyncLoadDBTask();
        dbTask.execute();

        loadData();
    }

    private void loadData(){
        int index = 0;
        if (mExamList.size()!=0){
            index = mExamList.get(0).getIndex();
        }
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("exam_record_modify_time", 12323232323l);
        requestParams.put("exam_index", index);
        Log.e("wj", "requestParams" + requestParams.toString());
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("wj","ExamAddTab"+response.toString());
            try {
                if(response.getInt("code") != 0){
                    Toast.makeText(ExamAddTabActivity.this,
                            response.getJSONArray("message").toString(), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
                }
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray!=null && jsonArray.length() > 0){
                    AsyncLoadJsonTask asyncLoadJsonTask  = new AsyncLoadJsonTask();
                    asyncLoadJsonTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.LOADING_FAILED, true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("wj","onFailure"+statusCode+throwable.toString());
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,Constant.NETWORK_UNUSABLE, true);
        }
    };

    private class AsyncLoadJsonTask extends AsyncTask<JSONArray, Void, List<ExamItem>>  {
    List<ExamItem> examItemList;
    @Override
    protected List<ExamItem> doInBackground(JSONArray... params) {
        examItemList = new ArrayList<ExamItem>(mExamList);
        try {
            UpdateClassListHelper.updateAllExamData(params[0], examItemList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return examItemList;
    }

    @Override
    protected void onPostExecute(List<ExamItem> examItemList){
        if(examItemList != null){
            mExamList.clear();
            mExamList.addAll(examItemList) ;
        }
        mExamAdapter.notifyDataSetChanged();
    }
}

    private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<ExamItem>> {
        @Override
        protected List<ExamItem> doInBackground(Void... params) {
            DbUtil dbUtil = DbUtil.getInstance(ExamAddTabActivity.this);
            return dbUtil.getExamList();
        }

        @Override
        protected void onPostExecute(List<ExamItem> courseList) {
            if(courseList != null){
                mExamList.clear();
                mExamList.addAll(courseList);
            }
            mExamAdapter.notifyDataSetChanged();
        }
    }

    private class ExamListOnItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(ExamAddTabActivity.this, AdminExamDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ExamInfo", mExamList.get(arg2));
            intent.putExtras(bundle);
            getParent().startActivityForResult(intent, 1);
        }
    }


    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Item data) {
        Log.e("wj","onTabActivityResult called"+ resultCode);
        switch (requestCode){
            case 1:
                if(data != null && data instanceof ExamItem){
                    Log.e("wj", "mExamList.size()e before" + mExamList.size());
                    mExamList.remove(data);
                    Log.e("wj", "mExamList.size()e after " + mExamList.size());
//                    mExamAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if(data != null && data instanceof ExamItem){
                    mExamList.add((ExamItem) data);
                }
            default:
                break;
        }
        mExamAdapter.notifyDataSetChanged();
    }
}
