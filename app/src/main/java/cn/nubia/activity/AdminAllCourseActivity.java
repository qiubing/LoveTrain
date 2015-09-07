package cn.nubia.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hexiao on 2015/9/2.
 */
public class AdminAllCourseActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_course);

//        imageView=(ImageView)findViewById(R.id.next);
//        imageView.setClickable(true);
//        imageView.setFocusable(false);



        ExpandableListAdapter expandableListViewAdapter = new BaseExpandableListAdapter() {
            private String[] groupArray = {"Java", "linux", "C++基础"};
            //课时名称
            private String[][] childArray = {
                    {"Java基础一", "Java基础二", "Java实践"},
                    {"Linux基础一", "Linux基础二", "Linux实践"},
                    {"C++基础一", "C++基础二", "C++实践"}
            };
            //课时信息，和课时名称一一对应
            private String[][] childInfoArray = {
                    {"C2-6,2015年9月6日,14:53", "C2-3,2015年9月8日,14:53", "C2-6,2015年8月6日,20:15"},
                    {"C2-6,2015年9月6日,14:53", "C2-3,2015年9月8日,14:53", "C2-6,2015年8月6日,20:15"},
                    {"C2-6,2015年9月6日,14:53", "C2-3,2015年9月8日,14:53", "C2-6,2015年8月6日,20:15"}
            };

            /**
             * ***************************************child
             */
            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return childArray[groupPosition][childPosition];
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return childArray[groupPosition].length;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                //获得布局文件要使用LayoutInflater这个类
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout relativeLayoutChild = (RelativeLayout) layoutInflater.inflate(R.layout.lesson_info_item, null);
                //设置课时名称
                TextView mLessonNameTextView = (TextView) relativeLayoutChild.findViewById(R.id.item_layout_title);
                mLessonNameTextView.setText(getChild(groupPosition, childPosition).toString());
                //设置课时信息
                TextView mLessonInfoTextView = (TextView) relativeLayoutChild.findViewById(R.id.item_layout_content);
                mLessonInfoTextView.setText(childInfoArray[groupPosition][childPosition]);
                return relativeLayoutChild;
            }

            /**
             * ***************************************group
             */
            @Override
            public Object getGroup(int groupPosition) {
                return groupArray[groupPosition];
            }

            @Override
            public int getGroupCount() {
                return groupArray.length;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout relativeLayoutGroup = (RelativeLayout) layoutInflater.inflate(R.layout.class_info_item, null);
                TextView mCourseNameTextView = (TextView) relativeLayoutGroup.findViewById(R.id.item_layout_title);
                mCourseNameTextView.setText(groupArray[groupPosition].toString());
                return relativeLayoutGroup;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
        ExpandableListView adminAllCourseExpListView = (ExpandableListView) findViewById(R.id.allCourse_ExpandableListView);
        adminAllCourseExpListView.setAdapter(expandableListViewAdapter);
//        // 将child默认全都展开
//        int groupCount=adminAllCourseExpListView.getCount();
//        for(int i=0;i<groupCount;i++){
//            adminAllCourseExpListView.expandGroup(i);
//        }
    }
}
