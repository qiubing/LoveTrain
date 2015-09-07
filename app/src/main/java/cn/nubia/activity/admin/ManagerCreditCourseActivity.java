package cn.nubia.activity.admin;

import android.app.Activity;
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
import cn.nubia.util.DialogUtil;

public class ManagerCreditCourseActivity extends Activity {

    private String[] mCourseNames;
    private String[] mAddress;
    private String[] mDate;
    private int[] mCredits;

    private void inti() {
        //TODO
        mCourseNames = new String[]{"Java基础一", "Android基础一", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰",
                "张三丰", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰", "张三丰"};
        mAddress = new String[]{"C2-2", "C2-6", "0016003349", "0016003347", "0016003348",
                "0016003349", "0016003347", "0016003348", "0016003349", "0016003347", "0016003348", "0016003349",
                "0016003348", "0016003349", "0016003347", "0016003348",
                "0016003349", "0016003347", "0016003348", "0016003349", "0016003347", "0016003348", "0016003349"};
        mDate = new String[]{"本周一晚七点","本周二晚七点","本周一晚七点","本周二晚七点","本周二晚七点","本周一晚七点","本周二晚七点",
                "本周二晚七点","本周一晚七点","本周二晚七点","本周二晚七点","本周一晚七点","本周二晚七点","本周二晚七点","本周一晚七点",
                "本周二晚七点","本周二晚七点","本周一晚七点","本周二晚七点","本周二晚七点","本周一晚七点","本周二晚七点","本周二晚七点",
                "本周一晚七点","本周二晚七点","本周二晚七点","本周一晚七点","本周二晚七点"};
        mCredits = new int[]{91, 92, 93, 94, 95, 96, 97, 98, 99, 100,
                101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117
                , 111, 112, 113, 114, 115, 116, 117};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_credit_total);
        List<Map<String, Object>> listItems = new ArrayList<>();
        inti();
        for (int i = 0; i < mCourseNames.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", mCourseNames[i]);
            item.put("id", mDate[i]+" "+ mAddress[i]);
            item.put("credit", mCredits[i]);
            listItems.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.credit_total_item,
                new String[]{"name", "id","credit"},
                new int[]{R.id.credit_total_username, R.id.credit_total_id,R.id.credit_totalcredit});
        ListView listView = (ListView) findViewById(R.id.credit_total_list);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogUtil.showDialog(ManagerCreditCourseActivity.this, mCourseNames[position] + "(" + mAddress[position] + ")的总积分为：" + mCredits[position]);
            }
        });

    }

}
