package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/11 15:58
 */
public class FeedBackActivity extends Activity {
    private EditText mContent;
    private EditText mEmail;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContent.setText(" ");
                mEmail.setText(" ");
                Toast.makeText(FeedBackActivity.this,"谢谢你们的反馈",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initViews(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.feedback_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("意见反馈");
        mContent = (EditText) findViewById(R.id.feedback_content);
        mEmail = (EditText) findViewById(R.id.feedback_email);
        mSubmit = (Button) findViewById(R.id.btn_submit);
    }

    public void back(View view) {
        this.finish();
    }
}
