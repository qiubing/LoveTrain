package cn.nubia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.component.CourseLevelSpinnerAdapter;
import cn.nubia.entity.ShareCourseItem;
import cn.nubia.entity.ShareCourseLevel;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class MyShareCourseDetailFillActivity extends Activity {
    private enum mTimeType {STARTTIME,ENDTIME}
    private TextView mCourseDateTextView;
    private TextView mCourseStarttimeTextView;
    private TextView mCourseEndtimeTextView;
    private EditText mCourseDescription;
    private Button mConfirmButton;
    private Spinner mSpinner;
    private Date mCourseStartTime;
    private Date mCourseEndTime;
    private ScrollView mContentScrollView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_fill);

        mCourseDateTextView =(TextView) findViewById(R.id
                .my_sharecourse_detail_fill_coursedate_filltextview);
        mCourseStarttimeTextView = (TextView) findViewById(R.id
                .my_sharecourse_detail_fill_coursestarttime_filltextview);
        mCourseEndtimeTextView = (TextView) findViewById(R.id
                .my_sharecourse_detail_fill_courseendtime_filltextview);
        mCourseDescription =((EditText) findViewById(R.id
                .my_sharecourse_detail_fill_coursedescription_filltextview));
        mConfirmButton = (Button) findViewById(R.id
                .my_sharecourse_detail_fill_confirmbutton);
        mContentScrollView =(ScrollView) findViewById(R.id
                .my_sharecourse_detail_fill_contentscrollview);
        /**设置显示分享课程级别的下拉列表框*/
        mSpinner = (Spinner) findViewById(R.id.my_sharecourse_detail_fill_courselevel_fillspinner);
        SpinnerAdapter spinnerAdapter = new CourseLevelSpinnerAdapter(this);
        mSpinner.setAdapter(spinnerAdapter);

        /**设置当输入框被触碰时，内容滚动界面不响应触碰事件，及手指在输入框上移动时，
         * 内容滚动界面不会滚动，只有输入框才会滚动*/
        mCourseDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    mContentScrollView.requestDisallowInterceptTouchEvent(false);
                else
                    mContentScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        /**监听日期输入动作，弹出选择日期框*/
        mCourseDateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDatePickDialog().show();
            }
        });
        /**监听时间输入动作，弹出选择时间框*/
        mCourseStarttimeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(mTimeType.STARTTIME).show();
            }
        });
        mCourseEndtimeTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(mTimeType.ENDTIME).show();
            }
        });
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCourseItem shareCourse = new ShareCourseItem();
                shareCourse.setCourseName(
                        ((TextView) findViewById(R.id.my_sharecourse_detail_fill_coursename_filltextView))
                                .getText().toString());
                shareCourse.setCourseDescription(mCourseDescription.getText().toString());
                shareCourse.setType((short) 2);
                shareCourse.setLessones(1);
                shareCourse.setCourseStatus((short) 1);
                shareCourse.setHasExam(false);
                shareCourse.setCourseLevel(
                        ((ShareCourseLevel) mSpinner.getSelectedItem())
                                .getmCourseLevelSign());
                shareCourse.setLocale(
                        ((TextView) findViewById(R.id.my_sharecourse_detail_fill_courselocale_filltextview))
                                .getTextLocale().toString());
                shareCourse.setStartTime(mCourseStartTime);
                shareCourse.setEndTime(mCourseEndTime);
            }
        });
    }
    /**
     * 创建选择日期的对话框
     * 每次都是创建一个新的对话框实例，包括DatePicker实例*/
    private AlertDialog.Builder makeDatePickDialog(){
        AlertDialog.Builder pickDateDialog = new AlertDialog.Builder(MyShareCourseDetailFillActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View datePickerLayout = inflater.inflate(R.layout.component_date_picker, null);
        final DatePicker datePicker = (DatePicker) datePickerLayout.findViewById(R.id.jiangyu_datepicker);

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        };

        DialogInterface.OnClickListener confirmButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String starttime = new StringBuilder()
                        .append(datePicker.getYear())
                        .append("-")
                        .append(datePicker.getMonth()+1)
                        .append("-")
                        .append(datePicker.getDayOfMonth())
                        .toString();
                /**将被选择的时间显示到文本框中去*/
                mCourseDateTextView.setText(starttime);
            }
        };

        pickDateDialog.setView(datePickerLayout);
        pickDateDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定",confirmButtonListener);
        return pickDateDialog;
    }

    /**
     * 创建选择时间的对话框
     * 每次都是创建一个新的对话框实例，包括TimePicker实例*/
    private AlertDialog.Builder makeTimePickDialog(final mTimeType type){
        AlertDialog.Builder pickTimeDialog = new AlertDialog.Builder(MyShareCourseDetailFillActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View timePickerLayout = inflater.inflate(R.layout.component_time_picker, null);
        final TimePicker timePicker = (TimePicker) timePickerLayout.findViewById(R.id.jiangyu_timepicker);

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        };

        DialogInterface.OnClickListener confirmButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String time = new StringBuilder()
                        .append(timePicker.getCurrentHour())
                        .append(":")
                        .append(timePicker.getCurrentMinute())
                        .toString();
                switch (type){
                    case STARTTIME:
                        try {
                            mCourseStartTime =  new SimpleDateFormat("yyyy-mm-dd hh:mm")
                                    .parse(mCourseDateTextView.getText().toString() + " " + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mCourseStarttimeTextView.setText(time);
                        break;
                    case ENDTIME:
                        try {
                            mCourseEndTime =  new SimpleDateFormat("yyyy-mm-dd hh:mm")
                                    .parse(mCourseDateTextView.getText().toString() + " " + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mCourseEndtimeTextView.setText(time);
                        break;
                }
            }
        };
        pickTimeDialog.setView(timePickerLayout);
        pickTimeDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定",confirmButtonListener);
        return pickTimeDialog;
    }

}
