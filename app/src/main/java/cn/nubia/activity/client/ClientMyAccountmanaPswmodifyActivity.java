package cn.nubia.activity.client;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyAccountmanaPswmodifyActivity extends Activity {
    private EditText mNewpswEditText;
    private Button mConfirmButton;

    private boolean mNextPressReady;

    private CommunicateService.CommunicateBinder mBinder;
    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (CommunicateService.CommunicateBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    public class Inter implements ActivityInter {
        public void handleResponse(Map<String,?> response,String responseURL){
            ClientMyAccountmanaPswmodifyActivity.this.handleResponse(response,responseURL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accountmana_pswmodify);

        //公用部分
        ((TextView) findViewById(R.id.manager_head_title))
                .setText(R.string.activity_my_accountmana_pswmodify_tittle_textview);

        holdView();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        connectService();
        mNextPressReady = true;
    }

    @Override
    public void onStop(){
        super.onStop();
        disconectService();
    }

    private boolean matchNewPsw() {
        String str = "^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mNewpswEditText.getText().toString().trim());
        return m.matches();
    }

    private View.OnClickListener makeConfirmOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNextPressReady ) {
                    String oldPsw = ((EditText) findViewById(R.id.
                            my_accountmana_pswmodify_oldpswedittext)).getText().toString().trim();
                    String newPsw = mNewpswEditText.getText().toString().trim();
                    String newPswConfirm = ((EditText) findViewById(R.id.
                            my_accountmana_pswmodify_newpswconfirmedittext)).getText().toString().trim();
                    if (newPsw.equals(newPswConfirm)) {
                        if (!matchNewPsw()) {
                            DialogUtil.showDialog(
                                    ClientMyAccountmanaPswmodifyActivity.this, "新密码格式不正确：\n" +
                                            "请确保密码长度至少为七位，且包含字母与数字，" +
                                            "其中至少有一个大写字母", false);
                        } else if (oldPsw.equals("")) {
                            DialogUtil.showDialog(
                                    ClientMyAccountmanaPswmodifyActivity.this, "原密码不能为空！", false);
                        } else {
                            PswModifyMsg pswModifyMsg = new PswModifyMsg();
                            pswModifyMsg.setOldPsw(oldPsw);
                            pswModifyMsg.setNewPsw(newPsw);
                            pswModifyMsg.setOperateType(CommunicateService.OperateType.UPDATE);
                            mBinder.communicate(
                                    pswModifyMsg, new Inter(), URLMap.URL_UPD_PSW);
                            mNextPressReady = false;
                        }
                    } else {
                        DialogUtil.showDialog(
                                ClientMyAccountmanaPswmodifyActivity.this, "确认密码输入不一致", false);
                    }
                }
            }
        };
    }

    private void holdView() {
        mConfirmButton = (Button) findViewById(
                R.id.my_accountmana_pswmodify_confirmbutton);
        mNewpswEditText = (EditText) findViewById(
                R.id.my_accountmana_pswmodify_newpswedittext);
    }

    private void connectService() {
        Intent intent = new Intent(
                ClientMyAccountmanaPswmodifyActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void disconectService(){
        unbindService(mConn);
    }

    private void setViewLogic() {
        (findViewById(R.id.manager_goback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }

    private void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        if(response==null){
            DialogMaker.make(
                   ClientMyAccountmanaPswmodifyActivity.this, "操作失败!", false).show();
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogMaker.make(
                        ClientMyAccountmanaPswmodifyActivity.this, "密码修改成功!", true).show();
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.make(
                        ClientMyAccountmanaPswmodifyActivity.this, "密码修改失败：\n" +
                                message, false).show();
            }
        }
    }
}
