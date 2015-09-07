package cn.nubia.activity.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.nubia.activity.R;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
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
    private Button mCourseModifyButton;
    private CourseItem mShareCourseItem;
    private LessonItem mShareCourseLessonItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);


        holdView();
        setViewLogic();
//        initViewData();
    }

    private void holdView(){
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
        mCourseModifyButton = (Button) findViewById(R.id
                .my_sharecourse_detail_display_modifybutton);
    }

    private void setViewLogic(){
        mCourseModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShareCourseDetailDisplayActivity.this
                        ,MyShareCourseDetailFillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shareCourse",mShareCourseItem);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initViewData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mShareCourseItem = (CourseItem) bundle.getSerializable("shareCourse");
        mShareCourseLessonItem = mShareCourseItem.getmLessonList().get(0);
        mCourseName.setText(mShareCourseItem.getCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get(mShareCourseItem.getShareType()));
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mShareCourseLessonItem.getStartTime()));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format( mShareCourseLessonItem.getStartTime()));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format(mShareCourseLessonItem.getEndTime()));
        mCourseLocale.setText(mShareCourseLessonItem.getLocation());
        mCourseDescription.setText(mShareCourseItem.getCourseDescription());
    }
}
