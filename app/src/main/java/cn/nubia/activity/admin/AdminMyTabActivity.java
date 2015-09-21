package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.nubia.activity.LoginActivity;
import cn.nubia.activity.R;
import cn.nubia.activity.client.AboutUsActivity;
import cn.nubia.activity.client.ClientMyAccountmanaPswmodifyActivity;
import cn.nubia.activity.client.ClientUpdateIconActivity;
import cn.nubia.component.CircleImageView;
import cn.nubia.entity.Constant;
import cn.nubia.util.Logger;
import cn.nubia.util.ProcessSPData;
import cn.nubia.util.Utils;

/**
 * 管理员-我的-主界面
 */
@SuppressWarnings("deprecation")
public class AdminMyTabActivity extends Activity implements View.OnClickListener {
    private static final int GET_PHOTO_CODE_ADMIN = 1010;


    private CircleImageView mCircleImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_my_setting);
        TextView mQueryScoreTV;
        TextView mQueryCreditTV;
        TextView mRateManageTV;
        TextView mAccountManageTV;
        TextView mUserManageTV;
        TextView mAboutUsTV;
        Button mChangeAccoutn;
        TextView mAwardCreditTV;
        TextView myUserName;

        mQueryScoreTV = (TextView) findViewById(R.id.queryscore);
        mQueryCreditTV = (TextView) findViewById(R.id.querycredit);
        mRateManageTV = (TextView) findViewById(R.id.ratemanage);
        mAccountManageTV = (TextView) findViewById(R.id.accountmanage);
        mUserManageTV = (TextView) findViewById(R.id.usermanage);
        mAboutUsTV = (TextView) findViewById(R.id.about_us);
        mChangeAccoutn = (Button) findViewById(R.id.change_account);
        mAwardCreditTV = (TextView) findViewById(R.id.awardcredit);

        myUserName = (TextView) findViewById(R.id.user_name);
        myUserName.setText(Constant.user.getUserName());
        mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
        mCircleImageView.setOnClickListener(this);

        mQueryScoreTV.setOnClickListener(this);
        mQueryCreditTV.setOnClickListener(this);
        mRateManageTV.setOnClickListener(this);
        mAccountManageTV.setOnClickListener(this);
        mUserManageTV.setOnClickListener(this);
        mAboutUsTV.setOnClickListener(this);
        mChangeAccoutn.setOnClickListener(this);
        mAwardCreditTV.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PHOTO_CODE_ADMIN && resultCode == 3) {
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
        Logger.Log(this, path);
        Bitmap bitmap = Utils.getPictureFromSD(path);
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
            mCircleImageView.setImageDrawable(drawable);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent;
        switch (viewId) {
            case R.id.icon1:
                intent = new Intent(this, ClientUpdateIconActivity.class);
                startActivityForResult(intent, GET_PHOTO_CODE_ADMIN);
                break;
            case R.id.queryscore:
                intent = new Intent(AdminMyTabActivity.this, AdminScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.querycredit:
                intent = new Intent(AdminMyTabActivity.this, AdminCreditActivity.class);
                startActivity(intent);
                break;
            case R.id.awardcredit:
                intent = new Intent(AdminMyTabActivity.this, AdminCreditsAwardActivity.class);
                startActivity(intent);
                break;
            case R.id.ratemanage:
                intent = new Intent(AdminMyTabActivity.this, AdminRateActivity.class);
                startActivity(intent);
                break;
            case R.id.usermanage:
                intent = new Intent(AdminMyTabActivity.this, AdminUserActivity.class);
                startActivity(intent);
                break;
            case R.id.accountmanage:
                intent = new Intent(AdminMyTabActivity.this, ClientMyAccountmanaPswmodifyActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                //DialogUtil.showDialog(AdminMyTabActivity.this, "LoveTrain!");
                intent = new Intent(AdminMyTabActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.change_account:
                intent = new Intent(AdminMyTabActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                ProcessSPData.clearSP(AdminMyTabActivity.this);
                startActivity(intent);
                this.finish();
            default:
                break;
        }
    }
}

