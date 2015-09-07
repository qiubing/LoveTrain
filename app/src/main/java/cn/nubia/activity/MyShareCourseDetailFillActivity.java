package cn.nubia.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.component.CourseLevelSpinnerAdapter;
import cn.nubia.entity.ShareCourse;
import cn.nubia.entity.ShareCourseLevel;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class MyShareCourseDetailFillActivity extends Activity {
    private TextView mCoursetimeTextView;
    private Button mConfirmButton;
    private Spinner mSpinner;
    private Date mCourseStartTime;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_fill);

        mCoursetimeTextView =
                (TextView) findViewById(R.id.my_sharecourse_detail_fill_coursetime_filltextview);
        mConfirmButton = (Button) findViewById(R.id.my_sharecourse_detail_fill_confirmbutton);

        mSpinner = (Spinner) findViewById(R.id.my_sharecourse_detail_fill_courselevel_fillspinner);
        SpinnerAdapter spinnerAdapter = new CourseLevelSpinnerAdapter(this);
        mSpinner.setAdapter(spinnerAdapter);

        /**监听时间输入*/
        mCoursetimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDatePickDialog().show();
            }
        });
        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCourse shareCourse = new ShareCourse();
                shareCourse.setCourseName(
                        ((TextView) findViewById(R.id.my_sharecourse_detail_fill_coursename_filltextView))
                                .getText().toString());
                shareCourse.setCourseDescription(
                        ((TextView) findViewById(R.id.my_sharecourse_detail_fill_coursedescription_filltextview))
                                .getText().toString());
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
            }
        });
    }

    /**
     * 创建选择时间的对话框
     * 每次都是创建一个新的对话框实例，包括DatePicker实例*/
    public AlertDialog.Builder makeDatePickDialog(){
        AlertDialog.Builder pickDateDialog = new AlertDialog.Builder(MyShareCourseDetailFillActivity.this);

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
                        .append(datePicker.getMonth()+1)
                        .append("-")
                        .append(datePicker.getDayOfMonth())
                        .toString();
                try {
                    mCourseStartTime =  new SimpleDateFormat("yyyy-MM-dd").parse(starttime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mCoursetimeTextView.setText(starttime);
            }
        };

        pickDateDialog.setView(datePickerLayout);
        pickDateDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定",confirmButtonListener);
        return pickDateDialog;
    }
}
