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
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by hexiao on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class AdminAddLessonActivity extends Activity implements View.OnClickListener {

    private EditText lessonDate;
    private EditText lessonName;
    private EditText teacherName;
    private EditText lessonDesc;
    private EditText lessonLocation;
    private EditText lessonStartTime;
    private EditText lessonEndTime;
    private EditText teacherGetPoints;
    private EditText studentGetPoints;

    private CourseItem mCourseItem=new CourseItem();
    private GestureDetector gestureDetector;

    private static final String addLessonURL = Constant.BASE_URL + "course/add_lesson.do";
    private int year;
    private int month;
    private int day;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private LessonItem mLessonItem;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lesson);

        TextView courseName=(TextView)findViewById(R.id.add_lesson_CourseName_TextView);
        lessonName = (EditText) findViewById(R.id.add_lesson_lessonName_editText);
        teacherName = (EditText) findViewById(R.id.add_lesson_teacherName_editText);
        lessonDesc = (EditText) findViewById(R.id.add_lesson_lessonDesc_editText);
        lessonLocation = (EditText) findViewById(R.id.add_lesson_lessonLocation_editText);
        lessonStartTime = (EditText) findViewById(R.id.add_lesson_lessonStartTime_editText);
        lessonEndTime = (EditText) findViewById(R.id.add_lesson_lessonEndTime_editText);
        teacherGetPoints = (EditText) findViewById(R.id.add_lesson_teacherGetPoints_editText);
        studentGetPoints = (EditText) findViewById(R.id.add_lesson_studentGetPoints_editText);
        Button addLessonSureBtn = (Button) findViewById(R.id.add_lesson_addLessonForSureButton);
        lessonDate = (EditText)findViewById(R.id.add_lesson_start_data_editText);

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

        mLessonItem = new LessonItem();
        addLessonSureBtn.setOnClickListener(this);
        lessonDate.setOnClickListener(this);
        lessonStartTime.setOnClickListener(this);
        lessonEndTime.setOnClickListener(this);

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


        Intent intent = getIntent();
        mCourseItem = (CourseItem)intent.getSerializableExtra("CourseItem");
        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("添加课时");
        if(mCourseItem!=null)
            courseName.setText(mCourseItem.getName());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.add_lesson_addLessonForSureButton:
                if(lessonName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teacherName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "教师姓名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonDesc.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonLocation.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "上课地点不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonDate.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "上课日期不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonStartTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "上课始时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonEndTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "下课时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teacherGetPoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "讲师积分", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(studentGetPoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "学员积分", Toast.LENGTH_SHORT).show();
                    return;
                }
                upData();
                break;
            case R.id.add_lesson_start_data_editText:

                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AdminAddLessonActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                AdminAddLessonActivity.this.year = year;
                                AdminAddLessonActivity.this.month = month;
                                AdminAddLessonActivity.this.day = dayOfMonth;
                                lessonDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }
                        //设置初始日期
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.add_lesson_lessonStartTime_editText:
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(AdminAddLessonActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAddLessonActivity.this.hourStart = hourOfDay;
                                AdminAddLessonActivity.this.minuteStart = minute;
                                lessonStartTime.setText(hourOfDay + ":" + minute);
                            }
                        }
                        //设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE)
                        //true表示采用24小时制
                        , true).show();
                break;
            case R.id.add_lesson_lessonEndTime_editText:
                new TimePickerDialog(AdminAddLessonActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAddLessonActivity.this.hourEnd = hourOfDay;
                                AdminAddLessonActivity.this.minuteEnd = minute;
                                lessonEndTime.setText(hourOfDay + ":" + minute);
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

        requestParams.put("course_index", mCourseItem.getIndex());
        mLessonItem.setCourseIndex(mCourseItem.getIndex());
        requestParams.add("lesson_name", lessonName.getText().toString());
        requestParams.add("teacher", teacherName.getText().toString());
        mLessonItem.setTeacherName(teacherName.getText().toString());
        requestParams.add("course_description", lessonDesc.getText().toString());
        mLessonItem.setDescription(lessonDesc.getText().toString());
        requestParams.add("locale", lessonLocation.getText().toString());
        mLessonItem.setLocation(lessonLocation.getText().toString());
        long startTime = TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart);
        requestParams.put("start_time",startTime);
        mLessonItem.setStartTime(startTime);
        long endTime = TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd);
        requestParams.put("end_time",endTime);
        mLessonItem.setEndTime(endTime);
        requestParams.add("teacher_credits", teacherGetPoints.getText().toString());
        mLessonItem.setTeacherCredits(Integer.valueOf(teacherGetPoints.getText().toString()));
        requestParams.add("check_credits", studentGetPoints.getText().toString());
        mLessonItem.setCheckCredits(Integer.valueOf(studentGetPoints.getText().toString()));

        AsyncHttpHelper.post(addLessonURL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e("929",response.toString());
                int code = response.getInt("code");
//                int isOk = response.getInt("data");
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                mLessonItem.setIndex(response.getInt("data"));
                if( code == 0) {
                    Toast.makeText(AdminAddLessonActivity.this, "添加课时成功", Toast.LENGTH_SHORT).show();
                    lessonDate.setText("");
                    lessonName.setText("");
                    teacherName.setText("");
                    lessonDesc.setText("");
                    lessonLocation.setText("");
                    lessonStartTime.setText("");
                    lessonEndTime.setText("");
                    teacherGetPoints.setText("");
                    studentGetPoints.setText("");
                }
            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminAddLessonActivity.this, "添加课时失败", Toast.LENGTH_SHORT).show();
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminAddLessonActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
