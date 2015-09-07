package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.nubia.activity.R;

/**
 * 管理员课程详细界面
 * Created by hexiao on 2015/9/1.
 */
public class AdminCourseDetailActivity extends Activity implements View.OnClickListener{

    private TextView courseRealNameTextview;
    private TextView courseRealDescTextview;

    private Button signUpAdminBtn;
    private Button alterCourseBtn;
    private Button lessonAddBtn;
    private Button courseDeleteBtn;
    private ImageView adminCourseDetailBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_detail);

        courseRealNameTextview=(TextView)findViewById(R.id.course_realName);
        courseRealDescTextview=(TextView)findViewById(R.id.course_realDesc);
        courseRealNameTextview.setText("JAVA基础");
        courseRealDescTextview.setText("JAVA基础是一门非常有用的入门课程");


        adminCourseDetailBackImage=(ImageView)findViewById(R.id.admin_course_detail_backImage);
        signUpAdminBtn=(Button)findViewById(R.id.signUpAdminBtn);
        alterCourseBtn=(Button)findViewById(R.id.alterCourseBtn);
        lessonAddBtn=(Button)findViewById(R.id.lessonAddBtn);
        courseDeleteBtn=(Button)findViewById(R.id.courseDeleteBtn);

        //set the listening event;
        adminCourseDetailBackImage.setOnClickListener(this);
        signUpAdminBtn.setOnClickListener(this);
        alterCourseBtn.setOnClickListener(this);
        lessonAddBtn.setOnClickListener(this);
        courseDeleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_course_detail_backImage:
                Intent intentBackImage = new Intent(AdminCourseDetailActivity.this,AdminAllCourseActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.signUpAdminBtn:
                Intent intentSignUp = new Intent(AdminCourseDetailActivity.this,AdminSignUpControlActivity.class);
                startActivity(intentSignUp);
                finish();
                break;
            case R.id.alterCourseBtn:
                Intent intentAlterCourse = new Intent(AdminCourseDetailActivity.this,AdminAlterCourseActivity.class);
                startActivity(intentAlterCourse);
                finish();
                break;
            case R.id.lessonAddBtn:
                Intent intentAddLesson = new Intent(AdminCourseDetailActivity.this,AdminAddLessonActivity.class);
                startActivity(intentAddLesson);
                finish();
                break;
            case R.id.courseDeleteBtn:
                Intent intentDeleteCourse = new Intent(AdminCourseDetailActivity.this,AdminDeleteCourseActivity.class);
                startActivity(intentDeleteCourse);
                finish();
                break;
        }
    }
}
