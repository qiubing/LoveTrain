package cn.nubia.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import cn.nubia.util.Constant;
import cn.nubia.util.SpUtil;


/*胡立加，开机动画
* activity_splash：相对布局，图片铺满全屏，然后app和版权TextView位于相对布局的底部，局部覆盖全屏图片
* 如果是首次运行，跳转到GuideActivity，否则跳转到登录Activity
* 非首次启动动画时间为600+1000
* */

public class SplashActivity extends Activity {

	private Timer mTimer = new Timer();
	private int flag = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
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
