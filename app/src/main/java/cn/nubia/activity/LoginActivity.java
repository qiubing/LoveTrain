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

    private Button mLoginButton;
    private Button mRegistButton;

    private EditText mUserIdET;
    private EditText mUserNameET;
    private EditText mPasswordET;
    private EditText mRegistPasswordET;
    private LinearLayout mSexLayout;
    private LinearLayout mIsMangerLayout;
    private TextView mTitleTV;
    private Spinner mSexSpinner;
    private Spinner mIsManagerSpinner;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.login_btn);
        mRegistButton = (Button) findViewById(R.id.regist_btn);
        mUserIdET = (EditText) findViewById(R.id.login_userid);
        mPasswordET = (EditText) findViewById(R.id.login_pwd);
        mUserNameET = (EditText) findViewById(R.id.lgoin_username);
        mRegistPasswordET = (EditText) findViewById(R.id.regist_pwd);
        mTitleTV = (TextView) findViewById(R.id.title);
        mSexSpinner = (Spinner) findViewById(R.id.sex);
        mIsManagerSpinner = (Spinner) findViewById(R.id.ismanager);
        mSexLayout = (LinearLayout) findViewById(R.id.layout_sex);
        mIsManagerSpinner = (Spinner) findViewById(R.id.ismanager);
        mIsMangerLayout = (LinearLayout) findViewById(R.id.layout_ismanager);

        mLoginButton.setOnClickListener(this);
        mRegistButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        String text;
        switch (v.getId()) {
            case R.id.login_btn:
                //TODO
                if (mIsManagerSpinner.getSelectedItem().toString().equals("是")) {
                    startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                    this.finish();
                }
                else{
                    startActivity(new Intent(LoginActivity.this, MainClientActivity.class));
                    this.finish();
                }

                text = mLoginButton.getText().toString();
                //TODO 模拟登录数据
                if (text.equals("登录")) {
                    if (mUserIdET.getText().toString().equals("user") && mPasswordET.getText().toString().equals("user")) {
                        Intent intent = new Intent(LoginActivity.this, MainAdminActivity.class);
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
                text = mRegistButton.getText().toString();
                if (text.equals("会员注册")) {
                    mRegistButton.setText("会员登录");
                    mLoginButton.setText("注册");
                    mIsMangerLayout.setVisibility(View.GONE);
                    mTitleTV.setText(R.string.registtxt);
                    mRegistPasswordET.setVisibility(View.VISIBLE);
                    mUserNameET.setVisibility(View.VISIBLE);
                    mSexLayout.setVisibility(View.VISIBLE);
                } else if (text.equals("会员登录")) {
                    mRegistButton.setText("会员注册");
                    mLoginButton.setText("登录");
                    mTitleTV.setText(R.string.logintxt);
                    mRegistPasswordET.setVisibility(View.GONE);
                    mUserNameET.setVisibility(View.GONE);
                    mSexLayout.setVisibility(View.GONE);
                    mIsMangerLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        String userID = mUserIdET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        String isManager = mIsManagerSpinner.getSelectedItem().toString();
        if (isManager.equals("是")) {

        }
        JSONObject jsonObject;
        try {
            jsonObject = query(userID, pwd);
            String result = jsonObject.getString("code");
            if (result.equals("0")) {
                DialogUtil.showToast(this, "登录成功");
            } else if (result.equals("2001")) {
                DialogUtil.showToast(this, "用户名不存在，请重新输入！");
            } else if (result.equals("2002")) {
                DialogUtil.showToast(this, "用户名密码错误，请重新输入！");
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
        String userID = mUserIdET.getText().toString();
        String userName = mUserNameET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        String sex = mSexSpinner.getSelectedItem().toString();
        try {
            jsonObject = insert(userName, userID, pwd, sex);
            String result = jsonObject.getString("code");
            if (result.equals("0")) {
                DialogUtil.showToast(this, "注册成功！");
            } else if (result.equals("3000")) {
                DialogUtil.showToast(this, "用户名已存在！");
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
        String username = mUserIdET.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showToast(this, "用户ID不能为空");
            return false;
        }
        String pwd = mPasswordET.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showToast(this, "密码不能为空");
            return false;
        }
        return true;
    }

    //对用户名和密码进行校验
    public boolean validateRegist() {
        String userID = mUserIdET.getText().toString().trim();
        if (userID.equals("")) {
            DialogUtil.showToast(this, "用户ID不能为空");
            return false;
        }
        String pwd = mPasswordET.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showToast(this, "密码不能为空");
            return false;
        }
        String pwd2 = mRegistPasswordET.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showToast(this, "确认密码不能为空");
            return false;
        }
        String username = mUserNameET.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showToast(this, "用户姓名不能为空");
            return false;
        }
        if (!pwd.equals(pwd2)) {
            DialogUtil.showToast(this, "两次输入的密码不一致");
            return false;
        }
        return true;
    }
}
