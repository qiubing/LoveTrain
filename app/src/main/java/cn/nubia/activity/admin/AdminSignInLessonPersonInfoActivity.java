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
import cn.nubia.entity.LessonItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by WJ on 2015/9/22.
 */
public class AdminSignInLessonPersonInfoActivity extends Activity implements View.OnClickListener{
    private LessonItem mLessonItem;
    private final static String URL = Constant.BASE_URL +"exam/check_list.do";
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
        ImageView backImageView = (ImageView) findViewById(R.id.manager_goback);
        backImageView.setOnClickListener(this);

        TextView barTxt = (TextView) findViewById(R.id.manager_head_title);
        barTxt.setText("签到人数");
    }

    private void initEvent(){
        mLessonItem = (LessonItem) getIntent().getSerializableExtra("LessonItem");
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
        requestParams.put("lesson_index", mLessonItem.getIndex());
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                    listItem.put("user_id", jsonObject.getString("user_id"));
                    if(!jsonObject.isNull("check_time")){
                        listItem.put("check_time",TimeFormatConversion.
                                toDateTime(Long.valueOf(jsonObject.getString("check_time"))));
                    }
                    listItems.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(AdminSignInLessonPersonInfoActivity.this,
                        listItems,R.layout.score_course_item_detail,
                        new String[]{"user_name","user_id","check_time"},
                        new int[]{R.id.score_course_detail_name,R.id.score_course_detail_id,R.id.score_course_detail_score});

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
            Log.e("test", "onFailure");
        }
    };

    public void back(View view){
        this.finish();
    }
}
