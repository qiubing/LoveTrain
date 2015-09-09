package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.MainAdminActivity;
import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminLessonDetailActivity extends Activity implements View.OnClickListener {

    private ImageView backImageView;
    private Button alterLessonBtn;
    private Button deleteLessonBtn;
    private TextView signUpPopulationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lesson_detail);

        backImageView=(ImageView)findViewById(R.id.admin_lesson_detail_backImageView);
        alterLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_alterLessonButton);
        deleteLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_deleteLessonButton);
        signUpPopulationTextView = (TextView) findViewById(R.id.lesson_detail_signIn_textView);

        backImageView.setOnClickListener(this);
        alterLessonBtn.setOnClickListener(this);
        deleteLessonBtn.setOnClickListener(this);
        signUpPopulationTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_lesson_detail_backImageView:
                Intent intentBackImage = new Intent(AdminLessonDetailActivity.this,MainAdminActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.admin_lesson_detail_alterLessonButton:
                Intent intentAlterLesson = new Intent(AdminLessonDetailActivity.this,AdminAlterLessonActivity.class);
                startActivity(intentAlterLesson);
                finish();
                break;
            case R.id.admin_lesson_detail_deleteLessonButton:
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了删除课时", Toast.LENGTH_LONG).show();
                Intent intentDeleteLesson = new Intent(AdminLessonDetailActivity.this,MainAdminActivity.class);
                startActivity(intentDeleteLesson);
                break;
            case R.id.lesson_detail_signIn_textView:
                Intent intentSignInInfo = new Intent(AdminLessonDetailActivity.this,AdminSignInExamPersonInfoActivity.class);
                startActivity(intentSignInInfo);
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了查看签到人员信息", Toast.LENGTH_LONG).show();
                finish();
                break;
        }

    }
}
