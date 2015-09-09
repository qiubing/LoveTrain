package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminAlterLessonActivity extends Activity implements View.OnClickListener{

    private Button alterLessonButton;
    private ImageView alterLessonBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter_lesson);


        alterLessonBackImage=(ImageView)findViewById(R.id.admin_alter_lesson_backImage);
        alterLessonButton=(Button)findViewById(R.id.admin_alter_lesson_alterForeSureButton);

        alterLessonButton.setOnClickListener(this);
        alterLessonBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_alter_lesson_backImage:
                Toast.makeText(AdminAlterLessonActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAlterLessonActivity.this,AdminLessonDetailActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.admin_alter_lesson_alterForeSureButton:
                Intent intentAlterForSure = new Intent(AdminAlterLessonActivity.this,AdminLessonDetailActivity.class);
                startActivity(intentAlterForSure);
                Toast.makeText(AdminAlterLessonActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
}
