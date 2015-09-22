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
import cn.nubia.entity.Constant;
import cn.nubia.entity.ShareCourseLevelModel;
import cn.nubia.entity.ShareCourseMsg;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.service.CommunicateService;

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
    private ShareCourseMsg mShareCourseMsg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);

        holdView();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String mSourse = bundle.getString("source");
        if(mSourse.equals("adminupdate")) {
            mShareCourseMsg = new ShareCourseMsg(
                    (TechnologyShareCourseItem) bundle.getSerializable("shareCourse"));
        }else if(mSourse.equals("myupdate")) {
            mShareCourseMsg = (ShareCourseMsg) bundle.getSerializable("shareCourse");
            mShareCourseMsg.setUserId(Constant.user.getUserID());
            mShareCourseMsg.setUserName(Constant.user.getUserName());
        }

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
                bundle.putSerializable("shareCourse", mShareCourseMsg);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        (findViewById(R.id.manager_goback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientMyShareCourseDetailDisplayActivity.this.finish();
            }
        });
    }

    private void initViewData() {
        mCourseName.setText(mShareCourseMsg.getCourseName());
        mCourseLevel.setText(
                ShareCourseLevelModel.SHARE_COURSE_MODEL.get((short) mShareCourseMsg.getCourseLevel()));
        Log.e("level", mShareCourseMsg.getCourseLevel() + " ");
        Date startTime = new Date();
        startTime.setTime(mShareCourseMsg.getStartTime());
        Date endTime = new Date();
        endTime.setTime(mShareCourseMsg.getEndTime());
        mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        mCourseStartTime.setText(
                new SimpleDateFormat("HH:mm").format(startTime));
        mCourseEndTime.setText(
                new SimpleDateFormat("HH:mm").format(endTime));
        mCourseLocale.setText(mShareCourseMsg.getLocale());
        mCourseDescription.setText(mShareCourseMsg.getCourseDescription());
    }
}
