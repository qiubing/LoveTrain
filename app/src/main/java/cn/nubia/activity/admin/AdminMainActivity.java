package cn.nubia.activity.admin;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.fragment.AdminMyFragment;
import cn.nubia.activity.admin.fragment.AdminShareFragment;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.activity.client.fragment.ClientAllCourceFragment;
import cn.nubia.activity.client.fragment.ClientExamFragment;
import cn.nubia.activity.client.fragment.ClientMyCourceFragment;
import cn.nubia.activity.client.fragment.ClientMyFragment;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.zxing.barcode.CaptureActivity;

/**admin主界面：底部点击导航栏
 * 布局为RelativeLayout，RadioGroup在View底部，RadioGroup上面为FrameLayout，FrameLayout装Fragment
 * 采用RadioButton+Fragment结构
 * RadioButton改变，FrameLayout中的Fragment跟着相应改变
 * Created by 胡立加 on 2015/10/22.
 */

public class AdminMainActivity extends FragmentActivity {
    private RadioGroup mRadioGroup;
    private ClientAllCourceFragment mClientAllCourceFragment;
    private ClientExamFragment mClientExamFragment;
    private AdminShareFragment mAdminShareFragment;
    private AdminMyFragment mAdminMyFragment;
    private long mExitTime;
    private FragmentTransaction mFragmentTransaction;
    private int currentItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initViews();
        initEvents();
    }

    private  void initViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.admin_group);
    }



    private  void initEvents() {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mClientAllCourceFragment = new ClientAllCourceFragment();
        mFragmentTransaction.add(R.id.admin_fragment_layout, mClientAllCourceFragment);
        currentItem = 0;
        mFragmentTransaction.commit();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.admin_radio_all_course:
                        if (currentItem != 0) {
                            setChoiceItem(0);
                        }
                        currentItem = 0;
                        break;
                    case R.id.admin_radio_exam:
                        if (currentItem != 1) {
                            setChoiceItem(1);
                        }
                        currentItem = 1;
                        break;
                    case R.id.admin_radio_share:
                        if (currentItem != 2) {
                            setChoiceItem(2);
                        }
                        currentItem = 2;
                        break;
                    case R.id.admin_radio_my:
                        if (currentItem != 3) {
                            setChoiceItem(3);
                        }
                        currentItem = 3;
                        break;
                }
            }
        });
    }

    public void setChoiceItem(int index) {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        hideFragments(mFragmentTransaction);
        switch (index) {
            case 0:
                mFragmentTransaction.show(mClientAllCourceFragment);
                break;

            case 1:
                if (mClientExamFragment == null) {
                    mClientExamFragment = new ClientExamFragment();
                    mFragmentTransaction.add(R.id.admin_fragment_layout, mClientExamFragment);
                } else {
                    mFragmentTransaction.show(mClientExamFragment);
                }
                break;

            case 2:
                if (mAdminShareFragment == null) {
                    mAdminShareFragment = new AdminShareFragment();
                    mFragmentTransaction.add(R.id.admin_fragment_layout, mAdminShareFragment);
                } else {
                    mFragmentTransaction.show(mAdminShareFragment);
                }
                break;
            case 3:
                if (mAdminMyFragment == null) {
                    mAdminMyFragment = new AdminMyFragment();
                    mFragmentTransaction.add(R.id.admin_fragment_layout, mAdminMyFragment);
                } else {
                    mFragmentTransaction.show(mAdminMyFragment);
                }
                break;
        }
        mFragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        switch (currentItem) {
            case 0 :
                if (mClientAllCourceFragment != null) {
                    transaction.hide(mClientAllCourceFragment);
                }
                break;
            case 1 :
                if (mClientExamFragment != null) {
                    transaction.hide(mClientExamFragment);
                }
                break;
            case 2:
                if (mAdminShareFragment != null) {
                    transaction.hide(mAdminShareFragment);
                }
                break;
            case 3:
                if (mAdminMyFragment != null) {
                    transaction.hide(mAdminMyFragment);
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

}
