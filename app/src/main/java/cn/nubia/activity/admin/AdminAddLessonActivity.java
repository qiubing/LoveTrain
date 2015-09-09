package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.nubia.activity.MainAdminActivity;
import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/7.
 */
public class AdminAddLessonActivity extends Activity implements View.OnClickListener {
    private EditText lessonName;
    private EditText teacherName;
    private EditText lessonDesc;
    private EditText lessonLocation;
    private EditText lessonStartTime;
    private EditText teacherGetPoints;
    private EditText studentGetPoints;
    private Button addLessonSureBtn;
    private ImageView addLessonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lesson);
        lessonName = (EditText) findViewById(R.id.add_lesson_lessonName_editText);
        teacherName = (EditText) findViewById(R.id.add_lesson_teacherName_editText);
        lessonDesc = (EditText) findViewById(R.id.add_lesson_lessonDesc_editText);
        lessonLocation = (EditText) findViewById(R.id.add_lesson_lessonLocation_editText);
        lessonStartTime = (EditText) findViewById(R.id.add_lesson_lessonStartTime_editText);
        teacherGetPoints = (EditText) findViewById(R.id.add_lesson_teacherGetPoints_editText);
        studentGetPoints = (EditText) findViewById(R.id.add_lesson_studentGetPoints_editText);
        addLessonSureBtn = (Button) findViewById(R.id.add_lesson_addLessonForSureButton);
        addLessonImageView = (ImageView) findViewById(R.id.admin_add_course_back);

        //添加课时按钮的事件
        addLessonSureBtn.setOnClickListener(this);
        addLessonImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_add_course_back:
                Intent intentBackImage = new Intent(AdminAddLessonActivity.this,MainAdminActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.add_lesson_addLessonForSureButton:
                String lessonNameStr = lessonName.getText().toString();
                String teacherNameStr = teacherName.getText().toString();
                String lessonDescStr = lessonDesc.getText().toString();
                String lessonLocationStr = lessonLocation.getText().toString();
                String lessonStartTimeStr = lessonStartTime.getText().toString();
                String teacherGetPointsStr = teacherGetPoints.getText().toString();
                String studentGetPointsStr = studentGetPoints.getText().toString();
                //添加课时
                Intent intentAddLesson = new Intent(AdminAddLessonActivity.this, MainAdminActivity.class);
                Toast.makeText(AdminAddLessonActivity.this, "添加成功，从添加课时跳转到所有课程", Toast.LENGTH_LONG).show();
                startActivity(intentAddLesson);
                finish();
                break;
        }
    }
}