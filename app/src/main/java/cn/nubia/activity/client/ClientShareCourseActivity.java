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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.ClientShareCourseAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * @Author: qiubing
 * @Date: 2015/9/6 10:01
 */
public class ClientShareCourseActivity extends Activity {
    private static final String TAG = "ShareCourse";
    private List<ShareCourseItem> mCourseList;
    private ListView mListView;
    private ClientShareCourseAdapter mAdapter;

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
                ShareCourseItem course = mCourseList.get(position);
                Intent intent = new Intent(ClientShareCourseActivity.this,
                        ClientMyShareCourseDetailDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shareCourse",course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initEvents(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_share_course_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的课程分享");

        mCourseList = new ArrayList<ShareCourseItem>();
        mListView = (ListView)findViewById(R.id.share_course_detail);

        //请求参数
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("user_id", "0016002946");
        RequestParams request = Utils.toParams(param);
        String url = Constant.BASE_URL + "/share/list_my_share.do";
        AsyncHttpHelper.post(url, request, mCheckRecordHandler);
    }

    MyJsonHttpResponseHandler mCheckRecordHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("onSuccess", response.toString());
            EntityFactoryGenerics factoryGenerics =
                    new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SHARECOURSE, response);
            int code = factoryGenerics.getCode();
            if (code == 0){
                mCourseList = (List<ShareCourseItem>) factoryGenerics.get();
                mAdapter = new ClientShareCourseAdapter(mCourseList,ClientShareCourseActivity.this);
                mListView.setAdapter(mAdapter);
                Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("onFailure", throwable.toString());
        }
    };

    /*AsyncHttpResponseHandler mCheckRecordHandler = new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            Log.e(TAG,"onSuccess");
            String strJson = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"course_name\":\"Java基础一\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础二\",\"course_index\":1,\"course_description\":\"bbbb\",\"locale\":\"C2-6\",\"course_level\":2,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础三\",\"course_index\":1,\"course_description\":\"cccc\",\"locale\":\"C2-6\",\"course_level\":3,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础五\",\"course_index\":1,\"course_description\":\"dddd\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础六\",\"course_index\":1,\"course_description\":\"eeee\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础七\",\"course_index\":1,\"course_description\":\"ffff\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础八\",\"course_index\":1,\"course_description\":\"gggg\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础九\",\"course_index\":1,\"course_description\":\"hhhh\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十一\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十二\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十三\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十四\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十五\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}," +
                    "{\"course_name\":\"Java基础十六\",\"course_index\":1,\"course_description\":\"aaaaa\",\"locale\":\"C2-6\",\"course_level\":1,\"start_time\":1442261016111,\"end_time\":1452261016111}]}";
            try {
                JSONObject result = new JSONObject(strJson);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.SHARECOURSE, result);
                int code = factoryGenerics.getCode();
                if (code == 0){
                    mCourseList = (List<ShareCourseItem>) factoryGenerics.get();
                    mAdapter = new ClientShareCourseAdapter(mCourseList,ClientShareCourseActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.e(TAG, "onFailure");
        }
    };*/

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
