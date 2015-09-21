package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import cn.nubia.model.admin.CreditUser;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;

public class AdminCreditTotalActivity extends Activity {

    private List<CreditUser> list;
    private TextView mNoRecord;

    private void init() {
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "credit/find_total_credits.do";

        RequestParams params = new RequestParams();
        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("token_key", Constant.tokenKep);

        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    handleData(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(AdminCreditTotalActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.showToast(AdminCreditTotalActivity.this, "连接服务器发生异常！");
            }
        });
    }

    private void handleData(JSONObject response) throws JSONException {
        String code = response.getString("code");
        if (code.equals("0")) {
            String data = response.getString("data");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CreditUser>>() {
            }.getType();
            list = gson.fromJson(data, listType);
            List<Map<String, Object>> listItems = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", list.get(i).getUser_name());
                item.put("id", list.get(i).getUser_id());
                item.put("credit", list.get(i).getUser_total_credits());
                listItems.add(item);
            }
            ListView listView = (ListView) findViewById(R.id.credit_total_list);
            if (list.size() == 0) {
                mNoRecord.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                return;
            }
            mNoRecord.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.credit_total_item,
                    new String[]{"name", "id", "credit"},
                    new int[]{R.id.credit_total_username, R.id.credit_total_id, R.id.credit_totalcredit});

            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogUtil.showDialog(AdminCreditTotalActivity.this, list.get(position).getUser_name() + "("
                            + list.get(position).getUser_id() + ")的总积分为：" + list.get(position).getUser_total_credits());
                }
            });
        } else {
            HandleResponse.excute(AdminCreditTotalActivity.this, code, response.getString("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_credit_total);
        mNoRecord = (TextView) findViewById(R.id.no_record);
        init();
    }

}
