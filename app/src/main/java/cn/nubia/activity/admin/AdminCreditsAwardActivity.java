package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.entity.CreditsAwardMsg;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_credits_award);

        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.activity_manager_award_title);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        /**监听确认按钮，进行提交动作*/

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
                }
            }
        };
    }
}
