package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientEvaluateActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by WJ on 2015/9/22.
 */
public class AdminSignInLessonPersonInfoActivity extends Activity {
    private LessonItem mLessonItem;
    private final static String URL = Constant.BASE_URL +"exam/check_list.do";
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_score_user_detail);
        initView();
        initEvent();
        /**获取数据**/
        loadData();
    }

    private void initView(){

        TextView barTxt = (TextView) findViewById(R.id.sub_page_title);
        barTxt.setText("签到人数");
        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
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

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    private void initEvent(){
        mLessonItem = (LessonItem) getIntent().getSerializableExtra("LessonItem");
    }


    private void loadData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);

        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.put("lesson_index", mLessonItem.getIndex());
        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                if(response.getInt("code") != 0){
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                    return;
                }
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray ==null || jsonArray.length() == 0)
                    return;
                List<Map<String, Object>> listItems =  new ArrayList<Map<String, Object>>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("detail");
                    Map<String, Object> listItem = new HashMap<>();
                    listItem.put("user_name",jsonObject.getString("user_name"));
                    listItem.put("user_id", jsonObject.getString("user_id"));
                    if(!jsonObject.isNull("check_time")){
                        listItem.put("check_time",TimeFormatConversion.
                                toDateTime(Long.valueOf(jsonObject.getString("check_time")),true));
                    }
                    listItems.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(AdminSignInLessonPersonInfoActivity.this,
                        listItems,R.layout.score_course_item_detail,
                        new String[]{"user_name","user_id","check_time"},
                        new int[]{R.id.score_course_detail_name,R.id.score_course_detail_id,R.id.score_course_detail_score});

                ListView listView = (ListView) findViewById(R.id.manager_score_user_detail_listview);
                listView.setAdapter(simpleAdapter);
                if(listItems.size()>0){
                    findViewById(R.id.no_record).setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminSignInLessonPersonInfoActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    public void back(View view) {
        this.finish();
    }
}
