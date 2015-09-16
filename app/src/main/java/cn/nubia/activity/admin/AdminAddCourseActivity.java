package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/9.
 */
public class AdminAddCourseActivity extends Activity implements View.OnClickListener {

    private EditText addCourseCourseNameEditText;
    private EditText addCourseCourseDescEditText;

    private Button addCourseButton;
    private ImageView addCourseBackImage;

    //复选框
    private CheckBox addCourseWhetherExamCheckBox;
    private CheckBox addCourseWhetherHighLevelCourseCheckBox;

    private TextView highLevelTextView;
    private EditText highLevelCoursePoints;

    //保存是否是高级课程
    private boolean whetherExam;
    private boolean whetherHighLevelCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        addCourseCourseNameEditText = (EditText) findViewById(R.id.add_course_courseName_editText);
        addCourseCourseDescEditText = (EditText) findViewById(R.id.add_course_courseDesc_editText);

        addCourseButton = (Button) findViewById(R.id.add_course_button);
        addCourseBackImage = (ImageView) findViewById(R.id.admin_add_course_backImage);

        addCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.add_course_whetherExam_checkBox);
        addCourseWhetherHighLevelCourseCheckBox = (CheckBox) findViewById(R.id.add_course_whetherHighLevelCourse_checkBox);

        highLevelTextView=(TextView)findViewById(R.id.add_course_highLevelCoursePoints_textView);
        highLevelCoursePoints=(EditText)findViewById(R.id.add_course_highLevelCoursePoints_editText);

        /**如果没有选中高级课程，则隐藏填高级课程积分的TextView*/
        if(!addCourseWhetherHighLevelCourseCheckBox.isChecked()){
            highLevelCoursePoints.setVisibility(View.GONE);
        }


        addCourseButton.setOnClickListener(this);
        addCourseBackImage.setOnClickListener(this);
        addCourseWhetherHighLevelCourseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!addCourseWhetherHighLevelCourseCheckBox.isChecked()){
                    highLevelTextView.setVisibility(View.GONE);
                    highLevelCoursePoints.setVisibility(View.GONE);
                }
                else{
                    highLevelTextView.setVisibility(View.VISIBLE);
                    highLevelCoursePoints.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_add_course_backImage:
                Toast.makeText(AdminAddCourseActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.add_course_button:
                Dialog addCourseDialog = new AlertDialog.Builder(AdminAddCourseActivity.this)
                        .setTitle("新增课程")
                        .setMessage("确定增加？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String courseName = addCourseCourseNameEditText.getText().toString();
                                String courseDesc = addCourseCourseDescEditText.getText().toString();
                                whetherExam = addCourseWhetherExamCheckBox.isChecked();
                                whetherHighLevelCourse = addCourseWhetherHighLevelCourseCheckBox.isChecked();
                                //加入到课程数据库中，返回是否加入成功的状态值
                                //....
                                Intent intentAddForSure = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
                                startActivity(intentAddForSure);
                                //这里执行添加课程操作
                                Toast.makeText(AdminAddCourseActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentAddCancel = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
                                startActivity(intentAddCancel);
                            }
                        }).create();
                addCourseDialog.show();
                break;
        }
    }
}
