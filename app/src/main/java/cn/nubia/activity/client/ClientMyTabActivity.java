package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.nubia.activity.LoginActivity;
import cn.nubia.activity.R;
import cn.nubia.component.CircleImageView;
import cn.nubia.util.Constant;
import cn.nubia.util.Utils;

/**
 * @Description:与我相关的
 * @Author: qiubing
 * @Date: 2015/9/6 19:28
 */

public class ClientMyTabActivity extends Activity implements OnClickListener {
    private static final String TAG = "UserSetting";
    private static final int GET_PHOTO_CODE = 1;

    private CircleImageView mCircleImageView;
    private TextView mCheckRecord;
    private TextView mCourseIntergration;
    private TextView mExamScore;
    private TextView mCourseShare;
    private TextView mIWantShare;
    private TextView mPasswordChange;
    private TextView mAboutMe;
    private TextView mFanKui;
    private TextView mVersion;
    private Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_setting);
        initViews();
        initEvents();
    }

    private void initViews(){
        mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
        mCheckRecord = (TextView) findViewById(R.id.check_in_record);
        mCourseIntergration = (TextView) findViewById(R.id.course_integration);
        mCourseShare = (TextView) findViewById(R.id.course_share);
        mIWantShare = (TextView) findViewById(R.id.i_want_share);
        mExamScore = (TextView) findViewById(R.id.exam_score);
        mPasswordChange = (TextView) findViewById(R.id.btn_passwd_change);
        mAboutMe = (TextView) findViewById(R.id.btn_about);
        mFanKui = (TextView) findViewById(R.id.btn_fankui);
        mVersion = (TextView) findViewById(R.id.btn_version);
        mLogoutButton = (Button) findViewById(R.id.bt_logout);
    }

    private void initEvents(){
        mCircleImageView.setOnClickListener(this);
        mCheckRecord.setOnClickListener(this);
        mCourseIntergration.setOnClickListener(this);
        mCourseShare.setOnClickListener(this);
        mIWantShare.setOnClickListener(this);
        mExamScore.setOnClickListener(this);
        mPasswordChange.setOnClickListener(this);
        mAboutMe.setOnClickListener(this);
        mFanKui.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon1:
                Intent intent = new Intent(this, UpdateIconActivity.class);
                startActivityForResult(intent, GET_PHOTO_CODE);
                break;
            case R.id.check_in_record:
               myStartActivity(MyCheckRecordActivity.class);
                break;
            case R.id.course_integration:
                myStartActivity(CourseIntegrationRecordActivity.class);
                break;
            case R.id.exam_score:
                myStartActivity(ExamScoreActivity.class);
                break;
            case R.id.course_share:
                myStartActivity(ShareCourseActivity.class);
                break;
            case R.id.i_want_share:
                myStartActivity(MyShareCourseDetailFillActivity.class);
                break;
            case R.id.btn_passwd_change:
                myStartActivity(MyAccountmanaPswmodifyActivity.class);
                break;
            case R.id.btn_about:
                Toast.makeText(this,"nubia LoveTrain",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_fankui:
                Toast.makeText(this,"谢谢您的反馈",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_version:
                Toast.makeText(this,"Version 1.0",Toast.LENGTH_LONG).show();
                break;
            case R.id.bt_logout:
                //注销登录
                Intent logoutIntent = new Intent(this,
                        LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                break;
        }
    }

    /**
     * 自定义的startActivity
     * @param cls 需要启动的Activity类
     */
    protected void myStartActivity(Class<?> cls) {
        Intent intent  = new Intent();
        intent.setClass(this,cls);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PHOTO_CODE && resultCode == 3) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(photo);
                mCircleImageView.setImageDrawable(drawable);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //头像图片存储路径
        String path = Constant.LOCAL_PATH + Constant.PORTRAIT;
        Log.v(TAG, path);
        Bitmap bitmap = Utils.getPictureFromSD(path);
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
            mCircleImageView.setImageDrawable(drawable);
        }
    }
}
