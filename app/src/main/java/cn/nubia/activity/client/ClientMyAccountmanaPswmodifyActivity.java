package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.activity.R;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyAccountmanaPswmodifyActivity extends Activity {
    private EditText mNewpswEditText;
    private Button mConfirmButton;
    private Button mBackButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accountmana_pswmodify);

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
                PswModifyMsg pswModifyMsg = new PswModifyMsg();
                String oldPsw =((EditText)findViewById(R.id.
                        my_accountmana_pswmodify_oldpswedittext)).getText().toString();
                String oldPswConfirm =((EditText)findViewById(R.id.
                        my_accountmana_pswmodify_oldpswconfirmedittext)).getText().toString();
                if(oldPsw.equals(oldPswConfirm)) {
                    if(!matchNewPsw()){
                        DialogUtil.showDialog(
                                ClientMyAccountmanaPswmodifyActivity.this, "新密码格式不正确，" +
                                        "请确保密码长度至少为七位，且包含字母与数字，" +
                                        "其中至少有一个大写字母", false);
                    }else{
                        pswModifyMsg.setOldPsw(oldPsw);
                        pswModifyMsg.setNewPsw(mNewpswEditText.getText().toString());
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
}
