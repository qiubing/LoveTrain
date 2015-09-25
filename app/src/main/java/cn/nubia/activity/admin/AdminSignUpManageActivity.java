package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.SignUpManageAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.SignUpItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by hexiao on 2015/9/11.
 */
public class AdminSignUpManageActivity extends Activity {
    private static final String TAG = "SignUpManage";
    private ArrayList<SignUpItem> mSignUpList;
    private CourseItem mCourseItem;
    private SignUpManageAdapter mSignUpAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_manage);
        initEvents();
        loadData();
    }

    private void initEvents(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.admin_signin_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("报名管理");

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_signin);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_signin);
        RelativeLayout networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_signin);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        mSignUpList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.admin_signIn_manage_listView);
        mSignUpAdapter = new SignUpManageAdapter(mSignUpList, AdminSignUpManageActivity.this);
        mListView.setAdapter(mSignUpAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AdminSignUpManageActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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

    private void loadData(){
        Intent intent = getIntent();
        mCourseItem = (CourseItem) intent.getSerializableExtra("CourseItem");
        /***构造请求参数*/
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("course_index", mCourseItem.getIndex());
        Log.e(TAG, "params: " + params.toString());
        String signUpInfoUrl = Constant.BASE_URL + "enroll/list_enroll_users.do";
        AsyncHttpHelper.post(signUpInfoUrl, params, jsonHttpResponseHandler);
    }

    /**请求课程数据服务器数据的Handler*/
    private final MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e(TAG,"onSuccess: " + response.toString());
                if(response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null){
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                }else {
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(AdminSignUpManageActivity.this, "请求异常", Toast.LENGTH_LONG).show();
        }
    };

    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<SignUpItem>> {

        @Override
        protected List<SignUpItem> doInBackground(JSONArray... params) {
            List<SignUpItem> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    itemList.add(makeUpSignUpItem(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<SignUpItem> signUpItems) {
            super.onPostExecute(signUpItems);
            mSignUpList.clear();
            if (signUpItems != null && signUpItems.size() != 0) {
                mSignUpList.addAll(signUpItems);
            }
            mSignUpAdapter.notifyDataSetChanged();
            //Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度*/
        }
    }

    private SignUpItem makeUpSignUpItem(JSONObject object) throws JSONException {
        SignUpItem item = new SignUpItem();
        item.setCourseID(mCourseItem.getIndex());
        item.setUserName(object.getString("user_name"));
        item.setUserID(object.getString("user_id"));
        item.setIsEnroll(object.getBoolean("is_enroll"));
        return item;
    }

    public void back(View view) {
        this.finish();
    }
}
