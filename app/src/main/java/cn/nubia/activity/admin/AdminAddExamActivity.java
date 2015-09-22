package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONObject;

import java.util.Calendar;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
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

        mExamStartDate.setOnClickListener(this);
        mExamStartTime.setOnClickListener(this);
        mExamEndTime.setOnClickListener(this);
        mAddButton.setOnClickListener(this);

    }


    private void upData(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");


        requestParams.add("course_index", null);
        requestParams.add("exam_name", mExamTitle.getText().toString());
        requestParams.add("exam_description", mExamInfo.getText().toString());
        requestParams.add("locale", mExamAddress.getText().toString());
        requestParams.put("start_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourStart, minuteStart));
        requestParams.put("end_time", TimeFormatConversion.toTimeInMillis(year, month, day, hourEnd, minuteEnd));
        requestParams.put("exam_credits", mExamCredit.getText().toString());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "addExam" + "onSuccess");
            try {
                int code = response.getInt("code");
                Log.i("huhu", "addExam" + code);
                boolean result = response.getBoolean("result");
                boolean isOk = response.getBoolean("data");
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + ","+result + "," +isOk);
                if(code == 0 && isOk) {
                    Toast.makeText(AdminAddExamActivity.this, "success", Toast.LENGTH_SHORT).show();
                    mExamTitle.setText("");
                    mExamInfo.setText("");
                    mExamAddress.setText("");
                    mExamStartTime.setText("");
                    mExamEndTime.setText("");
                    mExamCredit.setText("");
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            //mExamAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
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
        // TODO Auto-generated method stub
        this.finish();
    }
}
