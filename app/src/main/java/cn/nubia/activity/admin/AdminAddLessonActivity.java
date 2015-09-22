package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import cn.nubia.util.MyJsonHttpResponseHandler;
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
    private LessonItem mLessonItem=new LessonItem();

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
    private Button addLessonSureBtn;
    private TextView courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lesson);

        courseName=(TextView)findViewById(R.id.add_lesson_CourseName_TextView);
        lessonName = (EditText) findViewById(R.id.add_lesson_lessonName_editText);
        teacherName = (EditText) findViewById(R.id.add_lesson_teacherName_editText);
        lessonDesc = (EditText) findViewById(R.id.add_lesson_lessonDesc_editText);
        lessonLocation = (EditText) findViewById(R.id.add_lesson_lessonLocation_editText);
        lessonStartTime = (EditText) findViewById(R.id.add_lesson_lessonStartTime_editText);
        lessonEndTime = (EditText) findViewById(R.id.add_lesson_lessonEndTime_editText);
        teacherGetPoints = (EditText) findViewById(R.id.add_lesson_teacherGetPoints_editText);
        studentGetPoints = (EditText) findViewById(R.id.add_lesson_studentGetPoints_editText);
        addLessonSureBtn = (Button) findViewById(R.id.add_lesson_addLessonForSureButton);
        lessonDate = (EditText)findViewById(R.id.add_lesson_start_data_editText);

        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

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
                /*Bundle bundle=new Bundle();
                *//**此处这个mLessonItem的课时ID一直为0，并没有赋值
                 * *//*
                bundle.putSerializable("LessonItem", mLessonItem);
                Intent intentAddForSure = new Intent(AdminAddLessonActivity.this, AdminLessonDetailActivity.class);
                intentAddForSure.putExtras(bundle);
                startActivity(intentAddForSure);*/
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
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.put("course_index", mCourseItem.getIndex());
        requestParams.add("lesson_name", lessonName.getText().toString());
        requestParams.add("teacher", teacherName.getText().toString());
        requestParams.add("course_description", lessonDesc.getText().toString());
        requestParams.add("locale",lessonLocation.getText().toString());
        requestParams.put("start_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart));
        requestParams.put("end_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd));
        requestParams.add("teacher_credits", teacherGetPoints.getText().toString());
        requestParams.add("check_credits", studentGetPoints.getText().toString());

        AsyncHttpHelper.post(addLessonURL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                if( code == 0 && isOk) {
                    Toast.makeText(AdminAddLessonActivity.this, "success", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AdminAddLessonActivity.this, "in success exception ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminAddLessonActivity.this, "on failure ", Toast.LENGTH_SHORT).show();
        }
    };

    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }
}
