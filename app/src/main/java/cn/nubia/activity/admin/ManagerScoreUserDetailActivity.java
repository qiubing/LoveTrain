package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class ManagerScoreUserDetailActivity extends Activity {

//    private TextView mTitleTextView;
    private String[] mCourseNames;
    private String[] mAddress;
    private int[] mScores;
    private TextView mManagerTitle;
    private ImageView mGoBack;

    private void inti() {
        //TODO
        mCourseNames = new String[]{"Java基础", "面向对象", "Android基础"};
        mScores = new int[]{90, 98, 88};
        mAddress = new String[]{"C2-6", "C2-2", "C6-6"};
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
//        mTitleTextView = (TextView) findViewById(R.id.manager_score_user_detail_title);
//        mTitleTextView.setText("考试成绩查询" + "/" + getIntent().getStringExtra("name"));

        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < mCourseNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("coursename", mCourseNames[i]);
            item.put("address", mAddress[i]);
            item.put("score", mScores[i]);
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
                DialogUtil.showDialog(ManagerScoreUserDetailActivity.this,getIntent().getStringExtra("name")+
                        " 《"+ mCourseNames[position]+"》的成绩为："+ mScores[position]);
            }
        });
    }


}
