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
import cn.nubia.model.admin.ExamUser;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;

public class AdminScoreCourseDetailActivity extends Activity {


    private ImageView mGoBack;
    private TextView mManagerTitle;
    private List<ExamUser> list;
    private TextView mNoRecord;

    private void init() {
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/find_score_course.do";

        int courseindex = getIntent().getIntExtra("courseindex",0);
        RequestParams params = new RequestParams();
        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("token_key", Constant.tokenKep);
        params.put("course_index", courseindex);

        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    handleData(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(AdminScoreCourseDetailActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.showToast(AdminScoreCourseDetailActivity.this, "连接服务器发生异常！");
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
            Type listType = new TypeToken<ArrayList<ExamUser>>() {
            }.getType();
            list = gson.fromJson(data, listType);
            List<Map<String, Object>> listItems = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", list.get(i).getUser_name());
                item.put("id", list.get(i).getUser_id());
                item.put("score", list.get(i).getExam_score());
                listItems.add(item);
            }
            ListView listView = (ListView) findViewById(R.id.manager_score_course_detail_listview);
            if(list.size()==0){
                mNoRecord.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                return;
            }
            mNoRecord.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item_detail,
                    new String[]{"name", "id", "score"},
                    new int[]{R.id.score_course_detail_name, R.id.score_course_detail_id, R.id.score_course_detail_score});
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogUtil.showDialog(AdminScoreCourseDetailActivity.this, list.get(position).getUser_name() +
                            " 《" + getIntent().getStringExtra("coursename") + "》的成绩为：" + list.get(position).getExam_score());
                }
            });
        } else {
            HandleResponse.excute(AdminScoreCourseDetailActivity.this, code,response.getString("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_course_detail);

        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText("考试成绩查询" + "/" + getIntent().getStringExtra("coursename"));
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNoRecord = (TextView) findViewById(R.id.no_record);

        init();

    }


}
