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
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.nubia.activity.admin.AdminMainActivity;
import cn.nubia.activity.admin.ProcessSPData;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.db.DbUtil;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.IDFactory;
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
                    if (!isFirstRun) {
                        SpUtil.putBoolean(SplashActivity.this, Constant.IS_FIRST_RUN, true);

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
        init();
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

    /**
     * 初始化一些变量
     */
    private void init() {
        IDFactory factory = new IDFactory(this);
        Constant.devideID = factory.getDevideID();
        Constant.apkVersion = factory.getVersionCode();
        Constant.loginTime = System.currentTimeMillis();
        Log.i("LK","SplashActivity init():devideID="+ Constant.devideID + "-apkVersion=" + Constant.apkVersion);
        RequestParams params = new RequestParams();
        String url = Constant.BASE_URL + "ucent/get_system_time.do";
        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("0")) {
                        Constant.systemTime = jsonObject.getLong("data");
                        Log.i("LK", "SplashActivity init():" + jsonObject.getLong("data"));
                    } else {
                        Log.e("LK", "SplashActivity init():服务器返回异常！");
                        Constant.systemTime = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LK", "SplashActivity init():服务器返回异常！");
                    Constant.systemTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("LK", "SplashActivity init():连接服务器发生异常！");
                Constant.systemTime = System.currentTimeMillis();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

}
