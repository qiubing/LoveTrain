package cn.nubia.activity.client;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.activity.R;
import cn.nubia.entity.ShareCourseItem;
import cn.nubia.entity.ShareCourseLevelModel;

/**
 * Created by JiangYu on 2015/9/2.
 */
public class ClientMyShareCourseDetailDisplayActivity extends Activity {
    private TextView mCourseName;
    private TextView mCourseLevel;
    private TextView mCourseDate;
    private TextView mCourseStartTime;
    private TextView mCourseEndTime;
    private TextView mCourseLocale;
    private TextView mCourseDescription;
    private Button mCourseModifyButton;
    private ShareCourseItem mShareCourseItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);
        holdView();
        setViewLogic();
        initViewData();
    }

    private void holdView() {
        mCourseName = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursename_displaytextView);
        mCourseLevel = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_courselevel_displaytextView);
        mCourseDate = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedate_displaytextView);
        mCourseStartTime = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursestarttime_displaytextView);
        mCourseEndTime = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_courseendtime_displaytextView);
        mCourseLocale = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_courselocale_displaytextView);
        mCourseDescription = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedescription_displaytextView);
        mCourseModifyButton = (Button) findViewById(R.id
                .my_sharecourse_detail_display_modifybutton);
    }

    private void setViewLogic() {
        mCourseModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientMyShareCourseDetailDisplayActivity.this
                        , ClientMyShareCourseDetailFillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","update");
                bundle.putSerializable("shareCourse", mShareCourseItem);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initViewData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mShareCourseItem = (ShareCourseItem) bundle.getSerializable("shareCourse");

        mCourseName.setText(mShareCourseItem.getCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get((short)mShareCourseItem.getCourseLevel()));
        Log.e("level",mShareCourseItem.getCourseLevel() + " ");
        Date startTime = new Date();
        startTime.setTime(mShareCourseItem.getStartTime());
        Date endTime = new Date();
        endTime.setTime(mShareCourseItem.getEndTime());
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format(startTime));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format(endTime));
        mCourseLocale.setText(mShareCourseItem.getLocale());
        mCourseDescription.setText(mShareCourseItem.getCourseDescription());
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
