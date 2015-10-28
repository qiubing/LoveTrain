package cn.nubia.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.Constant;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyAccountmanaPswmodifyActivity extends BaseCommunicateActivity {
    private EditText mNewpswEditText;
    private Button mConfirmButton;
    private boolean mNextPressReady;

    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accountmana_pswmodify);

        //公用部分
        ((TextView) findViewById(R.id.sub_page_title))
                .setText(R.string.activity_my_accountmana_pswmodify_tittle_textview);

        holdView();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        mNextPressReady = true;
    }

//    @Override
//    protected void onBinderCompleted() {
//
//    }

    private boolean matchNewPsw() {
        String str = "^(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mNewpswEditText.getText().toString().trim());
        return !m.matches();
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
                        if (matchNewPsw()) {
                            DialogUtil.showDialog(
                                    ClientMyAccountmanaPswmodifyActivity.this, "新密码格式不正确：\n" +
                                            "请确保密码长度至少为七位，且包含字母与数字，" +
                                            "其中至少有一个大写字母", false);
                        } else if (oldPsw.equals("")) {
                            DialogUtil.showDialog(
                                    ClientMyAccountmanaPswmodifyActivity.this, "原密码不能为空！", false);
                        } else {
                            initLoadingUI();

                            PswModifyMsg pswModifyMsg = new PswModifyMsg();
                            pswModifyMsg.setOldPsw(oldPsw);
                            pswModifyMsg.setNewPsw(newPsw);
                            pswModifyMsg.setUserID(Constant.user.getUserID());
                            pswModifyMsg.setOperateType(CommunicateService.OperateType.UPDATE);
                            mConfirmButton.setText("正在修改...");
                            mBinder.communicate(pswModifyMsg, new Inter(), URLMap.URL_UPD_PSW);
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

        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
    }

    private void initLoadingUI(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                ClientMyAccountmanaPswmodifyActivity.this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);
    }

    private void setViewLogic() {
        (findViewById(R.id.title_back_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        mNextPressReady = true;
        mConfirmButton.setText(R.string.alterCourseForSure);
        if(response==null){
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            DialogMaker.finishCurrentDialog(ClientMyAccountmanaPswmodifyActivity.this,
                    ClientMyAccountmanaPswmodifyActivity.this, "连接服务器失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogMaker.finishCurrentDialog(ClientMyAccountmanaPswmodifyActivity.this,
                        ClientMyAccountmanaPswmodifyActivity.this, "密码修改成功!", true);
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.finishCurrentDialog(ClientMyAccountmanaPswmodifyActivity.this,
                        ClientMyAccountmanaPswmodifyActivity.this, "密码修改失败：\n" +
                                message, false);
            }
        }
    }
}
