package cn.nubia.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.nubia.entity.ShareCourse;
import cn.nubia.model.ShareCourseLevelModel;

/**
 * Created by JiangYu on 2015/9/2.
 */
public class MyShareCourseDetailDisplayActivity extends Activity {
    private TextView mCourseName;
    private TextView mCourseLevel;
    private TextView mCourseDate;
    private TextView mCourseStartTime;
    private TextView mCourseEndTime;
    private TextView mCourseLocale;
    private TextView mCourseDescription;

    private ShareCourse mShareCourse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mShareCourse = (ShareCourse) bundle.getSerializable("shareCourse");

        mCourseName =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursename_displaytextView);
        mCourseLevel =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_courselevel_displaytextView);
        mCourseDate =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedate_displaytextView);
        mCourseStartTime =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursestarttime_displaytextView);
        mCourseEndTime =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_courseendtime_displaytextView);
        mCourseLocale =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_courselocale_displaytextView);
        mCourseDescription =(TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedescription_displaytextView);

        String startTime = new SimpleDateFormat("yyyy-MM-dd").format( mShareCourse.getStartTime());

        mCourseName.setText(mShareCourse.getCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get(mShareCourse.getCourseLevel()));
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format( mShareCourse.getStartTime()));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format( mShareCourse.getStartTime()));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format( mShareCourse.getEndTime()));
        mCourseLocale.setText(mShareCourse.getLocale());
        mCourseDescription.setText(mShareCourse.getCourseDescription());
    }
}
