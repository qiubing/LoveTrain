package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;

/**
 * Created by hexiao on 2015/9/9.
 */
@SuppressWarnings("deprecation")
public class AdminAddCourseActivity extends Activity implements View.OnClickListener {

    private EditText addCourseCourseNameEditText;
    private EditText addCourseCourseDescEditText;
    private TextView addCourseHighLevelCouse;
    private EditText addCourseHighLevelCourseDeletePoints;

    private Spinner courseTypeSpinner;

    private EditText addCourseCoursePointsEditText;

    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;


    //复选框
    private CheckBox addCourseWhetherExamCheckBox;

    private CourseItem mCourseItem;


    private static final String addCourseURL = Constant.BASE_URL + "/course/add_course.do";


    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        mCourseItem = new CourseItem();
        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
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


        courseTypeSpinner = (Spinner) findViewById(R.id.add_course_courseType);

        addCourseHighLevelCouse = (TextView) findViewById(R.id.add_course_highLevelCoursePoints_textView);
        addCourseHighLevelCourseDeletePoints = (EditText) findViewById(R.id.add_course_highLevelCoursePoints_editText);
        addCourseCoursePointsEditText = (EditText) findViewById(R.id.add_course_CoursePoints_editText);

        Button addCourseButton = (Button) findViewById(R.id.add_course_button);

        addCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.add_course_whetherExam_checkBox);


        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });


        courseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!courseTypeSpinner.getSelectedItem().toString().equals("senior")) {
                    addCourseHighLevelCouse.setVisibility(View.GONE);
                    addCourseHighLevelCourseDeletePoints.setVisibility(View.GONE);
                } else {
                    addCourseHighLevelCouse.setVisibility(View.VISIBLE);
                    addCourseHighLevelCourseDeletePoints.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        addCourseButton.setOnClickListener(this);

    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_course_button:
                if (addCourseCourseNameEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (addCourseCourseDescEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (courseTypeSpinner.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程类型不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (addCourseCoursePointsEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "课程积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (addCourseHighLevelCourseDeletePoints.getVisibility() != View.GONE && addCourseHighLevelCourseDeletePoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAddCourseActivity.this, "高级课程所减积分", Toast.LENGTH_SHORT).show();
                    return;
                }

                mCourseItem.setName(addCourseCourseNameEditText.getText().toString());
                mCourseItem.setDescription(addCourseCourseDescEditText.getText().toString());

                mCourseItem.setType(courseTypeSpinner.getSelectedItem().toString());

                mCourseItem.setCourseCredits(Integer.parseInt(addCourseCoursePointsEditText.getText().toString()));
                mCourseItem.setHasExam(addCourseWhetherExamCheckBox.isChecked());
                upData();

                /*Intent intentAddForSure = new Intent(AdminAddCourseActivity.this, AdminMainActivity.class);
                finish();
                startActivity(intentAddForSure);
                break;*/
        }
    }

    private void upData() {
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);

        RequestParams requestParams = new RequestParams(Constant.getRequestParams());

        /**普通课程course，type为1；
         * 添加课程不能为技术分享share，type为2；
         * 高级课程senior，type为3；*/

        requestParams.add("course_name", addCourseCourseNameEditText.getText().toString());
        requestParams.add("course_description", addCourseCourseDescEditText.getText().toString());
        String courseTypeStr = courseTypeSpinner.getSelectedItem().toString();
        requestParams.add("type", (courseTypeStr.equals("course") ? 1 : 3) + "");
        requestParams.add("has_exam", addCourseWhetherExamCheckBox.isChecked() ? "true" : "false");
        requestParams.add("course_credits", addCourseCoursePointsEditText.getText().toString());

        /**如果是高级课程，则该参数为里面的值为edittext中的值，否则为空字符*/
        if (addCourseHighLevelCourseDeletePoints.getVisibility() != View.GONE) {
            requestParams.add("enroll_credits", addCourseHighLevelCourseDeletePoints.getText().toString());
        }
        AsyncHttpHelper.post(addCourseURL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                int code = response.getInt("code");
                if (code == 0) {
                    mCourseItem.setIndex(response.getInt("data"));
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CourseItem", mCourseItem);
                    intent.putExtras(bundle);
                    AdminAddCourseActivity.this.setResult(2, intent);
                    Toast.makeText(AdminAddCourseActivity.this, "添加课程成功", Toast.LENGTH_SHORT).show();
                    /**添加一个课程后finish掉Activity**/
                    finish();
                    addCourseCourseNameEditText.setText("");
                    addCourseCourseDescEditText.setText("");
                    courseTypeSpinner.setSelection(0);
                    addCourseCoursePointsEditText.setText("");
                    addCourseWhetherExamCheckBox.setChecked(false);
                }
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminAddCourseActivity.this, "添加课程失败", Toast.LENGTH_SHORT).show();
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminAddCourseActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    public void back(View view) {
        this.finish();
    }
}
