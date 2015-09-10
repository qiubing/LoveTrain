package cn.nubia.activity.client;

/**
 * Created by 胡立 on 2015/9/7.
 */


import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.zxing.barcode.CaptureActivity;

/**普通用户主界面：Tab分页导航
 * activity_main_client(未修改版)：布局为TabHost框架，布局最下面为3个单选按钮,最上面为头标题栏，中间为FrameLayout，废弃了TabWidget
 * TabHost的内容为3个TabHost.TabSpec，展示于FrameLayout
 * 为单选按钮绑定监听器，内容为修改相应TabHost.TabSpec页面
 * 直接用tabHost.setOnTabChangedListener监听器不好么，为何要用四个单选按钮
 * Created by 胡立 on 2015/9/6.
 */
public class ClientMainActivity extends ActivityGroup {

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
                    case R.id.main_client_radio_myCourse:
                        mTabHost.setCurrentTab(0);
                        break;
                    case R.id.main_client_radio_allCourse:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.main_client_radio_my:
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
        mTabHost.setup(ClientMainActivity.this.getLocalActivityManager());
        mTabHost.addTab(buildTabSpec("tab1", "0", new Intent(ClientMainActivity.this,
                ClientMyCourseActivity.class)));
        mTabHost.addTab(buildTabSpec("tab2", "1", new Intent(ClientMainActivity.this,
                ClientAllCourseActivity.class)));
        mTabHost.addTab(buildTabSpec("tab3", "2", new Intent(ClientMainActivity.this,
                ClientMyTabActivity.class)));
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
            finish();
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

    public void search(View view) {
        //startActivity(new Intent(this, SearchActivity.class));
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();
            Toast.makeText(this,"扫描二维码成功",Toast.LENGTH_LONG).show();
        }
    }
}


