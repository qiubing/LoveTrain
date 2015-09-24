package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;


/**
 * Created by hexiao on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class AdminAlterCourseActivity extends Activity implements View.OnClickListener {

    private EditText alterCourseCourseNameEditText;
    private EditText alterCourseCourseDescEditText;

    /**
     * 高级课程所需
     */
    private TextView alterCourseHighLevelCourse;
    private EditText alterCourseHighLevelCourseDeletePoints;


    private Spinner courseTypeSpinner;
    private EditText alterCourseCoursePointsEditText;

    private CourseItem mCourseItem;


    private GestureDetector gestureDetector;

    //复选框
    private CheckBox alterCourseWhetherExamCheckBox;

    /**
     * 修改课程URL
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alter_course);

        alterCourseCourseNameEditText = (EditText) findViewById(R.id.alter_course_courseName_editText);
        alterCourseCourseDescEditText = (EditText) findViewById(R.id.alter_course_courseDesc_editText);


        courseTypeSpinner = (Spinner) findViewById(R.id.alter_course_courseType);

        alterCourseHighLevelCourse = (TextView) findViewById(R.id.alter_course_highLevelCoursePoints_textView);
        alterCourseHighLevelCourseDeletePoints = (EditText) findViewById(R.id.alter_course_highLevelCoursePoints_editText);
        alterCourseCoursePointsEditText = (EditText) findViewById(R.id.alter_course_coursePoints_editText);

        //创建手势管理单例对象
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        /**出错处理*/
        RelativeLayout loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        RelativeLayout networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);


        Button alterCourseButton = (Button) findViewById(R.id.alter_course_button);

        alterCourseWhetherExamCheckBox = (CheckBox) findViewById(R.id.alter_course_whetherExam_checkBox);


        /**获取启动该Activity的Intent*/
        Intent intent = getIntent();
        mCourseItem = (CourseItem) intent.getSerializableExtra("CourseItem");
        if (mCourseItem != null) {
            alterCourseCourseNameEditText.setText(mCourseItem.getName());
            alterCourseCourseDescEditText.setText(mCourseItem.getDescription());

            String courseTypeStr = mCourseItem.getType();
            if (courseTypeStr.equals("1"))
                courseTypeSpinner.setSelection(0);
            else
                courseTypeSpinner.setSelection(1);

            courseTypeSpinner.setSelection(courseTypeStr.equals("course") ? 0 : 1);

            alterCourseCoursePointsEditText.setText(mCourseItem.getCourseCredits() + "");
            alterCourseWhetherExamCheckBox.setChecked(mCourseItem.hasExam());

            if (courseTypeSpinner.getSelectedItem().toString().equals("senior") && alterCourseHighLevelCourseDeletePoints.getVisibility() == View.VISIBLE) {
                alterCourseHighLevelCourseDeletePoints.setText(mCourseItem.getEnrollCredits() + "");
            }
        }
        courseTypeSpinner.setEnabled(false);

        /**如果没有选中高级课程，则隐藏填高级课程积分的TextView*/
        courseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!courseTypeSpinner.getSelectedItem().toString().equals("senior")) {
                    alterCourseHighLevelCourse.setVisibility(View.GONE);
                    alterCourseHighLevelCourseDeletePoints.setVisibility(View.GONE);
                } else {
                    alterCourseHighLevelCourse.setVisibility(View.VISIBLE);
                    alterCourseHighLevelCourseDeletePoints.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /**监听事件*/
        alterCourseButton.setOnClickListener(this);

    }


    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter_course_button:
                if (alterCourseCourseNameEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterCourseActivity.this, "课程名称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (alterCourseCourseDescEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterCourseActivity.this, "课程简介不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (courseTypeSpinner.getSelectedItem().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterCourseActivity.this, "课程类型不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (alterCourseCoursePointsEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterCourseActivity.this, "课程积分不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (alterCourseHighLevelCourseDeletePoints.getVisibility() != View.GONE && alterCourseHighLevelCourseDeletePoints.getText().toString().trim().equals("")) {
                    Toast.makeText(AdminAlterCourseActivity.this, "高级课程所减积分", Toast.LENGTH_SHORT).show();
                    return;
                }

                mCourseItem.setName(alterCourseCourseNameEditText.getText().toString());
                mCourseItem.setDescription(alterCourseCourseDescEditText.getText().toString());
                mCourseItem.setType(courseTypeSpinner.getSelectedItem().toString());
                mCourseItem.setCourseCredits(Integer.parseInt(alterCourseCoursePointsEditText.getText().toString()));
                mCourseItem.setHasExam(alterCourseWhetherExamCheckBox.isChecked());

                Dialog alterCourseDialog = new AlertDialog.Builder(AdminAlterCourseActivity.this)
                        .setTitle("修改课程")
                        .setMessage("确定修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                upData();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create();
                alterCourseDialog.show();
                break;
        }

    }

    private void upData() {
        String alterCourseUrl = Constant.BASE_URL + "course/edit_course.do";
        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
        requestParams.add("course_index", mCourseItem.getIndex() + "");
        requestParams.add("course_name", alterCourseCourseNameEditText.getText().toString());
        requestParams.add("course_description", alterCourseCourseDescEditText.getText().toString());
        /**普通课程，type为1；
         * 不可以修改为技术分享，type为2；
         * 高级课程，type为3；*/
        requestParams.add("has_exam", alterCourseWhetherExamCheckBox.isChecked() ? "true" : "false");
        requestParams.add("type", (courseTypeSpinner.getSelectedItem().toString().equals("course") ? 1 : 3) + "");
        requestParams.add("course_credits", alterCourseCoursePointsEditText.getText().toString());

        /**如果是高级课程，则该参数为里面的值为edittext中的值，否则为空字符*/
        if (alterCourseHighLevelCourseDeletePoints.getVisibility() != View.GONE) {
            requestParams.add("enroll_credits", alterCourseHighLevelCourseDeletePoints.getText().toString());
        } else
            requestParams.add("enroll_credits", "0");
        Log.i("alterCourse", requestParams.toString());
        AsyncHttpHelper.post(alterCourseUrl, requestParams, myJsonHttpResponseHandler);
    }


    private final MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                if (code == 0 && isOk) {
                    Toast.makeText(AdminAlterCourseActivity.this, "修改课程成功！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(AdminAlterCourseActivity.this, "修改课程失败！ ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Toast.makeText(AdminAlterCourseActivity.this, "连接服务器异常！ ", Toast.LENGTH_SHORT).show();
        }
    };

    public void back(View view) {
        this.finish();
    }
}
