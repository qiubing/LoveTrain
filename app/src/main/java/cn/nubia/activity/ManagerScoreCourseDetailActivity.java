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

public class ManagerScoreCourseDetailActivity extends Activity {

    private TextView titleTV;
    private String[] names;
    private String[] ids;
    private int[] scores;

    private void inti() {
        //TODO
        names = new String[]{"张三", "李四", "王五"};
        ids = new String[]{"0016003347", "0016003348", "0016003349"};
        scores = new int[]{90, 98, 88};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_course_detail);

        titleTV = (TextView) findViewById(R.id.manager_score_course_detail_title);
        titleTV.setText("考试成绩查询" + "/" + getIntent().getStringExtra("coursename"));

        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", names[i]);
            item.put("id", ids[i]);
            item.put("score", scores[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item_detail,
                new String[]{"name", "id", "score"},
                new int[]{R.id.score_course_detail_name, R.id.score_course_detail_id, R.id.score_course_detail_score});
        ListView listView = (ListView) findViewById(R.id.manager_score_course_detail_listview);
        listView.setAdapter(simpleAdapter);

    }


}
