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

public class SplashActivity extends Activity {

	Timer mTimer = new Timer();
	private int flag = 0;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (flag) {
			case 1:
				boolean isFirstRun = SpUtil.getBoolean(SplashActivity.this,
						Constant.IS_FIRST_RUN);
				if (!isFirstRun) {
					SpUtil.putBoolean(SplashActivity.this, Constant.IS_FIRST_RUN, true);
					mTimer.cancel();
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
				break;
			case 2:
				Intent intent = new Intent(SplashActivity.this, LoginTest.class);
				startActivity(intent);
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
