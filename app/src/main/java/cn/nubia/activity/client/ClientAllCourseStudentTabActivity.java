package cn.nubia.activity.client;

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

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminLessonDetailActivity;
import cn.nubia.adapter.CourseExpandableListAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.DataLoadUtil;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ClientAllCourseStudentTabActivity extends Activity {

    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private LoadViewUtil mLoadViewUtil;


    /*expandableListView*/
    private ExpandableListView mExpandableListView;

    /*adapter*/
    private CourseExpandableListAdapter mCourseExpandableListAdapter;

    /*用来存储courseItem的List*/
    private List<CourseItem> mCourseItemList;

    /*三个textview*/
//    private TextView mAddLessonTextView;
//    private TextView mCourseDetailTextView;
//    private TextView mSignUpExamTextView;


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
        Log.e("test", "mRefreshLayout" + mRefreshLayout);
        mErrorHintView = (ErrorHintView) findViewById(R.id.admin_all_course_errorHintView);

        /*初始化三个TextView*/
//        mAddLessonTextView = (TextView) findViewById(R.id.admin_all_course_addLessonTextView);
//        mCourseDetailTextView = (TextView) findViewById(R.id.admin_all_course_courseDetailTextView);
//        mSignUpExamTextView = (TextView) findViewById(R.id.class_signUpExamTextView);
    }

    protected void initEvents() {

        mCourseItemList = new ArrayList<>();

        mLoadViewUtil = new LoadViewUtil(this, mErrorHintView, mExpandableListView, mHandler);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());

        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this);

        //为ExpandableListView指定填充数据的adapter
        mExpandableListView.setAdapter(mCourseExpandableListAdapter);

        /*去掉箭头*/
        mExpandableListView.setGroupIndicator(null);

        /*项的监听事件*/
        mExpandableListView.setOnChildClickListener(new ExpandableListViewOnItemClickListener());

        /*for Debug  模拟第一次加载数据*/
        mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADING);
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        mHandler.sendMessage(msg);

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
                        mRefreshLayout.showNetworkFailedHeader(mLoadViewUtil.getNetworkFailedFlag());
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
                        mRefreshLayout.showNetworkFailedHeader(mLoadViewUtil.getNetworkFailedFlag());
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
                for (int i = 10; i < 30; i++) {
                    CourseItem mCourseItem = new CourseItem();
                    mCourseItem.setIndex(i);
                    mCourseItem.setName("Java基础");
                    mCourseList.add(0, mCourseItem);
                    for (int j = 0; j < 3; j++) {
                        LessonItem mLessonItem = new LessonItem();
                        mLessonItem.setIndex(i);
                        mLessonItem.setName("Java基础" + i);
                        mLessonItem.setStartTime("2015.9..6");
                        mLessonItem.setLocation("C-2");
                        mLessonList.add(0, mLessonItem);
                    }
                    mCourseItem.setLessonList(mLessonList);
                }
                mCourseItemList.addAll(mCourseList);
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LIST);
            }
            if (msg.what == 2) {
                for (int i = 40; i < 50; i++) {
                    CourseItem mCourseItem = new CourseItem();
                    mCourseItem.setIndex(i);
                    mCourseItem.setName("Android基础");
                    mCourseList.add(0, mCourseItem);
                    for (int j = 0; j < 3; j++) {
                        LessonItem mLessonItem = new LessonItem();
                        mLessonItem.setIndex(i);
                        mLessonItem.setName("Android基础" + i);
                        mLessonItem.setStartTime("2015.9..6");
                        mLessonItem.setLocation("C-2");
                        mLessonList.add(0, mLessonItem);
                    }
                    mCourseItem.setLessonList(mLessonList);
                }
                mCourseItemList.addAll(mCourseList);
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LIST);
            }
            UpdateClassListHelper.binarySort(mCourseItemList);
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }
    };

    private class ExpandableListViewOnItemClickListener implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(ClientAllCourseStudentTabActivity.this, AdminLessonDetailActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putSerializable("mCourseItem", mCourseItemList.get(arg2 - 1));
            intent.putExtra("value", bundle);
            startActivity(intent);
            Toast.makeText(ClientAllCourseStudentTabActivity.this, "你点击了ExpandableListView的某条", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
