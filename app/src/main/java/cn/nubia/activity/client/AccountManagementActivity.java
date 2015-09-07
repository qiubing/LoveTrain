package cn.nubia.activity.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.nubia.activity.BaseActivity;
import cn.nubia.activity.R;
import cn.nubia.component.CircleImageView;
import cn.nubia.model.ShareUserInfo;

/**
 * @Author: qiubing
 * @Date: 2015/9/6 10:17
 */
public class AccountManagementActivity extends BaseActivity implements OnClickListener{
    private static final String TAG = "Management";

    private static final int GET_PHOTO_CODE = 1;
    //private UserInfo mUserInfo;

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
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    public void back(View view) {
        this.finish();
    }

    private void findViews(){
        mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
        mPasswordChange = (TextView) findViewById(R.id.btn_passwd_change);
        mAboutMe = (TextView) findViewById(R.id.btn_about);
        mFanKui = (TextView) findViewById(R.id.btn_fankui);
        mVersion = (TextView)findViewById(R.id.btn_version);
        mLogoutButton = (Button) findViewById(R.id.bt_logout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon1:
                Intent intent = new Intent(AccountManagementActivity.this,UpdateIconActivity.class);
                startActivityForResult(intent,GET_PHOTO_CODE);
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
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_PHOTO_CODE && resultCode == 3){
            Bundle extras = data.getExtras();
            if(extras != null){
                Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(photo);
                ShareUserInfo.USER_INFO.setUserIcon(drawable);
                //mUserInfo.setUserIcon(drawable);
                mCircleImageView.setImageDrawable(ShareUserInfo.USER_INFO.getUserIcon());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
        mCircleImageView.setImageDrawable(ShareUserInfo.USER_INFO.getUserIcon());*/
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
