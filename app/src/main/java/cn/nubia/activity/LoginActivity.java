package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.util.DialogUtil;
import cn.nubia.util.HttpUtil;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button loginButton;
    private TextView regist;
    private TextView forgetPwd;

    private EditText etName ;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        regist = (TextView) findViewById(R.id.login_regist);
        forgetPwd = (TextView) findViewById(R.id.login_forgetpwd);
        etName = (EditText) findViewById(R.id.login_name);
        etPwd = (EditText) findViewById(R.id.login_pwd);

        loginButton.setOnClickListener(this);
        regist.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.login_btn:
                break;
            case R.id.login_regist:
                intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forgetpwd:
                DialogUtil.showDialog(this, "forget", false);
                break;
            default:
                break;
        }
    }

    private boolean login(){
        String username = etName.getText().toString();
        String pwd = etPwd.getText().toString();
        return false;
    }

    private JSONObject query(String username,String pwd) throws JSONException {
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",pwd);
        String url = "";
        return new JSONObject((HttpUtil.postRequest(url,"","")));
    }

    //对用户名和密码进行校验
    public boolean validate(){
        String username = etName.getText().toString().trim();
        if(username.equals("")){
            DialogUtil.showDialog(this,"用户名不能为空",false);
            return  false;
        }
        String pwd = etPwd.getText().toString().trim();
        if(pwd.equals("")){
            DialogUtil.showDialog(this,"密码不能为空",false);
            return false;
        }
        return true;
    }
}
