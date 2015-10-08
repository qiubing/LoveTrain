package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ExpandableListView;

import com.loopj.android.http.JsonHttpResponseHandler;
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
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;


/**
 * Created by 胡立 on 2015/9/7.
 */
public class ClientMyCourseTeacherTabActivity extends Activity {

    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;
    /**
     * expandableListView
     */
    private ExpandableListView mExpandableListView;
    /**
     * adapter
     */
    private CourseExpandableListAdapter mCourseExpandableListAdapter;
    /**
     * 用来存储courseItem的List
     */
    private List<CourseItem> mCourseItemList;


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

    private  void initEvents() {
        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(ClientMyCourseTeacherTabActivity.this, mExpandableListView, null);
        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this.getParent(),Constant.user.getUserID());
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
//                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
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
//                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
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
        /****然后从网络中获取数据**/
        loadShow();
        loadData();
    }


    /**
     * 从网络加载数据
     */
    private void loadData() {
        RecordModifyFlag.RecordPair recordPair = RecordModifyFlag.getInstance().getRecordModifyMap().get(SqliteHelper.TB_NAME_CLASS);
        /**请求课程数据*/
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("user_id", Constant.user.getUserID());
        requestParams.put("course_index", recordPair.getLastCourseIndex());
        requestParams.put("course_record_modify_time", recordPair.getLastCourseModifyTime());
        requestParams.put("lesson_index", recordPair.getLastLessonIndex());
        requestParams.put("lesson_record_modify_time",  recordPair.getLastLessonModifyTime());
        Log.e("hexiao", "parameter"+requestParams.toString());

        /**拉取所有课程的数据，然后从中选取自己为教师的课程显示*/
        String url = Constant.BASE_URL + "course/get_courses_lessons2.do";
        AsyncHttpHelper.post(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 请求课程数据服务器数据的Handler
     */
    private final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("hexiao","response"+response.toString());
            try {
                Log.e("hexiao","responseDataLength"+response.getJSONArray("data").length());
                if (response.getInt("code") != 0) {
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                    return;
                }else
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);

                cancelLoadShow();

                if (response.getString("data") != null) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncLoadHttpTask mLoadHttpTask = new AsyncLoadHttpTask();
                    mLoadHttpTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                Log.e("hexiao", e.toString());
                e.printStackTrace();
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                cancelLoadShow();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
            cancelLoadShow();
            mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.NETWORK_UNUSABLE, true);
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
                UpdateClassListHelper.updateAllClassData(params[0], courseItemList, SqliteHelper.TB_NAME_MY_CLASS_TEACHER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("courseItemList", courseItemList.size() + "");
            return courseItemList;
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList) {
            if (courseList != null) {
                mCourseItemList.clear();
                mCourseItemList.addAll(courseList);
            }
            /**只保留自己是讲师的课程*/
            getTeacherList(mCourseItemList);
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载数据库数据
     */
    private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<CourseItem>> {
        @Override
        protected List<CourseItem> doInBackground(Void... params) {
            DbUtil dbUtil = DbUtil.getInstance(ClientMyCourseTeacherTabActivity.this);
            return dbUtil.getCourseList(SqliteHelper.TB_NAME_MY_CLASS_TEACHER);
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList) {
            if (courseList != null) {
                mCourseItemList.addAll(courseList);
            }
            /**只保留自己是讲师的课程*/
            getTeacherList(mCourseItemList);
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 只保留自己是讲师的课程显示
     */
    private  void getTeacherList(List<CourseItem> mList) {
        ArrayList<CourseItem> resultList = new ArrayList<>();
        if (mList.size() != 0) {
            for(CourseItem courseItem : mList){
                for(LessonItem lessonItem : courseItem.getLessonList()){
                    if(lessonItem.getTeacherID().equals( Constant.user.getUserID())){
                        resultList.add(courseItem);
                        break;
                    }
                }
            }
        }
        mList.clear();
        mList.addAll(resultList);
        Log.e("0924MyCourseTeacher", mList.size()+"");
    }

    private void loadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.MYCOURCE_TEACHER);
        intent.putExtra(Constant.MYCOURCE_TEACHER, "visible");
        LocalBroadcastManager.getInstance(ClientMyCourseTeacherTabActivity.this).sendBroadcast(intent);
    }

    private void cancelLoadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.MYCOURCE_TEACHER);
        intent.putExtra(Constant.MYCOURCE_TEACHER, "gone");
        LocalBroadcastManager.getInstance(ClientMyCourseTeacherTabActivity.this).sendBroadcast(intent);
    }
}
