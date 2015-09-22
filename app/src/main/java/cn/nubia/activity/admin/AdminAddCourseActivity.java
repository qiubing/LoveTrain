package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by hexiao on 2015/9/9.
 */
public class AdminAddCourseActivity extends Activity implements View.OnClickListener {

    private EditText addCourseCourseNameEditText;
    private EditText addCourseCourseDescEditText;
    private TextView mTitleText;

    //    private EditText addCourseCourseTypeEditText;
    private Spinner courseTypeSpinner;

    private EditText addCourseCoursePointsEditText;

    private Button addCourseButton;
    //private ImageView addCourseBackImage;
    private GestureDetector gestureDetector;

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


    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        courseItem=new CourseItem();
        mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("新增课程");
        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });


        addCourseCourseNameEditText = (EditText) findViewById(R.id.add_course_courseName_editText);
        addCourseCourseDescEditText = (EditText) findViewById(R.id.add_course_courseDesc_editText);
//        addCourseCourseTypeEditText = (EditText) findViewById(R.id.add_course_courseType_editText);
        courseTypeSpinner=(Spinner)findViewById(R.id.add_course_courseType);

        addCourseCoursePointsEditText = (EditText) findViewById(R.id.add_course_CoursePoints_editText);

        addCourseButton = (Button) findViewById(R.id.add_course_button);
        //addCourseBackImage = (ImageView) findViewById(R.id.admin_add_course_backImage);

        addCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.add_course_whetherExam_checkBox);

        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);


        /**如果没有选中高级课程，则隐藏填高级课程积分的TextView*/
//        if(!addCourseWhetherHighLevelCourseCheckBox.isChecked()){
//            highLevelCoursePoints.setVisibility(View.GONE);
//        }


        addCourseButton.setOnClickListener(this);
       // addCourseBackImage.setOnClickListener(this);


    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.admin_add_course_backImage:
                Toast.makeText(AdminAddCourseActivity.this, "你点击了返回", Toast.LENGTH_LONG).show();
                Intent intentBackImage = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);
                finish();
                break;*/
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
                if(courseTypeSpinner.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程类型不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addCourseCoursePointsEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                courseItem.setName(addCourseCourseNameEditText.getText().toString());
                courseItem.setDescription(addCourseCourseDescEditText.getText().toString());

                courseItem.setType(courseTypeSpinner.getSelectedItem().toString());

                courseItem.setCourseCredits(Integer.parseInt(addCourseCoursePointsEditText.getText().toString()));
                courseItem.setHasExam(addCourseWhetherExamCheckBox.isChecked());
                upData();
                Bundle bundle=new Bundle();
                bundle.putSerializable("CourseItem", courseItem);
                Intent intentAddForSure = new Intent(AdminAddCourseActivity.this, AdminCourseDetailActivity.class);
                intentAddForSure.putExtras(bundle);
                startActivity(intentAddForSure);
                break;
        }
    }

    void upData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");


        // requestParams.add("course_index", "");
        requestParams.add("course_name", addCourseCourseNameEditText.getText().toString());
        requestParams.add("course_description", addCourseCourseDescEditText.getText().toString());

        /**普通课程course，type为1；
         * 技术分享share，type为2；
         * 高级课程senior，type为3；*/
        String courseTypeStr=courseTypeSpinner.getSelectedItem().toString();
        requestParams.add("type",(courseTypeStr.equals("course")?1:(courseTypeStr.equals("share")?2:3))+"");

        requestParams.add("has_exam", addCourseWhetherExamCheckBox.isChecked()?"1":"0");
        requestParams.add("course_credits", addCourseCoursePointsEditText.getText().toString());


        AsyncHttpHelper.post(addCourseURL, requestParams, myJsonHttpResponseHandler);
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
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("xx", "addCourseinside" + "onSuccess");
//                if(result && code == 0 && isOk) {
                if( code == 0 && isOk) {
                    Toast.makeText(AdminAddCourseActivity.this, "success", Toast.LENGTH_SHORT).show();
                    addCourseCourseNameEditText.setText("");
                    addCourseCourseDescEditText.setText("");

                    courseTypeSpinner.setSelection(0);

                    addCourseCoursePointsEditText.setText("");
                    addCourseWhetherExamCheckBox.setChecked(false);
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminAddCourseActivity.this, "in success exception ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminAddCourseActivity.this, "on failure ", Toast.LENGTH_SHORT).show();
        }
    };

    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }


}
