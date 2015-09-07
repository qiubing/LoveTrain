package cn.nubia.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerScoreUserDetailActivity extends Activity {

    private TextView titleTV;
    private String[] coursenames;
    private String[] address;
    private int[] scores;

    private void inti() {
        //TODO
        coursenames = new String[]{"Java基础", "面向对象", "Android基础"};
        scores = new int[]{90, 98, 88};
        address = new String[]{"C2-6", "C2-2", "C6-6"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_user_detail);

        titleTV = (TextView) findViewById(R.id.manager_score_user_detail_title);
        titleTV.setText("考试成绩查询" + "/" + getIntent().getStringExtra("name"));

        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < coursenames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("coursename", coursenames[i]);
            item.put("address", address[i]);
            item.put("score", scores[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_user_item_detail,
                new String[]{"coursename", "address", "score"},
                new int[]{R.id.score_user_detail_coursename, R.id.score_user_detail_address, R.id.score_user_detail_score});
        ListView listView = (ListView) findViewById(R.id.manager_score_user_detail_listview);
        listView.setAdapter(simpleAdapter);

    }


}
