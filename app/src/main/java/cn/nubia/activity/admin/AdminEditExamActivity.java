package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AdminEditExamActivity extends Activity  implements  View.OnClickListener{


    private EditText mExamInfo;
    private EditText mExamTitle;
    private EditText mExamAddress;
    private EditText mExamStartDate;
    private EditText mExamStartTime;
    private EditText mExamEndTime;
    private EditText mExamCredit;

    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private static final String URL = Constant.BASE_URL + "/exam/edit.do";
    private ExamItem mExamItemExamEdit;
    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    private int year;
    private int month;
    private int day;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_exam);

        mExamItemExamEdit = (ExamItem) getIntent().getSerializableExtra("ExamInfo");
        Log.i("huhu", "exzm" + mExamItemExamEdit.getIndex() + "," + mExamItemExamEdit.getCourseIndex());


        mExamTitle = (EditText) findViewById(R.id.activity_manager_add_exam_one);
        mExamInfo = (EditText) findViewById(R.id.activity_manager_add_exam_info);
        mExamAddress = (EditText) findViewById(R.id.activity_manager_add_exam_address);
        mExamStartTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_start);
        mExamEndTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_end);
        mExamCredit = (EditText) findViewById(R.id.activity_manager_add_exam_credit);
        Button mAddButton = (Button) findViewById(R.id.activity_manager_add_exam_button);
        mExamStartDate = (EditText) findViewById(R.id.activity_manager_add_exam_time_data);

        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("修改考试");
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
        mAddButton.setText("确认修改");

        mAddButton.setOnClickListener(this);
        mExamStartDate.setOnClickListener(this);
        mExamStartTime.setOnClickListener(this);
        mExamEndTime.setOnClickListener(this);

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

        if(mExamItemExamEdit != null) {
            mExamTitle.setText(mExamItemExamEdit.getName());
            mExamInfo.setText(mExamItemExamEdit.getDescription());
            mExamAddress.setText(mExamItemExamEdit.getLocale());
            mExamStartDate.setText(TimeFormatConversion.toTimeDate(mExamItemExamEdit.getStartTime()));
            mExamStartTime.setText(TimeFormatConversion.toTime(mExamItemExamEdit.getStartTime()));
            mExamEndTime.setText(TimeFormatConversion.toTime(mExamItemExamEdit.getEndTime()));
            mExamCredit.setText(mExamItemExamEdit.getExamCredits() + "");
            int [] startTimeArray = TimeFormatConversion.toDateTimeInArray(mExamItemExamEdit.getStartTime());
            int [] endTimeArray = TimeFormatConversion.toDateTimeInArray(mExamItemExamEdit.getEndTime());
            year = startTimeArray[0];
            month = startTimeArray[1];
            day = startTimeArray[2];
            hourStart = startTimeArray[3];
            minuteStart = startTimeArray[4];
            hourEnd = endTimeArray[3];
            minuteEnd = endTimeArray[4];
        }

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
//        requestParams.add("device_id", "MXJSDLJFJFSFS");
//        requestParams.add("request_time","1445545456456");
//        requestParams.add("apk_version","1");
//        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.put("course_index", mExamItemExamEdit.getCourseIndex());
        requestParams.put("exam_index", mExamItemExamEdit.getIndex());
        requestParams.add("exam_name",  mExamTitle.getText().toString());
        requestParams.add("exam_description",  mExamInfo.getText().toString());
        requestParams.add("locale",  mExamAddress.getText().toString());

        requestParams.put("start_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart));
        requestParams.put("end_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd));
        requestParams.put("exam_credits", mExamCredit.getText().toString());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        @SuppressWarnings("deprecation")
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                int code = response.getInt("code");
//                boolean result = response.getBoolean("result");
                boolean isOk = response.getBoolean("data");
                if(code == 0 && isOk) {
                    Toast.makeText(AdminEditExamActivity.this, "修改考试成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminEditExamActivity.this, "修改考试失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminEditExamActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdminEditExamActivity.this, "考试名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mExamAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminEditExamActivity.this, "考试地址不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamStartTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminEditExamActivity.this, "开始时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamEndTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminEditExamActivity.this, "结束时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamCredit.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminEditExamActivity.this, "考试积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mExamInfo.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminEditExamActivity.this, "考试简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                upData();
                break;
            case R.id.activity_manager_add_exam_time_data :
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AdminEditExamActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                AdminEditExamActivity.this.year = year;
                                AdminEditExamActivity.this.month = month;
                                AdminEditExamActivity.this.day = dayOfMonth;
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
                new TimePickerDialog(AdminEditExamActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminEditExamActivity.this.hourStart = hourOfDay;
                                AdminEditExamActivity.this.minuteStart = minute;
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
                new TimePickerDialog(AdminEditExamActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminEditExamActivity.this.hourEnd = hourOfDay;
                                AdminEditExamActivity.this.minuteEnd = minute;
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
