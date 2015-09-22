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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminAlterLessonActivity extends Activity implements View.OnClickListener {

    private LessonItem lessonItem;
    private Bundle bundle;

    private EditText lessonName;
    private EditText teacherName;
    private EditText lessonDesc;
    private EditText lessonLocation;
    private EditText lessonDate;
    private EditText lessonStartTime;
    private EditText lessonEndTime;
    private EditText lessonTeancherPoint;
    private EditText lessonStudentPoint;
    private Button lessonOk;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private GestureDetector gestureDetector;
    private static final String URL = Constant.BASE_URL + "course/edit_lesson.do";

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
        setContentView(R.layout.activity_admin_alter_lesson);
        bundle = new Bundle();

        lessonOk = (Button) findViewById(R.id.admin_alter_lesson_alterForeSureButton);
        lessonName = (EditText) findViewById(R.id.alter_lesson_lessonName_editText);
        teacherName = (EditText) findViewById(R.id.alter_lesson_teacherName_editText);
        lessonDesc = (EditText) findViewById(R.id.alter_lesson_lessonDesc_editText);
        lessonLocation = (EditText) findViewById(R.id.alter_lesson_lessonLocation_editText);
        lessonDate = (EditText) findViewById(R.id.alter_lesson_lessonStartDate_editText);
        lessonStartTime = (EditText) findViewById(R.id.alter_lesson_lessonStartTime_editText);
        lessonEndTime = (EditText) findViewById(R.id.alter_lesson_lessonEndTime_editText);
        lessonTeancherPoint = (EditText) findViewById(R.id.alter_lesson_teacherGetPoints_editText);
        lessonStudentPoint = (EditText) findViewById(R.id.alter_lesson_studentGetPoints_editText);

        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("修改课时");
        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        lessonOk.setOnClickListener(this);
        lessonDate.setOnClickListener(this);
        lessonStartTime.setOnClickListener(this);
        lessonEndTime.setOnClickListener(this);

        /**获取并放入LessonItem*/
        Intent intent = getIntent();
        lessonItem = (LessonItem) intent.getSerializableExtra("LessonItem");

        if (lessonItem != null) {
            lessonName.setText(lessonItem.getName());
            teacherName.setText(lessonItem.getTeacherName());
            lessonDesc.setText(lessonItem.getDescription());
            lessonLocation.setText(lessonItem.getLocation());
            lessonDate.setText(TimeFormatConversion.toTimeDate(lessonItem.getStartTime()));
            lessonStartTime.setText(TimeFormatConversion.toTime(lessonItem.getStartTime()));
            lessonEndTime.setText(TimeFormatConversion.toTime(lessonItem.getEndTime()));
            lessonTeancherPoint.setText(lessonItem.getTeacherCredits() + "");
            lessonStudentPoint.setText(lessonItem.getCheckCredits() + "");
        }

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

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void upData() {
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.put("lesson_index", lessonItem.getIndex());
        requestParams.add("lesson_name", lessonName.getText().toString());
        requestParams.add("teacher", teacherName.getText().toString());
        requestParams.add("course_description", lessonDesc.getText().toString());
        requestParams.add("locale", lessonLocation.getText().toString());

        requestParams.put("start_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart));
        requestParams.put("end_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd));
        requestParams.add("check_credits", lessonStudentPoint.getText().toString());
        requestParams.add("teacher_credits", lessonTeancherPoint.getText().toString());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler() {
        @Override
        @SuppressWarnings("deprecation")
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "alterLesson" + response.toString());
            try {
                int code = response.getInt("code");
                //boolean result = response.getBoolean("result");
                boolean isOk = response.getBoolean("data");
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + "," + result + "," + isOk);
                if (code == 0 && isOk) {
                    Toast.makeText(AdminAlterLessonActivity.this, "success", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            //mExamAdapter.notifyDataSetChanged();
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.admin_alter_lesson_alterForeSureButton:
                if (lessonName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "课时名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (teacherName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "讲师姓名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonDesc.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "课时简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonLocation.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "上课地点不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonDate.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "上课日期不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonStartTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "上课时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonEndTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "下课日期不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonTeancherPoint.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "讲师积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lessonStudentPoint.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterLessonActivity.this, "学员积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                upData();
                break;
            case R.id.activity_manager_add_exam_time_data:
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AdminAlterLessonActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                AdminAlterLessonActivity.this.year = year;
                                AdminAlterLessonActivity.this.month = month;
                                AdminAlterLessonActivity.this.day = dayOfMonth;
                                lessonDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }
                        //设置初始日期
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.activity_manager_add_exam_time_start:
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(AdminAlterLessonActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAlterLessonActivity.this.hourStart = hourOfDay;
                                AdminAlterLessonActivity.this.minuteStart = minute;
                                lessonStartTime.setText(hourOfDay + ":" + minute);
                            }
                        }
                        //设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY)
                        , c.get(Calendar.MINUTE)
                        //true表示采用24小时制
                        , true).show();
                break;
            case R.id.activity_manager_add_exam_time_end:
                new TimePickerDialog(AdminAlterLessonActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
                                AdminAlterLessonActivity.this.hourEnd = hourOfDay;
                                AdminAlterLessonActivity.this.minuteEnd = minute;
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




    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }
}

