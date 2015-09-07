package cn.nubia.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lesson);
        lessonName=(EditText)findViewById(R.id.add_lesson_lessonName_editText);
        teacherName=(EditText)findViewById(R.id.add_lesson_teacherName_editText);
        lessonDesc=(EditText)findViewById(R.id.add_lesson_lessonDesc_editText);
        lessonLocation=(EditText)findViewById(R.id.add_lesson_lessonLocation_editText);
        lessonStartTime=(EditText)findViewById(R.id.add_lesson_lessonStartTime_editText);
        teacherGetPoints=(EditText)findViewById(R.id.add_lesson_teacherGetPoints_editText);
        studentGetPoints=(EditText)findViewById(R.id.add_lesson_studentGetPoints_editText);
        addLessonSureBtn=(Button)findViewById(R.id.add_lesson_addLessonForSureButton);

        addLessonSureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String lessonNameStr=lessonName.getText().toString();
        String teacherNameStr=teacherName.getText().toString();
        String lessonDescStr=lessonDesc.getText().toString();
        String lessonLocationStr=lessonLocation.getText().toString();
        String lessonStartTimeStr=lessonStartTime.getText().toString();
        String teacherGetPointsStr=teacherGetPoints.getText().toString();
        String studentGetPointsStr=studentGetPoints.getText().toString();

    }
}
