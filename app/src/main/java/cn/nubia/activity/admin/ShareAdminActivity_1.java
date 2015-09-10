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

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.CourseItem;
import cn.nubia.util.DataLoadUtil;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ShareAdminActivity_1 extends Activity {
    private ListView mAllShareCourse;
    private CourseAdapter mCourseAdapter;
    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private LoadViewUtil mLoadViewUtil;
    private List<CourseItem> mCourseList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_unapproved_share_course);
        initViews();
        initEvents();
        initViewLogic();

    }

    private void initViews(){
        mAllShareCourse = (ListView) findViewById(R.id.admin_all_unapproved_share_course);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.unapproved_share_course_refresh);
        mErrorHintView = (ErrorHintView) findViewById(R.id.hintView_2);
        mLoadViewUtil = new LoadViewUtil(this,mErrorHintView,mAllShareCourse,handler);
    }

    private void initEvents(){
        mCourseAdapter = new CourseAdapter(mCourseList,this);
        mAllShareCourse.setAdapter(mCourseAdapter);
        mAllShareCourse.setOnItemClickListener(new CourseListOnItemClickListener());

    }

    private void initViewLogic(){

        /**
         * for Debug  模拟第一次加载数据
         */
        mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LOADING);
        Message msg = handler.obtainMessage();
        msg.what = 1;
        handler.sendMessage(msg);

        //设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //更新最新的数据
                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        //加载监听器
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mLoadViewUtil.showShortToast("加载更多");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataLoadUtil.setLoadViewUtil(mLoadViewUtil);
                        loadData();
                        mRefreshLayout.setLoading(false);
                    }
                }, 1500);
            }
        });
    }

    /**
     * For debug
     * */
    private void loadData(){
        DataLoadUtil.queryClassInfoDataforGet("aa");
    }

    /**
     * for debug
     * **/
    public void loadData(int page) {
        String url = "test" + page;
        DataLoadUtil.queryClassInfoDataforGet(url);
        Message msg = handler.obtainMessage();
        msg.what = 2;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<CourseItem> courseList = new ArrayList<>();
            /**
             * For Debug
             */
            if (msg.what == 1){
                for (int i = 10;i < 30; i++){
                    CourseItem courseItem = new CourseItem();
                    courseItem.setIndex(i);
                    courseItem.setName("Java 基础课程");
                    courseItem.setCourseCredits(20 + i * 5);
                    courseItem.setShareType((short)1);
                    courseItem.setDescription("Java学习课程");
                    courseList.add(0,courseItem);
                }
                mCourseList.addAll(courseList);
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LIST);
            }
            if (msg.what == 2){
                for(int i = 40; i < 50;i++){
                    CourseItem courseItem = new CourseItem();
                    courseItem.setIndex(i);
                    courseItem.setName("Android 讲义");
                    courseItem.setCourseCredits(30 + i * 5);
                    courseItem.setShareType((short)2);
                    courseItem.setDescription("Android学习课程");
                    courseList.add(0,courseItem);
                }
                mCourseList.addAll(courseList);
                mLoadViewUtil.showLoading(LoadViewUtil.VIEW_LIST);
            }
            UpdateClassListHelper.binarySort(mCourseList);
            mCourseAdapter.notifyDataSetChanged();
        }
    };

    /**
     * @ClassName: ShareAdminActivity_1
     * @Description: 实现点击列表选项监听
     * @Author: qiubing
     * @Date: 2015/9/9 15:12
     */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ShareAdminActivity_1.this, ShareCourseUnApprovedDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CourseInfo",mCourseList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



}
