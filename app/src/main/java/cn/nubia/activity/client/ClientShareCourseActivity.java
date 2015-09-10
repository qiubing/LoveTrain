package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
 * @Date: 2015/9/6 10:01
 */
public class ClientShareCourseActivity extends Activity {

    private static final String TAG = "ShareCourse";
    private ImageView mArrawImage;


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
        setContentView(R.layout.activity_user_share_course);

        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_share_course_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的课程分享");

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
                R.layout.activity_user_share_course_detail_item,
                new String[] {"course","date","time"},
                new int[] {R.id.share_course_title,R.id.share_course_date,R.id.share_course_time});
        ListView list = (ListView)findViewById(R.id.share_course_detail);
        list.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(list);

        /**
         * 为列表项绑定监听事件
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ListItem listItem = (ListItem) parent.getItemAtPosition(position);
                String course = courses[position];
                Intent intent = new Intent(ClientShareCourseActivity.this,
                        ClientMyShareCourseDetailDisplayActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
