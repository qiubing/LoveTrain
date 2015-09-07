package cn.nubia.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.util.DialogUtil;
import cn.nubia.util.HttpUtil;
import cn.nubia.util.Md5Encryption;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button loginButton;
    private Button registButton;

    private EditText etUserID;
    private EditText etUserName;
    private EditText etPwd;
    private EditText etRegistPwd;
    private LinearLayout layout_sex;
    private LinearLayout layout_ismanger;
    private TextView tvTitle;
    private Spinner spSex;
    private Spinner spIsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        registButton = (Button) findViewById(R.id.regist_btn);
        etUserID = (EditText) findViewById(R.id.login_userid);
        etPwd = (EditText) findViewById(R.id.login_pwd);
        etUserName = (EditText) findViewById(R.id.lgoin_username);
        etRegistPwd = (EditText) findViewById(R.id.regist_pwd);
        tvTitle = (TextView) findViewById(R.id.title);
        spSex = (Spinner) findViewById(R.id.sex);
        spIsManager = (Spinner) findViewById(R.id.ismanager);
        layout_sex = (LinearLayout) findViewById(R.id.layout_sex);
        spIsManager = (Spinner) findViewById(R.id.ismanager);
        layout_ismanger = (LinearLayout) findViewById(R.id.layout_ismanager);

        loginButton.setOnClickListener(this);
        registButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String text;
        switch (v.getId()) {
            case R.id.login_btn:
                text = loginButton.getText().toString();
                //TODO 模拟登录数据
                if (text.equals("登录")) {
                    if (etUserID.getText().toString().equals("user") && etPwd.getText().toString().equals("user")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        this.finish();
                    }
                    if (validateLogin()) {
                        login();
                    }
                } else if (text.equals("注册")) {
                    if (validateRegist()) {
                        regist();
                    }
                }
                break;
            case R.id.regist_btn:
                text = registButton.getText().toString();
                if (text.equals("会员注册")) {
                    registButton.setText("会员登录");
                    loginButton.setText("注册");
                    layout_ismanger.setVisibility(View.GONE);
                    tvTitle.setText(R.string.registtxt);
                    etRegistPwd.setVisibility(View.VISIBLE);
                    etUserName.setVisibility(View.VISIBLE);
                    layout_sex.setVisibility(View.VISIBLE);
                } else if (text.equals("会员登录")) {
                    registButton.setText("会员注册");
                    loginButton.setText("登录");
                    tvTitle.setText(R.string.logintxt);
                    etRegistPwd.setVisibility(View.GONE);
                    etUserName.setVisibility(View.GONE);
                    layout_sex.setVisibility(View.GONE);
                    layout_ismanger.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        String userID = etUserID.getText().toString();
        String pwd = etPwd.getText().toString();
        String isManager = spIsManager.getSelectedItem().toString();
        if(isManager.equals("是")){

        }
        JSONObject jsonObject;
        try {
            jsonObject = query(userID, pwd);
            String result = jsonObject.getString("code");
            if (result.equals("0")) {
                DialogUtil.showDialog(this, "登录成功", false);
            } else if (result.equals("2001")) {
                DialogUtil.showDialog(this, "用户名不存在，请重新输入！", false);
            } else if (result.equals("2002")) {
                DialogUtil.showDialog(this, "用户名密码错误，请重新输入！", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showToast(this, "连接服务器发生异常！");
        }
    }

    private JSONObject query(String username, String pwd) throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("UserID", username);
        map.put("Password", Md5Encryption.getMD5(pwd));
        String url = HttpUtil.BASE_URL + "user/login.do";
        return new JSONObject((HttpUtil.postRequest(url, map)));
    }

    private void regist() {
        JSONObject jsonObject;
        String userID = etUserID.getText().toString();
        String userName = etUserName.getText().toString();
        String pwd = etPwd.getText().toString();
        String sex = spSex.getSelectedItem().toString();
        try {
            jsonObject = insert(userName, userID, pwd, sex);
            String result = jsonObject.getString("code");
            if (result.equals("0")) {
                DialogUtil.showToast(this, "注册成功！");
            } else if (result.equals("3000")) {
                DialogUtil.showDialog(this, "用户名已存在！", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showToast(this, "连接服务器发生异常！");
        }
    }


    private JSONObject insert(String userName, String userID, String pwd, String sex) throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("UserName", userName);
        map.put("UserId", userID);
        map.put("UserPasswd", Md5Encryption.getMD5(pwd));
        if (sex.equals("男"))
            map.put("gender", "true");
        else
            map.put("gender", "false");
        String url = HttpUtil.BASE_URL + "user/register.do";
        return new JSONObject((HttpUtil.postRequest(url, map)));
    }

    //对用户名和密码进行校验
    public boolean validateLogin() {
        String username = etUserID.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showDialog(this, "用户ID不能为空", false);
            return false;
        }
        String pwd = etPwd.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showDialog(this, "密码不能为空", false);
            return false;
        }
        return true;
    }

    //对用户名和密码进行校验
    public boolean validateRegist() {
        String userID = etUserID.getText().toString().trim();
        if (userID.equals("")) {
            DialogUtil.showDialog(this, "用户ID不能为空", false);
            return false;
        }
        String pwd = etPwd.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showDialog(this, "密码不能为空", false);
            return false;
        }
        String pwd2 = etRegistPwd.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showDialog(this, "确认密码不能为空", false);
            return false;
        }
        String username = etUserName.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showDialog(this, "用户姓名不能为空", false);
            return false;
        }
        if (!pwd.equals(pwd2)) {
            DialogUtil.showDialog(this, "两次输入的密码不一致", false);
            return false;
        }
        return true;
    }
}
