package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

/**
 * 管理员-我的-主界面
 */
public class MyAdminActivity_1 extends Activity implements View.OnClickListener {
    TextView mQueryScoreTV;
    TextView mQueryCreditTV;
    TextView mRateManageTV;
    TextView mAccountManageTV;
    TextView mUserManageTV;
    TextView mAboutUsTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_my_setting);

        mQueryScoreTV = (TextView) findViewById(R.id.queryscore);
        mQueryCreditTV = (TextView) findViewById(R.id.querycredit);
        mRateManageTV = (TextView) findViewById(R.id.ratemanage);
        mAccountManageTV = (TextView) findViewById(R.id.accountmanage);
        mUserManageTV = (TextView) findViewById(R.id.usermanage);
        mAboutUsTV = (TextView) findViewById(R.id.about_us);

        mQueryScoreTV.setOnClickListener(this);
        mQueryCreditTV.setOnClickListener(this);
        mRateManageTV.setOnClickListener(this);
        mAccountManageTV.setOnClickListener(this);
        mUserManageTV.setOnClickListener(this);
        mAboutUsTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent = null;
        switch (viewId) {
            case R.id.queryscore:
                intent = new Intent(MyAdminActivity_1.this, ManagerScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.querycredit:
                intent = new Intent(MyAdminActivity_1.this, ManagerCreditActivity.class);
                startActivity(intent);
                break;
            case R.id.ratemanage:
                intent = new Intent(MyAdminActivity_1.this, ManagerRateActivity.class);
                startActivity(intent);
                break;
            case R.id.usermanage:
                intent = new Intent(MyAdminActivity_1.this,ManagerUserActivity.class);
                startActivity(intent);
                break;
            case R.id.accountmanage:
                break;
            case R.id.about_us:
                DialogUtil.showDialog(MyAdminActivity_1.this,"LoveTrain!");
                break;
            default:
                break;
        }
    }
}

