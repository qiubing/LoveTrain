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
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by LK on 2015/9/9.
 */

/*胡立加：添加手势类方法
Android为手势检测提供了一个GestureDetector类， GestureDetector实例代表了一个手势检测器，
创建GestureDetector时需要传入一个GestureDetector.OnGestureListener实例，
GestureDetector.OnGestureListener就是一个监听器，负责对用户手势行为提供响应。

开发流程
  1 创建一个GestureDetector对象，创建该对象时必须实现一个GestureDetector.OnGestureListener监听器实例
    //创建手势检测器   public GestureDetector(Context context, OnGestureListener listener)
	GestureDetector detector = new GestureDetector(this, this);
  2 为应用程序的Activity(特定组件也可以)的TouchEvent事件绑定监听器，在事件处理中把Activity(特定组件也可以)
  上的TouchEvent事件交给GestureDetector处理
     //将该Activity上的触碰事件交给GestureDetector处理
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}

共需如下五步：
*  1 private GestureDetector gestureDetector;
* 2 GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
  3 gestureDetector = new GestureDetector(this, gestureDetectorManager);

   @Override
  4  public boolean onTouchEvent(MotionEvent event) {

        return  gestureDetector.onTouchEvent(event);
    }

  5 gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
*
* */
public class AdminExamDetailActivity extends BaseCommunicateActivity implements View.OnClickListener{
    private Button mInputScore;
    private Button mDeleteExam;
    private Button mEditExam;
    private Button mExamMenber;
    private TextView mCourseName;
    private TextView mExamIntroduction;
    private TextView mExamInfo;
    private ExamItem mExamItemExamEdit;
    private static final String URL = Constant.BASE_URL + "/exam/delete.do";
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private Button mEnroll;
    private boolean mNextPressReady;


    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_detail);
        holdView();

        mExamItemExamEdit = (ExamItem) getIntent().getSerializableExtra("ExamInfo");
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);

        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        TextView mManagerTitle = (TextView) findViewById(R.id.sub_page_title);
        mManagerTitle.setText(mExamItemExamEdit.getName() + "考试");
        mExamMenber.setText(mExamItemExamEdit.getErollUsers() + "人报考");
        initViewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewLogic();
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
            case R.id.manager_exam_menber:
                intent = new Intent(AdminExamDetailActivity.this, AdminSignInExamPersonInfoActivity.class);
                Bundle examMenberBundle = new Bundle();
                examMenberBundle.putSerializable("ExamIndex", mExamItemExamEdit);
                intent.putExtras(examMenberBundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void holdView(){
        mEnroll = (Button) findViewById(R.id.manager_exam_enroll);
        mInputScore = (Button) findViewById(R.id.manager_exam_inputscorebtn);
        mDeleteExam = (Button) findViewById(R.id.manager_exam_deletebtn);
        mEditExam = (Button) findViewById(R.id.manager_exam_editbtn);
        mExamMenber = (Button) findViewById(R.id.manager_exam_menber);
        mExamIntroduction = (TextView) findViewById(R.id.exam_introduction);
        mExamInfo = (TextView) findViewById(R.id.exam_info);
        mCourseName = (TextView) findViewById(R.id.course_name);

        if(!Constant.IS_ADMIN){//普通用户
            mExamMenber.setVisibility(View.GONE);
            mInputScore .setVisibility(View.GONE);
            mDeleteExam.setVisibility(View.GONE);
            mEditExam.setVisibility(View.GONE);
            mEnroll.setVisibility(View.VISIBLE);
        }
    }

    private void setViewLogic(){
        mInputScore.setOnClickListener(this);
        mDeleteExam.setOnClickListener(this);
        mEditExam.setOnClickListener(this);
        mExamMenber.setOnClickListener(this);

//        if(Constant.IS_ADMIN == true || status.equals("teacher")){
        if(Constant.IS_ADMIN == true){
            mInputScore.setOnClickListener(this);
            mDeleteExam.setOnClickListener(this);
            mEditExam.setOnClickListener(this);
            mExamMenber.setOnClickListener(this);
        }

        else{
            mInputScore.setVisibility(View.GONE);
            mDeleteExam.setVisibility(View.GONE);
            mEditExam.setVisibility(View.GONE);
            mExamMenber.setVisibility(View.GONE);
            //mGenerateQRCode.setVisibility(View.GONE);
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

        mEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    mNextPressReady = false;

                }
            }
        });
    }

    private void initViewData() {
        mCourseName.setText(mExamItemExamEdit.getName());
        mExamIntroduction.setText(mExamItemExamEdit.getDescription());
        mExamInfo.setText(
                "考试地点：" + mExamItemExamEdit.getLocale() +
                        "\n考试时间：" + TimeFormatConversion.toDateTime(mExamItemExamEdit.getStartTime()) +
                        "\n结束时间：" + TimeFormatConversion.toDateTime(mExamItemExamEdit.getEndTime()) +
                        "\n考试积分：" + mExamItemExamEdit.getExamCredits());
    }


    private void deleteData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        RequestParams requestParams = new RequestParams(Constant.getRequestParams());
//        requestParams.add("device_id", "MXJSDLJFJFSFS");
//        requestParams.add("request_time","1445545456456");
//        requestParams.add("apk_version","1");
//        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.put("exam_index", mExamItemExamEdit.getIndex());

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "addExam" + "onSuccess");
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + "," +isOk);
                if( code == 0 && isOk) {
                    Toast.makeText(AdminExamDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminExamDetailActivity.this, "该课程不存在", Toast.LENGTH_SHORT).show();
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
        // TODO Auto-generated method stub
        this.finish();
    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        if(response==null){
            DialogMaker.make(AdminExamDetailActivity.this,
                    AdminExamDetailActivity.this, "操作失败!", false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogMaker.make(AdminExamDetailActivity.this,
                        AdminExamDetailActivity.this,
                        "考试报名成功!", true);
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogMaker.make(AdminExamDetailActivity.this,
                        AdminExamDetailActivity.this,
                        "考试报名失败：\n" +
                                message, false);
            }
        }
    }

}
