package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.nubia.activity.MainAdminActivity;
import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/7.
 */
public class AdminAlterCourseActivity extends Activity implements View.OnClickListener{
    private EditText alterCourseCourseNameEditText;
    private EditText alterCourseCourseDescEditText;

    private Button alterCourseButton;
    private ImageView alterCourseBackImage;

    //复选框
    private CheckBox alterCourseWhetherExamCheckBox;
    private CheckBox alterCourseWhetherHighLevelCourseCheckBox;

    //保存是否是高级课程
    private boolean whetherExam;
    private boolean whetherHighLevelCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter_course);
        alterCourseCourseNameEditText=(EditText)findViewById(R.id.alter_course_courseName_editText);
        alterCourseCourseDescEditText=(EditText)findViewById(R.id.alter_course_courseDesc_editText);
        alterCourseButton=(Button)findViewById(R.id.alter_course_button);
        alterCourseBackImage=(ImageView)findViewById(R.id.admin_alter_course_backImage);

        alterCourseWhetherExamCheckBox=(CheckBox)findViewById(R.id.alter_course_whetherExam_checkBox);
        alterCourseWhetherHighLevelCourseCheckBox=(CheckBox)findViewById(R.id.alter_course_whetherHighLevelCourse_checkBox);

        alterCourseButton.setOnClickListener(this);
        alterCourseBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_alter_course_backImage:
                Toast.makeText(AdminAlterCourseActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAlterCourseActivity.this,MainAdminActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.alter_course_button:
                String courseName=alterCourseCourseNameEditText.getText().toString();
                String courseDesc=alterCourseCourseDescEditText.getText().toString();
                whetherExam=alterCourseWhetherExamCheckBox.isChecked();
                whetherHighLevelCourse=alterCourseWhetherHighLevelCourseCheckBox.isChecked();
                //加入到课程数据库中，返回是否加入成功的状态值
                //....
                Intent intentAlterForSure = new Intent(AdminAlterCourseActivity.this,AdminCourseDetailActivity.class);
                startActivity(intentAlterForSure);
                Toast.makeText(AdminAlterCourseActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
                finish();
                break;
        }

    }
}
