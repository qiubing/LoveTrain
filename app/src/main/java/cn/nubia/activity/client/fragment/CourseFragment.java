package cn.nubia.activity.client.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

public class CourseFragment extends Fragment {
    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;
    /*expandableListView*/
    private ExpandableListView mExpandableListView;
    /*adapter*/
    private CourseExpandableListAdapter mCourseExpandableListAdapter;
    /*用来存储courseItem的List*/
    private List<CourseItem> mCourseItemList;
    private View rootView;
    private ClientAllCourceFragment.ClassFitler mClassFitler;

    protected CourseFragment(ClientAllCourceFragment.ClassFitler classFitler){
        mClassFitler = classFitler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*和管理员共用同一个页面*/
        rootView = inflater.inflate(R.layout.activity_admin_all_course, container, false);
        initViews();
        return rootView;
    }

    /*初始化View*/
    private  void initViews() {
        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.allCourse_ExpandableListView);
        mRefreshLayout = (RefreshLayout) rootView.findViewById(R.id.admin_all_course_refreshLayout);
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

  /*      // 加载监听器
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
        });*/
        /****从网络中获取数据**/
        loadShow();
        loadData();
    }

    private  void initEvents() {
        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(getActivity(), mExpandableListView, null);
        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, getActivity(), Constant.user.getUserID());

        /****先从数据库中加载数据**/
        AsyncLoadDBTask mAsyncTask = new AsyncLoadDBTask();
        mAsyncTask.execute();
    }

    /**请求课程数据服务器数据的Handler*/
    private final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if(response.getInt("code") != 0){
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                    return;
                }else
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
                cancelLoadShow();
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray!=null) {
                    AsyncLoadHttpTask loadHttpTask = new AsyncLoadHttpTask();
                    loadHttpTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                cancelLoadShow();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
            cancelLoadShow();
            mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,Constant.NETWORK_UNUSABLE, true);
        }
    };

    private void loadData() {
        /**请求课程数据*/
        RecordModifyFlag.RecordPair recordPair = RecordModifyFlag.getInstance().getRecordModifyMap().get(SqliteHelper.TB_NAME_CLASS);
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("course_index", recordPair.getLastCourseIndex());
        requestParams.put("course_record_modify_time", recordPair.getLastCourseModifyTime());
        requestParams.put("lesson_index", recordPair.getLastLessonIndex());
        requestParams.put("lesson_record_modify_time",  recordPair.getLastLessonModifyTime());

        String classUrl = Constant.BASE_URL + "course/get_courses_lessons2.do";
        AsyncHttpHelper.post(classUrl, requestParams, jsonHttpResponseHandler);
    }

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
        protected void onPostExecute(List<CourseItem> courseList){
            if(courseList != null){
                mCourseItemList.clear();
                mCourseItemList = filterClassList(courseList);
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<CourseItem>>  {
        @Override
        protected List<CourseItem> doInBackground(Void... params) {
            DbUtil dbUtil = DbUtil.getInstance(getActivity());
            return dbUtil.getCourseList(SqliteHelper.TB_NAME_CLASS);
        }

        @Override
        protected void onPostExecute(List<CourseItem> courseList) {
            if(courseList != null && courseList.size() != 0){
                mCourseItemList.clear();
                mCourseItemList = filterClassList(courseList);
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    private void loadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.ALLCOURCE);
        intent.putExtra(Constant.ALLCOURCE, "visible");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private void cancelLoadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.ALLCOURCE);
        intent.putExtra(Constant.ALLCOURCE, "gone");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private List<CourseItem> filterClassList(List<CourseItem> courseItems){
        for (CourseItem courseItem : courseItems) {
            if(mClassFitler.filterClass(courseItem))
                mCourseItemList.add(courseItem);
        }
        return mCourseItemList;
    }
}