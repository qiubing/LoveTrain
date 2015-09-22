package cn.nubia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminAddLessonActivity;
import cn.nubia.activity.admin.AdminCourseDetailActivity;

import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;

/**
 * Created by hexiao on 2015/9/9.
 * 显示全部课程的CourseExpandableAdapter
 */
public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private List<CourseItem> mGroupList;
    private Context mContext;

    private CourseItem courseItem=new CourseItem();
    private LessonItem lessonItem=new LessonItem();


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
        /**空指针异常**/
        if(mGroupList.get(groupPosition).getLessonList() != null)
            return mGroupList.get(groupPosition).getLessonList().size();
        else
            return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int mGroupID=groupPosition;
        final int mChildID=childPosition;
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

        final Bundle bundle = new Bundle();
        /**这里传过去的lessonItem中没有任何数据*/
        lessonItem=mGroupList.get(mGroupID).getLessonList().get(mChildID);
//        Log.e("HEXIAOAAAA", mGroupList.get(mGroupID).getLessonList().get(mChildID).getIndex() + "+ExpandableListViewAA");
//        Log.e("HEXIAOAAAA", mGroupList.get(mGroupID).getLessonList().get(mChildID).getLessonName() + "+ExpandableListViewAA");
        bundle.putSerializable("LessonItem", lessonItem);

        Log.e("hexiao", mGroupList.get(mGroupID).getLessonList().get(mChildID).getIndex() + "+ExpandableListView");

//        /**设置课时点击事件*/
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AdminLessonDetailActivity.class);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//                Log.e("HEXIAOAAAA", mGroupList.get(mGroupID).getLessonList().get(mChildID).getIndex() + "+ExpandableListView");
//                Log.e("HEXIAOAAAA", mGroupList.get(mGroupID).getLessonList().get(mChildID).getLessonName() + "+ExpandableListView");
//            }
//        });
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
        final int groupID=groupPosition;
        if (convertView == null) {
            /**不能用LayoutInflater，要使用inflate**/
            convertView = View.inflate(mContext, R.layout.class_info_item, null);
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mAddLessonTextView = (TextView) convertView.findViewById(R.id.admin_all_course_addLessonTextView);
            groupViewHolder.mCourseDetailTextView = (TextView) convertView.findViewById(R.id.admin_all_course_courseDetailTextView);
            groupViewHolder.mCourseNameTextView = (TextView) convertView.findViewById(R.id.item_layout_title);
            groupViewHolder.mSignUpExamTextView = (TextView) convertView.findViewById(R.id.class_signUpExamTextView);

            /**four tags**/
            groupViewHolder.mCourseLevel = (TextView) convertView.findViewById(R.id.flag_share_level);
            groupViewHolder.mTeacher = (TextView) convertView.findViewById(R.id.flag_person);
            groupViewHolder.mCourseType = (TextView) convertView.findViewById(R.id.flag_share);
            groupViewHolder.mWhetherExam = (TextView) convertView.findViewById(R.id.flag_exam);


            /**
             * 四个标记的意思是：
             * a.部：课程级别，只有分享课程才有的标记
             * b.讲：是否是讲师，因此只有讲师才会出现这个标记
             * c.享：是否是分享课程，和“部”是同时出现的
             * d.考：表明该课程是否有考试
             * 如果不是管理员，学员只能看到“考”的标记
             * 讲师还可以看到“讲”的标记
             * 还有其他需要隐去的标记*/
            if (Constant.IS_ADMIN==false) {
                /**隐去添加课时标记*/
                groupViewHolder.mAddLessonTextView.setVisibility(View.GONE);

                /** 普通用户看不到“部”“享”不可见**/
                groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                groupViewHolder.mCourseType.setVisibility(View.GONE);
                /**如果hasExam属性为0表示没有考试，则将该标记也隐去，同时肯定就不用报名考试了*/
                if(mGroupList.get(groupPosition).hasExam()==false) {
                    groupViewHolder.mWhetherExam.setVisibility(View.GONE);
                    groupViewHolder.mSignUpExamTextView.setVisibility(View.GONE);
                }
                /** 如果不是讲师看不到“讲”**/
                if(!isTeacher(groupPosition)) {
                    groupViewHolder.mTeacher.setVisibility(View.GONE);
                }
            }
            /**如果是管理员*/
            else{
                /**如果不是分享课程,则隐去“享”，同时课程级别“部”也要隐去*/
                if(!mGroupList.get(groupPosition).getType().equals("2")){
                    groupViewHolder.mCourseType.setVisibility(View.GONE);
                    groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                }
                /**如果没有考试,则隐去“考”，同时隐藏报名考试标记*/
                if(mGroupList.get(groupPosition).hasExam()==false) {
                    groupViewHolder.mWhetherExam.setVisibility(View.GONE);
                    groupViewHolder.mSignUpExamTextView.setVisibility(View.GONE);
                }
                /**管理员不应该在管理员界面看到“讲”，即使它是讲师，也应该在普通账号里面看到*/
                groupViewHolder.mTeacher.setVisibility(View.GONE);
            }

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }


        /**为 "添加课时" 的textView添加监听事件**/
        groupViewHolder.mAddLessonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddLesson = new Intent(mContext, AdminAddLessonActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("CourseItem",mGroupList.get(groupID));
                intentAddLesson.putExtras(bundle);
                mContext.startActivity(intentAddLesson);
            }
        });

        /**为 "课程详细" 的textView添加监听事件**/
        groupViewHolder.mCourseDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCourseDetail = new Intent(mContext, AdminCourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CourseItem", mGroupList.get(groupID));
                intentCourseDetail.putExtras(bundle);
                mContext.startActivity(intentCourseDetail);
            }
        });

        /**为"报名考试" 的textview添加点击事件**/
        groupViewHolder.mSignUpExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**怎么getparent()？**/
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

    /**判断是否是讲师*/
    private boolean isTeacher(int groupPosition){
        ArrayList<LessonItem> mLessonList=(ArrayList<LessonItem>)mGroupList.get(groupPosition).getLessonList();
        if(mLessonList == null)
            return false;
        for(int i=0;i<mLessonList.size();i++){
            if(Constant.user.getUserID().equals(mLessonList.get(i).getTeacherID())){
                /**如果i找到最后一个LessonItem还不是讲师，说明当前登录者不是该课程下任何课程的讲师*/
                if(i==mLessonList.size()-1){
                    return false;
                }
            }
            else {
                break;
            }
        }
        return true;
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
    }

    static class GroupViewHolder {
        TextView mCourseNameTextView;
        TextView mAddLessonTextView;
        TextView mCourseDetailTextView;
        TextView mSignUpExamTextView;
        /**four tags**/
        TextView mCourseLevel;
        TextView mTeacher;
        TextView mCourseType;
        TextView mWhetherExam;

    }
}
