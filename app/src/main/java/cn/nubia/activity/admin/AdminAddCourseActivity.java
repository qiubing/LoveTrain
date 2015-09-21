package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by hexiao on 2015/9/9.
 */
public class AdminAddCourseActivity extends Activity implements View.OnClickListener {

    private EditText addCourseCourseNameEditText;
    private EditText addCourseCourseDescEditText;

    private EditText addCourseCourseTypeEditText;
    private EditText addCourseCoursePointsEditText;

    private Button addCourseButton;
    private ImageView addCourseBackImage;

    //复选框
    private CheckBox addCourseWhetherExamCheckBox;

    private CourseItem courseItem;
//    private CheckBox addCourseWhetherHighLevelCourseCheckBox;

//    private TextView highLevelTextView;
//    private EditText highLevelCoursePoints;

    //保存是否是高级课程
//    private boolean whetherExam;
//    private boolean whetherHighLevelCourse;

    private static final String addCourseURL = Constant.BASE_URL + "course/add_course.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        courseItem=new CourseItem();

        addCourseCourseNameEditText = (EditText) findViewById(R.id.add_course_courseName_editText);
        addCourseCourseDescEditText = (EditText) findViewById(R.id.add_course_courseDesc_editText);
        addCourseCourseTypeEditText = (EditText) findViewById(R.id.add_course_courseType_editText);
        addCourseCoursePointsEditText = (EditText) findViewById(R.id.add_course_CoursePoints_editText);

        addCourseButton = (Button) findViewById(R.id.add_course_button);
        addCourseBackImage = (ImageView) findViewById(R.id.admin_add_course_backImage);

        addCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.add_course_whetherExam_checkBox);
//        addCourseWhetherHighLevelCourseCheckBox = (CheckBox) findViewById(R.id.add_course_whetherHighLevelCourse_checkBox);

//        highLevelTextView=(TextView)findViewById(R.id.add_course_highLevelCoursePoints_textView);
//        highLevelCoursePoints=(EditText)findViewById(R.id.add_course_highLevelCoursePoints_editText);

        /**如果没有选中高级课程，则隐藏填高级课程积分的TextView*/
//        if(!addCourseWhetherHighLevelCourseCheckBox.isChecked()){
//            highLevelCoursePoints.setVisibility(View.GONE);
//        }


        addCourseButton.setOnClickListener(this);
        addCourseBackImage.setOnClickListener(this);

//        addCourseWhetherHighLevelCourseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(!addCourseWhetherHighLevelCourseCheckBox.isChecked()){
//                    highLevelTextView.setVisibility(View.GONE);
//                    highLevelCoursePoints.setVisibility(View.GONE);
//                }
//                else{
//                    highLevelTextView.setVisibility(View.VISIBLE);
//                    highLevelCoursePoints.setVisibility(View.VISIBLE);
//                }
//            }
//        });

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

//                Dialog addCourseDialog = new AlertDialog.Builder(AdminAddCourseActivity.this)
//                        .setTitle("新增课程")
//                        .setMessage("确定增加？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                String courseName = addCourseCourseNameEditText.getText().toString();
//                                String courseDesc = addCourseCourseDescEditText.getText().toString();
//                                whetherExam = addCourseWhetherExamCheckBox.isChecked();
//                                whetherHighLevelCourse = addCourseWhetherHighLevelCourseCheckBox.isChecked();
//                                //加入到课程数据库中，返回是否加入成功的状态值
//                                //....
//                                Intent intentAddForSure = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
//                                startActivity(intentAddForSure);
//                                //这里执行添加课程操作
//                                Toast.makeText(AdminAddCourseActivity.this, "你点击了确认修改", Toast.LENGTH_LONG).show();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intentAddCancel = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
//                                startActivity(intentAddCancel);
//                            }
//                        }).create();
//                addCourseDialog.show();
                if(addCourseCourseNameEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addCourseCourseDescEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addCourseCourseTypeEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程类型不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addCourseCoursePointsEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                courseItem.setName(addCourseCourseNameEditText.getText().toString());
                courseItem.setDescription(addCourseCourseDescEditText.getText().toString());
                courseItem.setType(addCourseCourseTypeEditText.getText().toString());
                courseItem.setCourseCredits(10);
                courseItem.setHasExam(addCourseWhetherExamCheckBox.isChecked());
                upData();
                Bundle bundle=new Bundle();
                bundle.putSerializable("CourseItem", courseItem);
                Intent intentAddForSure = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
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


        // requestParams.add("course_index", "");
        requestParams.add("course_name", addCourseCourseNameEditText.getText().toString());
        requestParams.add("course_description", addCourseCourseDescEditText.getText().toString());
        requestParams.add("type", addCourseCourseTypeEditText.getText().toString());
        requestParams.add("course_credits", addCourseCoursePointsEditText.getText().toString());
        requestParams.add("has_exam", addCourseWhetherExamCheckBox.isChecked()?"1":"0");

        AsyncHttpHelper.post(addCourseURL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "addExam" + "onSuccess");
            try {
                int code = response.getInt("code");
//                boolean result = response.getBoolean("result");
                boolean isOk = response.getBoolean("data");
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + "," + "," +isOk);
//                if(result && code == 0 && isOk) {
                if(code == 0 && isOk) {
                    Toast.makeText(AdminAddCourseActivity.this, "success", Toast.LENGTH_SHORT).show();
                    addCourseCourseNameEditText.setText("");
                    addCourseCourseDescEditText.setText("");
                    addCourseCourseTypeEditText.setText("");
                    addCourseCoursePointsEditText.setText("");
                    addCourseWhetherExamCheckBox.setChecked(false);
                }

            } catch (Exception e) {
                Toast.makeText(AdminAddCourseActivity.this, "in success exception ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Toast.makeText(AdminAddCourseActivity.this, "on failure ", Toast.LENGTH_SHORT).show();
        }
    };
}
