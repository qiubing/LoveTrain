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
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.zxing.barcode.CaptureActivity;

/**普通用户主界面：Tab分页导航
 * activity_main_client(未修改版)：布局为TabHost框架，布局最下面为3个单选按钮,最上面为头标题栏，中间为FrameLayout，废弃了TabWidget
 * TabHost的内容为3个TabHost.TabSpec，展示于FrameLayout
 * 为单选按钮绑定监听器，内容为修改相应TabHost.TabSpec页面
 * 直接用tabHost.setOnTabChangedListener监听器不好么，为何要用四个单选按钮
 * Created by 胡立 on 2015/9/6.
 */
@SuppressWarnings("deprecation")
public class ClientMainActivity extends ActivityGroup {
    private static final String TAG = "ClientMainActivity";
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
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_client);
        initBeforeData();
        initEvents();
    }

    private void initBeforeData() {
        addTabIntent();
        mTabHost.setCurrentTab(0);
        mRadioGroup=(RadioGroup) findViewById(R.id.main_client_group);
    }

    private void initEvents() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            int lesson_index = Integer.parseInt(scanResult);
            if (!Constant.user.getUserID().equals("")&& lesson_index > 0){
                //请求参数，包括用户的Id和课程的索引信息index
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("user_id",Constant.user.getUserID());
                params.put("lesson_index",lesson_index);
                //网络链接
                String url = Constant.BASE_URL + "user/user_check.do";
                AsyncHttpHelper.post(url, params, mCheckHandler);
            }
        }
    }

    private final MyJsonHttpResponseHandler mCheckHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) throws JSONException {
            if (response != null && response.getInt("code") == 0){
                JSONObject obj = response.getJSONObject("data");
                if (obj != null){
                    long check_time = obj.getLong("check_time");
                    int check_credits = obj.getInt("check_credits");
                    Date date = new Date();
                    date.setTime(check_time);
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                    Toast.makeText(ClientMainActivity.this,
                            "签到时间:" + time +" ,获取的积分:" + check_credits,Toast.LENGTH_LONG).show();
                }
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(ClientMainActivity.this,"扫描二维码失败，请稍后重试",Toast.LENGTH_LONG).show();
        }
    };
}


