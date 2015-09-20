package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientMyShareCourseDetailFillActivity;
import cn.nubia.entity.ShareCourseLevelModel;
import cn.nubia.entity.ShareCourseMsg;
import cn.nubia.entity.TechnologyShareCourseItem;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/10 9:23
 */
public class AdminShareCourseApprovedDetailActivity extends Activity {
    private TextView mCourseName;
    private TextView mCourseLevel;
    private TextView mCourseDate;
    private TextView mCourseStartTime;
    private TextView mCourseEndTime;
    private TextView mCourseLocale;
    private TextView mCourseDescription;
    private Button mCourseModifyButton;
    private TechnologyShareCourseItem mApprovedCourseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_share_course_detail);
        initViews();
        initEvents();

        mCourseModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminShareCourseApprovedDetailActivity.this,
                        ClientMyShareCourseDetailFillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","update");
                ShareCourseMsg msg = new ShareCourseMsg(mApprovedCourseItem);
                bundle.putSerializable("shareCourse", msg);
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
        mApprovedCourseItem = (TechnologyShareCourseItem) bundle.getSerializable("CourseInfo");
        mCourseName.setText(mApprovedCourseItem.getmCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get((short) mApprovedCourseItem.getmCourseLevel()));
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mApprovedCourseItem.getmStartTime()));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format(mApprovedCourseItem.getmStartTime()));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format(mApprovedCourseItem.getmEndTime()));
        mCourseLocale.setText(mApprovedCourseItem.getmLocation());
        mCourseDescription.setText(mApprovedCourseItem.getmCourseName());
    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
