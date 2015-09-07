package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerScoreCourseActivity extends Activity {

    private String[] coursenames;
    private String[] address;

    private void inti() {
        //TODO
        coursenames = new String[]{"Java基础", "面向对象", "Android基础"};
        address = new String[]{"C2-6", "C2-2", "C6-6"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_course);
        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < coursenames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("coursename", coursenames[i]);
            item.put("address", address[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item,
                new String[]{"coursename", "address"},
                new int[]{R.id.score_course_coursename, R.id.score_course_address});
        ListView listView = (ListView) findViewById(R.id.score_course_list);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = coursenames[position];
                Intent intent = new Intent(ManagerScoreCourseActivity.this, ManagerScoreCourseDetailActivity.class);
                intent.putExtra("coursename", name);
                startActivity(intent);
            }
        });

    }

}
