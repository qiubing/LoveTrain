package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by hexiao on 2015/9/7.
 */
public class AdminAddLessonActivity extends Activity implements View.OnClickListener {

    private TextView courseName;
    private EditText lessonName;
    private EditText teacherName;
    private EditText lessonDesc;
    private EditText lessonLocation;
    private EditText lessonStartTime;
    private EditText lessonEndTime;
    private EditText teacherGetPoints;
    private EditText studentGetPoints;
    private Button addLessonSureBtn;
    private ImageView addLessonImageView;

    private CourseItem mCourseItem=new CourseItem();
    private LessonItem mLessonItem=new LessonItem();

    private static final String addLessonURL = Constant.BASE_URL + "course/add_lesson.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_lesson);

        courseName=(TextView)findViewById(R.id.add_lesson_CourseName_TextView);
        lessonName = (EditText) findViewById(R.id.add_lesson_lessonName_editText);
        teacherName = (EditText) findViewById(R.id.add_lesson_teacherName_editText);
        lessonDesc = (EditText) findViewById(R.id.add_lesson_lessonDesc_editText);
        lessonLocation = (EditText) findViewById(R.id.add_lesson_lessonLocation_editText);
        lessonStartTime = (EditText) findViewById(R.id.add_lesson_lessonStartTime_editText);
        lessonEndTime = (EditText) findViewById(R.id.add_lesson_lessonEndTime_editText);
        teacherGetPoints = (EditText) findViewById(R.id.add_lesson_teacherGetPoints_editText);
        studentGetPoints = (EditText) findViewById(R.id.add_lesson_studentGetPoints_editText);
        addLessonSureBtn = (Button) findViewById(R.id.add_lesson_addLessonForSureButton);
        addLessonImageView = (ImageView) findViewById(R.id.admin_add_course_back);

        Intent intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");

        courseName.setText(mCourseItem.getName()+"");

        //添加课时按钮的事件
        addLessonSureBtn.setOnClickListener(this);
        addLessonImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_add_course_back:
                Intent intentBackImage = new Intent(AdminAddLessonActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
            case R.id.add_lesson_addLessonForSureButton:
                mLessonItem.setName(lessonName.getText().toString());
                mLessonItem.setTeacherName(teacherName.getText().toString());
                mLessonItem.setDescription(lessonDesc.getText().toString());
                mLessonItem.setLocation(lessonLocation.getText().toString());

                mLessonItem.setStartTime(Long.parseLong(lessonStartTime.getText().toString()));
                mLessonItem.setEndTime(Long.parseLong(lessonEndTime.getText().toString()));

                mLessonItem.setCheckCredits(Integer.parseInt(studentGetPoints.getText().toString()));
                mLessonItem.setTeacherCredits(Integer.parseInt(teacherGetPoints.getText().toString()));


                if(lessonName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teacherName.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "教师姓名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonDesc.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonDesc.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonStartTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时开始时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lessonEndTime.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "课时结束时间不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(teacherGetPoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "讲师积分", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(studentGetPoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddLessonActivity.this, "学员积分", Toast.LENGTH_SHORT).show();
                    return;
                }

                upData();
                Bundle bundle=new Bundle();
                /**此处这个mLessonItem的课时ID一直为0，并没有赋值
                 * */
                bundle.putSerializable("LessonItem", mLessonItem);
                Intent intentAddForSure = new Intent(AdminAddLessonActivity.this, AdminLessonDetailActivity.class);
                intentAddForSure.putExtras(bundle);
                startActivity(intentAddForSure);
                break;
        }
    }
    void upData(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.add("course_index", mCourseItem.getIndex()+"");
        requestParams.add("lesson_name", lessonName.getText().toString());
        requestParams.add("teacher", teacherName.getText().toString());
        requestParams.add("course_description", lessonDesc.getText().toString());
        requestParams.add("locale",lessonLocation.getText().toString());
        requestParams.add("start_time", lessonStartTime.getText().toString());
        requestParams.add("end_time", lessonEndTime.getText().toString());
        requestParams.add("teacher_credits", teacherGetPoints.getText().toString());
        requestParams.add("check_credits", studentGetPoints.getText().toString());

        AsyncHttpHelper.post(addLessonURL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("xx", "addCourse" + "onSuccess");
            Log.i("xx", response.toString());
            try {
                Log.i("xx", "code");
                int code = response.getInt("code");
                Log.i("xx", "aftercode"+code);
                boolean isOk = response.getBoolean("data");
                Log.i("xx", "afterdata"+isOk);
                Log.i("xx", "addLesson" + "onSuccess");
//                if(result && code == 0 && isOk) {
                if( code == 0 && isOk) {
                    Toast.makeText(AdminAddLessonActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(AdminAddLessonActivity.this, "in success exception ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Toast.makeText(AdminAddLessonActivity.this, "on failure ", Toast.LENGTH_SHORT).show();
        }
    };
}
