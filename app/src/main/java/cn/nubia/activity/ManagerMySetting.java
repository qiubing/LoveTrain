package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by LK on 2015/9/6.
 */
public class ManagerMySetting extends Activity implements View.OnClickListener {
    LinearLayout queryscoreLayout;
    LinearLayout querycreditLayout;
    LinearLayout ratemanageLayout;
    LinearLayout accountmanageLayout;
    LinearLayout usermanageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_my_setting);

        queryscoreLayout = (LinearLayout) findViewById(R.id.layout_queryscore);
        querycreditLayout = (LinearLayout) findViewById(R.id.layout_querycredit);
        ratemanageLayout = (LinearLayout) findViewById(R.id.layout_ratemanage);
        accountmanageLayout = (LinearLayout) findViewById(R.id.layout_accountmanage);
        usermanageLayout = (LinearLayout) findViewById(R.id.layout_usermanage);

        queryscoreLayout.setOnClickListener(this);
        querycreditLayout.setOnClickListener(this);
        ratemanageLayout.setOnClickListener(this);
        accountmanageLayout.setOnClickListener(this);
        usermanageLayout.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
