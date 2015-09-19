package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cn.nubia.activity.R;
import cn.nubia.entity.ShareCourseLevelModel;
import cn.nubia.entity.TechnologyShareCourseItem;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/10 14:43
 */
public class AdminShareCourseUnApprovedDetailActivity extends Activity implements View.OnClickListener {
    private TextView mCourseName;
    private TextView mCourseLevel;
    private TextView mCourseDate;
    private TextView mCourseStartTime;
    private TextView mCourseEndTime;
    private TextView mCourseLocale;
    private TextView mCourseDescription;
    private Button mPassButton;
    private Button mRejectButton;
    private TechnologyShareCourseItem mUnApprovedCourseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unapproved_share_course_detail);
        initViews();
        initEvents();
        logicProcess();
    }

    private void initViews() {
        mCourseName = (TextView) findViewById(R.id.unapproved_share_course_name);
        mCourseLevel = (TextView) findViewById(R.id.unapproved_share_course_level);
        mCourseDate = (TextView) findViewById(R.id.unapproved_share_course_date);
        mCourseLocale = (TextView) findViewById(R.id.unapproved_share_course_locale);
        mCourseStartTime = (TextView) findViewById(R.id.unapproved_share_course_begin_time);
        mCourseEndTime = (TextView) findViewById(R.id.unapproved_share_course_end_time);
        mCourseDescription = (TextView) findViewById(R.id.unapproved_share_course_description);
        mPassButton = (Button) findViewById(R.id.btn_pass);
        mRejectButton = (Button) findViewById(R.id.btn_reject);
    }

    private void initEvents() {
        mRejectButton.setOnClickListener(this);
        mPassButton.setOnClickListener(this);
    }

    /**
     * 业务逻辑处理
     */
    private void logicProcess() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mUnApprovedCourseItem = (TechnologyShareCourseItem) bundle.getSerializable("CourseInfo");
        mCourseName.setText(mUnApprovedCourseItem.getmCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get((short)mUnApprovedCourseItem.getmCourseLevel()));
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mUnApprovedCourseItem.getmStartTime()));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format(mUnApprovedCourseItem.getmStartTime()));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format(mUnApprovedCourseItem.getmEndTime()));
        mCourseLocale.setText(mUnApprovedCourseItem.getmLocation());
        mCourseDescription.setText(mUnApprovedCourseItem.getmCourseDescription());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pass:
                //TODO:审核通过
                Toast.makeText(this,"审核通过",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_reject:
                //TODO:否决
                Toast.makeText(this,"否决",Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     *
     * @param view
     */
    public void back(View view) {
        this.finish();
    }
}
