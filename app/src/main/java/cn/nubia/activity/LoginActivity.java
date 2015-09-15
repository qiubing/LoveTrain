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

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.activity.admin.AdminMainActivity;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;
import cn.nubia.util.Md5Encryption;
import cn.nubia.util.TestData;


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
                text = mLoginButton.getText().toString();
                if (text.equals("登录")) {
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
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleLogin(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
                handleLogin(errorResponse);
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("UserID", userID);
        params.put("Password", Md5Encryption.getMD5(pwd));
        String url = Constant.BASE_URL + "user/login.do";
        if (isManager.equals("是")) {
            params.put("isAdmin", "true");
        } else {
            params.put("isAdmin", "false");
        }
        AsyncHttpHelper.post(url, params, handler);
    }

    private void handleLogin(JSONObject response) {
        try {
            //TODO
            response = TestData.getLoginResult();
            String code = response.getString("code");
            if (code.equals("0")) {
                DialogUtil.showToast(LoginActivity.this, "登录成功");
                if (mIsManagerSpinner.getSelectedItem().toString().equals("是")) {
                    Constant.IS_ADMIN = true;
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Constant.IS_ADMIN = false;
                    startActivity(new Intent(LoginActivity.this, ClientMainActivity.class));
                    LoginActivity.this.finish();
                }
            } else {
                HandleResponse.excute(LoginActivity.this, code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
        }
    }

    private void regist() {
        JSONObject jsonObject;
        String userID = mUserIdET.getText().toString();
        String userName = mUserNameET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        String sex = mSexSpinner.getSelectedItem().toString();

        Map<String, String> params = new HashMap<>();
        params.put("UserID", userID);
        params.put("UserName", userName);
        params.put("UserPasswd", Md5Encryption.getMD5(pwd));
        if (sex.equals("男")) {
            params.put("gender", "true");
        } else {
            params.put("gender", "false");
        }
        String url = Constant.BASE_URL + "user/register.do";
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String code = response.getString("code");
                    if (code.equals("0")) {
                        DialogUtil.showToast(LoginActivity.this, "注册成功！");
                    } else if (code.equals("3000")) {
                        DialogUtil.showToast(LoginActivity.this, "用户名已存在！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
            }
        };
        AsyncHttpHelper.post(url, params, handler);
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
        if (pwd2.equals("")) {
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
