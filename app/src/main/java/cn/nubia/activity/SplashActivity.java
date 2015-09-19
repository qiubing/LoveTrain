package cn.nubia.activity;



/*胡立加，开机动画
* activity_splash：相对布局，图片铺满全屏，然后app名称和版权TextView位于相对布局的底部，局部覆盖全屏图片
* 如果是首次运行，跳转到GuideActivity，否则跳转到登录Activity
* 非首次启动动画时间为600+1000
* */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import cn.nubia.activity.admin.AdminMainActivity;
import cn.nubia.activity.admin.ProcessSPData;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.entity.Constant;
import cn.nubia.util.DbUtil;
import cn.nubia.util.SpUtil;


public class SplashActivity extends Activity {

    private Timer mTimer = new Timer();
    private int flag = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			DbUtil.getInstance(SplashActivity.this);

			switch (flag) {
			case 1:
				boolean isFirstRun = SpUtil.getBoolean(SplashActivity.this,
						Constant.IS_FIRST_RUN);
				if (isFirstRun) {
					SpUtil.putBoolean(SplashActivity.this, Constant.IS_FIRST_RUN, false);
					mTimer.cancel();
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
				break;
			case 2:
				Boolean isLogin = ProcessSPData.getBoolenFromSP(SplashActivity.this, "isLogin");
				if (isLogin) {
					Constant.user = ProcessSPData.getUserFromSP(SplashActivity.this);
					Constant.tokenKep = ProcessSPData.getStringFromSP(SplashActivity.this, "tokenKey");
					Boolean isAdmin = ProcessSPData.getBoolenFromSP(SplashActivity.this, "isAdmin");
					if (isAdmin) {
						Constant.IS_ADMIN = true;
						startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
						SplashActivity.this.finish();
					} else {
						Constant.IS_ADMIN = false;
						startActivity(new Intent(SplashActivity.this, ClientMainActivity.class));
						SplashActivity.this.finish();
					}
					break;
				}
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
				break;
			}

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                flag++;
                Message mesasge = new Message();
                mesasge.what = flag;
                handler.sendMessage(mesasge);
            }
        }, 600, 1000);
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

}
