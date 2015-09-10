package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * 管理员课程详细界面
 * Created by hexiao on 2015/9/1.
 */
public class AdminCourseDetailActivity extends Activity implements View.OnClickListener {

    private TextView courseRealNameTextview;
    private TextView courseRealDescTextview;

    private Button signUpAdminBtn;
    private Button alterCourseBtn;
    private Button lessonAddBtn;
    private Button courseDeleteBtn;
    private ImageView adminCourseDetailBackImage;

    private String items[]={"zhangsan","lisi","wangwu"};

    private AdminCourseDetailActivity adminCourseDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_detail);
        adminCourseDetailActivity = this;

        courseRealNameTextview = (TextView) findViewById(R.id.course_realName);
        courseRealDescTextview = (TextView) findViewById(R.id.course_realDesc);
        courseRealNameTextview.setText("JAVA基础");
        courseRealDescTextview.setText("JAVA基础是一门非常有用的入门课程");


        adminCourseDetailBackImage = (ImageView) findViewById(R.id.admin_course_detail_backImage);
        /*four button*/
        signUpAdminBtn = (Button) findViewById(R.id.signUpAdminBtn);
        alterCourseBtn = (Button) findViewById(R.id.alterCourseBtn);
        lessonAddBtn = (Button) findViewById(R.id.lessonAddBtn);
        courseDeleteBtn = (Button) findViewById(R.id.courseDeleteBtn);



        //set the listening event;
        adminCourseDetailBackImage.setOnClickListener(this);
        signUpAdminBtn.setOnClickListener(this);
        alterCourseBtn.setOnClickListener(this);
        lessonAddBtn.setOnClickListener(this);
        courseDeleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_course_detail_backImage:
                Intent intentBackImage = new Intent(AdminCourseDetailActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);
                break;
            case R.id.signUpAdminBtn:
                //添加一个对话框即可
                Dialog signUpAdminDialog = new AlertDialog.Builder(AdminCourseDetailActivity.this)
                        .setTitle("课程报名情况")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminCourseDetailActivity.this, "你选择了其中一项", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminCourseDetailActivity.this, "确定", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminCourseDetailActivity.this, "取消", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                signUpAdminDialog.show();
                break;
            case R.id.alterCourseBtn:
                Intent intentAlterCourse = new Intent(AdminCourseDetailActivity.this, AdminAlterCourseActivity.class);
                startActivity(intentAlterCourse);
                break;
            case R.id.lessonAddBtn:
                Intent intentAddLesson = new Intent(AdminCourseDetailActivity.this, AdminAddLessonActivity.class);
                startActivity(intentAddLesson);
                break;
            case R.id.courseDeleteBtn:
                Dialog alterCourseDialog = new AlertDialog.Builder(adminCourseDetailActivity)
                        .setTitle("删除课程")
                        .setMessage("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentDeleteCourse = new Intent(AdminCourseDetailActivity.this, AdminMainActivity.class);
                                startActivity(intentDeleteCourse);
                                //这里执行删除课程操作
                                Toast.makeText(AdminCourseDetailActivity.this, "确定 ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminCourseDetailActivity.this, "取消", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                alterCourseDialog.show();
                break;
        }
    }
}
