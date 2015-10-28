package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.nubia.activity.R;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.GestureDetectorManager;

public class AdminRateActivity extends Activity {


    private EditText mRate_A;
    private EditText mRate_B;
    private EditText mRate_C;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rate);

        Button mButton;
        TextView mManagerTitle;
        ImageView mGoBack;
        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.activity_manager_rate_title);
        mGoBack = (ImageView) findViewById(R.id.title_back_image);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButton = (Button) findViewById(R.id.activity_manager_rate_button);
        mRate_A = (EditText) findViewById(R.id.activity_manager_rate_A);
        mRate_B = (EditText) findViewById(R.id.activity_manager_rate_B);
        mRate_C = (EditText) findViewById(R.id.activity_manager_rate_C);

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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RequestParams params = new RequestParams();
//                params.put("level_name");
//                String url = Constant.BASE_URL + "my/judge_manage.do";
//                AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        try {
//                            String s = new String(bytes, "UTF-8");
//                            JSONObject response = new JSONObject(s);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                        DialogUtil.showToast(AdminRateActivity.this, "连接服务器发生异常！");
//                    }
//                });
                if (validate()) {
                    DialogUtil.showDialog(AdminRateActivity.this, "评级规则设置成功！");
//                    startActivity(new Intent(AdminRateActivity.this,AdminMyTabActivity.class));
//                    finish();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    private boolean validate() {
        String input = mRate_A.getText().toString();
        int value;
        if (input.equals("")) {
            DialogUtil.showToast(this, "请输入优秀课程取值");
            return false;
        } else {
            try {
                value = Integer.valueOf(input);
                if (value < 0 || value > 5) {
                    DialogUtil.showToast(this, "请输入0~5之间的值");
                    return false;
                }
            } catch (Exception e) {
                DialogUtil.showToast(this, "请输入0~5之间的值");
                return false;
            }
        }
        input = mRate_B.getText().toString();
        if (input.equals("")) {
            DialogUtil.showToast(this, "请输入良好课程取值");
            return false;
        } else {
            try {
                value = Integer.valueOf(input);
                if (value < 0 || value > 5) {
                    DialogUtil.showToast(this, "请输入0~5之间的值");
                    return false;
                }
            } catch (Exception e) {
                DialogUtil.showToast(this, "请输入0~5之间的值");
                return false;
            }
        }
        input = mRate_C.getText().toString();
        if (input.equals("")) {
            DialogUtil.showToast(this, "请输入一般课程取值");
            return false;
        } else {
            try {
                value = Integer.valueOf(input);
                if (value < 0 || value > 5) {
                    DialogUtil.showToast(this, "请输入0~5之间的值");
                    return false;
                }
            } catch (Exception e) {
                DialogUtil.showToast(this, "请输入0~5之间的值");
                return false;
            }
        }
        return true;
    }
}
