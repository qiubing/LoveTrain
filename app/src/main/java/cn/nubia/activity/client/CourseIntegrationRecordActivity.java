package cn.nubia.activity.client;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.util.Utils;

/**
 * @Author: qiubing
 * @Date: 2015/9/2 9:32
 */
public class CourseIntegrationRecordActivity extends Activity {

    private static final String TAG = "CourseIntegrationRecord";

    private String[] courses = new String[]{
            "Java基础一","Android开发一","OO思想",
            "Java基础二","Android开发二","OO思想",
            "Java基础三","Android开发三","OO思想",
            "Java基础四","Android开发四","OO思想",
            "Java基础四","Android开发四","OO思想"
    };

    private int[] scores = new int[]{
            10,20,30,
            20,30,40,
            30,40,60,
            40,50,70,
            40,50,70
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_integration_record);

        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_course_integration_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        TextView scoreText = (TextView) findViewById(R.id.show_total_course_integration);
        text.setText("课程积分记录");

        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
        int totalScore = 0;
        for(int i = 0; i < courses.length; i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("course",courses[i]);
            listItem.put("score",scores[i]);
            listItems.add(listItem);
            totalScore += scores[i];
        }
        Log.v(TAG, listItems.toString());

        scoreText.setText("截止到当前，您的积分总分为" + totalScore + "分" );
        SimpleAdapter adapter = new SimpleAdapter(this,listItems,
                R.layout.activity_user_course_integration_record_detail_item,
                new String[] {"course","score"},
                new int[] {R.id.course_title,R.id.course_score});
        ListView list = (ListView)findViewById(R.id.course_integration_detail);
        list.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(list);
    }
}
