package cn.nubia.activity.admin;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class AdminCreditsAwardActivity extends Activity {
    private Button mConfirmButton;
    private EditText mAwardedName;
    private EditText mAwardCredits;
    private EditText mAwardCause;

    private TextView mManagerTitle;
    private ImageView mGoBack;

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
            AdminCreditsAwardActivity.this.showOperateResult((List<String>)list);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_credits_award);
        connectService();
        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.activity_manager_award_title);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);


        holdView();
        setViewLogic();
    }

    private void holdView() {
        mConfirmButton = (Button) findViewById(
                R.id.activity_manager_award_confirm_button);
        mAwardedName = (EditText) findViewById(
                R.id.activity_manager_awarded_name);
        mAwardCredits = (EditText) findViewById(
                R.id.activity_manager_awarded_credit);
        mAwardCause = (EditText) findViewById(
                R.id.activity_manager_awarded_cause);
    }

    private void setViewLogic() {
        /**监听后退按钮，返回上一个activity*/
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }

    private boolean checkData() {
        if (mAwardedName.getText().toString() == null) {
            Toast.makeText(this, "被奖励人姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mAwardCredits.getText().toString() == null) {
            Toast.makeText(this, "奖励积分不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private View.OnClickListener makeConfirmOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    CreditsAwardMsg creditsAwardMsg = new CreditsAwardMsg();
                    creditsAwardMsg.setAwardedName(mAwardedName.getText().toString());
                    creditsAwardMsg.setAwardedCredits(
                            Integer.parseInt(mAwardCredits.getText().toString()));
                    creditsAwardMsg.setAwardedCause(mAwardCause.getText().toString());

                    mBinder.communicate(creditsAwardMsg, new Inter());
                }
            }
        };
    }

    private void connectService(){
        Intent intent = new Intent(
                AdminCreditsAwardActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void showOperateResult(List<String> list) {
        Boolean result = Boolean.getBoolean(list.get(0));
        if(result)
            DialogUtil.showDialog(
                    AdminCreditsAwardActivity.this,"积分奖励成功!",false);
        else
            DialogUtil.showDialog(
                    AdminCreditsAwardActivity.this,"积分奖励失败!",false);

    }
}
