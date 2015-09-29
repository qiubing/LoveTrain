package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientMyShareCourseDetailDisplayActivity;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class AdminSharePassTabActivity extends Activity {
    private static final String TAG = "SharePass";
    private ListView mListView;
    private CourseAdapter mCourseAdapter;
    private List<TechnologyShareCourseItem> mCourseList;
    private RefreshLayout mRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_approved_share_course);
        initViews();
        initEvents();
        loadData();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.admin_all_approved_share_course);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_pass);
        RelativeLayout loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed_pass);
        RelativeLayout networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_pass);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
    }

    private void initEvents() {
        mCourseList = new ArrayList<>();
        mListView.setOnItemClickListener(new CourseListOnItemClickListener());
        mCourseAdapter = new CourseAdapter(mCourseList, AdminSharePassTabActivity.this);
        mListView.setAdapter(mCourseAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AdminSharePassTabActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    private void loadData() {
        //获取请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        String url = Constant.BASE_URL + "share/list_going_course.do";
        AsyncHttpHelper.post(url, params, mSharePassHandler);
        mCourseAdapter = new CourseAdapter(mCourseList, this);
        mListView.setAdapter(mCourseAdapter);
    }

    private final MyJsonHttpResponseHandler mSharePassHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                    cancelLoadShow();
//                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
                cancelLoadShow();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            cancelLoadShow();
        }
    };


    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<TechnologyShareCourseItem>> {

        @Override
        protected List<TechnologyShareCourseItem> doInBackground(JSONArray... params) {
            List<TechnologyShareCourseItem> courseList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    courseList.add(AdminShareCheckTabActivity.makeTechnologyShareCourse(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(courseList);
            return courseList;
        }

        @Override
        protected void onPostExecute(List<TechnologyShareCourseItem> technologyShareCourseItems) {
            super.onPostExecute(technologyShareCourseItems);
            mCourseList.clear();
            if (technologyShareCourseItems != null && technologyShareCourseItems.size() != 0) {
                mCourseList.addAll(technologyShareCourseItems);
            }
            mCourseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ClassName:
     * Description: 实现点击列表选项监听
     * Author: qiubing
     * Date: 2015/9/9 15:12
     */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AdminSharePassTabActivity.this, ClientMyShareCourseDetailDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("source", "adminupdate");
            bundle.putSerializable("shareCourse", mCourseList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /*private void loadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.SHARE_WAITE);
        intent.putExtra(Constant.SHARE, "visibleOk");
        LocalBroadcastManager.getInstance(AdminSharePassTabActivity.this).sendBroadcast(intent);

    }*/
    private void cancelLoadShow() {
        Intent intent = new Intent();
        intent.setAction(Constant.SHARE_OK);
        intent.putExtra(Constant.SHARE, "goneOk");
        LocalBroadcastManager.getInstance(AdminSharePassTabActivity.this).sendBroadcast(intent);
        Log.i("huhu", "已通过广播发出");
    }
}
