package cn.nubia.activity.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ExpandableListView;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseExpandableListAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.db.DbUtil;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.db.SqliteHelper;
import cn.nubia.util.UpdateClassListHelper;
import cn.nubia.util.Utils;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ClientAllCourseTabActivity extends Activity {

    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;
    /*expandableListView*/
    private ExpandableListView mExpandableListView;
    /*adapter*/
    private CourseExpandableListAdapter mCourseExpandableListAdapter;
    /*用来存储courseItem的List*/
    private List<CourseItem> mCourseItemList;

    private String classUrl = Constant.BASE_URL + "course/get_courses_lessons.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*和管理员共用同一个页面*/
        setContentView(R.layout.activity_admin_all_course);
        initView();
        initEvents();
    }

    /*初始化View*/
    public void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.allCourse_ExpandableListView);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.admin_all_course_refreshLayout);
    }

    protected void initEvents() {
        mCourseItemList = new ArrayList<>();

        mLoadViewUtil = new LoadViewUtil(ClientAllCourseTabActivity.this, mExpandableListView, null);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());
        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this);
        /**为ExpandableListView指定填充数据的adapter*/
        mExpandableListView.setAdapter(mCourseExpandableListAdapter);
        /**去掉箭头**/
        mExpandableListView.setGroupIndicator(null);

        /**设置下拉刷新监听器**/
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /** 更新最新数据 **/
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

        /****先从数据库中加载数据**/
        AsyncLoadDBTask mAsyncTask = new AsyncLoadDBTask();
        mAsyncTask.execute();
        /****从网络中获取数据**/
        loadData();
    }

    /**请求课程数据服务器数据的Handler*/
    MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if(response.getInt("code") != 0){
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                    return;
                }else
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray!=null) {
                    AsyncLoadHttpTask loadHttpTask = new AsyncLoadHttpTask();
                    loadHttpTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
        }
    };

    /*For debug*/
    private void loadData() {
        /**请求课程数据*/
        HashMap<String,String> getClassParam = new HashMap<>();
        getClassParam.put("course_index", "1");
        getClassParam.put("course_record_modify_time", "1245545456456");
        getClassParam.put("lesson_index", "1");
        getClassParam.put("lesson_record_modify_time", "1245545456456");
        RequestParams requestParams = Utils.toParams(getClassParam);
        AsyncHttpHelper.post(classUrl, requestParams, jsonHttpResponseHandler);
    }

    private class AsyncLoadHttpTask extends AsyncTask<JSONArray, Void, List<CourseItem>>  {
        List<CourseItem> courseItemList;
        @Override
        protected List<CourseItem> doInBackground(JSONArray... params) {
            courseItemList = new ArrayList<CourseItem>(mCourseItemList);
            try {
                UpdateClassListHelper.updateAllClassData(params[0], courseItemList, SqliteHelper.TB_NAME_CLASS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return courseItemList;
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList){
            if(courseList != null){
                mCourseItemList.clear();
                mCourseItemList.addAll(courseList) ;
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<CourseItem>>  {
        @Override
        protected List<CourseItem> doInBackground(Void... params) {
            DbUtil dbUtil = DbUtil.getInstance(ClientAllCourseTabActivity.this);
            return dbUtil.getCourseList(SqliteHelper.TB_NAME_CLASS);
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList) {
            if(courseList !=null && courseList.size() != 0){
                mCourseItemList.clear();
                mCourseItemList.addAll(courseList);
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }
}