package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.GestureDetectorManager;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/11 15:58
 */
public class FeedBackActivity extends Activity {
    private EditText mContent;
    private EditText mEmail;
    private Button mSubmit;

    private GestureDetector gestureDetector;

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
                Toast.makeText(FeedBackActivity.this,"这只是一个演示而已！",Toast.LENGTH_LONG).show();
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


    /*//将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    public void back(View view) {
        this.finish();
    }
}
