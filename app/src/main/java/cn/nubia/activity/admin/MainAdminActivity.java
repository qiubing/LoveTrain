package cn.nubia.activity.admin;


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import cn.nubia.activity.R;

/**管理员主界面：Tab分页导航
 * activity_main_admin(修改对比版)：布局为TabHost框架，布局最下面为4个单选按钮,最上面为头标题栏，中间为FrameLayout，废弃了TabWidget
 * TabHost的内容为4个TabHost.TabSpec，展示于FrameLayout
 * 为单选按钮绑定监听器，内容为修改相应TabHost.TabSpec页面
 * 直接用tabHost.setOnTabChangedListener监听器不好么，为何要用四个单选按钮
 * Created by 胡立加 on 2015/9/6.
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
                    case R.id.main_admin_radio_course:
                        mTabHost.setCurrentTab(0);
                        break;
                    case R.id.main_admin_radio_exam:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.main_admin_radio_share:
                        mTabHost.setCurrentTab(2);
                        break;
                    case R.id.main_admin_radio_my:
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
                MyAdminActivity_1.class)));
    }
    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private boolean exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(this, "再按一次返回退出应用", Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        } else {
            /*android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);*/
            finish();

        }
        return true;
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

    public void search(View view) {
        //startActivity(new Intent(this, SearchActivity.class));
        Toast.makeText(this, "管理员不需要签到", Toast.LENGTH_LONG).show();
    }
}

