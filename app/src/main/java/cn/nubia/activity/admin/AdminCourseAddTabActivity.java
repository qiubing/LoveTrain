package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseExpandableListAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.db.DbUtil;
import cn.nubia.db.SqliteHelper;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */

public class AdminCourseAddTabActivity extends ActivityGroup {

    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;

    private ExpandableListView mExpandableListView;
    private CourseExpandableListAdapter mCourseExpandableListAdapter;

    private List<CourseItem> mCourseItemList;

    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_course);
        initView();
        initEvents();
    }

    private void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.allCourse_ExpandableListView);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.admin_all_course_refreshLayout);
    }

    private void initEvents() {
        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(AdminCourseAddTabActivity.this, mExpandableListView, null);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());
        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this, Constant.user.getUserID());
        /**为ExpandableListView指定填充数据的adapter*/
        mExpandableListView.setAdapter(mCourseExpandableListAdapter);
        /**去掉箭头*/
        mExpandableListView.setGroupIndicator(null);

        /**设置下拉刷新监听器*/
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

        /** 加载监听器*/
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

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Bundle bundle = new Bundle();
                LessonItem lessonItem = mCourseItemList.get(groupPosition).getLessonList().get(childPosition);
                bundle.putSerializable("LessonItem", lessonItem);
                Intent intent = new Intent(AdminCourseAddTabActivity.this, AdminLessonDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });

        /*ImageView loading_iv = (ImageView)findViewById(R.id.loading_iv);
        loading_iv.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loading_iv.getDrawable();
        animationDrawable.start();*/
        /****先从数据库中加载数据**/
        AsyncLoadDBTask mAsyncTask = new AsyncLoadDBTask();
        mAsyncTask.execute();
        /****从网络中获取数据**/
        loadData();
    }

    /**
     * 从网络加载数据
     */
    private void loadData() {
       /* ImageView loading_iv = (ImageView)getParent().findViewById(R.id.loading_iv);
        loading_iv.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loading_iv.getDrawable();
        animationDrawable.start();*/
        TabHost tabHost = (TabHost) getParent().findViewById(R.id.admin_course_tabhost);
        tabHost.setup(this.getLocalActivityManager());
        Log.i("huhu", getParent().getLocalClassName() + "\n"+tabHost.getTabWidget().getChildAt(0) +
                tabHost.getTabWidget().getChildTabViewAt(0) + tabHost.getTabWidget().getChildCount() + tabHost.getTabWidget() +
                + tabHost.getTabWidget().getTabCount() + "\n" +tabHost.getCurrentTabTag() + tabHost.getCurrentTabView());

//        TabHost tabHost1 = ((AdminCourseActivity)getParent()).tabHost;
        /*TabHost tabHost1 = AdminCourseActivity.tabHost;
        tabHost1.setup(this.getLocalActivityManager());
        Log.i("huhu", ""+tabHost1.getTabWidget().getChildAt(0) +
                tabHost1.getTabWidget().getChildTabViewAt(0) + tabHost1.getTabWidget().getChildCount() +
                tabHost1.getChildCount() + tabHost1.getChildAt(0));*/

        /**请求课程数据*/
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("course_index", Constant.sLastCourseIndex);
        requestParams.put("course_record_modify_time", Constant.sLastCourseRecordModifyTime);
        requestParams.put("lesson_index", Constant.sLastLessonIndex);
        requestParams.put("lesson_record_modify_time", Constant.slastLessonRecordModifyTime);
        String url = Constant.BASE_URL + "course/get_courses_lessons.do";
        AsyncHttpHelper.post(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 请求课程数据服务器数据的Handler
     */
    private final MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response.getInt("code") != 0) {
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                    return;
                }
                if (response.getInt("code") == 0 && response.getString("data") != null) {
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncLoadHttpTask mLoadHttpTask = new AsyncLoadHttpTask();
                    mLoadHttpTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
        }
    };

    /**
     * 异步加载数据
     */
    private class AsyncLoadHttpTask extends AsyncTask<JSONArray, Void, List<CourseItem>> {
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
        protected void onPostExecute(List<CourseItem> courseList) {
            if (courseList != null) {
                mCourseItemList.clear();
                mCourseItemList.addAll(courseList);
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载数据库数据
     */
    private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<CourseItem>> {
        @Override
        protected List<CourseItem> doInBackground(Void... params) {
            return DbUtil.getInstance(AdminCourseAddTabActivity.this)
                    .getCourseList(SqliteHelper.TB_NAME_CLASS);
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList) {
            if (courseList != null) {
                mCourseItemList.addAll(courseList);
            }
            int listLength = mCourseItemList.size();
            for (int i = 0; i < listLength; i++) {
                String type = mCourseItemList.get(i).getType().equals("senior")? "高级课程" : "普通课程";
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

}
