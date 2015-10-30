package cn.nubia.activity.client;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.activity.R;
import cn.nubia.activity.client.fragment.AllExamFragment;
import cn.nubia.activity.client.fragment.ClientAllCourceFragment;
import cn.nubia.activity.client.fragment.ClientMyCourceFragment;
import cn.nubia.activity.client.fragment.ClientMyFragment;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.zxing.barcode.CaptureActivity;

/**普通用户主界面：底部点击导航栏
 * 布局为RelativeLayout，RadioGroup在View底部，RadioGroup上面为FrameLayout，FrameLayout装Fragment
 * 采用RadioButton+Fragment结构
 * RadioButton改变，FrameLayout中的Fragment跟着相应改变
 * Created by 胡立加 on 2015/10/22.
 */

public class ClientMainActivity extends FragmentActivity  implements View.OnClickListener{
    private static final String TAG = "ClientMainActivity";
    private ClientMyCourceFragment mClientMyCourceFragment;
    private ClientAllCourceFragment mClientAllCourceFragment;
    private AllExamFragment mAllExamFragment;
    private ClientMyFragment mClientMyFragment;
    private long mExitTime;
    private FragmentTransaction mFragmentTransaction;
    private int currentItem = -1;
    private RelativeLayout signIn;
    private RelativeLayout [] mBackgrounds = new RelativeLayout[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        initViews();
        initEvents();
    }

    private  void initViews() {
        mBackgrounds[0] = (RelativeLayout) findViewById(R.id.client_radio_my_course);
        mBackgrounds[1] = (RelativeLayout) findViewById(R.id.client_radio_all_course);
        mBackgrounds[2] = (RelativeLayout) findViewById(R.id.client_radio_exam);
        mBackgrounds[3] = (RelativeLayout) findViewById(R.id.client_radio_my);
        signIn = (RelativeLayout) findViewById(R.id.client_sign_in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client_radio_my_course:
                if (currentItem != 0) {
                    setChoiceItem(0);
                }
                break;
            case R.id.client_radio_all_course:
                if (currentItem != 1) {
                    setChoiceItem(1);
                }
                break;
            case R.id.client_sign_in:
                Intent openCameraIntent = new Intent(ClientMainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.client_radio_exam:
                if (currentItem != 2) {
                    setChoiceItem(2);
                }
                break;
            case R.id.client_radio_my:
                if (currentItem != 3) {
                    setChoiceItem(3);
                }
                break;
        }
    }

    private  void initEvents() {

        for (RelativeLayout mRelativeLayout : mBackgrounds) {
            mRelativeLayout.setOnClickListener(this);
        }
        signIn.setOnClickListener(this);

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mClientMyCourceFragment = new ClientMyCourceFragment();
        mFragmentTransaction.add(R.id.client_fragment_layout, mClientMyCourceFragment);
        currentItem = 0;
        mFragmentTransaction.commit();

        updataItemBackground(0, R.color.toolbar_bg_selected);
    }

    public void setChoiceItem(int index) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        hideFragments(mFragmentTransaction);
        switch (index) {
            case 0:
                mFragmentTransaction.show(mClientMyCourceFragment);
                break;

            case 1:
                if (mClientAllCourceFragment == null) {
                    mClientAllCourceFragment = new ClientAllCourceFragment();
                    mFragmentTransaction.add(R.id.client_fragment_layout, mClientAllCourceFragment);
                } else {
                    mFragmentTransaction.show(mClientAllCourceFragment);
                }
                break;

            case 2:
                if (mAllExamFragment == null) {
                    mAllExamFragment = new AllExamFragment();
                    mFragmentTransaction.add(R.id.client_fragment_layout, mAllExamFragment);
                } else {
                    mFragmentTransaction.show(mAllExamFragment);
                }
                break;
            case 3:
                if (mClientMyFragment == null) {
                    mClientMyFragment = new ClientMyFragment();
                    mFragmentTransaction.add(R.id.client_fragment_layout, mClientMyFragment);
                } else {
                    mFragmentTransaction.show(mClientMyFragment);
                }
                break;
        }
        mFragmentTransaction.commit();

        updataItemBackground(index, R.color.toolbar_bg_selected);
        updataItemBackground(currentItem, R.color.toolbar_bg);
        currentItem = index;
    }

    void updataItemBackground(int index, int textColorResId) {
//        mBackgrounds[index].setBackgroundColor(textColorResId);
        mBackgrounds[index].setBackgroundColor(getResources().getColor(textColorResId));
    }

    private void hideFragments(FragmentTransaction transaction) {
        switch (currentItem) {
            case 0 :
                if (mClientMyCourceFragment != null) {
                    transaction.hide(mClientMyCourceFragment);
                }
                break;
            case 1 :
                if (mClientAllCourceFragment != null) {
                    transaction.hide(mClientAllCourceFragment);
                }
                break;
            case 2:
                if (mAllExamFragment != null) {
                    transaction.hide(mAllExamFragment);
                }
                break;
            case 3:
                if (mClientMyFragment != null) {
                    transaction.hide(mClientMyFragment);
                }
                break;
        }
    }

    private void exit() {
        if (System.currentTimeMillis() - mExitTime > Constant.INTERVAL) {
            Toast.makeText(this, R.string.exit, Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        } else {
            /*android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);*/
            //DbUtil.getInstance(this).closeDb();
            finishAffinity();

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                exit();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Log.e(TAG,scanResult);
            int lesson_index = Integer.parseInt(scanResult);
            if (!Constant.user.getUserID().equals("") && lesson_index > 0){
                //请求参数，包括用户的Id和课程的索引信息index
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("user_id",Constant.user.getUserID());
                params.put("lesson_index",lesson_index);
                Log.e(TAG,params.toString());
                //网络链接
                String url = Constant.BASE_URL + "user/user_check.do";
                AsyncHttpHelper.post(url, params, mCheckHandler);
            }
        }
    }

    private final JsonHttpResponseHandler mCheckHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0){
                    JSONObject obj = response.getJSONObject("data");
                    Log.e(TAG,obj.toString());
                    if (obj != null){
                        long check_time = obj.getLong("check_time");
                        int check_credits = obj.getInt("check_credits");
                        Date date = new Date();
                        date.setTime(check_time);
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                        Toast.makeText(ClientMainActivity.this,
                                "签到时间:" + time +" ,获取的积分:" + check_credits,Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(ClientMainActivity.this,"扫描二维码失败，请稍后重试",Toast.LENGTH_LONG).show();
        }
    };

}
