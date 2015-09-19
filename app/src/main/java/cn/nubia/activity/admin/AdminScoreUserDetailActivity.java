package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import cn.nubia.model.admin.Exam;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;

public class AdminScoreUserDetailActivity extends Activity {

    private List<Exam> list;
    private TextView mManagerTitle;
    private ImageView mGoBack;

    private void init() {
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_score_student.do";

        String userID = getIntent().getStringExtra("userid");
        RequestParams params = new RequestParams();
        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("token_key", Constant.tokenKep);
        params.put("user_id",userID);

        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    handleData(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(AdminScoreUserDetailActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.showToast(AdminScoreUserDetailActivity.this, "连接服务器发生异常！");
            }
        });
    }

    private void handleData(JSONObject response) throws JSONException {
//        if (null == response)
//            response = TestData.getCourseUserDetailData();
        String code = response.getString("code");
        if (code.equals("0")) {
            String data = response.getString("data");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Exam>>() {
            }.getType();
            list = gson.fromJson(data, listType);
            List<Map<String, Object>> listItems = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("coursename", list.get(i).getExam_name());
                item.put("address", list.get(i).getExam_name());
                item.put("score", list.get(i).getExam_score());
                listItems.add(item);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_user_item_detail,
                    new String[]{"coursename", "address", "score"},
                    new int[]{R.id.score_user_detail_coursename, R.id.score_user_detail_address, R.id.score_user_detail_score});
            ListView listView = (ListView) findViewById(R.id.manager_score_user_detail_listview);
            listView.setAdapter(simpleAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogUtil.showDialog(AdminScoreUserDetailActivity.this, getIntent().getStringExtra("name") +
                            " 《" + list.get(position).getExam_name() + "》的成绩为：" + list.get(position).getExam_score());
                }
            });
        } else {
            HandleResponse.excute(AdminScoreUserDetailActivity.this, code,response.getString("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_user_detail);

        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText("考试成绩查询" + "/" + getIntent().getStringExtra("name"));
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }


}
