package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.component.ErrorHintView;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class AdminShareCheckTabActivity extends Activity {
    private ListView mListView;
    private CourseAdapter mCourseAdapter;
    private RefreshLayout mRefreshLayout;
    private ErrorHintView mErrorHintView;
    private LoadViewUtil mLoadViewUtil;
    private List<TechnologyShareCourseItem> mCourseList = new ArrayList<TechnologyShareCourseItem>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_unapproved_share_course);
        initViews();
        initEvents();
        loadData();
        //initViewLogic();

    }

    private void initViews(){
        mListView = (ListView) findViewById(R.id.admin_all_unapproved_share_course);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.unapproved_share_course_refresh);
        mErrorHintView = (ErrorHintView) findViewById(R.id.hintView_2);
        //mLoadViewUtil = new LoadViewUtil(this,mListView,handler);
    }

    private void initEvents(){
        mCourseList = new ArrayList<TechnologyShareCourseItem>();
        mListView.setOnItemClickListener(new CourseListOnItemClickListener());
    }

    MyJsonHttpResponseHandler mShareCheckHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("onSuccess", response.toString());
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mCourseList.add(makeTechnologyShareCourse(obj));
                }
                mCourseAdapter = new CourseAdapter(mCourseList, AdminShareCheckTabActivity.this);
                mListView.setAdapter(mCourseAdapter);
                Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("onFailure", throwable.toString());
        }
    };

    /**
     * 加载全部数据
     */
    private void loadData(){
        //获取请求参数
        HashMap<String ,String> param = new HashMap<String,String>();
        RequestParams request = Utils.toParams(param);
        String url = Constant.BASE_URL + "share/list_apply_course.do";
        AsyncHttpHelper.post(url, request, mShareCheckHandler);
    }

    public static TechnologyShareCourseItem makeTechnologyShareCourse(JSONObject jsonObject) throws JSONException {
        TechnologyShareCourseItem technology = new TechnologyShareCourseItem();
        technology.setmCourseName(jsonObject.getString("course_name"));
        technology.setmCourseIndex(jsonObject.getInt("course_index"));
        technology.setmCourseDescription(jsonObject.getString("course_description"));
        technology.setmCourseLevel(jsonObject.getInt("course_level"));
        technology.setmLocation(jsonObject.getString("location"));
        technology.setmUserName(jsonObject.getString("user_name"));
        technology.setmUserId(jsonObject.getString("user_id"));
        technology.setmStartTime(jsonObject.getLong("start_time"));
        technology.setmEndTime(jsonObject.getLong("end_time"));
        return technology;
    }


   /* private void initViewLogic(){

        *//**
         * for Debug  模拟第一次加载数据
         *//*
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
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
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
                        mRefreshLayout.showLoadFailedView(Constant.SHOW_FOOTER,
                                mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
                    }
                }, 1500);
            }
        });
    }

    *//**
     * For debug
     * *//*
    private void loadData(){
//        DataLoadUtil.queryClassInfoDataforGet("aa");
    }

    *//**
     * for debug
     * **//*
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
            List<TechnologyShareCourseItem> courseList = new ArrayList<TechnologyShareCourseItem>();
            *//**
             * For Debug
             *//*
            if (msg.what == 1){
                for (int i = 10;i < 30; i++){
                    TechnologyShareCourseItem courseItem = new TechnologyShareCourseItem();
                    courseItem.setmCourseIndex(i);
                    courseItem.setmCourseName("Java 基础课程");
                    courseItem.setmCourseLevel((short) 1);
                    courseItem.setmCourseDescription("Java学习课程");
                    courseList.add(0,courseItem);
                }
                mCourseList.addAll(courseList);
            }
            if (msg.what == 2){
                for(int i = 40; i < 50;i++){
                    TechnologyShareCourseItem courseItem = new TechnologyShareCourseItem();
                    courseItem.setmCourseIndex(i);
                    courseItem.setmCourseName("Android 基础课程");
                    courseItem.setmCourseLevel((short) 2);
                    courseItem.setmCourseDescription("Android 学习课程");
                    courseList.add(0,courseItem);
                }
                mCourseList.addAll(courseList);
            }
            //UpdateClassListHelper.binarySort(mCourseList);
            mCourseAdapter.notifyDataSetChanged();
        }
    };*/


    /**
     * @ClassName:
     * @Description: 实现点击列表选项监听
     * @Author: qiubing
     * @Date: 2015/9/9 15:12
     */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AdminShareCheckTabActivity.this, AdminShareCourseUnApprovedDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CourseInfo",mCourseList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



}
