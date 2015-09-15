package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.model.User;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.HandleResponse;
import cn.nubia.util.TestData;

public class AdminScoreUserActivity extends Activity {

    private List<User> list;

    private void init() {
        //TODO
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_student.do";
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handleData(errorResponse);
            }
        };
        Map<String, String> params = new HashMap<>();
        AsyncHttpHelper.post(url, params, handler);
    }

    private void handleData(JSONObject response) {
        try {
            response = TestData.getCourseUserData();
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
                SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_user_item,
                        new String[]{"name", "id"},
                        new int[]{R.id.score_user_name, R.id.score_user_id});
                ListView listView = (ListView) findViewById(R.id.score_user_list);
                listView.setAdapter(simpleAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = list.get(position).getUserName();
                        Intent intent = new Intent(AdminScoreUserActivity.this, AdminScoreUserDetailActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            } else {
                HandleResponse.excute(AdminScoreUserActivity.this, code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_user);

        init();

    }

}
