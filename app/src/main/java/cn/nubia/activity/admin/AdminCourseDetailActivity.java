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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * 管理员课程详细界面
 * Created by hexiao on 2015/9/1.
 */
public class AdminCourseDetailActivity extends Activity implements View.OnClickListener {

    private CourseItem mCourseItem;
    private Bundle bundle;

    private AdminCourseDetailActivity adminCourseDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_detail);
        adminCourseDetailActivity = this;
        bundle=new Bundle();
         TextView courseRealNameTextview;
         TextView courseRealDescTextview;
         TextView courseRealTypeTextView;

         Button signUpAdminBtn;
         Button alterCourseBtn;
         Button lessonAddBtn;
         Button courseDeleteBtn;
         ImageView adminCourseDetailBackImage;

        adminCourseDetailBackImage = (ImageView) findViewById(R.id.admin_course_detail_backImage);
        /*four button*/
        signUpAdminBtn = (Button) findViewById(R.id.signUpAdminBtn);
        alterCourseBtn = (Button) findViewById(R.id.alterCourseBtn);
        lessonAddBtn = (Button) findViewById(R.id.lessonAddBtn);
        courseDeleteBtn = (Button) findViewById(R.id.courseDeleteBtn);

        if(Constant.IS_ADMIN==false){
            signUpAdminBtn.setVisibility(View.GONE);
            alterCourseBtn.setVisibility(View.GONE);
            lessonAddBtn.setVisibility(View.GONE);
            courseDeleteBtn.setVisibility(View.GONE);
        }

        courseRealNameTextview = (TextView) findViewById(R.id.course_realName);
        courseRealDescTextview = (TextView) findViewById(R.id.course_realDesc);
        courseRealTypeTextView = (TextView) findViewById(R.id.course_realType);


        /**获取启动该Activity的Intent*/
        Intent intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");
        if(mCourseItem!=null) {
            courseRealNameTextview.setText(mCourseItem.getName());
            courseRealDescTextview.setText(mCourseItem.getDescription());
            courseRealTypeTextView.setText(mCourseItem.getType());
        }

        bundle.putSerializable("CourseItem",mCourseItem);

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
                finish();
                break;
            case R.id.signUpAdminBtn:
                Intent intentSignInManage = new Intent(AdminCourseDetailActivity.this, AdminSignUpManageActivity.class);
                intentSignInManage.putExtras(bundle);
                startActivity(intentSignInManage);
                break;
            case R.id.alterCourseBtn:
                Intent intentAlterCourse = new Intent(AdminCourseDetailActivity.this, AdminAlterCourseActivity.class);
                bundle.putSerializable("CourseItem",mCourseItem);
                intentAlterCourse.putExtras(bundle);
                startActivity(intentAlterCourse);
                break;
            case R.id.lessonAddBtn:
                Log.i("hexiao","lesonAddBtn");
                bundle.putSerializable("CourseItem", mCourseItem);
                Intent intentAddLesson = new Intent(AdminCourseDetailActivity.this, AdminAddLessonActivity.class);
                intentAddLesson.putExtras(bundle);
                startActivity(intentAddLesson);
                break;
            case R.id.courseDeleteBtn:
                Dialog alterCourseDialog = new AlertDialog.Builder(adminCourseDetailActivity)
                        .setTitle("删除课程")
                        .setMessage("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**这里执行删除课程操作*/
                                deleteCourse();
                                Intent intentDeleteCourse = new Intent(AdminCourseDetailActivity.this, AdminMainActivity.class);
                                startActivity(intentDeleteCourse);
                                Toast.makeText(AdminCourseDetailActivity.this, "删除成功 ", Toast.LENGTH_LONG).show();
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

    private void deleteCourse() {

        RequestParams requestParams = new RequestParams(Constant.getRequestParams());

        String deleteCourseURL = Constant.BASE_URL + "course/del_course.do";
        requestParams.add("course_index", mCourseItem.getIndex()+"");
        Log.i("hexiao1", "参数" + requestParams.toString());
        AsyncHttpHelper.post(deleteCourseURL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("hexiao1", response.toString());
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");

                if (code == 0 && isOk) {
                    Log.i("hexiao1", "delete sucess");
//                    Intent intent=new Intent(AdminCourseDetailActivity.this,AdminMainActivity.class);
//                    startActivity(intent);
                    Toast.makeText(AdminCourseDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(AdminCourseDetailActivity.this, "in success exception ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.i("hexiao1", "delete failure");
//            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminCourseDetailActivity.this, "on failure ", Toast.LENGTH_SHORT).show();
        }
    };


}
