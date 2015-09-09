package cn.nubia.activity.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.SimpleDateFormat;

import cn.nubia.activity.R;
import cn.nubia.component.CourseLevelSpinnerAdapter;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.entity.ShareCourseLevel;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class MyShareCourseDetailFillActivity extends Activity {
    private enum OperateType {INSERT,UPDATE}
    private enum TimeType {STARTTIME,ENDTIME}

    private EditText mCourseName;
    private EditText mLessonLocation;
    private TextView mCourseDate;
    private TextView mCourseStarttime;
    private TextView mCourseEndtime;
    private EditText mCourseDescription;
    private Button mConfirmButton;
    private Spinner mShareTypeSpinner;
    private ScrollView mContentScrollView;

    private OperateType mOperateType;
    private CourseItem mCourseItem;
    private LessonItem mLessonItem;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_fill);

        holdView();
        initViewLogic();
        //initViewData();
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
                mCourseDate.setText(starttime);
            }
        };

        pickDateDialog.setView(datePickerLayout);
        pickDateDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定", confirmButtonListener);
        return pickDateDialog;
    }

    /**
     * 创建选择时间的对话框
     * 每次都是创建一个新的对话框实例，包括TimePicker实例*/
    private AlertDialog.Builder makeTimePickDialog(final TimeType type){
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
//                        try {
//                            mCourseStartTime =  new SimpleDateFormat("yyyy-mm-dd hh:mm")
//                                    .parse(mCourseDate.getText().toString() + " " + time);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                        mCourseStarttime.setText(time);
                        break;
                    case ENDTIME:
                        mCourseEndtime.setText(time);
                        break;
                }
            }
        };
        pickTimeDialog.setView(timePickerLayout);
        pickTimeDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定",confirmButtonListener);
        return pickTimeDialog;
    }

    private void holdView(){
        mCourseName = (EditText) findViewById(R.id
                .my_sharecourse_detail_fill_coursename_filltextView);
        /**设置显示分享课程级别的下拉列表框*/
        mShareTypeSpinner = (Spinner) findViewById(R.id
                .my_sharecourse_detail_fill_courselevel_fillspinner);
        SpinnerAdapter spinnerAdapter = new CourseLevelSpinnerAdapter(this);
        mShareTypeSpinner.setAdapter(spinnerAdapter);
        mLessonLocation = (EditText) findViewById(R.id
                .my_sharecourse_detail_fill_courselocale_filltextview);
        mCourseDate =(TextView) findViewById(R.id
                .my_sharecourse_detail_fill_coursedate_filltextview);
        mCourseStarttime = (TextView) findViewById(R.id
                .my_sharecourse_detail_fill_coursestarttime_filltextview);
        mCourseEndtime = (TextView) findViewById(R.id
                .my_sharecourse_detail_fill_courseendtime_filltextview);
        mCourseDescription =((EditText) findViewById(R.id
                .my_sharecourse_detail_fill_coursedescription_filltextview));
        mConfirmButton = (Button) findViewById(R.id
                .my_sharecourse_detail_fill_confirmbutton);
        mContentScrollView =(ScrollView) findViewById(R.id
                .my_sharecourse_detail_fill_contentscrollview);
    }

    private void initViewLogic(){
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
        mCourseDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                makeDatePickDialog().show();
            }
        });
        /**监听时间输入动作，弹出选择时间框*/
        mCourseStarttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(TimeType.STARTTIME).show();
            }
        });
        mCourseEndtime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(TimeType.ENDTIME).show();
            }
        });

        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseItem shareCourse = new CourseItem();
                shareCourse.setName(((TextView) findViewById(R.id
                        .my_sharecourse_detail_fill_coursename_filltextView))
                        .getText().toString());
                shareCourse.setDescription(mCourseDescription.getText().toString());
                shareCourse.setType("2");
                shareCourse.setLessones((short)2);
                shareCourse.setCourseStatus((short) 1);
                shareCourse.setHasExam((short) 0);
                shareCourse.setShareType(((ShareCourseLevel)mShareTypeSpinner.getSelectedItem())
                         .getmCourseLevelSign());

                LessonItem shareCourseLesson = new LessonItem();
                shareCourseLesson.setLocation(((EditText) findViewById(R.id
                        .my_sharecourse_detail_fill_courselocale_filltextview))
                        .getTextLocale().toString());
                shareCourseLesson.setStartTime(new SimpleDateFormat("yyyy-mm-dd hh:mm")
                        .format(mCourseDate.getText() + " " + mCourseStarttime.getText()));
                shareCourseLesson.setEndTime(new SimpleDateFormat("yyyy-mm-dd hh:mm")
                        .format(mCourseDate.getText()+" "+mCourseEndtime.getText()));
            }
        });
    }

    private void initViewData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCourseItem = (CourseItem) bundle.getSerializable("shareCourse");

        if(mCourseItem!=null){
            mOperateType = OperateType.UPDATE;
            mLessonItem = mCourseItem.getLessonList().get(0);

            mCourseName.setText(mCourseItem.getName());
            mShareTypeSpinner.setSelection(mCourseItem.getShareType());
            mCourseDescription.setText(mCourseItem.getDescription());
            mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mLessonItem.getStartTime()));
            mCourseStarttime.setText(
                    new SimpleDateFormat("HH:mm").format(mLessonItem.getStartTime()));
            mCourseEndtime.setText(
                    new SimpleDateFormat("HH:mm").format(mLessonItem.getEndTime()));
            mLessonLocation.setText(mLessonItem.getLocation());
        }else{
            mOperateType = OperateType.INSERT;
        }
    }
}
