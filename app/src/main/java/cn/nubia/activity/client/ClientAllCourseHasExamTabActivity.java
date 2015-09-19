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

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminLessonDetailActivity;
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
import cn.nubia.util.Utils;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class ClientAllCourseHasExamTabActivity extends Activity {
    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private LoadViewUtil mLoadViewUtil;
    /*expandableListView*/
    private ExpandableListView mExpandableListView;
    /*adapter*/
    private CourseExpandableListAdapter mCourseExpandableListAdapter;
    /*用来存储courseItem的List*/
    private List<CourseItem> mCourseItemList;
    private String classUrl = Constant.BASE_URL + "course/get_courses_lessons2.do";


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
        mErrorHintView = (ErrorHintView) findViewById(R.id.admin_all_course_errorHintView);
    }

    protected void initEvents() {

        mCourseItemList = new ArrayList<>();
        mLoadViewUtil = new LoadViewUtil(ClientAllCourseHasExamTabActivity.this, mExpandableListView, mHandler);
        mLoadViewUtil.setNetworkFailedView(mRefreshLayout.getNetworkLoadFailView());
        /**生成ExpandableListAdapter*/
        mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, this);
        /**为ExpandableListView指定填充数据的adapter*/
        mExpandableListView.setAdapter(mCourseExpandableListAdapter);
        /**去掉箭头**/
        mExpandableListView.setGroupIndicator(null);
        /**项的监听事件**/
        mExpandableListView.setOnChildClickListener(new ExpandableListViewOnItemClickListener());


        /**请求课程数据*/
        HashMap<String,String> getClassParam = new HashMap<String,String>();
        getClassParam.put("user_id", "0016002946");
        getClassParam.put("course_index", "1");
        getClassParam.put("course_record_modify_time", "1245545456456");
        getClassParam.put("lesson_index", "1");
        getClassParam.put("lesson_record_modify_time", "1245545456456");
        RequestParams requestParams = Utils.toParams(getClassParam);
        Log.e("requestParams", requestParams.toString());
        AsyncHttpHelper.post(classUrl, requestParams, jsonHttpResponseHandler);


//        /*for Debug  模拟第一次加载数据*/
//        Message msg = mHandler.obtainMessage();
//        msg.what = 1;
//        mHandler.sendMessage(msg);

        /**设置下拉刷新监听器**/
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadViewUtil.showShortToast("刷新");
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /** 更新最新数据 **/
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

    /**请求课程数据服务器数据的Handler*/
    MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("ClassonSuccess", response.toString());
            try {
                if(response.getInt("code")==0) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.e("HasExam", mCourseItemList.size()+"" );
                    UpdateClassListHelper.updateAllClassData(jsonArray, mCourseItemList);
                    Log.e("HasExam", mCourseItemList.size() + "");
                    /**根据user_id，只保留有考试的课程*/
                    /**没有执行到这一步？*/
                    getExamList(mCourseItemList);
                    Log.e("HasExam", mCourseItemList.toString() );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCourseExpandableListAdapter.notifyDataSetChanged();
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("Class onSuccess", "onFailure");
        }
    };


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
//                    mCourseItem.setHasExam(true);
//                    mCourseList.add(0, mCourseItem);
//                    for (int j = 0; j < 3; j++) {
//                        LessonItem mLessonItem = new LessonItem();
//                        mLessonItem.setIndex(i);
//                        mLessonItem.setName("Java基础" + i);
////                        mLessonItem.setStartTime("2015.9..6");
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
////                        mLessonItem.setStartTime("2015.9..6");
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

    /**只保留自己是讲师的课程显示*/
    public void getExamList(List<CourseItem> mList){
        ArrayList<CourseItem> resultList=new ArrayList<>();
        if(mList.size()!=0){
            Iterator<CourseItem> it=mList.iterator();
            while(it.hasNext()){
                CourseItem courseItem=it.next();
                if(courseItem.hasExam()) {
                    resultList.add(courseItem);
                }
            }
        }
        mList.clear();
        mList.addAll(resultList);
    }



    private class ExpandableListViewOnItemClickListener implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(ClientAllCourseHasExamTabActivity.this, AdminLessonDetailActivity.class);
            intent.putExtra("status","student");
            Bundle bundle = new Bundle();
            //bundle.putSerializable("mCourseItem", mCourseItemList.get(arg2 - 1));
            intent.putExtra("value", bundle);
            startActivity(intent);
            Toast.makeText(ClientAllCourseHasExamTabActivity.this, "你点击了ExpandableListView的某条", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
