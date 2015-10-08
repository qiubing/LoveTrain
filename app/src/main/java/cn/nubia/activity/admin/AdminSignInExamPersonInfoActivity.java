package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.util.AsyncHttpHelper;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminSignInExamPersonInfoActivity extends Activity implements View.OnClickListener{
    private ExamItem mExamItem;
    private final static String URL = Constant.BASE_URL +"exam/exam_people_list.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_user_detail);
        initView();
        initEvent();
        /**获取数据**/
        loadData();
    }

    private void initView(){
        TextView barTxt = (TextView) findViewById(R.id.manager_head_title);
        barTxt.setText("报名考试人数");
    }

    private void initEvent(){
        mExamItem = (ExamItem) getIntent().getSerializableExtra("ExamIndex");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.manager_goback:
                finish();
                break;
        }
    }

    private void loadData(){
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("exam_index", mExamItem.getIndex());
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("wj","signExamPerson"+response.toString());

            try {
                if(response.getInt("code") != 0){
                    return;
                }
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray ==null || jsonArray.length() == 0)
                    return;
                List<Map<String, Object>> listItems =  new ArrayList<Map<String, Object>>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("detail");
                    Map<String, Object> listItem = new HashMap<>();
                    listItem.put("user_name",jsonObject.getString("user_name"));
                    listItem.put("user_id",jsonObject.getString("user_id"));
                    listItems.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(AdminSignInExamPersonInfoActivity.this,listItems,R.layout.layout_sign_item,
                        new String[]{"user_name","user_id"},new int[]{R.id.user_name,R.id.user_id});
                ListView listView = (ListView) findViewById(R.id.manager_score_user_detail_listview);
                listView.setAdapter(simpleAdapter);
                if(listItems.size()>0){
                    findViewById(R.id.no_record).setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("test","onFailure");
        }
    };

    public void back(View view){
        this.finish();
    }
}
