package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.entity.LessonItem;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminAlterLessonActivity extends Activity implements View.OnClickListener{

    private LessonItem lessonItem;
    private Bundle bundle;

    private EditText lessonName;
    private EditText teacherName;
    private EditText lessonDesc;
    private EditText lessonLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter_lesson);
        bundle=new Bundle();

        ImageView alterLessonBackImage = (ImageView) findViewById(R.id.admin_alter_lesson_backImage);
        Button alterLessonButton = (Button) findViewById(R.id.admin_alter_lesson_alterForeSureButton);

        lessonName=(EditText)findViewById(R.id.alter_lesson_lessonName_editText);
        teacherName=(EditText)findViewById(R.id.alter_lesson_teacherName_editText);
        lessonDesc=(EditText)findViewById(R.id.alter_lesson_lessonDesc_editText);
        lessonLocation=(EditText)findViewById(R.id.alter_lesson_lessonLocation_editText);
        EditText lessonStartTime = (EditText) findViewById(R.id.alter_lesson_lessonStartTime_editText);
        EditText teacherPoints = (EditText) findViewById(R.id.alter_lesson_teacherGetPoints_editText);
        EditText studentPoints = (EditText) findViewById(R.id.alter_lesson_studentGetPoints_editText);

        /**获取并放入LessonItem*/
        Intent intent = getIntent();
        lessonItem=(LessonItem) intent.getSerializableExtra("LessonItem");

        if(lessonItem!=null) {
            lessonName.setText(lessonItem.getName());
            teacherName.setText(lessonItem.getTeacherName());
            lessonDesc.setText(lessonItem.getDescription());
            lessonLocation.setText(lessonItem.getLocation());
            lessonStartTime.setText(lessonItem.getStartTime() + "");
            teacherPoints.setText(lessonItem.getTeacherCredits() + "");
            studentPoints.setText(lessonItem.getCheckCredits() + "");
        }


        alterLessonButton.setOnClickListener(this);
        alterLessonBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_alter_lesson_backImage:
                Toast.makeText(AdminAlterLessonActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAlterLessonActivity.this,AdminLessonDetailActivity.class);
                bundle.putSerializable("LessonItem", lessonItem);
                intentBackImage.putExtras(bundle);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.admin_alter_lesson_alterForeSureButton:
                Intent intentAlterForSure = new Intent(AdminAlterLessonActivity.this,AdminLessonDetailActivity.class);
                lessonItem.setName(lessonName.getText().toString());
                lessonItem.setTeacherName(teacherName.getText().toString());
                lessonItem.setDescription(lessonDesc.getText().toString());
                lessonItem.setLocation(lessonLocation.getText().toString());
                lessonItem.setStartTime((long) 10096);
                lessonItem.setTeacherCredits(20);
                lessonItem.setCheckCredits(10);

                bundle.putSerializable("LessonItem", lessonItem);
                intentAlterForSure.putExtras(bundle);

                startActivity(intentAlterForSure);
                Toast.makeText(AdminAlterLessonActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}
