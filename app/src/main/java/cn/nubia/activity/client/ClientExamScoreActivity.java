package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * @Date: 2015/9/6 9:19
 */

public class ClientExamScoreActivity extends Activity {

    private static final String TAG = "ExamScore";

    private String[] courses = new String[]{
            "Java基础一","Android开发一","OO思想",
            "Java基础二","Android开发二","OO思想",
            "Java基础三","Android开发三","OO思想",
            "Java基础四","Android开发四","OO思想",
            "Java基础四","Android开发四","OO思想"
    };

    private String[] scores = new String[]{
            "10","20","30",
            "20","30","40",
            "30","40","未考试",
            "40","未考试","70",
            "40","50","未考试"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam_score);

        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_exam_score_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("考试成绩");

        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < courses.length; i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("course",courses[i]);
            listItem.put("score", scores[i]);
            listItems.add(listItem);
        }
        Log.v(TAG, listItems.toString());

        SimpleAdapter adapter = new SimpleAdapter(this,listItems,
                R.layout.activity_user_exam_score_detail_item,
                new String[] {"course","score"},
                new int[] {R.id.exam_course_title,R.id.exam_score_one});
        ListView list = (ListView)findViewById(R.id.exam_score_detail);
        list.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(list);
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
