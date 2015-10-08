package cn.nubia.activity.admin;


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import cn.nubia.activity.R;

/**管理员主界面：Tab分页导航
 * activity_main_admin(修改对比版)：布局为TabHost框架，布局最下面为4个单选按钮,最上面为头标题栏，中间为FrameLayout，废弃了TabWidget
 * TabHost的内容为4个TabHost.TabSpec，展示于FrameLayout
 * 为单选按钮绑定监听器，内容为修改相应TabHost.TabSpec页面
 * 直接用tabHost.setOnTabChangedListener监听器不好么，为何要用四个单选按钮
 * TabActivity，ActivityGroup，ActionBar均已废弃，不知应怎样用未废弃的类实现静态分页功能
 * ViewPager为动态分页，且该类也很古老了
 * Created by 胡立加 on 2015/9/6.
 */
@SuppressWarnings("deprecation")
public class AdminMainActivity extends ActivityGroup {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        /*ImageView loading_iv = (ImageView)findViewById(R.id.loading_iv);
        loading_iv.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loading_iv.getDrawable();
        animationDrawable.start();
        ProgressBar loading_iv = (ProgressBar) findViewById(R.id.loading_iv);
        loading_iv.setVisibility(View.VISIBLE);*/
        initBeforeData();
        initEvents();
    }

    private  void initBeforeData() {
        addTabIntent();
        mTabHost.setCurrentTab(0);
        mRadioGroup=(RadioGroup) findViewById(R.id.main_admin_group);
        ImageView signIn = (ImageView) findViewById(R.id.signIn);
        signIn.setVisibility(View.GONE);
    }



    private  void initEvents() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
        mTabHost.setup(this.getLocalActivityManager());
       //这个不行，所以无法避免废弃类ActivityGroup mTabHost.setup();
        mTabHost.addTab(buildTabSpec("tab1", "0", new Intent(AdminMainActivity.this,
                AdminCourseActivity.class)));
        mTabHost.addTab(buildTabSpec("tab2", "1", new Intent(AdminMainActivity.this,
                AdminExamActivity.class)));
        mTabHost.addTab(buildTabSpec("tab3", "2", new Intent(AdminMainActivity.this,
                AdminShareActivity.class)));
        mTabHost.addTab(buildTabSpec("tab4", "3", new Intent(AdminMainActivity.this,
                AdminMyTabActivity.class)));
    }
    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(this, "再按一次返回退出应用", Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        } else {
            /*android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);*/
//            DbUtil.getInstance(this).closeDb();
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

