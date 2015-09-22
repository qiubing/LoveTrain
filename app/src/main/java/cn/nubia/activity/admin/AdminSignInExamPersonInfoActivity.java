package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.adapter.SignInExamPersonInfoAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminSignInExamPersonInfoActivity extends Activity implements View.OnClickListener{
    private LessonItem mLessonItem;
    private final static String URL = Constant.BASE_URL +"exam/exam_people_list.do";
    private ArrayList<Map.Entry<String,String>> mListData;
    private SignInExamPersonInfoAdapter mSignInExamPersonInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_course);
        ImageView backImageView = (ImageView) findViewById(R.id.admin_signIn_info_back);
        backImageView.setOnClickListener(this);
        mListData = new ArrayList<Map.Entry<String,String>>();

        Intent intent=getIntent();
        mLessonItem=(LessonItem)intent.getSerializableExtra("LessonItem");

        /**获取数据**/
        loadData();

        /**要填充数据的ListView**/
        ListView listView = (ListView) findViewById(R.id.score_course_list);

        mSignInExamPersonInfoAdapter = new SignInExamPersonInfoAdapter(mListData, this);
        /**设置填充ListView的Adapter**/
        listView.setAdapter(mSignInExamPersonInfoAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_signIn_info_back:
                Intent intentBackImage = new Intent(AdminSignInExamPersonInfoActivity.this,AdminLessonDetailActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
        }
    }

    private void loadData(){
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("exam_index", mLessonItem.getIndex());
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("test", "onSuccess");
            Log.e("test", "onSuccess" + response.toString());
            try {
                if(response.getInt("code") != 0){
                    return;
                }

                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray ==null || jsonArray.length() == 0)
                    return;

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("detail");
                    final String userName = jsonObject.getString("user_name");
                    final String userID = jsonObject.getString("user_id");
                    Map.Entry<String,String> entry = new AbstractMap.SimpleEntry<String, String>(userName,userID);
                    mListData.add(entry);
                }

                if(mListData.size() > 0){
                    mSignInExamPersonInfoAdapter.notifyDataSetChanged();
                }
                Log.i("huhu", jsonArray.toString());
                Log.e("test","onSuccess2");
            } catch (JSONException e) {
                Log.e("test","onSuccess3");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("test","onFailure");
        }
    };
}
