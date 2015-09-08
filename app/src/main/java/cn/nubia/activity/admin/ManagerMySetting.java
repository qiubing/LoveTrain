package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.nubia.activity.R;

/**
 * Created by LK on 2015/9/6.
 */
public class ManagerMySetting extends Activity implements View.OnClickListener {
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
                intent = new Intent(ManagerMySetting.this, ManagerScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.querycredit:
                intent = new Intent(ManagerMySetting.this, ManagerCreditActivity.class);
                startActivity(intent);
                break;
            case R.id.ratemanage:
                intent = new Intent(ManagerMySetting.this, ManagerRateActivity.class);
                startActivity(intent);
                break;
            case R.id.usermanage:
                break;
            case R.id.accountmanage:
                break;
            case R.id.about_us:
                break;
            default:
                break;
        }
    }
}
