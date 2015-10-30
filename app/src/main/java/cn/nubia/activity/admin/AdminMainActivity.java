package cn.nubia.activity.admin;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.fragment.AdminMyFragment;
import cn.nubia.activity.admin.fragment.AdminShareFragment;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.activity.client.fragment.AllExamFragment;
import cn.nubia.activity.client.fragment.ClientAllCourceFragment;
import cn.nubia.entity.Constant;
import cn.nubia.zxing.barcode.CaptureActivity;

/**admin主界面：底部点击导航栏
 * 布局为RelativeLayout，RadioGroup在View底部，RadioGroup上面为FrameLayout，FrameLayout装Fragment
 * 采用RadioButton+Fragment结构
 * RadioButton改变，FrameLayout中的Fragment跟着相应改变
 * Created by 胡立加 on 2015/10/22.
 * RadioGroup change to layout
 */

public class AdminMainActivity extends FragmentActivity implements View.OnClickListener{
    private RadioGroup mRadioGroup;
    private ClientAllCourceFragment mClientAllCourceFragment;
    private AllExamFragment mClientExamFragment;
    private AdminShareFragment mAdminShareFragment;
    private AdminMyFragment mAdminMyFragment;
    private long mExitTime;
    private FragmentTransaction mFragmentTransaction;
    private int currentItem = -1;
    private RelativeLayout[] mBackgrounds = new RelativeLayout[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initViews();
        initEvents();
    }

    private  void initViews() {
        mBackgrounds[0] = (RelativeLayout) findViewById(R.id.admin_radio_all_course);
        mBackgrounds[1] = (RelativeLayout) findViewById(R.id.admin_radio_exam);
        mBackgrounds[2] = (RelativeLayout) findViewById(R.id.admin_radio_share);
        mBackgrounds[3] = (RelativeLayout) findViewById(R.id.admin_radio_my);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_radio_all_course:
                if (currentItem != 0) {
                    setChoiceItem(0);
                }
                break;
            case R.id.admin_radio_exam:
                if (currentItem != 1) {
                    setChoiceItem(1);
                }
                break;

            case R.id.admin_radio_share:
                if (currentItem != 2) {
                    setChoiceItem(2);
                }
                break;
            case R.id.admin_radio_my:
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

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mClientAllCourceFragment = new ClientAllCourceFragment();
        mFragmentTransaction.add(R.id.admin_fragment_layout, mClientAllCourceFragment);
        currentItem = 0;
        mFragmentTransaction.commit();

        updataItemBackground(0, R.color.toolbar_bg_selected);

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
                    mClientExamFragment = new AllExamFragment();
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
