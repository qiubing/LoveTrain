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
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import cn.nubia.model.User;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;
import cn.nubia.util.TestData;

public class AdminScoreUserActivity extends Activity {

    private List<User> list;

    private void init() {
        //TODO
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_student.do";

        RequestParams params = new RequestParams();
        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    handleData(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(AdminScoreUserActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.showToast(AdminScoreUserActivity.this, "连接服务器发生异常！");
                //TODO
                try {
                    handleData(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleData(JSONObject response) throws JSONException {
        //TODO
        if (response == null)
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
                    String userid = list.get(position).getUserID();
                    int userIndex = list.get(position).getUser_index();
                    Intent intent = new Intent(AdminScoreUserActivity.this, AdminScoreUserDetailActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("userid", userid);
                    intent.putExtra("userindex",userIndex);
                    startActivity(intent);
                }
            });
        } else {
            HandleResponse.excute(AdminScoreUserActivity.this, code);
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
