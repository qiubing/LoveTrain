package cn.nubia.activity.client;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.R;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyAccountmanaPswmodifyActivity extends Activity {
    private EditText mNewpswEditText;
    private Button mConfirmButton;
    private Button mBackButton;

    private CommunicateService.CommunicateBinder mBinder;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (CommunicateService.CommunicateBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    public class Inter implements ActivityInter {
        public void alter(List<?> list){
            ClientMyAccountmanaPswmodifyActivity.this.showOperateResult((List<String>)list);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accountmana_pswmodify);
        connectService();

        holdView();
        setViewLogic();
    }

    private boolean matchNewPsw(){
        String str="^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mNewpswEditText.getText().toString());
        return m.matches();
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPsw =((EditText)findViewById(R.id.
                        my_accountmana_pswmodify_oldpswedittext)).getText().toString();
                String newPsw =mNewpswEditText.getText().toString();
                String newPswConfirm =((EditText)findViewById(R.id.
                        my_accountmana_pswmodify_newpswconfirmedittext)).getText().toString();
                if(newPsw.equals(newPswConfirm)) {
                    if(!matchNewPsw()){
                        DialogUtil.showDialog(
                                ClientMyAccountmanaPswmodifyActivity.this, "新密码格式不正确，" +
                                        "请确保密码长度至少为七位，且包含字母与数字，" +
                                        "其中至少有一个大写字母", false);
                    }else{
                        PswModifyMsg pswModifyMsg = new PswModifyMsg();
                        pswModifyMsg.setOldPsw(oldPsw);
                        pswModifyMsg.setNewPsw(newPsw);

                        mBinder.communicate(pswModifyMsg, new Inter());
                    }
                }else {
                    DialogUtil.showDialog(
                            ClientMyAccountmanaPswmodifyActivity.this, "确认密码输入不一致", false);
                }
            }
        };
    }

    private void holdView(){
        mConfirmButton =(Button) findViewById(
                R.id.my_accountmana_pswmodify_confirmbutton);
        mBackButton =(Button) findViewById(
                R.id.my_accountmana_pswmodify_backbutton);
        mNewpswEditText =(EditText) findViewById(
                R.id.my_accountmana_pswmodify_newpswedittext);
    }

    private void connectService(){
        Intent intent = new Intent(
                ClientMyAccountmanaPswmodifyActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void setViewLogic(){
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientMyAccountmanaPswmodifyActivity.this.finish();
            }
        });
    }

    private void showOperateResult(List<String> list) {
        Boolean result = Boolean.getBoolean(list.get(0));
        if(result)
            DialogUtil.showDialog(
                    ClientMyAccountmanaPswmodifyActivity.this,"密码修改成功!",false);
        else
            DialogUtil.showDialog(
                    ClientMyAccountmanaPswmodifyActivity.this,"密码修改失败!",false);

    }
}
