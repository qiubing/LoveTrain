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
import cn.nubia.model.admin.User;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.HandleResponse;
import cn.nubia.util.MyJsonHttpResponseHandler;

public class AdminScoreUserActivity extends Activity {

    private List<User> list;
    private TextView mNoRecord;

    private final MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e("onSuccess", response.toString());
                handleData(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("onFailure", statusCode + "");
            Log.e("onFailure", throwable.toString());
        }
    };

    private void init() {
        //TODO
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_student.do";

        RequestParams params = new RequestParams();
        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("token_key", Constant.tokenKep);

        AsyncHttpHelper.post(url, params, myJsonHttpResponseHandler);
    }

    private void handleData(JSONObject response) throws JSONException {

        String code = response.getString("code");
        if (code.equals("0")) {
            String data = response.getString("data");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            list = gson.fromJson(data, listType);
            List<Map<String, Object>> listItems = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", list.get(i).getUserName());
                item.put("id", list.get(i).getUserID());
                listItems.add(item);
            }
            ListView listView = (ListView) findViewById(R.id.score_user_list);
            if (list.size() == 0) {
                mNoRecord.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                return;
            }
            mNoRecord.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_user_item,
                    new String[]{"name", "id"},
                    new int[]{R.id.score_user_name, R.id.score_user_id});
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = list.get(position).getUserName();
                    String userid = list.get(position).getUserID();
                    Intent intent = new Intent(AdminScoreUserActivity.this, AdminScoreUserDetailActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
            });
        } else {
            HandleResponse.excute(AdminScoreUserActivity.this, code, response.getString("message"));
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_user);
        mNoRecord = (TextView) findViewById(R.id.no_record);
        init();

    }

}
