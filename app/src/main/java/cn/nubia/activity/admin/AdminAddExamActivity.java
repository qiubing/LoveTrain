package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Calendar;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

@SuppressWarnings("deprecation")
public class AdminAddExamActivity extends Activity implements  View.OnClickListener{

    private EditText mExamInfo;
    private EditText mExamTitle;
    private EditText mExamAddress;
    private EditText mExamStartDate;
    private EditText mExamStartTime;
    private EditText mExamEndTime;
    private EditText mExamCredit;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private static final String URL = Constant.BASE_URL + "/exam/add.do";
    private ExamItem mExamItem;
    private int year;
    private int month;
    private int day;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;
    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_exam);
        mExamItem = new ExamItem();

        mExamTitle = (EditText) findViewById(R.id.activity_manager_add_exam_one);
        mExamInfo = (EditText) findViewById(R.id.activity_manager_add_exam_info);
        mExamAddress = (EditText) findViewById(R.id.activity_manager_add_exam_address);
        mExamStartTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_start);
        mExamEndTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_end);
        mExamCredit = (EditText) findViewById(R.id.activity_manager_add_exam_credit);

        Button mAddButton = (Button) findViewById(R.id.activity_manager_add_exam_button);
        mExamStartDate = (EditText) findViewById(R.id.activity_manager_add_exam_time_data);

        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("新增考试");
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        mExamStartDate.setOnClickListener(this);
        mExamStartTime.setOnClickListener(this);
        mExamEndTime.setOnClickListener(this);
        mAddButton.setOnClickListener(this);

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
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    private void upData(){
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
        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.add("course_index", null);
        requestParams.add("exam_name", mExamTitle.getText().toString());
        mExamItem.setName(mExamTitle.getText().toString());
        requestParams.add("exam_description", mExamInfo.getText().toString());
        mExamItem.setDescription(mExamInfo.getText().toString());
        requestParams.add("locale", mExamAddress.getText().toString());
        mExamItem.setLocale(mExamInfo.getText().toString());
        requestParams.put("start_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart));
        long startTime = TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart);
        mExamItem.setStartTime(startTime);
        requestParams.put("end_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd));
        long endTime = TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd);
        mExamItem.setEndTime(endTime);
        requestParams.put("exam_credits", mExamCredit.getText().toString());
        mExamItem.setExamCredits(20);

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                int code = response.getInt("code");
//                boolean result = response.getBoolean("result");
                mExamItem.setIndex(response.getInt("data"));
                //JSONArray jsonArray = response.getJSONArray("data");
                if(code == 0) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ExamItem",mExamItem);
                    intent.putExtras(bundle);
                    AdminAddExamActivity.this.setResult(2, intent);
                    mExamTitle.setText("");
                    mExamInfo.setText("");
                    mExamAddress.setText("");
                    mExamStartTime.setText("");
                    mExamEndTime.setText("");
                    mExamCredit.setText("");
                    mExamStartDate.setText("");
                    Toast.makeText(AdminAddExamActivity.this, "添加考试成功", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminAddExamActivity.this, "添加考试失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminAddExamActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case  R.id.activity_manager_add_exam_button :
                if (mExamTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "考试名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mExamAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "考试地址不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamStartTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "开始时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamEndTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "结束时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamCredit.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "考试积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamInfo.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddExamActivity.this, "考试简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                upData();
                break;
            case R.id.activity_manager_add_exam_time_data :
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AdminAddExamActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                AdminAddExamActivity.this.year = year;
                                AdminAddExamActivity.this.month = month;
                                AdminAddExamActivity.this.day = dayOfMonth;
                                mExamStartDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }
                        //设置初始日期
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.activity_manager_add_exam_time_start :
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(AdminAddExamActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAddExamActivity.this.hourStart = hourOfDay;
                                AdminAddExamActivity.this.minuteStart = minute;
                                mExamStartTime.setText(hourOfDay + ":" + minute);
                            }
                        }
                        //设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE)
                        //true表示采用24小时制
                        , true).show();
                break;
            case R.id.activity_manager_add_exam_time_end :
                new TimePickerDialog(AdminAddExamActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAddExamActivity.this.hourEnd = hourOfDay;
                                AdminAddExamActivity.this.minuteEnd = minute;
                                mExamEndTime.setText(hourOfDay + ":" + minute);
                            }
                        }
                        //设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE)
                        //true表示采用24小时制
                        , true).show();
                break;

        }
    }

    public void back(View view) {
        this.finish();
    }
}
