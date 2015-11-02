package cn.nubia.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class AdminCreditsAwardActivity extends BaseCommunicateActivity{
    private CreditsAwardMsg creditsAwardMsg;
    private boolean mNextPressReady;

    private Button mConfirmButton;
    private EditText mAwardedName;
    private EditText mAwardCredits;
    private EditText mAwardCause;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_credits_award);

        //公用部分
        ((TextView) findViewById(R.id.sub_page_title))
                .setText(R.string.activity_manager_award_title);

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
                if(mNextPressReady) {
                    if (checkData()) {
                        creditsAwardMsg = new CreditsAwardMsg();
                        creditsAwardMsg.setAwardedName(mAwardedName.getText().toString().trim());
                        creditsAwardMsg.setAwardedCredits(
                                Integer.parseInt(mAwardCredits.getText().toString().trim()));
                        creditsAwardMsg.setAwardedCause(mAwardCause.getText().toString().trim());
                        creditsAwardMsg.setOperateType(CommunicateService.OperateType.INSERT);
                        mBinder.communicate(
                                creditsAwardMsg, new Inter(), URLMap.URL_AWARD_CREDITS);
                        mNextPressReady = false;
                    }
                }
            }
        };
    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        if(response==null){
            DialogMaker.finishCurrentDialog(AdminCreditsAwardActivity.this,
                    AdminCreditsAwardActivity.this, "操作失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogMaker.finishCurrentDialog(AdminCreditsAwardActivity.this,
                        AdminCreditsAwardActivity.this,
                        "对 " + creditsAwardMsg.getAwardedName() + " 的积分奖励成功!", true);
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.finishCurrentDialog(AdminCreditsAwardActivity.this,
                        AdminCreditsAwardActivity.this,
                        "对 " + creditsAwardMsg.getAwardedName() + " 的积分奖励失败：\n" +
                                message, false);
            }
        }
    }

    public void back(View view) {
        this.finish();
    }
}
