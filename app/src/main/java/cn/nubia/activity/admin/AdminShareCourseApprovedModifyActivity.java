package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseLevelSpinnerAdapter;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.entity.ShareCourseLevel;
import cn.nubia.entity.TechnologyShareCourseItem;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/10 10:41
 */
public class AdminShareCourseApprovedModifyActivity extends Activity {
    private enum OperateType {INSERT, UPDATE}

    private enum TimeType {STARTTIME, ENDTIME}

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
    private TechnologyShareCourseItem mModifiedItem;
    //private CourseItem mCourseItem;
    //private LessonItem mLessonItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approved_share_course_modified);
        initViews();
        initEvents();
        initViewLogic();
    }

    private void initViews() {
        mCourseName = (EditText) findViewById(R.id
                .approved_share_course_modified_name);
        /**设置显示分享课程级别的下拉列表框*/
        mShareTypeSpinner = (Spinner) findViewById(R.id
                .approved_share_course_modified_level);
        SpinnerAdapter spinnerAdapter = new CourseLevelSpinnerAdapter(this);
        mShareTypeSpinner.setAdapter(spinnerAdapter);
        mLessonLocation = (EditText) findViewById(R.id
                .approved_share_course_modified_locale);
        mCourseDate = (TextView) findViewById(R.id
                .approved_share_course_modified_date);
        mCourseStarttime = (TextView) findViewById(R.id
                .approved_share_course_modified_start_time);
        mCourseEndtime = (TextView) findViewById(R.id
                .approved_share_course_modified_end_time);
        mCourseDescription = ((EditText) findViewById(R.id
                .approved_share_course_modified_edit_text));
        mConfirmButton = (Button) findViewById(R.id
                .btn_modify_share_course);
        mContentScrollView = (ScrollView) findViewById(R.id
                .approved_share_course_modified_scrollview);
    }

    private void initEvents() {

        //获取当前的Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        /**
         * edit by qiubing 2015.9.19 20:46
         */
        mModifiedItem = (TechnologyShareCourseItem) bundle.getSerializable("ModifiedCourseInfo");
        if (mModifiedItem != null){
            mOperateType = OperateType.UPDATE;
            mCourseName.setText(mModifiedItem.getmCourseName());
            mShareTypeSpinner.setSelection((short) mModifiedItem.getmCourseLevel());
            mCourseDescription.setText(mModifiedItem.getmCourseDescription());
            mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(mModifiedItem.getmStartTime()));
            mCourseStarttime.setText(
                    new SimpleDateFormat("HH:mm").format(mModifiedItem.getmStartTime()));
            mCourseEndtime.setText(
                    new SimpleDateFormat("HH:mm").format(mModifiedItem.getmEndTime()));
            mLessonLocation.setText(mModifiedItem.getmLocation());
        }else {
            mOperateType = OperateType.INSERT;
        }

        /*mCourseItem = (CourseItem) bundle.getSerializable("ModifiedCourseInfo");
        if (mCourseItem != null) {
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
        } else {
            mOperateType = OperateType.INSERT;
        }*/
    }

    /**
     * 创建选择日期的对话框
     * 每次都是创建一个新的对话框实例，包括DatePicker实例
     */
    private AlertDialog.Builder makeDatePickDialog() {
        AlertDialog.Builder pickDateDialog = new AlertDialog.Builder(AdminShareCourseApprovedModifyActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View datePickerLayout = inflater.inflate(R.layout.component_date_picker, null);
        final DatePicker datePicker = (DatePicker) datePickerLayout.findViewById(R.id.jiangyu_datepicker);

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        };

        DialogInterface.OnClickListener confirmButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String starttime = new StringBuilder()
                        .append(datePicker.getYear())
                        .append("-")
                        .append(datePicker.getMonth() + 1)
                        .append("-")
                        .append(datePicker.getDayOfMonth())
                        .toString();
                /**将被选择的时间显示到文本框中去*/
                mCourseDate.setText(starttime);
            }
        };

        pickDateDialog.setView(datePickerLayout);
        pickDateDialog.setNegativeButton("取消", cancelButtonListener).setPositiveButton("确定", confirmButtonListener);
        return pickDateDialog;
    }

    /**
     * 创建选择时间的对话框
     * 每次都是创建一个新的对话框实例，包括TimePicker实例
     */
    private AlertDialog.Builder makeTimePickDialog(final TimeType type) {
        AlertDialog.Builder pickTimeDialog = new AlertDialog.Builder(AdminShareCourseApprovedModifyActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View timePickerLayout = inflater.inflate(R.layout.component_time_picker, null);
        final TimePicker timePicker = (TimePicker) timePickerLayout.findViewById(R.id.jiangyu_timepicker);
        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        };

        DialogInterface.OnClickListener confirmButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String time = new StringBuilder()
                        .append(timePicker.getCurrentHour())
                        .append(":")
                        .append(timePicker.getCurrentMinute())
                        .toString();
                switch (type) {
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
        pickTimeDialog.setNegativeButton("取消", cancelButtonListener).setPositiveButton("确定", confirmButtonListener);
        return pickTimeDialog;
    }

    private void initViewLogic() {
        /**
         * 设置当输入框被触碰时，内容滚动界面不响应触碰事件，及手指在输入框上移动时，
         * 内容滚动界面不会滚动，只有输入框才会滚动
         */
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

        /**
         * 监听日期输入动作，弹出选择日期框
         */
        mCourseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDatePickDialog().show();
            }
        });
        /**
         * 监听时间输入动作，弹出选择时间框
         */
        mCourseStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(TimeType.STARTTIME).show();
            }
        });
        mCourseEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTimePickDialog(TimeType.ENDTIME).show();
            }
        });

        /**
         * 监听确认按钮，进行提交动作
         */
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseItem approvedCourse = new CourseItem();
                approvedCourse.setName(((TextView) findViewById(R.id
                        .approved_share_course_modified_name))
                        .getText().toString());
                approvedCourse.setDescription(mCourseDescription.getText().toString());
                approvedCourse.setType("2");
                approvedCourse.setLessones((short) 2);
                approvedCourse.setCourseStatus((short) 1);
                approvedCourse.setHasExam(false);
                approvedCourse.setShareType(((ShareCourseLevel) mShareTypeSpinner.getSelectedItem())
                        .getmCourseLevelSign());

                LessonItem shareCourseLesson = new LessonItem();
                shareCourseLesson.setLocation(((EditText) findViewById(R.id
                        .approved_share_course_modified_locale))
                        .getText().toString());
//                shareCourseLesson.setStartTime(new SimpleDateFormat("yyyy-mm-dd hh:mm")
//                        .format(mCourseDate.getText() + " " + mCourseStarttime.getText()));
//                shareCourseLesson.setEndTime(new SimpleDateFormat("yyyy-mm-dd hh:mm")
//                        .format(mCourseDate.getText() + " " + mCourseEndtime.getText()));

                //TODO:提交修改后的课程信息到后台服务器
                Toast.makeText(AdminShareCourseApprovedModifyActivity.this,"确认修改",Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
