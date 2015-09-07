package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.CourseItem;

public class ManagerScoreUserActivity extends Activity {

    private String[] mUserNames;
    private String[] mIds;

    private void inti() {
        //TODO
        mUserNames = new String[]{"张三", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰",
                "张三丰", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰", "张三丰"};
        mIds = new String[]{"0016003347", "0016003348", "0016003349", "0016003347", "0016003348",
                "0016003349", "0016003347", "0016003348", "0016003349", "0016003347", "0016003348", "0016003349",
                "0016003348", "0016003349", "0016003347", "0016003348",
                "0016003349", "0016003347", "0016003348", "0016003349", "0016003347", "0016003348", "0016003349"};
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_user);
        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < mUserNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", mUserNames[i]);
            item.put("id", mIds[i]);
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
                String name = mUserNames[position];
                Intent intent = new Intent(ManagerScoreUserActivity.this, ManagerScoreUserDetailActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

    }

}
