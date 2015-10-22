package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import cn.nubia.entity.Item;
import cn.nubia.entity.LessonItem;
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.interfaces.OnTabActivityResultListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */

public class AdminCourseAddTabActivity extends Activity implements OnTabActivityResultListener {

    private RefreshLayout mRefreshLayout;
    private LoadViewUtil mLoadViewUtil;

    private ExpandableListView mExpandableListView;
    private CourseExpandableListAdapter mCourseExpandableListAdapter;

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

    private void initEvents() {
        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(AdminCourseAddTabActivity.this, mExpandableListView, null);
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
   /*     mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
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


        AsyncLoadDBTask mAsyncTask = new AsyncLoadDBTask();
        mAsyncTask.execute();
        /****从网络中获取数据**/
        loadShow();
        loadData();
    }

    /**
     * 从网络加载数据
     */
    private void loadData() {
        /**请求课程数据*/
        RecordModifyFlag.RecordPair recordPair = RecordModifyFlag.getInstance().getRecordModifyMap().get(SqliteHelper.TB_NAME_CLASS);
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("course_index", recordPair.getLastCourseIndex());
        requestParams.put("course_record_modify_time", recordPair.getLastCourseModifyTime());
        requestParams.put("lesson_index", recordPair.getLastLessonIndex());
        requestParams.put("lesson_record_modify_time",  recordPair.getLastLessonModifyTime());

        String url = Constant.BASE_URL + "course/get_courses_lessons.do";
        AsyncHttpHelper.post(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 请求课程数据服务器数据的Handler
     */
    private final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response.getInt("code") != 0) {
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                    return;
                }
                cancelLoadShow();
                if (response.getInt("code") == 0 && response.getString("data") != null) {
                    mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncLoadHttpTask mLoadHttpTask = new AsyncLoadHttpTask();
                    mLoadHttpTask.execute(jsonArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
                cancelLoadShow();
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
            cancelLoadShow();
            mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.NETWORK_UNUSABLE, true);
        }
    };

    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Item data) {
        switch (resultCode){
            case 1:
                if(data != null && data instanceof CourseItem){
                    mCourseItemList.remove(data);
                    DbUtil.getInstance(null).deleteCourseItem((CourseItem) data);
                }
                break;
            case 2:
                if(data != null && data instanceof CourseItem){
                    mCourseItemList.add(0, (CourseItem) data);
                    DbUtil.getInstance(null).insertCourseItem((CourseItem) data);
                }
                break;
            case 3:
                if(data != null && data instanceof LessonItem){
                    int index = UpdateClassListHelper.binarySearch(mCourseItemList,((LessonItem)data).getCourseIndex());
                    if(index > 0){
                        mCourseItemList.get(index).getLessonList().remove(data);
                        DbUtil.getInstance(null).deleteLessonItem((LessonItem) data);
                    }
                }
                break;
            default:
                break;
        }
        mCourseExpandableListAdapter.notifyDataSetChanged();
    }

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
//            int listLength = mCourseItemList.size();?
//            for (int i = 0; i < listLength; i++) {
//                String type = mCourseItemList.get(i).getType().equals("senior")? "高级课程" : "普通课程";
//            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    }

    private void loadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.COURCE);
        intent.putExtra(Constant.COURCE, "visible");
        LocalBroadcastManager.getInstance(AdminCourseAddTabActivity.this).sendBroadcast(intent);
    }

    private void cancelLoadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.COURCE);
        intent.putExtra(Constant.COURCE, "gone");
        LocalBroadcastManager.getInstance(AdminCourseAddTabActivity.this).sendBroadcast(intent);
    }

}
