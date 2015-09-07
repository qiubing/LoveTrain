package cn.nubia.activity;

/**
 * Created by 胡立 on 2015/9/7.
 */


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import cn.nubia.activity.client.AllCourceClientActivity;
import cn.nubia.activity.client.MyClientActivity_1;
import cn.nubia.activity.client.MyCourseClientActivity;

/**
 * Created by 胡立 on 2015/9/6.
 */
public class MainClientActivity extends ActivityGroup {

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
        setContentView(R.layout.activity_main_client);
        initBeforeData();
        initEvents();
    }

    protected void initBeforeData() {
        addTabIntent();
        mTabHost.setCurrentTab(0);
        mRadioGroup=(RadioGroup) findViewById(R.id.main_client_group);
    }



    protected void initEvents() {
        // TODO Auto-generated method stub
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.main_client_radio_button1:
                        mTabHost.setCurrentTab(0);
                        break;
                    case R.id.main_client_radio_button2:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.main_client_radio_button3:
                        mTabHost.setCurrentTab(2);
                        break;
                }
            }
        });
    }

    private TabHost.TabSpec buildTabSpec(String tag, String m, final Intent content) {
        return mTabHost.newTabSpec(tag).setIndicator(m).setContent(content);
    }

    private void addTabIntent() {
        mTabHost = (TabHost) findViewById(R.id.main_client_tabhost);
        mTabHost.setup(MainClientActivity.this.getLocalActivityManager());
        mTabHost.addTab(buildTabSpec("tab1", "0", new Intent(MainClientActivity.this,
                MyCourseClientActivity.class)));
        mTabHost.addTab(buildTabSpec("tab2", "1", new Intent(MainClientActivity.this,
                AllCourceClientActivity.class)));
        mTabHost.addTab(buildTabSpec("tab3", "2", new Intent(MainClientActivity.this,
                MyClientActivity_1.class)));
    }
    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(this, "再按一次返回退出应用", Toast.LENGTH_LONG).show();
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


