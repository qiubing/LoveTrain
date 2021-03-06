package cn.nubia.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminCourseDetailActivity;
import cn.nubia.activity.admin.AdminLessonDetailActivity;
import cn.nubia.activity.client.ClientMyCourseJudgeDetailFillActivity;
import cn.nubia.activity.client.ClientMyShareCourseDetailDisplayActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by hexiao on 2015/9/9.
 * 显示全部课程的CourseExpandableAdapter
 */
public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private final List<CourseItem> mGroupList;
    private final Context mContext;
    private String mParentClazz;
    /**
     * 当前登录用户的ID
     */
    private final String mID;
    private final String mName;

    public CourseExpandableListAdapter(List<CourseItem> mCourseList, Context mCtx, String id, String name) {
        this.mGroupList = mCourseList;
        this.mContext = mCtx;
        this.mID = id;
        this.mName = name;
    }

    public void setFragmentClazz(String parentClazz){
        mParentClazz = parentClazz;
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
        else {
//            mGroupList.get(groupPosition)
            return 0;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        final int mGroupID = groupPosition;
//        final int mChildID = childPosition;
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = View.inflate(mContext, R.layout.lesson_info_item, null);
            childViewHolder.mLessonNameTextView = (TextView) convertView.findViewById(R.id.item_layout_title);
            childViewHolder.mLessonDetailTextView = (TextView) convertView.findViewById(R.id.item_layout_content);
            childViewHolder.mLessonIcon = (ImageView) convertView.findViewById(R.id.item_layout_imageview);
            childViewHolder.mGotoEvaluateTV = (TextView) convertView.findViewById(R.id.goto_evaluate);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        /**设置课时名称*/
        childViewHolder.mLessonNameTextView.setText(mGroupList.get(groupPosition).getLessonList().get(childPosition).getName());
        switch (childPosition){
            case 0: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson1);break;
            case 1: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson2);break;
            case 2: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson3);break;
            case 3: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson4);break;
            case 4: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson5);break;
            case 5: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson6);break;
            case 6: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson7);break;
            case 7: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson8);break;
            case 8: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson9);break;
            case 9: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson10);break;
            default: childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson10);break;

        }
//        switch (mGroupList.get(groupPosition).getType()) {
//            case "course":
//                childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson_course);
//                break;
//            case "share":
//                childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson_share);
//                break;
//            case "senior":
//                childViewHolder.mLessonIcon.setImageResource(R.mipmap.icon_lesson_gao);
//                break;
//        }
        /**设置课时信息*/
        childViewHolder.mLessonDetailTextView.setText("地点：" +
                        mGroupList.get(groupPosition).getLessonList().get(childPosition).getLocation() + "" + "\n时间：" +
                        TimeFormatConversion.toTimeDate(mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime()) +
                        "  " + TimeFormatConversion.toTime(mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime()) +
                        " ~ " + TimeFormatConversion.toTime(mGroupList.get(groupPosition).getLessonList().get(childPosition).getEndTime())
        );

        /**是否为管理员或者老师
         * 如果为管理员或者老师，则隐藏评价按钮
         * */

        long startTime = mGroupList.get(groupPosition).getLessonList().get(childPosition).getStartTime();
        Log.e("system time ", System.currentTimeMillis() +(-startTime)
                +mGroupList.get(groupPosition).getLessonList().get(childPosition).getName());

        LinearLayout evaluate = (LinearLayout) convertView.findViewById(R.id.evaluateBtn);
        boolean isGone = false;
        if (Constant.IS_ADMIN
                || isTeacher(groupPosition,childPosition)
                || "cn.nubia.activity.client.fragment.CourseFragment".equals(mParentClazz) ) {//|| System.currentTimeMillis() < startTime
            evaluate.setVisibility(View.GONE);
            isGone = true;
            /*ImageView parentEvaluate = (ImageView) parent.findViewById(R.id.evaluateBtn);
            parentEvaluate.setVisibility(View.GONE);*/
        }
        if(mGroupList.get(groupPosition).getLessonList().get(childPosition).isIsJudged()) {
            evaluate.setVisibility(View.GONE);
            isGone = true;
        }
        if(mGroupList.get(groupPosition).getType().equals("share")) {
            evaluate.setVisibility(View.GONE);
            isGone = true;
        }
        if(!isGone){
            final LessonItem lessonItem = mGroupList.get(groupPosition).getLessonList().get(childPosition);
            childViewHolder.mGotoEvaluateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("LK","goto evaluate...");
                    if(lessonItem.getStartTime()>System.currentTimeMillis()){
                        Toast.makeText(mContext, "该课时尚未开始，不能评价！", Toast.LENGTH_SHORT).show();
                    }
                    else if(lessonItem.getEndTime()>System.currentTimeMillis() && lessonItem.getEndTime()>System.currentTimeMillis()){
                        Toast.makeText(mContext, "该课时尚未结束，不能评价！", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(mContext, ClientMyCourseJudgeDetailFillActivity.class);
                        intent.putExtra("lessonIndex", lessonItem.getIndex());
                        intent.putExtra("operate",CommunicateService.OperateType.INSERT);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        final Bundle bundle = new Bundle();
        /**这里传过去的lessonItem中没有任何数据,错有数据，但是没有讲师ID，huhu*/
        final LessonItem lessonItem = mGroupList.get(groupPosition).getLessonList().get(childPosition);
        bundle.putSerializable("LessonItem", lessonItem);

        /**设置课时点击事件*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lessonItem.getCourseType().equals("share")) {
                    Intent intent = new Intent(mContext, ClientMyShareCourseDetailDisplayActivity.class);
                    Bundle bundleShare = new Bundle();
                    bundleShare.putString("shareType", "share_lesson");
                    bundleShare.putSerializable("shareCourse", lessonItem);
                    intent.putExtras(bundleShare);
                    mContext.startActivity(intent);
                }else {
//                    Log.i("huhu", "notshare" + lessonItem.getCourseType()  + lessonItem.getName());
                    Intent intent = new Intent(mContext, AdminLessonDetailActivity.class);
//                    Log.e("jiangyu","before jump"+lessonItem.getIndex()+ String.valueOf(((LessonItem)(bundle.get("LessonItem"))).isIsJudged()));
                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivityForResult(intent, 2);
                }
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

            groupViewHolder.mCourseDetailTextView = (ImageView) convertView.findViewById(R.id.item_layout_imageview);
            groupViewHolder.mCourseNameTextView = (TextView) convertView.findViewById(R.id.item_layout_title);
//            groupViewHolder.mSignUpExamTextView = (TextView) convertView.findViewById(R.id.class_signUpExamTextView);
            groupViewHolder.mExpendedIV = (ImageView) convertView.findViewById(R.id.admin_all_course_courseDetailTextView);
            groupViewHolder.mCourseIconIV = (ImageView) convertView.findViewById(R.id.item_layout_imageview);
            groupViewHolder.mEvaluateView = (ImageView) convertView.findViewById(R.id.evaluateBtn);
            /**four tags**/
//            groupViewHolder.mCourseType = (TextView) convertView.findViewById(R.id.flag_courseType);
//            groupViewHolder.mCourseLevel = (TextView) convertView.findViewById(R.id.flag_shareLevel);
//            groupViewHolder.mTeacher = (TextView) convertView.findViewById(R.id.flag_isTeacher);
            groupViewHolder.mWhetherExam = (ImageView) convertView.findViewById(R.id.flag_wetherExam);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)convertView.getLayoutParams();
//        params.width =;
//        params.height = ;
//
//        convertView.setLayoutParams(params);

        /**
         * 四个标记的意思是：
         * a.普、享、高：是否是分享课程，和“部”是同时出现的
         * b.部、科、团：课程级别，只有分享课程才有的标记
         * c.讲：是否是讲师，因此只有讲师才会出现这个标记
         * d.考：表明该课程是否有考试
         * 显示规则：
         * 1.管理员可以看到a,b,d三种标志，普通用户可以看到a,b,c,d四种标志。管理员看不到“讲”，如果管理员是讲师，也是在其普通账户中才看得到。
         * 2.普通用户看不到添加课时标记，管理员看不到报名考试。即使管理员想报名考试，应该是在他对应的普通用户账号里面看得到。
         * 3.根据课程的相应信息来隐藏；
         * */

        /**课程类别和分享级别：不管是管理员还是普通用户，显示规则是一样的*/
        Log.e("0925hexiao","mID"+mID+","+ "Name:" + mGroupList.get(groupPosition).getName() + "," + "CourseType:" + mGroupList.get(groupPosition).getType() +
                "," + "ShareType:" + mGroupList.get(groupPosition).getShareType() + "," + "HasExam:" + mGroupList.get(groupPosition).hasExam() + "," + "IsTeacher:" + isTeacher(groupPosition));

        //设置课程评价按钮是否显示
        if (getChildrenCount(groupPosition) <= 0) {
            groupViewHolder.mEvaluateView.setVisibility(View.GONE);
        } else {
            groupViewHolder.mEvaluateView.setVisibility(View.GONE);
            for (int i = 0; i < mGroupList.get(groupPosition).getLessonList().size(); i++) {
                LessonItem item = mGroupList.get(groupPosition).getLessonList().get(i);
                if (!(item.getTeacherID().equals(mID)||item.getTeacherName().equals(mName))) {
                    if(!item.isIsJudged()) {
                        groupViewHolder.mEvaluateView.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
        if(Constant.IS_ADMIN){
            groupViewHolder.mEvaluateView.setVisibility(View.GONE);
        }
        //个人分享课程不显示评价按钮
        if(mGroupList.get(groupPosition).getType().equals("share")
                || "cn.nubia.activity.client.fragment.CourseFragment".equals(mParentClazz)){
            groupViewHolder.mEvaluateView.setVisibility(View.GONE);
        }

        //隐藏部科团这三个级别
//        groupViewHolder.mCourseType.setVisibility(View.GONE);
        switch (mGroupList.get(groupPosition).getType()) {
            case "course":
                groupViewHolder.mCourseIconIV.setImageResource(R.mipmap.icon_course);
//                groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                break;
            case "share":
//                groupViewHolder.mCourseLevel.setVisibility(View.VISIBLE);

                groupViewHolder.mCourseIconIV.setImageResource(R.mipmap.icon_share);
                /*switch (mGroupList.get(groupPosition).getShareType()) {
                    case 0:
                        groupViewHolder.mCourseLevel.setText("部");
                        break;
                    case 1:
                        groupViewHolder.mCourseLevel.setText("科");
                        break;
                    case 2:
                        groupViewHolder.mCourseLevel.setText("团");
                        break;
                    default:
                        Log.e("courseExp+shareType", mGroupList.get(groupPosition).getShareType() + "");
                        groupViewHolder.mCourseLevel.setText("???");
                        break;
                }*/
                break;
            case "senior":
                groupViewHolder.mCourseIconIV.setImageResource(R.mipmap.icon_gao);
//                groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                break;
            default:
                Log.e("courseExp+type", mGroupList.get(groupPosition).getType() + "");
//                groupViewHolder.mCourseType.setText("???");
//                groupViewHolder.mCourseLevel.setVisibility(View.GONE);
                break;
        }

        /**不管管理员还是普通用户，隐去“报名考试”标记*/
//        groupViewHolder.mSignUpExamTextView.setVisibility(View.INVISIBLE);
        /**不管是管理员还是普通用户，设置“考”标记的规则是一样的*/
        /**判断是否有考试*/
        if(mGroupList.get(groupPosition).hasExam() == false){
            /**如果没有考试，则隐去“考”*/
            groupViewHolder.mWhetherExam.setVisibility(View.GONE);
        }
        else
            groupViewHolder.mWhetherExam.setVisibility(View.VISIBLE);

        /**如果是管理员*/
//        if(Constant.IS_ADMIN){
//            /**管理员不用看到“讲”，隐去“讲”*/
//            groupViewHolder.mTeacher.setVisibility(View.GONE);
//        }
        /**如果不是管理员*/
//        else {
            /**隐藏添加课时标记*/
            /**判断是否是讲师*/

//            if(mContext.getClass().getName().equals("cn.nubia.activity.client.ClientAllCourseActivity")
//                    && isTeacher(groupPosition)){
//                /**不是讲师则隐去“讲”*/
//                groupViewHolder.mTeacher.setVisibility(View.VISIBLE);
//            }
//        }


        if(isExpanded){
            Bitmap img= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.new_go);
            Matrix matrix = new Matrix();
            matrix.postRotate(90); /*翻转90度*/
            int width = img.getWidth();
            int height =img.getHeight();
            img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
            groupViewHolder.mExpendedIV.setImageBitmap(img);
        }
        else {
            groupViewHolder.mExpendedIV.setImageResource(R.mipmap.new_go);
        }

        /**为 "添加课时" 的textView添加监听事件**/
//        groupViewHolder.mAddLessonTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentAddLesson = new Intent(mContext, AdminAddLessonActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("CourseItem", mGroupList.get(groupID));
//                intentAddLesson.putExtras(bundle);
//                mContext.startActivity(intentAddLesson);
//            }
//        });

        /**为 "课程详细" 的textView添加监听事件**/
        groupViewHolder.mCourseDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCourseDetail = new Intent(mContext, AdminCourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CourseItem", mGroupList.get(groupID));
                intentCourseDetail.putExtras(bundle);
                ((Activity) mContext).startActivityForResult(intentCourseDetail,1);
            }
        });

        /**为"报名考试" 的textview添加点击事件**/
       /* groupViewHolder.mSignUpExamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//**怎么getparent()？**//*
                Toast.makeText(mContext, "报名考试成功", Toast.LENGTH_LONG).show();
            }
        });*/

        groupViewHolder.mCourseNameTextView.setText(mGroupList.get(groupPosition).getName());
        return convertView;
    }


    /**
     * 判断是否是讲师
     */
    private boolean isTeacher(int groupPosition){
        ArrayList<LessonItem> mLessonList = (ArrayList<LessonItem>)mGroupList.get(groupPosition).getLessonList();
        if(mLessonList == null)
            return false;
        for(LessonItem lessonItem : mLessonList){
            Log.e("LK", "username:" + mName + "lesson Name:" + lessonItem.getName() + ",teacherid:" + lessonItem.getTeacherID()+ ",teachername:" + lessonItem.getTeacherName());
            if(lessonItem.getTeacherID().equals("-1")){
                if(lessonItem.getTeacherName().equals(mName))
                    return true;
            }else if(mID.equals(lessonItem.getTeacherID())){
                return true;
            }
        }
        return false;
    }

    private boolean isTeacher(int groupPosition, int childPosition) {
        if (groupPosition < 0 || childPosition < 0)
            return false;
        ArrayList<LessonItem> mLessonList = (ArrayList<LessonItem>) mGroupList.get(groupPosition).getLessonList();
        if (mLessonList == null)
            return false;
        LessonItem lessonItem = mLessonList.get(childPosition);
        Log.e("LK", "username:" + mName + "lesson Name:" + lessonItem.getName() + ",teacherid:" + lessonItem.getTeacherID());
        if (lessonItem.getTeacherID().equals("-1")) {
            if (lessonItem.getTeacherName().equals(mName))
                return true;
        } else if(mID.equals(lessonItem.getTeacherID())) {
            return true;
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
        ImageView mLessonIcon;
        TextView mGotoEvaluateTV;
    }

    static class GroupViewHolder {
        TextView mCourseNameTextView;
        ImageView mExpendedIV;
        ImageView mCourseDetailTextView;
        ImageView mCourseIconIV;
        ImageView mEvaluateView;
        /**
         * four tags*
         */
//        TextView mCourseLevel;
//        TextView mTeacher;
//        TextView mCourseType;
        ImageView mWhetherExam;
    }
}
