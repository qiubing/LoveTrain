package com.example.bing.lovetrain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @ClassName: MyCheckRecordActivity
* @Description: TODO
* @Author: qiubing
* @Date: 2015/9/2 9:26
*/ 
public class MyCheckRecordActivity extends Activity {
    private static final String TAG = "MyCheckRecordActivity";

    private String[] courses = new String[]{
            "Java基础一","Android开发一","OO思想"
    };

    private String dates[] = new String[]{
            "2015年8月21日","2015年8月23日","2015年7月26日"
    };
    private String times[] = new String[]{
            "15:33:21" ,"16:23:46","18:32:12"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checked_record);
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_check_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的签到记录");

        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < courses.length; i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("course",courses[i]);
            listItem.put("date",dates[i]);
            listItem.put("time",times[i]);
            listItems.add(listItem);
        }
        Log.v(TAG, listItems.toString());

        SimpleAdapter adapter = new SimpleAdapter(this,listItems,
                R.layout.activity_check_record_detail_item,
                new String[] {"course","date","time"},
                new int[] {R.id.check_course_title,R.id.check_record_date,R.id.check_record_time});
        ListView list = (ListView)findViewById(R.id.check_detail);
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
