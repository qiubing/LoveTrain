package cn.nubia.adapter;

import android.app.Activity;
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
import cn.nubia.activity.admin.AdminLessonDetailActivity;

import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by hexiao on 2015/9/9.
 * 显示全部课程的CourseExpandableAdapter
 */
public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private final List<CourseItem> mGroupList;
    private final Context mContext;
    /**
     * 当前登录用户的ID
     */
    private final String mID;

    public CourseExpandableListAdapter(List<CourseItem> mCourseList, Context mCtx, String id) {
        this.mGroupList = mCourseList;
        this.mContext = mCtx;
        this.mID = id;
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
        if (mGroupList.get(groupPosition).getLessonList() != null)
            return mGroupList.get(groupPosition).getLessonList().size();
        else
            return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int mGroupID = groupPosition;
        final int mChildID = childPosition;
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

        /**设置课时名称*/
        childViewHolder.mLessonNameTextView.setText(mGroupList.get(groupPosition).getLessonList().get(childPosition).getName());
        /**设置课时信息*/
        childViewHolder.mLessonDetailTextView.setText("上课地点：" +
                        mGroupList.get(groupPosition).getLessonList().get(childPosition).getLocation() + "" + "\n上课时间：" +
                        TimeFormatConversion.toTimeDate(mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime()) +
                        "  " + TimeFormatConversion.toTime(mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime()) +
                        " ~ " + TimeFormatConversion.toTime(mGroupList.get(groupPosition).getLessonList().get(childPosition).getEndTime())
        );

        /**是否为管理员或者老师
         * 如果为管理员或者老师，则隐藏评价按钮
         * */
        if (Constant.IS_ADMIN || isTeacher(groupPosition)) {
            TextView evaluate = (TextView) convertView.findViewById(R.id.evaluateBtn);
            evaluate.setVisibility(View.GONE);
        }

        final Bundle bundle = new Bundle();
        /**这里传过去的lessonItem中没有任何数据*/
        LessonItem lessonItem = mGroupList.get(groupPosition).getLessonList().get(childPosition);
        bundle.putSerializable("LessonItem", lessonItem);

        Log.e("hexiao", mGroupList.get(mGroupID).getLessonList().get(mChildID).getIndex() + "+ExpandableListView");

        /**设置课时点击事件*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminLessonDetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("startActivity", ((Activity) mContext).getLocalClassName());
                mContext.startActivity(intent);
                Log.e("HEXIAOAAAA", mGroupList.get(mGroupID).getLessonList().get(mChildID).getIndex() + "+ExpandableListView");

            }
        });
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
        final int groupID = groupPosition;
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
             * a.部、科、团：课程级别，只有分享课程才有的标记
             * b.普、享、高：是否是分享课程，和“部”是同时出现的
             * c.讲：是否是讲师，因此只有讲师才会出现这个标记
             * d.考：表明该课程是否有考试
             * 显示规则：
             * 1.管理员和普通用户可以看到a,b,c,d四种标志，因为管理员可也能是讲师；
             * 2.普通用户看不到添加课时标记，管理员看不到报名考试。即使管理员想报名考试，应该是在他对应的普通用户账号里面看得到。
             * 3.根据课程的相应信息来隐藏；
             * */
            /**二者共同的部分**/
            /**1.处理a,b*/
            switch (mGroupList.get(groupPosition).getType()) {
                case "course":
                    groupViewHolder.mCourseType.setText("普");
                    groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                    break;
                case "share":
                    groupViewHolder.mCourseType.setText("享");
                    switch (mGroupList.get(groupPosition).getShareType()) {
                        case 1:
                            groupViewHolder.mCourseLevel.setText("部");
                            break;
                        case 2:
                            groupViewHolder.mCourseLevel.setText("科");
                            break;
                        case 3:
                            groupViewHolder.mCourseLevel.setText("团");
                            break;
                    }
                    break;
                case "senior":
                    groupViewHolder.mCourseType.setText("高");
                    groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                    break;
            }
            /**2.处理c:如果不是讲师看不到“讲”*/
            if (!isTeacher(groupPosition)) {
                groupViewHolder.mTeacher.setVisibility(View.GONE);
            }
            /**3.处理d:如果hasExam属性为false表示没有考试，则将该标记也隐去，同时肯定就不用报名考试了*/
            if (mGroupList.get(groupPosition).hasExam() == false) {
                groupViewHolder.mWhetherExam.setVisibility(View.GONE);
            }
            /**如果不是管理员，看不到添加课时标记*/
            if (Constant.IS_ADMIN == false) {
                groupViewHolder.mAddLessonTextView.setVisibility(View.GONE);
            }
            /**如果不是管理员，看不到添加课时标记*/
            if (Constant.IS_ADMIN == true) {
                groupViewHolder.mSignUpExamTextView.setVisibility(View.INVISIBLE);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("CourseItem", mGroupList.get(groupID));
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
                intentCourseDetail.putExtra("startActivity", ((Activity) mContext).getLocalClassName());
                mContext.startActivity(intentCourseDetail);
            }
        });

        /**为"报名考试" 的textview添加点击事件**/
        groupViewHolder.mSignUpExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**怎么getparent()？**/
                Toast.makeText(mContext, "报名考试成功", Toast.LENGTH_LONG).show();
            }
        });

        groupViewHolder.mCourseNameTextView.setText(mGroupList.get(groupPosition).getName());

        return convertView;
    }

    /**
     * 判断是否是讲师
     */
    private boolean isTeacher(int groupPosition){
        ArrayList<LessonItem> mLessonList=(ArrayList<LessonItem>)mGroupList.get(groupPosition).getLessonList();
        if(mLessonList == null)
            return false;
        Log.e("hexiao0924",mLessonList.size()+"");

        for(LessonItem lessonItem : mLessonList){
            Log.e("hexiao0924",mGroupList.get(groupPosition).getName()+":::"+lessonItem.getTeacherID()+"_____"+mID);
            if(mID.equals(lessonItem.getTeacherID())){
                return true;
            }
        }
        return false;
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
        /**
         * four tags*
         */
        TextView mCourseLevel;
        TextView mTeacher;
        TextView mCourseType;
        TextView mWhetherExam;

    }
}
