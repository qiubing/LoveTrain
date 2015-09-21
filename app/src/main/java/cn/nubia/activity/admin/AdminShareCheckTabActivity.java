package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class AdminShareCheckTabActivity extends Activity {
    private static final String TAG = "ShareCheck";
    private ListView mListView;
    private CourseAdapter mCourseAdapter;
    private List<TechnologyShareCourseItem> mCourseList;
    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_unapproved_share_course);
        initViews();
        initEvents();
        loadData();
    }

    private void initViews(){
        mListView = (ListView) findViewById(R.id.admin_all_unapproved_share_course);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_share);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_share);
        RelativeLayout networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_share);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

    }

    private void initEvents(){
        mCourseList = new ArrayList<>();
        mListView.setOnItemClickListener(new CourseListOnItemClickListener());

        mCourseAdapter = new CourseAdapter(mCourseList, AdminShareCheckTabActivity.this);
        mListView.setAdapter(mCourseAdapter);

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(AdminShareCheckTabActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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

    private final MyJsonHttpResponseHandler mShareCheckHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e(TAG,"onSuccess: " + response.toString());
            try {
                if (response != null && response.getJSONArray("data") != null) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
                    asyncParseJsonTask.execute(jsonArray);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }else {
                    Log.e(TAG,"VIEW_LOADFAILURE");
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "VIEW_LOADFAILURE");
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG, "onFailure: " + errorResponse.toString());
        }
    };


    private class AsyncParseJsonTask extends AsyncTask<JSONArray,Void,List<TechnologyShareCourseItem>> {
        @Override
        protected List<TechnologyShareCourseItem> doInBackground(JSONArray... params) {
            List<TechnologyShareCourseItem> CourseList = new ArrayList<TechnologyShareCourseItem>();
            for (int i = 0; i < params[0].length();i++){
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    CourseList.add(makeTechnologyShareCourse(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CourseList;
        }

        @Override
        protected void onPostExecute(List<TechnologyShareCourseItem> technologyShareCourseItems) {
            super.onPostExecute(technologyShareCourseItems);
            if(technologyShareCourseItems != null && technologyShareCourseItems.size()!=0){
                mCourseList.clear();
                mCourseList.addAll(technologyShareCourseItems);
            }
            mCourseAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载全部数据
     */
    private void loadData(){
        //获取请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        String SHARE_CHECK_URL = Constant.BASE_URL + "share/list_apply_course.do";
        AsyncHttpHelper.post(SHARE_CHECK_URL, params, mShareCheckHandler);
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


    /**
     * ClassName:
     * Description: 实现点击列表选项监听
     * Author: qiubing
     * Date: 2015/9/9 15:12
     */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AdminShareCheckTabActivity.this, AdminShareCourseUnApprovedDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CourseInfo", mCourseList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        Log.e("ShareCheck","onResume");
        super.onResume();
        loadData();
    }

}
