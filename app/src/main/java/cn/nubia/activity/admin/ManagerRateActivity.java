package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class ManagerRateActivity extends Activity {

    private Button mButton;
    private EditText mRate_A;
    private EditText mRate_B;
    private EditText mRate_C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rate);

        mButton = (Button) findViewById(R.id.activity_manager_rate_button);
        mRate_A = (EditText) findViewById(R.id.activity_manager_rate_A);
        mRate_B = (EditText) findViewById(R.id.activity_manager_rate_B);
        mRate_C = (EditText) findViewById(R.id.activity_manager_rate_C);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                DialogUtil.showDialog(ManagerRateActivity.this, "save");
            }
        });
    }


}
