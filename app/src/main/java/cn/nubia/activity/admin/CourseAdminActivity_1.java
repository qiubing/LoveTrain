package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class CourseAdminActivity_1 extends Activity {

    private static CourseAdminActivity_1 courseAdminActivity_1;

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

    Handler mHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (11 == msg.what) {
                Log.i("g", "ggghh");
                Dialog signUpExamDialog = new AlertDialog.Builder(getParent())
                        .setTitle("报名考试")
                        .setMessage("确定报名考试？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //这里执行报名操作
                                Toast.makeText(CourseAdminActivity_1.this, "报名XXX的考试成功", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CourseAdminActivity_1.this, "取消", Toast.LENGTH_LONG).show();
                            }
                        }).create();
                signUpExamDialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_course);
        courseAdminActivity_1 = this;

        ExpandableListAdapter expandableListViewAdapter = new BaseExpandableListAdapter() {
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

                //为"报名考试"添加点击事件


                //为 "添加课时" 的textView添加监听事件
                TextView addLessonTextView = (TextView) relativeLayoutGroup.findViewById(R.id.admin_all_course_addLessonTextView);
                addLessonTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentAddLesson = new Intent(CourseAdminActivity_1.this, AdminAddLessonActivity.class);
                        startActivity(intentAddLesson);
                    }
                });

                //为 "课程详细" 的textView添加监听事件
                TextView courseDetailTextView = (TextView) relativeLayoutGroup.findViewById(R.id.admin_all_course_courseDetailTextView);
                courseDetailTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCourseDetail = new Intent(CourseAdminActivity_1.this, AdminCourseDetailActivity.class);
                        startActivity(intentCourseDetail);
                    }
                });


                TextView signUpExamTextView = (TextView) relativeLayoutGroup.findViewById(R.id.class_signUpExamTextView);
                signUpExamTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message message = new Message();
                        message.what = 11;
                        mHandle.sendMessage(message);
                        Toast.makeText(CourseAdminActivity_1.this, "报名考试成功", Toast.LENGTH_LONG).show();
                        Log.i("g", "gggg");
                    }
                });
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

        adminAllCourseExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //String name = childArray[position];
                //intent.putExtra("name", name);
                Toast.makeText(CourseAdminActivity_1.this, "你点击了lesson detail", Toast.LENGTH_LONG).show();
                Intent intentLessonDetail = new Intent(CourseAdminActivity_1.this, AdminLessonDetailActivity.class);
                startActivity(intentLessonDetail);
                return false;
            }
        });
    }
}
