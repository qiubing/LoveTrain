package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.nubia.activity.R;
import cn.nubia.entity.ExamItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.GestureDetectorManager;

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

共需如下四步：
* GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
  gestureDetector = new GestureDetector(this, gestureDetectorManager);

   @Override
    public boolean onTouchEvent(MotionEvent event) {

        return  gestureDetector.onTouchEvent(event);
    }

  gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
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
    private ImageView mImageView;
    private TextView mCourseName;
    private TextView mExamIntroduction;
    private TextView mExamInfo;
    private ExamItem mExamItem;

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_detail);

        mInputScore = (Button) findViewById(R.id.manager_exam_inputscorebtn);
        mDeleteExam = (Button) findViewById(R.id.manager_exam_deletebtn);
        mEditExam = (Button) findViewById(R.id.manager_exam_editbtn);
        mImageView = (ImageView) findViewById(R.id.manager_exam_detail_backImage);
        mExamIntroduction = (TextView) findViewById(R.id.exam_introduction);
        mExamInfo = (TextView) findViewById(R.id.exam_info);
        mCourseName = (TextView) findViewById(R.id.course_name);
        mImageView.setOnClickListener(this);
        mInputScore.setOnClickListener(this);
        mDeleteExam.setOnClickListener(this);
        mEditExam.setOnClickListener(this);

        mExamItem = (ExamItem) getIntent().getSerializableExtra("ExamInfo");
        mCourseName.setText(mExamItem.getName());
        mExamIntroduction.setText(mExamItem.getDescription());
        mExamInfo.setText(mExamItem.getLocale());

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
    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.manager_exam_detail_backImage:
                finish();
                break;
            case R.id.manager_exam_inputscorebtn:
                intent = new Intent(AdminExamDetailActivity.this, AdminExamInputScoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ExamInfo",mExamItem);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.manager_exam_deletebtn:
                new AlertDialog.Builder(AdminExamDetailActivity.this).setTitle("确认要删除《"+mExamItem.getName()+ "》这门考试吗?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogUtil.showToast(AdminExamDetailActivity.this, "你删除了《"+mExamItem.getName()+"》考试!");
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.manager_exam_editbtn:
                intent = new Intent(AdminExamDetailActivity.this, AdminEditExamActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
