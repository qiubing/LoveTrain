package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.nubia.activity.LoginActivity;
import cn.nubia.activity.R;
import cn.nubia.activity.client.MyAccountmanaPswmodifyActivity;
import cn.nubia.util.DialogUtil;

/**
 * 管理员-我的-主界面
 */
public class AdminMyTabActivity extends Activity implements View.OnClickListener {
    private TextView mQueryScoreTV;
    private TextView mQueryCreditTV;
    private TextView mRateManageTV;
    private TextView mAccountManageTV;
    private TextView mUserManageTV;
    private TextView mAboutUsTV;
    private Button mChangeAccoutn;

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
        mChangeAccoutn = (Button) findViewById(R.id.change_account);

        mQueryScoreTV.setOnClickListener(this);
        mQueryCreditTV.setOnClickListener(this);
        mRateManageTV.setOnClickListener(this);
        mAccountManageTV.setOnClickListener(this);
        mUserManageTV.setOnClickListener(this);
        mAboutUsTV.setOnClickListener(this);
        mChangeAccoutn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent = null;
        switch (viewId) {
            case R.id.queryscore:
                intent = new Intent(AdminMyTabActivity.this, ManagerScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.querycredit:
                intent = new Intent(AdminMyTabActivity.this, ManagerCreditActivity.class);
                startActivity(intent);
                break;
            case R.id.ratemanage:
                intent = new Intent(AdminMyTabActivity.this, ManagerRateActivity.class);
                startActivity(intent);
                break;
            case R.id.usermanage:
                intent = new Intent(AdminMyTabActivity.this, ManagerUserActivity.class);
                startActivity(intent);
                break;
            case R.id.accountmanage:
                intent = new Intent(AdminMyTabActivity.this, MyAccountmanaPswmodifyActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                DialogUtil.showDialog(AdminMyTabActivity.this, "LoveTrain!");
                break;
            case R.id.change_account:
                intent = new Intent(AdminMyTabActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
            default:
                break;
        }
    }
}

