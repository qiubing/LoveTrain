package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.entity.LessonJudgement;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class AdminCreditsAwardActivity extends Activity{
    private Button mConfirmButton;
    private ImageView mBackButton;
    private EditText mAwardedName;
    private EditText mAwardCredits;
    private EditText mAwardCause;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_credits_award);

        holdView();
        setViewLogic();
    }

    private void holdView(){
        mConfirmButton =(Button) findViewById(
                R.id.activity_manager_award_confirm_button);
        mBackButton =(ImageView) findViewById(
                R.id.manager_award_backImage);
        mAwardedName =(EditText) findViewById(
                R.id.activity_manager_awarded_name);
        mAwardCredits =(EditText) findViewById(
                R.id.activity_manager_awarded_credit);
        mAwardCause =(EditText) findViewById(
                R.id.activity_manager_awarded_cause);
    }

    private void setViewLogic(){
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminCreditsAwardActivity.this.finish();
            }
        });
    }

    private boolean checkData(){
        if(mAwardedName.getText().toString()==null){
            Toast.makeText(this, "被奖励人姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mAwardCredits.getText().toString()==null){
            Toast.makeText(this, "奖励积分不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    CreditsAwardMsg creditsAwardMsg = new CreditsAwardMsg();
                    creditsAwardMsg.setAwardedName(mAwardedName.getText().toString());
                    creditsAwardMsg.setAwardedCredits(
                            Integer.parseInt(mAwardCredits.getText().toString()));
                    creditsAwardMsg.setAwardedCause(mAwardCause.getText().toString());
                }
            }
        };
    }
}
