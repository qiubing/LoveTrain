package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.util.Utils;

/**
 * Created by hexiao on 2015/9/7.
 */
public class AdminAlterCourseActivity extends Activity implements View.OnClickListener {
    private EditText alterCourseCourseNameEditText;
    private EditText alterCourseCourseDescEditText;

    private Button alterCourseButton;
    private ImageView alterCourseBackImage;

    private CourseItem mCourseItem;

    Bundle bundle;

    //复选框
    private CheckBox alterCourseWhetherExamCheckBox;
    private CheckBox alterCourseWhetherHighLevelCourseCheckBox;

    //保存是否是高级课程
    private boolean whetherExam;
    private boolean whetherHighLevelCourse;
    /**修改课程URL*/
    private String alterCourseUrl = Constant.BASE_URL + "course/edit_ course.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter_course);
        bundle=new Bundle();
        alterCourseCourseNameEditText = (EditText) findViewById(R.id.alter_course_courseName_editText);
        alterCourseCourseDescEditText = (EditText) findViewById(R.id.alter_course_courseDesc_editText);
        alterCourseButton = (Button) findViewById(R.id.alter_course_button);
        alterCourseBackImage = (ImageView) findViewById(R.id.admin_alter_course_backImage);

        alterCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.alter_course_whetherExam_checkBox);
        alterCourseWhetherHighLevelCourseCheckBox = (CheckBox) findViewById(R.id.alter_course_whetherHighLevelCourse_checkBox);

        /**获取启动该Activity的Intent*/
        Intent intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");
        if(mCourseItem!=null) {
            alterCourseCourseNameEditText.setText(mCourseItem.getName());
            alterCourseCourseDescEditText.setText(mCourseItem.getDescription());
            alterCourseWhetherExamCheckBox.setChecked(mCourseItem.hasExam());
            alterCourseWhetherHighLevelCourseCheckBox.setChecked(mCourseItem.getType()=="3");

        }

        /**监听事件*/
        alterCourseButton.setOnClickListener(this);
        alterCourseBackImage.setOnClickListener(this);


    }

    /**构造请求参数*/
    private void upLoadData() {
        /**请求课程数据*/
        HashMap<String,String> getClassParam = new HashMap<>();
        getClassParam.put("course_index", "1");
        getClassParam.put("course_record_modify_time", "1245545456456");
        getClassParam.put("lesson_index", "1");
        getClassParam.put("lesson_record_modify_time", "1245545456456");
        RequestParams requestParams = Utils.toParams(getClassParam);
        Log.e("requestParams", requestParams.toString());
//        AsyncHttpHelper.post(alterCourseUrl, requestParams, jsonHttpResponseHandler);
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_alter_course_backImage:
                Toast.makeText(AdminAlterCourseActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAlterCourseActivity.this, AdminCourseDetailActivity.class);
                bundle.putSerializable("CourseItem",mCourseItem);
                intentBackImage.putExtras(bundle);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.alter_course_button:
                Dialog alterCourseDialog = new AlertDialog.Builder(AdminAlterCourseActivity.this)
                        .setTitle("修改课程")
                        .setMessage("确定修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String courseName = alterCourseCourseNameEditText.getText().toString();
                                String courseDesc = alterCourseCourseDescEditText.getText().toString();
                                whetherExam = alterCourseWhetherExamCheckBox.isChecked();
                                whetherHighLevelCourse = alterCourseWhetherHighLevelCourseCheckBox.isChecked();

                                mCourseItem.setName(courseName);
                                mCourseItem.setDescription(courseDesc);
                                mCourseItem.setHasExam(whetherExam);
                                if(whetherHighLevelCourse) {
                                    mCourseItem.setType("3");
                                }

                                /**加入到课程数据库中，返回是否加入成功的状态值*/
                                /**如何更新课程？？？*/

                                Intent intentAlterForSure = new Intent(AdminAlterCourseActivity.this, AdminCourseDetailActivity.class);
                                bundle.putSerializable("CourseItem",mCourseItem);
                                intentAlterForSure.putExtras(bundle);
                                startActivity(intentAlterForSure);
                                //这里执行修改课程操作
                                Toast.makeText(AdminAlterCourseActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentAlterCancel = new Intent(AdminAlterCourseActivity.this, AdminCourseDetailActivity.class);
                                startActivity(intentAlterCancel);
                            }
                        })
                        .create();
                alterCourseDialog.show();
                break;
        }

    }
}
