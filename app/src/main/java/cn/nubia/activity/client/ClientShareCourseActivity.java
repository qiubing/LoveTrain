package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import cn.nubia.adapter.ClientShareCourseAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ShareCourseMsg;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;


/**
 * Author: qiubing
 * Date: 2015/9/6 10:01
 */
public class ClientShareCourseActivity extends Activity {
    private static final String TAG = "ShareCourse";
    private List<ShareCourseMsg> mCourseList;
    private ClientShareCourseAdapter mAdapter;

    private RefreshLayout mRefreshLayout;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_share_course);
        initEvents();
        loadData();
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_share_course_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的课程分享");

        mCourseList = new ArrayList<>();
        ListView mListView = (ListView) findViewById(R.id.share_course_detail);
        mAdapter = new ClientShareCourseAdapter(mCourseList, ClientShareCourseActivity.this);
        mListView.setAdapter(mAdapter);

        /**
         * 为列表项绑定监听事件
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShareCourseMsg course = mCourseList.get(position);
                Intent intent = new Intent(ClientShareCourseActivity.this,
                        ClientMyShareCourseDetailDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shareCourse", course);
                bundle.putString("source", "myupdate");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRefreshLayout = (RefreshLayout) findViewById(R.id.evaluate_refreshLayout_sharecourse);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed_sharecourse);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable_sharecourse);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientShareCourseActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private final MyJsonHttpResponseHandler mCheckRecordHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
                    Log.e(TAG, "onSuccess" + response.toString());
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
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };

    private void loadData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "share/list_my_share.do";
        AsyncHttpHelper.post(url, params, mCheckRecordHandler);
    }


    private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<ShareCourseMsg>> {

        @Override
        protected List<ShareCourseMsg> doInBackground(JSONArray... params) {
            List<ShareCourseMsg> itemList = new ArrayList<>();
            for (int i = 0; i < params[0].length(); i++) {
                JSONObject obj;
                try {
                    obj = params[0].getJSONObject(i);
                    itemList.add(makeShareCourse(obj));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ShareCourseMsg> shareCourseItems) {

            mCourseList.clear();
            if (shareCourseItems != null && shareCourseItems.size() != 0) {
                mCourseList.addAll(shareCourseItems);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private ShareCourseMsg makeShareCourse(JSONObject jsonObject) throws JSONException {
        ShareCourseMsg course = new ShareCourseMsg();
        course.setCourseName(jsonObject.getString("course_name"));
        course.setCourseIndex(jsonObject.getInt("course_index"));
        course.setCourseDescription(jsonObject.getString("course_description"));
        course.setCourseLevel(jsonObject.getInt("course_level"));
        course.setLocale(jsonObject.getString("locale"));
        course.setStartTime(jsonObject.getLong("start_time"));
        course.setEndTime(jsonObject.getLong("end_time"));
        return course;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
