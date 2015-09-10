package cn.nubia.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminAddLessonActivity;
import cn.nubia.activity.admin.AdminCourseDetailActivity;
import cn.nubia.entity.CourseItem;

/**
 * Created by hexiao on 2015/9/9.
 */
public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private List<CourseItem> mGroupList;
    private Context mContext;

    public CourseExpandableListAdapter(List<CourseItem> mCourseList, Context mCtx) {
        this.mGroupList = mCourseList;
        this.mContext = mCtx;
    }

    /**
     * ***************************************child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupList.get(groupPosition).getLessonList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupList.get(groupPosition).getLessonList().size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = View.inflate(mContext, R.layout.lesson_info_item, null);
            childViewHolder.mLessonNameTextView = (TextView) convertView.findViewById(R.id.item_layout_title);
            childViewHolder.mLessonDetailTextView = (TextView) convertView.findViewById(R.id.item_layout_content);

            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        //设置课时名称
        childViewHolder.mLessonNameTextView.setText(mGroupList.get(groupPosition).getLessonList().get(childPosition).getName());
        //设置课时信息
        childViewHolder.mLessonDetailTextView.setText(
                mGroupList.get(groupPosition).getLessonList().get(childPosition).getLocation() + mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime()
        );
        return convertView;
    }

    /**
     * ***************************************group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            //不能用LayoutInflater，要使用inflate
            convertView = View.inflate(mContext, R.layout.class_info_item, null);
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mAddLessonTextView = (TextView) convertView.findViewById(R.id.admin_all_course_addLessonTextView);
            groupViewHolder.mCourseDetailTextView = (TextView) convertView.findViewById(R.id.admin_all_course_courseDetailTextView);
            groupViewHolder.mCourseNameTextView = (TextView) convertView.findViewById(R.id.item_layout_title);
            groupViewHolder.mSignUpExamTextView = (TextView) convertView.findViewById(R.id.class_signUpExamTextView);
            /** 加 标记**/


            Log.e("mAddLessonTextView", "mAddLessonTextView" + groupViewHolder.mAddLessonTextView);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        //为 "添加课时" 的textView添加监听事件
        groupViewHolder.mAddLessonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddLesson = new Intent(mContext, AdminAddLessonActivity.class);
                mContext.startActivity(intentAddLesson);
            }
        });

        //为 "课程详细" 的textView添加监听事件
        groupViewHolder.mCourseDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCourseDetail = new Intent(mContext, AdminCourseDetailActivity.class);
                mContext.startActivity(intentCourseDetail);
            }
        });

        //为"报名考试" 的textview添加点击事件
        groupViewHolder.mSignUpExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*怎么getparent()？*/
//                Dialog signUpExamDialog = new AlertDialog.Builder(mContext)
//                        .setTitle("报名考试")
//                        .setMessage("确定报名考试？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //这里执行报名操作
//                                Toast.makeText(mContext, "报名XXX的考试成功", Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(mContext, "取消", Toast.LENGTH_LONG).show();
//                            }
//                        }).create();
//                signUpExamDialog.show();
                Toast.makeText(mContext, "报名考试成功", Toast.LENGTH_LONG).show();
            }
        });

        groupViewHolder.mCourseNameTextView.setText(mGroupList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    public class ChildViewHolder {
        TextView mLessonNameTextView;
        TextView mLessonDetailTextView;
        //TextView mSignUpExamTextView;
    }

    public class GroupViewHolder {
        TextView mCourseNameTextView;
        TextView mAddLessonTextView;
        TextView mCourseDetailTextView;
        TextView mSignUpExamTextView;
        /*four tags*/
        TextView mFlagShareLevel;
        TextView mFlagPerson;
        TextView mFlagShare;
        TextView mFlagExam;

    }
}
