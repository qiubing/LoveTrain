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

public class ManagerScoreUserActivity extends Activity {

    private String[] names;
    private String[] ids;

    private void inti() {
        //TODO
        names = new String[]{"张三", "李四", "王五"};
        ids = new String[]{"0016003347", "0016003348", "0016003349"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_score_user);
        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", names[i]);
            item.put("id", ids[i]);
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
                String name = names[position];
                Intent intent = new Intent(ManagerScoreUserActivity.this, ManagerScoreUserDetailActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

    }

}
