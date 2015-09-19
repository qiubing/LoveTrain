package cn.nubia.activity.admin;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Created by 胡立 on 2015/9/6.
 */
@SuppressWarnings("deprecation")
public class AdminMyActivity extends ActivityGroup implements View.OnClickListener {
    TextView score_query;
    TextView integral_query;
    TextView assess_manage;
    TextView user_manage;
    TextView account_manage;
    TextView about_us;
    Button change_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_my_admin);
        score_query = (TextView) findViewById(R.id.score_query);
        integral_query = (TextView) findViewById(R.id.integral_query);
        assess_manage = (TextView) findViewById(R.id.assess_manage);
        user_manage = (TextView) findViewById(R.id.user_manage);
        account_manage = (TextView) findViewById(R.id.admin_account_manage);
        about_us = (TextView) findViewById(R.id.about_us);
        change_account = (Button) findViewById(R.id.change_account);

        score_query.setOnClickListener(this);
        integral_query.setOnClickListener(this);
        assess_manage.setOnClickListener(this);
        user_manage.setOnClickListener(this);
        account_manage.setOnClickListener(this);
        about_us.setOnClickListener(this);
        change_account.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.score_query:
                Toast.makeText(this, "fghfgh", Toast.LENGTH_LONG).show();
                break;
            case R.id.integral_query:

                break;
            case R.id.assess_manage:

                break;
            case R.id.user_manage:

                break;
            case R.id.admin_account_manage:

                break;
            case R.id.about_us:
                break;
            case R.id.change_account:

                break;
        }
    }

    public void search(View view) {
        Toast.makeText(this, "扫二维码签名", Toast.LENGTH_LONG).show();
    }

}

