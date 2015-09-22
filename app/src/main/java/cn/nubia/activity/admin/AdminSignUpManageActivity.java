package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cn.nubia.activity.R;
import cn.nubia.adapter.SignUpManageAdapter;
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
    ArrayList<SignUpItem> mSignUpList;
    private CourseItem mCourseItem;
    private ListView mListView;
    private SignUpManageAdapter mSignUpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_manage);
        initEvents();
        loadData();
    }


    private void initEvents(){
        ImageView backImageView = (ImageView) findViewById(R.id.admin_signIn_manage_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackImage = new Intent(AdminSignUpManageActivity.this, AdminCourseDetailActivity.class);
                startActivity(intentBackImage);
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.admin_signIn_manage_listView);
        /*mSignUpList = getData();
        mSignUpAdapter = new SignUpManageAdapter(mSignUpList, AdminSignUpManageActivity.this);
        mListView.setAdapter(mSignUpAdapter);*/

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
    private MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e(TAG,"onSuccess: " + response.toString());
                if(response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null){
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        mSignUpList.add(makeUpSignUpItem(obj));
                    }
                    mSignUpAdapter = new SignUpManageAdapter(mSignUpList, AdminSignUpManageActivity.this);
                    mListView.setAdapter(mSignUpAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG,"onFailure: " );
            Toast.makeText(AdminSignUpManageActivity.this, "请求异常", Toast.LENGTH_LONG).show();
        }
    };

    private SignUpItem makeUpSignUpItem(JSONObject object) throws JSONException {
        SignUpItem item = new SignUpItem();
        item.setCourseID(mCourseItem.getIndex());
        item.setUserName(object.getString("user_name"));
        item.setUserID(object.getString("user_id"));
        item.setIsEnroll(object.getBoolean("is_enroll"));
        return item;
    }


    private ArrayList<SignUpItem> getData(){
        ArrayList<SignUpItem> listData=new ArrayList<>();
        for(int i = 0;i < 30;i++){
            SignUpItem item = new SignUpItem();
            item.setUserName("张" + i);
            item.setUserID("001600330" + i);
            item.setCourseID(2);
            item.setIsEnroll(false);
            listData.add(item);
        }
        return listData;
    }

}
