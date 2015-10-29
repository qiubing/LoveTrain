package cn.nubia.activity.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.Map;

import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamEnrollMsg;
import cn.nubia.entity.ExamItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

public class AdminExamDetailActivity extends BaseCommunicateActivity implements View.OnClickListener{
    private Button mInputScore;
    private Button mDeleteExam;
    private Button mEditExam;
    private Button mExamEnroll;

    private TextView mExamDes;
    private TextView mExamAddress;
    private TextView mExamStartTime;
    private TextView mExamTimeLong;
    private TextView mExamCredits;
    private TextView mExamPeapleNumber;
    private TextView mExamPeapleNumberDetails;

    private static final String URL = Constant.BASE_URL + "/exam/delete.do";
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private boolean mNextPressReady;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;
    private GestureDetector gestureDetector;
    private  ExamItem mExamItemExamEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_detail);
        holdView();

        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);

        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        mExamItemExamEdit = (ExamItem) getIntent().getSerializableExtra("ExamInfo");
        TextView sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        sub_page_title.setText("考试详情");
        TextView lessonNameTextView = (TextView) findViewById(R.id.title_text);
        lessonNameTextView.setText(mExamItemExamEdit.getName());
        mExamDes.setText("考试简介：" + mExamItemExamEdit.getDescription());
        mExamAddress.setText(mExamItemExamEdit.getLocale());
        mExamStartTime.setText(TimeFormatConversion.toDateTime(mExamItemExamEdit.getStartTime()));
        mExamTimeLong.setText(TimeFormatConversion.toTimeLong(mExamItemExamEdit.getStartTime(), mExamItemExamEdit.getEndTime()) + "分钟");
        mExamCredits.setText(mExamItemExamEdit.getExamCredits() + "");
        mExamPeapleNumber.setText(mExamItemExamEdit.getErollUsers()+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        mNextPressReady = true;
    }


    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.manager_exam_inputscorebtn:
                intent = new Intent(AdminExamDetailActivity.this, AdminExamInputScoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ExamInfo", mExamItemExamEdit);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.manager_exam_deletebtn:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminExamDetailActivity.this)
                        //设置对话框标题
                        .setTitle("删除考试")
                        //设置图标
                        .setIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                        .setMessage("确认要删除《" + mExamItemExamEdit.getName()+ "》这门考试吗?");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            case R.id.manager_exam_editbtn:
                intent = new Intent(AdminExamDetailActivity.this, AdminEditExamActivity.class);
                Bundle bundleExamEdit = new Bundle();
                bundleExamEdit.putSerializable("ExamInfo", mExamItemExamEdit);
                intent.putExtras(bundleExamEdit);
                startActivity(intent);
                break;
            case R.id.exam_number_button:
                intent = new Intent(AdminExamDetailActivity.this, AdminSignInExamPersonInfoActivity.class);
                Bundle examMenberBundle = new Bundle();
                examMenberBundle.putSerializable("ExamIndex", mExamItemExamEdit);
                intent.putExtras(examMenberBundle);
                startActivity(intent);
                break;
            case R.id.manager_exam_enrollbtn:
                if (mNextPressReady) {
                    ExamEnrollMsg examEnrollMsg = new ExamEnrollMsg();
                    examEnrollMsg.setUserID(Constant.user.getUserID());
                    examEnrollMsg.setExamIndex(mExamItemExamEdit.getIndex());
                    examEnrollMsg.setOperateType(CommunicateService.OperateType.INSERT);
//                    if(null==mExamItemExamEdit.getCourseIndex()){
                    mBinder.communicate(
                            examEnrollMsg, new Inter(), URLMap.URL_ADD_NORMALEXAMENROLL);
//                    }else{
//                        mBinder.communicate(
//                                examEnrollMsg, new Inter(), URLMap.URL_ADD_SPECIALEXAMENROLL);
//                    }
                    mExamEnroll.setText("报名中...");
                    mNextPressReady = false;

                }
                break;
        }
    }

    private void holdView(){
        mInputScore = (Button) findViewById(R.id.manager_exam_inputscorebtn);
        mDeleteExam = (Button) findViewById(R.id.manager_exam_deletebtn);
        mEditExam = (Button) findViewById(R.id.manager_exam_editbtn);
        mExamEnroll = (Button) findViewById(R.id.manager_exam_enrollbtn);
        mExamDes = (TextView) findViewById(R.id.exam_describe);
        mExamAddress = (TextView) findViewById(R.id.exam_address);
        mExamStartTime = (TextView) findViewById(R.id.exam_start_time);
        mExamTimeLong = (TextView) findViewById(R.id.exam_time);
        mExamCredits = (TextView) findViewById(R.id.exam_credits);
        mExamPeapleNumber = (TextView) findViewById(R.id.exam_number);
        mExamPeapleNumberDetails = (TextView) findViewById(R.id.exam_number_button);

    }

    private void setViewLogic(){

        if(Constant.IS_ADMIN == true){
            mInputScore.setOnClickListener(this);
            mDeleteExam.setOnClickListener(this);
            mEditExam.setOnClickListener(this);
            mExamPeapleNumberDetails.setOnClickListener(this);
        } else{
            mExamEnroll.setVisibility(View.VISIBLE);
            mExamEnroll.setOnClickListener(this);
            mInputScore .setVisibility(View.GONE);
            mDeleteExam.setVisibility(View.GONE);
            mEditExam.setVisibility(View.GONE);
            mExamPeapleNumberDetails.setVisibility(View.GONE);
        }

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
        requestParams.add("record_modify_time_course", "1435125456111");
        requestParams.put("exam_index", mExamItemExamEdit.getIndex());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                if( code == 0 && isOk) {
                    Toast.makeText(AdminExamDetailActivity.this, "删除考试成功", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ExamItem",mExamItemExamEdit);
                    intent.putExtras(bundle);
                    AdminExamDetailActivity.this.setResult(1, intent);
                }else {
                    Toast.makeText(AdminExamDetailActivity.this, "删除考试失败", Toast.LENGTH_SHORT).show();
                }
                AdminExamDetailActivity.this.finish();
            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminExamDetailActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
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
        mExamEnroll.setText(R.string.activity_manager_exam_enrollbtn);
        if(response==null){
            DialogMaker.finishCurrentDialog(AdminExamDetailActivity.this,
                    AdminExamDetailActivity.this, "连接服务器失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogMaker.finishCurrentDialog(AdminExamDetailActivity.this,
                        AdminExamDetailActivity.this,
                        "考试报名成功!", true);
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.finishCurrentDialog(AdminExamDetailActivity.this,
                        AdminExamDetailActivity.this,
                        "考试报名失败：\n" +
                                message, false);
            }
        }
    }

}
