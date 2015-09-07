package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class ManagerScoreCourseDetailActivity extends Activity {

    private TextView mTitleTextView;
    private String[] mNames;
    private String[] mIds;
    private int[] mScores;

    private void inti() {
        //TODO
        mNames = new String[]{"张三", "李四", "王五"};
        mIds = new String[]{"0016003347", "0016003348", "0016003349"};
        mScores = new int[]{90, 98, 88};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_course_detail);

        mTitleTextView = (TextView) findViewById(R.id.manager_score_course_detail_title);
        mTitleTextView.setText("考试成绩查询" + "/" + getIntent().getStringExtra("coursename"));

        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < mNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", mNames[i]);
            item.put("id", mIds[i]);
            item.put("score", mScores[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.score_course_item_detail,
                new String[]{"name", "id", "score"},
                new int[]{R.id.score_course_detail_name, R.id.score_course_detail_id, R.id.score_course_detail_score});
        ListView listView = (ListView) findViewById(R.id.manager_score_course_detail_listview);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogUtil.showDialog(ManagerScoreCourseDetailActivity.this, mNames[position]+
                        " 《" + getIntent().getStringExtra("coursename") + "》的成绩为：" + mScores[position], false);
            }
        });
    }


}
