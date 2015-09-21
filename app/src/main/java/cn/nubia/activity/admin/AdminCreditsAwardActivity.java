package cn.nubia.activity.admin;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.R;
import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class AdminCreditsAwardActivity extends Activity {
    private CreditsAwardMsg creditsAwardMsg;
    private boolean mNextPress;

    private Button mConfirmButton;
    private EditText mAwardedName;
    private EditText mAwardCredits;
    private EditText mAwardCause;

    private TextView mManagerTitle;
    private ImageView mGoBack;

    private CommunicateService.CommunicateBinder mBinder;
    private final ServiceConnection mConn = new ServiceConnection() {
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
        public void alter(List<?> list,String URL){
            AdminCreditsAwardActivity.this.showOperateResult((List<String>)list,URL);
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

    @Override
    public void onStart(){
        super.onStart();
        mNextPress = true;
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
                disconectService();
                finish();
            }
        });
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }

    private boolean checkData() {
        if (mAwardedName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "被奖励人姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mAwardCredits.getText().toString().trim().equals("")) {
            Toast.makeText(this, "奖励积分不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(mAwardCredits.getText().toString().trim());
            if(!isNum.matches()){
                Toast.makeText(this, "请不要输入非法字符！", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        }
    }

    private View.OnClickListener makeConfirmOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNextPress) {
                    if (checkData()) {
                        creditsAwardMsg = new CreditsAwardMsg();
                        creditsAwardMsg.setAwardedName(mAwardedName.getText().toString().trim());
                        creditsAwardMsg.setAwardedCredits(
                                Integer.parseInt(mAwardCredits.getText().toString().trim()));
                        creditsAwardMsg.setAwardedCause(mAwardCause.getText().toString().trim());
                        creditsAwardMsg.setOperateType(CommunicateService.OperateType.INSERT);
                        mBinder.communicate(
                                creditsAwardMsg, new Inter(), URLMap.URL_AWARD_CREDITS);
                        mNextPress = false;
                    }
                }
            }
        };
    }

    private void connectService(){
        Intent intent = new Intent(
                AdminCreditsAwardActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void disconectService(){
        unbindService(mConn);
    }

    private void showOperateResult(List<String> list,String tagetURL) {
        if(list==null){
            DialogUtil.showDialog(
                    AdminCreditsAwardActivity.this,"操作失败!",false);
        }else{
            String result = list.get(0);
            if(result.equals("0"))
                DialogUtil.showDialog(
                        AdminCreditsAwardActivity.this,
                        "对 "+creditsAwardMsg.getAwardedName()+" 的积分奖励成功!",true);
            else if(result.equals("1"))
                DialogUtil.showDialog(
                        AdminCreditsAwardActivity.this,
                        "对 "+creditsAwardMsg.getAwardedName()+" 的积分奖励失败!",true);
        }
        mNextPress = true;

    }
}
