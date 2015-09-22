package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.ClientShareCourseAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ShareCourseMsg;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Author: qiubing
 * Date: 2015/9/6 10:01
 */
public class ClientShareCourseActivity extends Activity {
    private static final String TAG = "ShareCourse";
    private List<ShareCourseMsg> mCourseList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_share_course);
        initEvents();

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
    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_share_course_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的课程分享");

        mCourseList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.share_course_detail);

        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "share/list_my_share.do";
        AsyncHttpHelper.post(url, params, mCheckRecordHandler);
    }

    private final MyJsonHttpResponseHandler mCheckRecordHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0) {
                    EntityFactoryGenerics factoryGenerics =
                            new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SHARECOURSE, response);
                    Log.e(TAG, "onSuccess" + response.toString());
                    mCourseList = (List<ShareCourseMsg>) factoryGenerics.get();
                    ClientShareCourseAdapter mAdapter = new ClientShareCourseAdapter(mCourseList, ClientShareCourseActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e(TAG, TAG + ":onFailure");
        }
    };

    /**
     * 返回箭头绑定事件，即退出该页面
     * param view
     */
    public void back(View view) {
        this.finish();
    }
}
