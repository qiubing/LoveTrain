package cn.nubia.activity.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.nubia.util.MyJsonHttpResponseHandler;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_detail);
        bundle=new Bundle();

        holdView();
        setViewLogic();

        Button signUpAdminBtn = (Button) findViewById(R.id.signUpAdminBtn);
        Button alterCourseBtn = (Button) findViewById(R.id.alterCourseBtn);
        Button lessonAddBtn = (Button) findViewById(R.id.lessonAddBtn);
        Button courseDeleteBtn = (Button) findViewById(R.id.courseDeleteBtn);
        TextView courseRealNameTextview = (TextView) findViewById(R.id.course_realName);
        TextView courseRealDescTextview = (TextView) findViewById(R.id.course_realDesc);
        TextView courseRealTypeTextView = (TextView) findViewById(R.id.course_realType);


        loadingFailedRelativeLayout = (RelativeLayout) findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);



        /**获取启动该Activity的Intent*/
        Intent intent=getIntent();
        mCourseItem=(CourseItem)intent.getSerializableExtra("CourseItem");
        TextView mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText(mCourseItem.getName() + "课程管理");
        String startActivity = intent.getStringExtra("startActivity");
        Log.i("huhu", "AdminCourceDetail: " + startActivity);

        switch (startActivity) {
            case "cn.nubia.activity.admin.AdminCourseAddTabActivity":
                signUpAdminBtn.setOnClickListener(this);
                alterCourseBtn.setOnClickListener(this);
                lessonAddBtn.setOnClickListener(this);
                courseDeleteBtn.setOnClickListener(this);
                break;
            case "cn.nubia.activity.client.ClientAllCourseActivity":
                signUpAdminBtn.setVisibility(View.GONE);
                alterCourseBtn.setVisibility(View.GONE);
                lessonAddBtn.setVisibility(View.GONE);
                courseDeleteBtn.setVisibility(View.GONE);
                if(mCourseItem.getType().equals("senior"))
                    mEnrollSeniorCourse.setVisibility(View.VISIBLE);
//                mEnrollSeniorCourse.setOnClickListener(this);
                break;
            case "cn.nubia.activity.client.ClientMyCourseActivity":
                signUpAdminBtn.setVisibility(View.GONE);
                alterCourseBtn.setVisibility(View.GONE);
                lessonAddBtn.setVisibility(View.GONE);
                courseDeleteBtn.setVisibility(View.GONE);
                break;
            default:
                Log.i("huhu", "AdminLessonDetail  startActivity异常了");
        }

        if(mCourseItem!=null) {
            courseRealNameTextview.setText(mCourseItem.getName());
            courseRealDescTextview.setText(mCourseItem.getDescription());
            courseRealTypeTextView.setText("课程类型：" + mCourseItem.getType() + "\n是否考试：" +
                    mCourseItem.hasExam() + "\n课程积分：" + mCourseItem.getCourseCredits());
        }

        bundle.putSerializable("CourseItem", mCourseItem);

    }

    @Override
    public void onStart(){
        super.onStart();
        connectService();
        mNextPressReady = true;
    }

    @Override
    public void onStop(){
        super.onStop();
        disconectService();
    }

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
            case R.id.signUpAdminBtn:
                Intent intentSignInManage = new Intent(AdminCourseDetailActivity.this, AdminSignUpManageActivity.class);
                intentSignInManage.putExtras(bundle);
                startActivity(intentSignInManage);
                break;
            case R.id.alterCourseBtn:
                Intent intentAlterCourse = new Intent(AdminCourseDetailActivity.this, AdminAlterCourseActivity.class);
                bundle.putSerializable("CourseItem", mCourseItem);
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
                        finish();
                    }
                });
                builderDelete.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderDelete.create().show();
                break;
        }
    }

    private void deleteData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        RequestParams requestParams = new RequestParams(Constant.getRequestParams());

        requestParams.put("course_index", mCourseItem.getIndex());
        String deleteCourseURL = Constant.BASE_URL + "course/del_course.do";
        AsyncHttpHelper.post(deleteCourseURL, requestParams, myJsonHttpResponseHandler);
    }

    private final MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");

                if( code == 0 && isOk) {
                    Toast.makeText(AdminCourseDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminCourseDetailActivity.this, "该课程不存在", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };

    public void back(View view) {
        this.finish();
    }

    private void holdView(){
        mEnrollSeniorCourse = (Button) findViewById(R.id.senior_course_enrollbtn);
    }

    private void setViewLogic(){
        mEnrollSeniorCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        DialogMaker.make(AdminCourseDetailActivity.this,
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
            }
        });

    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        mEnrollSeniorCourse.setText(R.string.enrollseniorcoursebtn);
        if(response==null){
            DialogMaker.make(AdminCourseDetailActivity.this,
                    AdminCourseDetailActivity.this, "连接服务器失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                    DialogMaker.make(AdminCourseDetailActivity.this,
                            AdminCourseDetailActivity.this, "报名申请成功，请等待管理员审核!", true);
            }else if (operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.make(AdminCourseDetailActivity.this,
                        AdminCourseDetailActivity.this, "报名申请失败：\n" +
                                message, false);
            }
        }
    }
}
