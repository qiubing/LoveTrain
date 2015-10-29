package cn.nubia.activity.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;
import cn.nubia.entity.SeniorEnrollMsg;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;

/**
 * 管理员课程详细界面
 * Created by hexiao on 2015/9/1.
 */
public class AdminCourseDetailActivity extends BaseCommunicateActivity implements View.OnClickListener {

    private CourseItem mCourseItem;
    private Bundle bundle;

    private boolean mNextPressReady;
    private Button mEnrollSeniorCourse;

    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_detail);
        bundle=new Bundle();

        Button signUpAdminBtn = (Button) findViewById(R.id.cource_enroll_manager);
        Button alterCourseBtn = (Button) findViewById(R.id.cource_change);
        Button lessonAddBtn = (Button) findViewById(R.id.cource_add);
        Button courseDeleteBtn = (Button) findViewById(R.id.cource_delete);
        TextView courseIsExamTextview = (TextView) findViewById(R.id.cource_is_exam);
        TextView courseRealDescTextview = (TextView) findViewById(R.id.cource_describe);
        TextView courseRealTypeTextView = (TextView) findViewById(R.id.cource_type);
        mEnrollSeniorCourse = (Button) findViewById(R.id.cource_enroll);


        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });


        /**获取启动该Activity的Intent*/
        Intent intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");

        if(Constant.IS_ADMIN) {
            signUpAdminBtn.setOnClickListener(this);
            alterCourseBtn.setOnClickListener(this);
            lessonAddBtn.setOnClickListener(this);
            courseDeleteBtn.setOnClickListener(this);
        } else {
            signUpAdminBtn.setVisibility(View.GONE);
            alterCourseBtn.setVisibility(View.GONE);
            lessonAddBtn.setVisibility(View.GONE);
            courseDeleteBtn.setVisibility(View.GONE);
            if(mCourseItem.getType().equals("senior")) {
                mEnrollSeniorCourse.setVisibility(View.VISIBLE);
                mEnrollSeniorCourse.setOnClickListener(this);
            }
        }

        if(mCourseItem != null) {
            String courceType = "";
            switch(mCourseItem.getType()) {
                case "cource" :
                    courceType = "普通课程";
                    break;
                case "senior" :
                    courceType = "高级课程";
                    break;
                case "share" :
                    courceType = "分享课程";
                    break;
            }
            TextView sub_page_title = (TextView) findViewById(R.id.sub_page_title);
            TextView courceNameTextView = (TextView) findViewById(R.id.title_text);
            sub_page_title.setText("课程详情");
            courceNameTextView.setText(mCourseItem.getName());
            courseRealDescTextview.setText("课程简介：" + mCourseItem.getDescription());
            courseRealTypeTextView.setText(courceType);
            courseIsExamTextview.setText(mCourseItem.hasExam() ? "是" : "否");

           /* if(mCourseItem.getType().equals("senior")){
                courseRealTypeTextView.setText("课程类型：" + mCourseItem.getType() + "\n是否考试：" +
                        mCourseItem.hasExam()
//                        + "\n课程积分：" + mCourseItem.getCourseCredits()
//                        + "\n" + "需扣积分："+mCourseItem.getEnrollCredits());
                        +"");
            }
            else {
                courseRealTypeTextView.setText("课程类型：" + mCourseItem.getType() + "\n是否考试：" +
                        mCourseItem.hasExam()
//                        + "\n课程积分：" + mCourseItem.getCourseCredits()
                );
            }*/
        }
        bundle.putSerializable("CourseItem", mCourseItem);

    }

    @Override
    public void onStart(){
        super.onStart();
        mNextPressReady = true;
    }

//    @Override
//    protected void onBinderCompleted() {
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cource_enroll_manager:
                Intent intentSignInManage = new Intent(AdminCourseDetailActivity.this, AdminSignUpManageActivity.class);
                intentSignInManage.putExtras(bundle);
                startActivity(intentSignInManage);
                break;
            case R.id.cource_change:
                Intent intentAlterCourse = new Intent(AdminCourseDetailActivity.this, AdminAlterCourseActivity.class);
                bundle.putSerializable("CourseItem", mCourseItem);
                intentAlterCourse.putExtras(bundle);
                startActivity(intentAlterCourse);
                break;
            case R.id.cource_add:
                bundle.putSerializable("CourseItem", mCourseItem);
                Intent intentAddLesson = new Intent(AdminCourseDetailActivity.this, AdminAddLessonActivity.class);
                intentAddLesson.putExtras(bundle);
                startActivityForResult(intentAddLesson,2);
                break;
            case R.id.cource_delete:
                final AlertDialog.Builder builderDelete = new AlertDialog.Builder(AdminCourseDetailActivity.this)
                        //设置对话框标题
                        .setTitle("删除课程")
                                //设置图标
                        .setIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                        .setMessage("确认要删除《" + mCourseItem.getName() + "》这门课时吗?");
                builderDelete.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                });
                builderDelete.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderDelete.create().show();
                break;
            case R.id.cource_enroll:
                if(mNextPressReady) {
                    boolean isTeacher = false;
                    List<LessonItem> lessonList = mCourseItem.getLessonList();
                    for (LessonItem item : lessonList) {
                        if (item.getTeacherID().equals(Constant.user.getUserID())) {
                            isTeacher = true;
                            break;
                        }
                    }
                    if (isTeacher) {
                        DialogMaker.finishCurrentDialog(AdminCourseDetailActivity.this,
                                AdminCourseDetailActivity.this, "不能报名自己的课程!", true);
                    } else {
                        SeniorEnrollMsg msg = new SeniorEnrollMsg();
                        msg.setUserID(Constant.user.getUserID());
                        msg.setCourseIndex(mCourseItem.getIndex());
                        msg.setOperateType(CommunicateService.OperateType.INSERT);
                        mEnrollSeniorCourse.setText("报名中...");
                        mBinder.communicate(msg, new Inter(), URLMap.URL_ADD_SENIORCOURSEENROLL);
                        mNextPressReady = false;
                    }
                }
                break;
        }
    }

    private void deleteData(){
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
        requestParams.put("course_index", mCourseItem.getIndex());
        String deleteCourseURL = Constant.BASE_URL + "course/del_course.do";
        AsyncHttpHelper.post(deleteCourseURL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);

                if( code == 0 && isOk) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CourseItem",mCourseItem);
                    intent.putExtras(bundle);
                    AdminCourseDetailActivity.this.setResult(1, intent);

                    Toast.makeText(AdminCourseDetailActivity.this, "删除课程成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(AdminCourseDetailActivity.this, "删除课程失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminCourseDetailActivity.this, "删除课程失败", Toast.LENGTH_SHORT).show();
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminCourseDetailActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    public void back(View view) {
        this.finish();
    }


    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        mEnrollSeniorCourse.setText(R.string.enrollseniorcoursebtn);
        if(response==null){
            DialogMaker.finishCurrentDialog(AdminCourseDetailActivity.this,
                    AdminCourseDetailActivity.this, "连接服务器失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                    DialogMaker.finishCurrentDialog(AdminCourseDetailActivity.this,
                            AdminCourseDetailActivity.this, "报名申请成功，请等待管理员审核!", true);
            }else if (operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.finishCurrentDialog(AdminCourseDetailActivity.this,
                        AdminCourseDetailActivity.this, "报名申请失败：\n" +
                                message, false);
            }
        }
    }
}
