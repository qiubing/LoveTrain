package cn.nubia.activity.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseLevelSpinnerAdapter;
import cn.nubia.entity.ShareCourseItem;
import cn.nubia.entity.ShareCourseLevel;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class ClientMyShareCourseDetailFillActivity extends Activity {
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

    private CommunicateService.OperateType mOperateType;
    private ShareCourseItem mShareCourseItem;

    private CommunicateService.CommunicateBinder mBinder;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (CommunicateService.CommunicateBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    public class Inter implements ActivityInter {
        public void alter(List<?> list,CommunicateService.OperateType type){
            ClientMyShareCourseDetailFillActivity.this.showOperateResult((List<String>)list,type);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_fill);
        connectService();

        holdView();
        setViewLogic();
        initViewData();
    }

    /**
     * 创建选择日期的对话框
     * 每次都是创建一个新的对话框实例，包括DatePicker实例*/
    private AlertDialog.Builder makeDatePickDialog(){
        AlertDialog.Builder pickDateDialog = new AlertDialog.Builder(ClientMyShareCourseDetailFillActivity.this);
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
                        .append(datePicker.getMonth() + 1)
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
        AlertDialog.Builder pickTimeDialog = new AlertDialog.Builder(ClientMyShareCourseDetailFillActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View timePickerLayout = inflater.inflate(R.layout.component_time_picker, null);
        final TimePicker timePicker = (TimePicker) timePickerLayout.findViewById(R.id.jiangyu_timepicker);
        timePicker.setIs24HourView(true);
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
                        mCourseStarttime.setText(time);
                        break;
                    case ENDTIME:
                        mCourseEndtime.setText(time);
                        break;
                }
            }
        };
        pickTimeDialog.setView(timePickerLayout);
        pickTimeDialog.setNegativeButton("取消",cancelButtonListener).setPositiveButton("确定", confirmButtonListener);
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

    private void setViewLogic(){
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
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    ShareCourseItem shareCourse = new ShareCourseItem();
                    shareCourse.setCourseName(mCourseName.getText().toString());
                    shareCourse.setCourseDescription(mCourseDescription.getText().toString());
                    shareCourse.setCourseLevel(((ShareCourseLevel) mShareTypeSpinner.getSelectedItem())
                            .getmCourseLevelSign());

                    shareCourse.setLocale(mLessonLocation.getText().toString());
                    try {
                        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
                                mCourseDate.getText().toString()
                                        + " "
                                        + mCourseStarttime.getText());
                        Date endTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
                                mCourseDate.getText().toString()
                                        + " "
                                        + mCourseStarttime.getText());
                        shareCourse.setStartTime(startTime.getTime());
                        shareCourse.setEndTime(endTime.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    shareCourse.setOperateType(mOperateType);
                    if(mOperateType==CommunicateService.OperateType.INSERT)
                        mBinder.communicate(shareCourse, new Inter(),"newsharecourse.do");
                    else{
                        shareCourse.setCourseIndex(mShareCourseItem.getCourseIndex());
                        mBinder.communicate(shareCourse, new Inter(),"updatesharecourse.do");
                    }
                }
            }
        };
    }

    private void initViewData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("type");

        if(type.equals("update")){
            mOperateType = CommunicateService.OperateType.UPDATE;
            mShareCourseItem = (ShareCourseItem) bundle.getSerializable("shareCourse");

            mCourseName.setText(mShareCourseItem.getCourseName());
            mShareTypeSpinner.setSelection(mShareCourseItem.getCourseLevel());
            mCourseDescription.setText(mShareCourseItem.getCourseDescription());
            Date startTime = new Date();
            startTime.setTime(mShareCourseItem.getStartTime());
            Date endTime = new Date();
            endTime.setTime(mShareCourseItem.getEndTime());
            mCourseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
            mCourseStarttime.setText(
                    new SimpleDateFormat("HH:mm").format(startTime));
            mCourseEndtime.setText(
                    new SimpleDateFormat("HH:mm").format(endTime));
            mLessonLocation.setText(mShareCourseItem.getLocale());
        }else{
            mOperateType = CommunicateService.OperateType.INSERT;
        }
    }

    private boolean checkData(){
        if(mCourseName.getText().toString().equals("")) {
            Toast.makeText(this,"课程名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mCourseDescription.getText().toString().equals("")){
            Toast.makeText(this,"课程描述不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mShareTypeSpinner.getSelectedItem()==null){
            Toast.makeText(this,"分享级别不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mLessonLocation.getText().toString().equals("")){
            Toast.makeText(this,"上课地点不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
                    mCourseDate.getText().toString()
                    + " "
                    + mCourseStarttime.getText());

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this,"日期或上课时间设置不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
                    mCourseDate.getText().toString()
                    + " "
                    + mCourseEndtime.getText());
        }catch (ParseException e){
            e.printStackTrace();
            Toast.makeText(this,"日期或下课时间设置不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void connectService(){
        Intent intent = new Intent(
                ClientMyShareCourseDetailFillActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void showOperateResult(List<String> list,CommunicateService.OperateType type) {
        Boolean result = Boolean.getBoolean(list.get(0));
        if(type==CommunicateService.OperateType.INSERT){
            if(result)
                DialogUtil.showDialog(
                        ClientMyShareCourseDetailFillActivity.this, "申请提交成功!", false);
            else
                DialogUtil.showDialog(
                        ClientMyShareCourseDetailFillActivity.this,"申请提交失败!",false);
        }else{
            if(result)
                DialogUtil.showDialog(
                        ClientMyShareCourseDetailFillActivity.this, "课程修改成功!", false);
            else
                DialogUtil.showDialog(
                        ClientMyShareCourseDetailFillActivity.this,"课程修改失败!",false);
        }
    }
}
