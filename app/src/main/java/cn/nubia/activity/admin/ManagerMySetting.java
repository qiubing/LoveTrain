package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import cn.nubia.activity.R;

/**
 * Created by LK on 2015/9/6.
 */
public class ManagerMySetting extends Activity implements View.OnClickListener {
    LinearLayout mQueryScoreLayout;
    LinearLayout mQueryCreditLayout;
    LinearLayout mRateManageLayout;
    LinearLayout mAccountManageLayout;
    LinearLayout mUserManageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_my_setting);

        mQueryScoreLayout = (LinearLayout) findViewById(R.id.layout_queryscore);
        mQueryCreditLayout = (LinearLayout) findViewById(R.id.layout_querycredit);
        mRateManageLayout = (LinearLayout) findViewById(R.id.layout_ratemanage);
        mAccountManageLayout = (LinearLayout) findViewById(R.id.layout_accountmanage);
        mUserManageLayout = (LinearLayout) findViewById(R.id.layout_usermanage);

        mQueryScoreLayout.setOnClickListener(this);
        mQueryCreditLayout.setOnClickListener(this);
        mRateManageLayout.setOnClickListener(this);
        mAccountManageLayout.setOnClickListener(this);
        mUserManageLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent = null;
        switch (viewId) {
            case R.id.layout_queryscore:
                intent = new Intent(ManagerMySetting.this, ManagerScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_querycredit:
                intent = new Intent(ManagerMySetting.this, ManagerCreditActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_ratemanage:
                intent = new Intent(ManagerMySetting.this, ManagerRateActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
