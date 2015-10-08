package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.GestureDetectorManager;
/**
 * Created by JiangYu on 2015/9/2.
 */
public class ClientMyShareCourseDetailDisplayActivity extends Activity {

    private TextView mCourseName;
    private TextView mCourseDescription;
    private TextView mCourseDetail;
    private Button mCourseModifyButton;
    private ShareCourseMsg mShareCourseMsg;

    private final GestureDetectorManager mGestureDetectorManager  = GestureDetectorManager.getInstance();
    private GestureDetector gestureDetector ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);

        gestureDetector = new GestureDetector(this, mGestureDetectorManager);
        mGestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return  gestureDetector.onTouchEvent(event);
    }

    private void holdView() {
        mCourseName = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursename_displaytextView);
        mCourseDescription = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedescription_displaytextView);
        mCourseDetail = (TextView) findViewById(R.id
                .my_sharecourse_detail_display_coursedetail_displaytextView);
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
                ClientMyShareCourseDetailDisplayActivity.this.finish();
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

        Date startTime = new Date();
        startTime.setTime(mShareCourseMsg.getStartTime());
        Date endTime = new Date();
        endTime.setTime(mShareCourseMsg.getEndTime());
        mCourseName.setText(mShareCourseMsg.getCourseName());
        mCourseDescription.setText(mShareCourseMsg.getCourseDescription());
        mCourseDetail.setText(
                "分享级别：" + ShareCourseLevelModel.SHARE_COURSE_MODEL.get((short) mShareCourseMsg.getCourseLevel()) +
                        "\n上课日期：" + new SimpleDateFormat("yyyy-MM-dd").format(startTime) +
                        "\n开始时间：" + new SimpleDateFormat("HH:mm").format(startTime) +
                        "\n结束时间：" + new SimpleDateFormat("HH:mm").format(endTime) +
                        "\n上课地点：" + mShareCourseMsg.getLocale());
    }
}
