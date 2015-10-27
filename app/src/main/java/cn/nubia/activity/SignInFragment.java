package cn.nubia.activity;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;

public class SignInFragment extends Fragment {
    private Button mRegistButton;

    private EditText mUserIdET;
    private EditText mUserNameET;
    private EditText mPasswordET;
    private EditText mRegistPasswordET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*和管理员共用同一个页面*/
        View rootView = inflater.inflate(R.layout.activity_login_regist, container, false);

        mRegistButton = (Button) rootView.findViewById(R.id.login_btn_regist);
        mUserIdET = (EditText) rootView.findViewById(R.id.login_userid_regist);
        mPasswordET = (EditText) rootView.findViewById(R.id.login_pwd_regist);
        mRegistPasswordET = (EditText)rootView.findViewById(R.id.login_pwd_regist1);
        mUserNameET = (EditText) rootView.findViewById(R.id.login_username_regist);

        mRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegist()) {
                    regist();
                }
            }
        });
        return rootView;
    }
    //对用户名和密码进行校验
    private boolean validateRegist() {
        String userID = mUserIdET.getText().toString().trim();
        if (userID.equals("")) {
            DialogUtil.showToast(getActivity(), "用户ID不能为空");
            return false;
        }
        String pwd = mPasswordET.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showToast(getActivity(), "密码不能为空");
            return false;
        }
        String pwd2 = mRegistPasswordET.getText().toString().trim();
        if (pwd2.equals("")) {
            DialogUtil.showToast(getActivity(), "确认密码不能为空");
            return false;
        }
        String username = mUserNameET.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showToast(getActivity(), "用户姓名不能为空");
            return false;
        }
        if (!pwd.equals(pwd2)) {
            DialogUtil.showToast(getActivity(), "两次输入的密码不一致");
            return false;
        }
        return true;
    }
    private void regist() {
        String userID = mUserIdET.getText().toString();
        String userName = mUserNameET.getText().toString();
        String pwd = mPasswordET.getText().toString();
//        String sex = mSexSpinner.getSelectedItem().toString();

        RequestParams params = new RequestParams(Constant.getRequestParams());

        params.put("user_id", userID);
        params.put("user_name", userName);
        params.put("password", (pwd));
        params.put("gender","1");
//        if (sex.equals("男")) {
//            params.put("gender", "1");
//        } else {
//            params.put("gender", "0");
//        }
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
                        DialogUtil.showToast(getActivity(), "注册成功！");
                        mUserIdET.setText("");
                        mUserNameET.setText("");
                        mPasswordET.setText("");
                        mRegistPasswordET.setText("");
                    } else if (code.equals("3000")) {
                        DialogUtil.showToast(getActivity(), "用户名已存在！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(getActivity(), "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                afterProcess("登录");
                DialogUtil.showToast(getActivity(), "连接服务器发生异常！");
            }
        });
        onProcess("注册中...");
    }

    private void onProcess(String s) {
//        dialog.show();
        mRegistButton.setEnabled(false);
    }

    private void afterProcess(String s) {
//        dialog.dismiss();
        mRegistButton.setEnabled(true);
    }
    /*初始化View*/
    private  void initViews() {

    }

    private  void initEvents() {

    }


}