package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nubia.activity.R;
import cn.nubia.adapter.SignUpManageAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.SignUpItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Created by hexiao on 2015/9/11.
 */
public class AdminSignUpManageActivity extends Activity {

    private ImageView backImageView;

    private ListView listView;

    ArrayList<String> listData;
    ArrayList<SignUpItem> signUpList;

    private TextView name;
    private Button agreeButton;
    private Button disagreeButton;
    private Intent intent;
    private CourseItem mCourseItem;

    private SignUpManageAdapter signUpManageAdapter;
    private Context ctx;

    private String signUpInfoUrl = Constant.BASE_URL + "enroll/list_enroll_users.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_manage);
        ctx = this;

        intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");

        /***构造请求参数*/
        HashMap<String,String> getClassParam = new HashMap<>();
        getClassParam.put("course_index", mCourseItem.getIndex() + "");
        RequestParams requestParams = Utils.toParams(getClassParam);
        Log.e("requestParams", requestParams.toString());
        AsyncHttpHelper.post(signUpInfoUrl, requestParams, jsonHttpResponseHandler);


        backImageView = (ImageView) findViewById(R.id.admin_signIn_manage_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackImage = new Intent(AdminSignUpManageActivity.this, AdminCourseDetailActivity.class);
                startActivity(intentBackImage);
                finish();
            }
        });
//        listData = getData();
        listData=new ArrayList<>();
        listView = (ListView) findViewById(R.id.admin_signIn_manage_listView);
        signUpManageAdapter=new SignUpManageAdapter(listData,ctx);
        listView.setAdapter(signUpManageAdapter);

    }

    /**请求课程数据服务器数据的Handler*/
    MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e("TEST statusCode", "" + statusCode);
                Log.e("TEST code",""+response.getInt("code"));
                if(response.getInt("code") != 0){
                    Log.e("TEST code2",""+response.getInt("code"));
                    Toast.makeText(AdminSignUpManageActivity.this, "请求出错", Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.getInt("code")==0 && response.getString("data")!=null) {
                    Toast.makeText(AdminSignUpManageActivity.this, "请求成功", Toast.LENGTH_LONG).show();
                    /**JsonArray，不用异步请求*/
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.e("SignUp","this is request"+jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listData.add(jsonObject.getString("user_name")+jsonObject.getString("user_id"));
                    }

                }
            } catch (JSONException e) {
                Log.e("TEST statusCode json",e.toString());
                e.printStackTrace();
                Toast.makeText(AdminSignUpManageActivity.this, "请求异常", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("TEST onFailure", ""+statusCode);
            Toast.makeText(AdminSignUpManageActivity.this, "请求异常", Toast.LENGTH_LONG).show();
        }
    };


//    //初始化数据
//    private ArrayList<String> getData() {
//        ArrayList<String> listData = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            listData.add("张" + i);
//        }
//        return listData;
//    }


}
