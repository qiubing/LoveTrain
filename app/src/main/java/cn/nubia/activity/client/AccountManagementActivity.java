package cn.nubia.activity.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.nubia.activity.BaseActivity;
import cn.nubia.activity.LoginActivity;
import cn.nubia.activity.R;
import cn.nubia.component.CircleImageView;
import cn.nubia.util.Constant;
import cn.nubia.util.Utils;

/**
 * @Author: qiubing
 * @Date: 2015/9/6 10:17
 */
public class AccountManagementActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "Management";

    private static final int GET_PHOTO_CODE = 1;
    //private UserInfo mUserInfo;

    private TextView mTitle;
    private CircleImageView mCircleImageView;
    private TextView mPasswordChange;
    private TextView mAboutMe;
    private TextView mFanKui;
    private TextView mVersion;
    private Button mLogoutButton;

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_account_manage);
        findViews();
    }

    @Override
    protected void initBeforeData() {
    }

    @Override
    protected void initEvents() {
        mCircleImageView.setOnClickListener(this);
        mPasswordChange.setOnClickListener(this);
        mAboutMe.setOnClickListener(this);
        mFanKui.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
        mTitle.setText("账号管理");
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    public void back(View view) {
        this.finish();
    }

    private void findViews() {
        mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
        mPasswordChange = (TextView) findViewById(R.id.btn_passwd_change);
        mAboutMe = (TextView) findViewById(R.id.btn_about);
        mFanKui = (TextView) findViewById(R.id.btn_fankui);
        mVersion = (TextView) findViewById(R.id.btn_version);
        mLogoutButton = (Button) findViewById(R.id.bt_logout);
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.account_manage_title);
        mTitle = (TextView) linear.findViewById(R.id.sub_page_title);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon1:
                Intent intent = new Intent(AccountManagementActivity.this, UpdateIconActivity.class);
                startActivityForResult(intent, GET_PHOTO_CODE);
                break;
            case R.id.btn_passwd_change:
                Intent intent1 = new Intent(AccountManagementActivity.this,
                        MyAccountmanaPswmodifyActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_about:
                break;
            case R.id.btn_fankui:
                break;
            case R.id.btn_version:
                break;
            case R.id.bt_logout:
                //注销登录
                Intent logoutIntent = new Intent(AccountManagementActivity.this,
                        LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                break;
        }
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
        Log.v(TAG,path);
        Bitmap bitmap = Utils.getPictureFromSD(path);
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
            mCircleImageView.setImageDrawable(drawable);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
