package cn.nubia.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.admin.AdminMainActivity;
import cn.nubia.util.ProcessSPData;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.UserInfo;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;


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
        mIsManagerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = mIsManagerSpinner.getSelectedItem().toString();
                if (select.equals("是")) {
                    mUserIdET.setText("0016002652");
                    mPasswordET.setText("123456");
                } else {
                    mUserIdET.setText("0016003347");
                    mPasswordET.setText("111111");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLoginButton.setOnClickListener(this);
        mRegistButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        CustomProgressDialog dialog;
        String text;
        switch (v.getId()) {
            case R.id.login_btn:
                text = mLoginButton.getText().toString();
                if (text.equals("登录")) {
                    if (validateLogin()) {
//                        dialog = new CustomProgressDialog(this, "登录中...", R.anim.loading);
                        login();
                    }
                } else if (text.equals("注册")) {
                    if (validateRegist()) {
//                        dialog = new CustomProgressDialog(this, "注册中...", R.anim.loading);
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
        RequestParams params = new RequestParams(Constant.getRequestParams());

        Constant.USER_ID = userID;
//        params.put("device_id", Constant.devideID);
//        params.put("request_time", System.currentTimeMillis());
//        params.put("apk_version", Constant.apkVersion);
//        params.put("token_key", Constant.tokenKep);

//        Constant.user.setUserID(userID);
        String url;
        if (isManager.equals("是")) {
            params.put("admin_id", userID);
            params.put("password", (pwd));
            url = Constant.BASE_URL + "ucent/admin_login.do";
        } else {
            params.put("user_id", userID);
            params.put("password", (pwd));
            url = Constant.BASE_URL + "ucent/login.do";
        }
        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    afterProcess("登录");
                    String s = new String(bytes, "UTF-8");
                    JSONObject response = new JSONObject(s);
                    handleLogin(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(LoginActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                afterProcess("登录");
                DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
            }
        });
        onProcess("登录中...");
    }

    private void handleLogin(JSONObject response) {
        try {
            String code = response.getString("code");
            if (code.equals("0")) {
                JSONObject json = response.getJSONObject("data");
                String tokenKey = json.getString("token_key");
                UserInfo user = new UserInfo();

                if (null != tokenKey) {
                    Constant.tokenKep = tokenKey;
                }
                ProcessSPData.putIntoSP(this, "tokenKey", Constant.tokenKep);
                ProcessSPData.putIntoSP(this, "isLogin", true);

                DialogUtil.showToast(LoginActivity.this, "登录成功");
                if (mIsManagerSpinner.getSelectedItem().toString().equals("是")) {
                    user.setUserID(json.getString("admin_id"));
                    user.setUserName(json.getString("admin_name"));
                    user.setGender(true);
                    user.setUserIconURL(null);
                    user.setUserTotalCredits(0);
                    user.setLastLoginTime(System.currentTimeMillis());
                    Constant.user = user;

                    ProcessSPData.putIntoSP(this, user);
                    ProcessSPData.putIntoSP(this, "isAdmin", true);
                    Constant.IS_ADMIN = true;
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    user.setUserID(json.getString("user_id"));
                    user.setGender(json.getBoolean("gender"));
                    user.setUserName(json.getString("user_name"));
                    user.setUserIconURL(json.getString("icon_url"));
                    user.setUserTotalCredits(json.getInt("userTotalCredits"));
                    user.setLastLoginTime(System.currentTimeMillis());
                    Constant.user = user;

                    ProcessSPData.putIntoSP(this, user);
                    ProcessSPData.putIntoSP(this, "isAdmin", false);
                    Constant.IS_ADMIN = false;
                    startActivity(new Intent(LoginActivity.this, ClientMainActivity.class));
                    LoginActivity.this.finish();
                }
            } else {
                String msg = response.getString("message");
                HandleResponse.excute(LoginActivity.this, code, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showToast(LoginActivity.this, "服务器返回异常！");
        }
    }

    private void regist() {
        String userID = mUserIdET.getText().toString();
        String userName = mUserNameET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        String sex = mSexSpinner.getSelectedItem().toString();

        RequestParams params = new RequestParams();

        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("sign", "");

        params.put("user_id", userID);
        params.put("user_name", userName);
        params.put("password", (pwd));
        if (sex.equals("男")) {
            params.put("gender", "1");
        } else {
            params.put("gender", "0");
        }
        //ucent/admin_register.do?admin_name&admin_id&password
        String url = Constant.BASE_URL + "user/register.do";
        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                afterProcess("注册");
                try {
                    String s = new String(bytes, "UTF-8");
                    JSONObject response = new JSONObject(s);
                    String code = response.getString("code");
                    if (code.equals("0")) {
                        DialogUtil.showToast(LoginActivity.this, "注册成功！");

                        mRegistButton.setText("会员注册");
                        mLoginButton.setText("登录");
                        mTitleTV.setText(R.string.logintxt);
                        mRegistPasswordET.setVisibility(View.GONE);
                        mUserNameET.setVisibility(View.GONE);
                        mSexLayout.setVisibility(View.GONE);
                        mIsMangerLayout.setVisibility(View.VISIBLE);

                    } else if (code.equals("3000")) {
                        DialogUtil.showToast(LoginActivity.this, "用户名已存在！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(LoginActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                afterProcess("登录");
                DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
            }
        });
        onProcess("注册中...");
    }

    /**
    private void adminRegist() {
        String userID = mUserIdET.getText().toString();
        String userName = mUserNameET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        String sex = mSexSpinner.getSelectedItem().toString();

        RequestParams params = new RequestParams();

        params.put("device_id", Constant.devideID);
        params.put("request_time", System.currentTimeMillis());
        params.put("apk_version", Constant.apkVersion);
        params.put("sign", "");

        params.put("admin_id", userID);
        params.put("admin_name", userName);
        params.put("password", (pwd));
        if (sex.equals("男")) {
            params.put("gender", "1");
        } else {
            params.put("gender", "0");
        }
        //ucent/admin_register.do?admin_name&admin_id&password
        String url = Constant.BASE_URL + "ucent/admin_register.do";
        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {

            @ Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                afterProcess("注册");
                try {
                    String s = new String(bytes, "UTF-8");
                    JSONObject response = new JSONObject(s);
                    String code = response.getString("code");
                    if (code.equals("0")) {
                        DialogUtil.showToast(LoginActivity.this, "注册成功！");

                        mRegistButton.setText("会员注册");
                        mLoginButton.setText("登录");
                        mTitleTV.setText(R.string.logintxt);
                        mRegistPasswordET.setVisibility(View.GONE);
                        mUserNameET.setVisibility(View.GONE);
                        mSexLayout.setVisibility(View.GONE);
                        mIsMangerLayout.setVisibility(View.VISIBLE);

                    } else if (code.equals("3000")) {
                        DialogUtil.showToast(LoginActivity.this, "用户名已存在！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(LoginActivity.this, "服务器返回异常！");
                }
            }

            @ Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                afterProcess("登录");
                DialogUtil.showToast(LoginActivity.this, "连接服务器发生异常！");
            }
        });
        onProcess("注册中...");
    }
**/
    private void onProcess(String s) {
//        dialog.show();
        mLoginButton.setEnabled(false);
        mRegistButton.setEnabled(false);
        mLoginButton.setText(s);
    }

    private void afterProcess(String s) {
//        dialog.dismiss();
        mLoginButton.setEnabled(true);
        mRegistButton.setEnabled(true);
        mLoginButton.setText(s);
    }

    //对用户名和密码进行校验
    private boolean validateLogin() {
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
    private boolean validateRegist() {
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
