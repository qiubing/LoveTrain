package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button loginButton;
    private TextView regist;
    private TextView forgetPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        regist = (TextView) findViewById(R.id.login_regist);
        forgetPwd = (TextView) findViewById(R.id.login_forgetpwd);

        loginButton.setOnClickListener(this);
        regist.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.login_btn:
                break;
            case R.id.login_regist:
                intent = new Intent(this,RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forgetpwd:
                break;
            default:
                break;
        }
    }
}
