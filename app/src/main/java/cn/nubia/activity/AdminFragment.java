package cn.nubia.activity;

import android.content.Intent;
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

import cn.nubia.activity.admin.AdminMainActivity;
import cn.nubia.db.DbUtil;
import cn.nubia.entity.Constant;
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.entity.UserInfo;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.HandleResponse;
import cn.nubia.util.ProcessSPData;

public class AdminFragment extends Fragment {
    private Button mLoginButton;

    private EditText mUserIdET;
    private EditText mPasswordET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*和管理员共用同一个页面*/
        View rootView = inflater.inflate(R.layout.activity_login_admin, container, false);
        mLoginButton = (Button) rootView.findViewById(R.id.login_btn_admin);
        mUserIdET = (EditText) rootView.findViewById(R.id.login_userid_admin);
        mPasswordET = (EditText) rootView.findViewById(R.id.login_pwd_admin);

        mUserIdET.setText("0016002652");
        mPasswordET.setText("123456");

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin())
                    login();
            }
        });
        initViews();
        initEvents();
        return rootView;
    }

    private void login() {
        String userID = mUserIdET.getText().toString();
        String pwd = mPasswordET.getText().toString();
        RequestParams params = new RequestParams(Constant.getRequestParams());

        Constant.user.setUserID(userID);
        String url;
        params.put("admin_id", userID);
        params.put("password", (pwd));
        url = Constant.BASE_URL + "ucent/admin_login.do";
        //如果跟上次登录不一致，清空数据
        String lastLoginID = ProcessSPData.getStringFromSP(getActivity(), "userID");
        if (!(lastLoginID != null && lastLoginID.equals(userID))) {
            DbUtil.getInstance(getActivity()).dropDatabase();
            RecordModifyFlag.getInstance().initRequestParams();
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
                    DialogUtil.showToast(getActivity(), "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                afterProcess("登录");
                DialogUtil.showToast(getActivity(), "连接服务器发生异常！");
            }
        });
        onProcess("登录中...");
    }

    private void onProcess(String s) {
//        dialog.show();
        mLoginButton.setEnabled(false);
        mLoginButton.setText(s);
    }

    private void afterProcess(String s) {
//        dialog.dismiss();
        mLoginButton.setEnabled(true);
        mLoginButton.setText(s);
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
                ProcessSPData.putIntoSP(getActivity(), "tokenKey", Constant.tokenKep);
                ProcessSPData.putIntoSP(getActivity(), "isLogin", true);

                user.setUserID(json.getString("admin_id"));
                user.setUserName(json.getString("admin_name"));
                user.setGender(true);
                user.setUserIconURL(null);
                user.setUserTotalCredits(0);
                user.setLastLoginTime(System.currentTimeMillis());
                Constant.user = user;

                ProcessSPData.putIntoSP(getActivity(), user);
                ProcessSPData.putIntoSP(getActivity(), "isAdmin", true);
                Constant.IS_ADMIN = true;
                startActivity(new Intent(getActivity(), AdminMainActivity.class));
                DialogUtil.showToast(getActivity(), "登录成功");
                getActivity().finish();
            } else {
                String msg = response.getString("message");
                HandleResponse.excute(getActivity(), code, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showToast(getActivity(), "服务器返回异常！");
        }
    }

    //对用户名和密码进行校验
    private boolean validateLogin() {
        String username = mUserIdET.getText().toString().trim();
        if (username.equals("")) {
            DialogUtil.showToast(getActivity(), "账号不能为空");
            return false;
        }
        String pwd = mPasswordET.getText().toString().trim();
        if (pwd.equals("")) {
            DialogUtil.showToast(getActivity(), "密码不能为空");
            return false;
        }
        return true;
    }

    /*初始化View*/
    private  void initViews() {

    }

    private  void initEvents() {

    }


}