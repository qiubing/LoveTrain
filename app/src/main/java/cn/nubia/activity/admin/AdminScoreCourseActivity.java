package cn.nubia.activity.admin;

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

import cn.nubia.activity.R;

public class AdminScoreCourseActivity extends Activity {

    private String[] mCourseNames;
    private String[] mAddress;

    private void inti() {
        //TODO
        mCourseNames = new String[]{"Java基础", "面向对象", "Android基础"};
        mAddress = new String[]{"C2-6", "C2-2", "C6-6"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_course);
        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < mCourseNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("coursename", mCourseNames[i]);
            item.put("address", mAddress[i]);
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
                String name = mCourseNames[position];
                Intent intent = new Intent(AdminScoreCourseActivity.this, AdminScoreCourseDetailActivity.class);
                intent.putExtra("coursename", name);
                startActivity(intent);
            }
        });

    }

}
