package cn.nubia.activity;


import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import cn.nubia.activity.admin.CourseAdminActivity;
import cn.nubia.activity.admin.ExamAdminActivity;
import cn.nubia.activity.admin.MyAdminActivity;
import cn.nubia.activity.admin.ShareAdminActivity;

/**
 * Created by 胡立 on 2015/9/6.
 */
public class MainAdminActivity extends ActivityGroup {

    private TabHost mTabHost;
    private RadioGroup mRadioGroup;
    /**
     * 退出时间
     */
    private long mExitTime;
    /**
     * 退出间隔
     */
    private static final int INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_admin);
        initBeforeData();
        initEvents();
    }

    protected void initBeforeData() {
        addTabIntent();
        mTabHost.setCurrentTab(0);
        mRadioGroup=(RadioGroup) findViewById(R.id.main_admin_group);
    }



    protected void initEvents() {
        // TODO Auto-generated method stub
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.main_admin_radio_button1:
                        mTabHost.setCurrentTab(0);
                        break;
                    case R.id.main_admin_radio_button2:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.main_admin_radio_button3:
                        mTabHost.setCurrentTab(2);
                        break;
                    case R.id.main_admin_radio_button4:
                        mTabHost.setCurrentTab(3);
                        break;
                }
            }
        });
    }

    private TabHost.TabSpec buildTabSpec(String tag, String m, final Intent content) {
        return mTabHost.newTabSpec(tag).setIndicator(m).setContent(content);
    }

    private void addTabIntent() {
        mTabHost = (TabHost) findViewById(R.id.main_admin_tabhost);
        mTabHost.setup(MainAdminActivity.this.getLocalActivityManager());
        mTabHost.addTab(buildTabSpec("tab1", "0", new Intent(MainAdminActivity.this,
                CourseAdminActivity.class)));
        mTabHost.addTab(buildTabSpec("tab2", "1", new Intent(MainAdminActivity.this,
                ExamAdminActivity.class)));
        mTabHost.addTab(buildTabSpec("tab3", "2", new Intent(MainAdminActivity.this,
                ShareAdminActivity.class)));
        mTabHost.addTab(buildTabSpec("tab4", "3", new Intent(MainAdminActivity.this,
                MyAdminActivity.class)));
    }
    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(this, "再按一次返回退出应用", 1000).show();
            mExitTime = System.currentTimeMillis();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
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

