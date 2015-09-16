package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseExpandableListAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DataLoadUtil;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class AdminCourseAddTabActivity extends Activity {

    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private LoadViewUtil mLoadViewUtil;


    private ExpandableListView mExpandableListView;
    private CourseExpandableListAdapter mCourseExpandableListAdapter;

    private List<CourseItem> mCourseItemList;

    /*三个textview*/
//    private TextView mAddLessonTextView;
//    private TextView mCourseDetailTextView;
//    private TextView mSignUpExamTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_course);
        initView();
        initEvents();
    }


    public void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.allCourse_ExpandableListView);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.admin_all_course_refreshLayout);
        mErrorHintView = (ErrorHintView) findViewById(R.id.admin_all_course_errorHintView);
    }

    protected void initEvents() {
        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(this, mExpandableListView, mHandler);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());

        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this);
        /**为ExpandableListView指定填充数据的adapter*/
        mExpandableListView.setAdapter(mCourseExpandableListAdapter);



        /**请求数据*/
        RequestParams requestParams = new RequestParams();
        requestParams.add("course_index", "1");
        requestParams.add("course_record_modify_time", "1245545456456");
        requestParams.add("lesson_index", "1");
        requestParams.add("lesson_record_modify_time", "1245545456456");
        requestParams.add("device_id","MXJSDLJFJFSFS");
        requestParams.add("request_time", "1445545456456");
        requestParams.add("apk_version", "1");
        requestParams.add("token_key", "wersdfffthnjimhtrfedsaw");

        String url = Constant.BASE_URL + "course/get_courses_lessons.do";
        AsyncHttpHelper.post(url, requestParams, jsonHttpResponseHandler);



        /*去掉箭头*/
        mExpandableListView.setGroupIndicator(null);
        /*项的监听事件*/
        mExpandableListView.setOnChildClickListener(new ExpandableListViewOnItemClickListener());

        /*for Debug  模拟第一次加载数据*/
//        Message msg = mHandler.obtainMessage();
//        msg.what = 1;
//        mHandler.sendMessage(msg);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新最新数据
                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
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
                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
                        loadData();
                        mRefreshLayout.setLoading(false);
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_FOOTER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
                    }
                }, 1500);
            }
        });
    }

    /*For debug*/
    private void loadData() {
        DataLoadUtil.queryClassInfoDataforGet("aa");
    }

    /**
     * for debug
     * *
     */
    public void loadData(int page) {
        String url = "test" + page;
        DataLoadUtil.queryClassInfoDataforGet(url);
        Message msg = mHandler.obtainMessage();
        msg.what = 2;
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            List<CourseItem> mCourseList = new ArrayList<>();
            List<LessonItem> mLessonList = new ArrayList<>();
            /*For DEBUG  Need add data*/
            if (msg.what == 1) {
//                for (int i = 10; i < 30; i++) {
//                    CourseItem mCourseItem = new CourseItem();
//                    mCourseItem.setIndex(i);
//                    mCourseItem.setName("Java基础");
//                    mCourseList.add(0, mCourseItem);
//                    for (int j = 0; j < 3; j++) {
//                        LessonItem mLessonItem = new LessonItem();
//                        mLessonItem.setIndex(i);
//                        mLessonItem.setName("Java基础" + i);
//                        mLessonItem.setStartTime((long)12345);
//                        mLessonItem.setLocation("C-2");
//                        mLessonList.add(0, mLessonItem);
//                    }
//                    mCourseItem.setLessonList(mLessonList);
//                }
//                mCourseItemList.addAll(mCourseList);
            }
            if (msg.what == 2) {
//                for (int i = 40; i < 50; i++) {
//                    CourseItem mCourseItem = new CourseItem();
//                    mCourseItem.setIndex(i);
//                    mCourseItem.setName("Android基础");
//                    mCourseList.add(0, mCourseItem);
//                    for (int j = 0; j < 3; j++) {
//                        LessonItem mLessonItem = new LessonItem();
//                        mLessonItem.setIndex(i);
//                        mLessonItem.setName("Android基础" + i);
//                        mLessonItem.setStartTime((long)23456);
//                        mLessonItem.setLocation("C-2");
//                        mLessonList.add(0, mLessonItem);
//                    }
//                    mCourseItem.setLessonList(mLessonList);
//                }
//                mCourseItemList.addAll(mCourseList);
            }
//            UpdateClassListHelper.binarySort(mCourseItemList);
//            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    };


    /**请求服务器数据的Handler*/
    MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            super.onSuccess(statusCode, headers, response);
            Log.e("AdminCourseAddTabA", "onSuccess");
            Log.e("AdminCourseAddTabA", response.toString());

            try {
                UpdateClassListHelper.updateAllClassData(response.getJSONArray("data"), mCourseItemList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("AdminCourseAddTabA", "onFailure");
//            String jsonStr= "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[],\"data\":[{\"type\":\"course\",\"operate\":\"insert\",\"detail\":{\"course_index\":1,\"course_name\":\"Java基础\",\"course_description\":\"Java是一门好课程\",\"lessons\":2,\"has_exam\":1,\"course_record_modify_time\":201503141545}},{\"type\":\"lesson\",\"operate\":\"insert\",\"detail\":{\"course_index\":1,\"lesson_index\":1,\"lesson_name\":\"Java基础一\",\"lesson_description\":\"Java Hello World!第一课\",\"user_id\":\"0016002946\",\"teacher_name\":\"张三\",\"locale\":\"C-6室\",\"start_time\":1322211211211,\"end_time\":12312312312,\"check_credits\":20,\"teacher_credits\":30,\"judge_score\":25,\"lesson_record_modify_time\":1231221312123}},{\"type\":\"lesson\",\"operate\":\"insert\",\"detail\":{\"course_index\":1,\"lesson_index\":2,\"lesson_name\":\"Java基础二\",\"lesson_description\":\"Java Hello World!第二课\",\"user_id\":\"0016002946\",\"teacher_name\":\"李四\",\"locale\":\"C-6室\",\"start_time\":1322211211211,\"end_time\":12312312312,\"check_credits\":20,\"teacher_credits\":30,\"judge_score\":25,\"lesson_record_modify_time\":1231221312123}},{\"type\":\"course\",\"operate\":\"insert\",\"detail\":{\"course_index\":2,\"course_name\":\"Android\",\"course_description\":\"Android是一门好课程\",\"lessons\":2,\"has_exam\":1,\"course_record_modify_time\":201503141545}},{\"type\":\"lesson\",\"operate\":\"insert\",\"detail\":{\"course_index\":2,\"lesson_index\":1,\"lesson_name\":\"Android基础一\",\"lesson_description\":\"Android Hello World!第一课\",\"user_id\":\"0016002946\",\"teacher_name\":\"张三\",\"locale\":\"C-6室\",\"start_time\":1322211211211,\"end_time\":12312312312,\"check_credits\":20,\"teacher_credits\":30,\"judge_score\":25,\"lesson_record_modify_time\":1231221312123}},{\"type\":\"lesson\",\"operate\":\"insert\",\"detail\":{\"course_index\":2,\"lesson_index\":2,\"lesson_name\":\"Android基础二\",\"lesson_description\":\"Android Hello World!第二课\",\"user_id\":\"0016002946\",\"teacher_name\":\"李四\",\"locale\":\"C-6室\",\"start_time\":1322211211211,\"end_time\":12312312312,\"check_credits\":20,\"teacher_credits\":30,\"judge_score\":25,\"lesson_record_modify_time\":1231221312123}}]}";
//            try {
//                errorResponse = new JSONObject(jsonStr);
//                UpdateClassListHelper.updateAllClassData(errorResponse.getJSONArray("data"),mCourseItemList);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//           mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    };

    private class ExpandableListViewOnItemClickListener implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(AdminCourseAddTabActivity.this, AdminLessonDetailActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putSerializable("mCourseItem", mCourseItemList.get(arg2 - 1));
            intent.putExtra("value", bundle);
            startActivity(intent);
            Toast.makeText(AdminCourseAddTabActivity.this, "你点击了ExpandableListView的某条", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
