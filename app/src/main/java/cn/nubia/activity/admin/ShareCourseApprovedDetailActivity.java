package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.nubia.activity.R;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.model.ShareCourseLevelModel;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/10 9:23
 */
public class ShareCourseApprovedDetailActivity extends Activity {
    private TextView mCourseName;
    private TextView mCourseLevel;
    private TextView mCourseDate;
    private TextView mCourseStartTime;
    private TextView mCourseEndTime;
    private TextView mCourseLocale;
    private TextView mCourseDescription;
    private Button mCourseModifyButton;
    private CourseItem mApprovedCourseItem;
    private LessonItem mApprovedCourseLessonItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_share_course_detail);
        initViews();
        initEvents();

        mCourseModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCourseApprovedDetailActivity.this,
                        ShareCourseApprovedModifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ModifiedCourseInfo", mApprovedCourseItem);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        mCourseName = (TextView) findViewById(R.id.approved_share_course_name);
        mCourseLevel = (TextView) findViewById(R.id.approved_share_course_level);
        mCourseDate = (TextView) findViewById(R.id.approved_share_course_date);
        mCourseLocale = (TextView) findViewById(R.id.approved_share_course_locale);
        mCourseStartTime = (TextView) findViewById(R.id.approved_share_course_begin_time);
        mCourseEndTime = (TextView) findViewById(R.id.approved_share_course_end_time);
        mCourseDescription = (TextView) findViewById(R.id.approved_share_course_description);
        mCourseModifyButton = (Button) findViewById(R.id.btn_modify_share_course);
    }

    private void initEvents(){


        //获取上一个Activity发送的Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mApprovedCourseItem = (CourseItem) bundle.getSerializable("CourseInfo");
        //mApprovedCourseLessonItem = (LessonItem) mApprovedCourseItem.getLessonList().get(0);
        mCourseName.setText(mApprovedCourseItem.getName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get(mApprovedCourseItem.getShareType()));
        //mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mApprovedCourseLessonItem.getStartTime()));
//        mCourseStartTime.setText(
//                new SimpleDateFormat("HH:mm").format(mApprovedCourseLessonItem.getStartTime()));
//        mCourseEndTime.setText(
//                new SimpleDateFormat("HH:mm").format(mApprovedCourseLessonItem.getEndTime()));
//        mCourseLocale.setText(mApprovedCourseLessonItem.getLocation());
        mCourseDescription.setText(mApprovedCourseItem.getDescription());
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
