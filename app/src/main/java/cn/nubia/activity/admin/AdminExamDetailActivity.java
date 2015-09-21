package cn.nubia.activity.admin;

import android.app.Activity;
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

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;

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
public class AdminExamDetailActivity extends Activity implements View.OnClickListener{
    private Button mInputScore;
    private Button mDeleteExam;
    private Button mEditExam;
    private Button mExamMenber;
    private TextView mCourseName;
    private TextView mExamIntroduction;
    private TextView mExamInfo;
    private ExamItem mExamItem;
    private ExamItem mExamItemExamEdit;
    private TextView mManagerTitle;
    private static final String URL = Constant.BASE_URL + "/exam/delete.do";
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;


    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_detail);
        //公用部分
        mManagerTitle = (TextView) findViewById(R.id.sub_page_title);
        mManagerTitle.setText(R.string.activity_manager_exam_detail_title);

        holdView();


        mExamItemExamEdit = (ExamItem) getIntent().getSerializableExtra("ExamInfo");
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        initViewData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewLogic();
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.manager_exam_inputscorebtn:
                intent = new Intent(AdminExamDetailActivity.this, AdminExamInputScoreActivity.class);
                Bundle bundle = new Bundle();
                mExamItem = new ExamItem();
                mExamItem.setIndex(1);
                bundle.putSerializable("ExamInfo", mExamItem);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.manager_exam_deletebtn:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminExamDetailActivity.this)
                        //设置对话框标题
                        .setTitle("确认要删除《\"+mExamItem.getName()+ \"》这门考试吗?")
                        //设置图标
                        .setIcon(R.drawable.icon_class_selector);
                        //.setMessage("请选择将要进行的操作");
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
            default:
                break;
        }
    }

    private void holdView(){

        mInputScore = (Button) findViewById(R.id.manager_exam_inputscorebtn);
        mDeleteExam = (Button) findViewById(R.id.manager_exam_deletebtn);
        mEditExam = (Button) findViewById(R.id.manager_exam_editbtn);
        mExamMenber = (Button) findViewById(R.id.manager_exam_menber);
        mExamIntroduction = (TextView) findViewById(R.id.exam_introduction);
        mExamInfo = (TextView) findViewById(R.id.exam_info);
        mCourseName = (TextView) findViewById(R.id.course_name);
    }

    private void setViewLogic(){
        mInputScore.setOnClickListener(this);
        mDeleteExam.setOnClickListener(this);
        mEditExam.setOnClickListener(this);
        mExamMenber.setOnClickListener(this);

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

    private void initViewData() {
        mCourseName.setText(mExamItemExamEdit.getName());
        mExamIntroduction.setText(mExamItemExamEdit.getDescription());
        mExamInfo.setText(R.string.activity_manager_add_exam_address + ": " + mExamItemExamEdit.getLocale() +
                "\n考试时间: " + mExamItemExamEdit.getStartTime() + "-" + mExamItemExamEdit.getEndTime() +
                "\n" + R.string.activity_manager_add_exam_credit + ": " + mExamItemExamEdit.getExamCredits());
    }


    void deleteData(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.add("exam_index", mExamItemExamEdit.getIndex() + "");

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
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
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            //mExamAdapter.notifyDataSetChanged();
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

}
