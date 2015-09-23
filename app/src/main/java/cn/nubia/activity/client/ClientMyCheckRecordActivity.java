package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.adapter.ClientCheckRecordAdapter;
import cn.nubia.entity.CheckRecordItem;
import cn.nubia.entity.Constant;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * ClassName:
 * Description: 签到记录类
 * Author: qiubing
 * Date: 2015/9/2 9:26
 */
public class ClientMyCheckRecordActivity extends Activity {
    private static final String TAG = "MyCheckRecord";
    private List<CheckRecordItem> mCheckList;//签到记录表
    private ListView mListView;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checked_record);
        setGesture();
        initEvents();

    }

    private void initEvents() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_check_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("我的签到记录");
        mCheckList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.check_detail);
        //请求参数
        RequestParams params = new RequestParams(Constant.getRequestParams());
        params.put("user_id", Constant.user.getUserID());
        String url = Constant.BASE_URL + "user/find_check_record.do";
        Log.e(TAG,"user_id: " + Constant.user.getUserID() + ",url: " + url);
        AsyncHttpHelper.post(url, params, mCheckRecordHandler);
    }

    private final MyJsonHttpResponseHandler mCheckRecordHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response != null && response.getInt("code") == 0) {
                    EntityFactoryGenerics factoryGenerics =
                            new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.CHECKRECORD, response);
                    Log.e(TAG, "onSuccess:" + response.toString());
                    mCheckList = (List<CheckRecordItem>) factoryGenerics.get();
                    ClientCheckRecordAdapter mCheckAdapter = new ClientCheckRecordAdapter(mCheckList, ClientMyCheckRecordActivity.this);
                    mListView = (ListView) findViewById(R.id.check_detail);
                    mListView.setAdapter(mCheckAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e(TAG, TAG + ":onFailure");
        }
    };


    /**
     * 返回箭头绑定事件，即退出该页面
     * <p/>
     * param view
     */
    public void back(View view) {
        this.finish();
    }

    /**
     *设置手势函数
     */
    private void setGesture(){
        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);
        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

}
