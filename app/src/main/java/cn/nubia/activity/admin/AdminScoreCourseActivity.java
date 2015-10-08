package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.model.Course;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.HandleResponse;

public class AdminScoreCourseActivity extends Activity {

    private List<Course> list;
    private TextView mNoRecord;

    private void init() {
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_course.do";

        RequestParams params = new RequestParams(Constant.getRequestParams());
//        params.put("device_id", Constant.devideID);
//        params.put("request_time", System.currentTimeMillis());
//        params.put("apk_version", Constant.apkVersion);
//        params.put("token_key", Constant.tokenKep);
        JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.e("onSuccess", response.toString());
                    handleData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("onFailure", statusCode + "");
                Log.e("onFailure", throwable.toString());
            }
        };
        AsyncHttpHelper.post(url, params, myJsonHttpResponseHandler);
    }

    private void handleData(JSONObject response) throws JSONException {

        String code = response.getString("code");
        if (code.equals("0")) {
            String data = response.getString("data");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Course>>() {
            }.getType();
            list = gson.fromJson(data, listType);
            List<Map<String, Object>> listItems = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("coursename", list.get(i).getCourse_name());
//                item.put("address", list.get(i).getCourse_name());
                listItems.add(item);
            }
            ListView listView = (ListView) findViewById(R.id.score_course_list);
            if (list.size() == 0) {
                mNoRecord.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                return;
            }
            mNoRecord.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

//            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item,
//                    new String[]{"coursename", "address"},
//                    new int[]{R.id.score_course_coursename, R.id.score_course_address});
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item,
                    new String[]{"coursename"},
                    new int[]{R.id.score_course_coursename});
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String coursename = list.get(position).getCourse_name();
                    int courseindex = list.get(position).getCourse_index();
                    Intent intent = new Intent(AdminScoreCourseActivity.this, AdminScoreCourseDetailActivity.class);
                    intent.putExtra("coursename", coursename);
                    intent.putExtra("courseindex", courseindex);
                    startActivity(intent);
                }
            });
        } else {
            HandleResponse.excute(AdminScoreCourseActivity.this, code, response.getString("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_course);
        mNoRecord = (TextView) findViewById(R.id.no_record);
        init();
    }

}
