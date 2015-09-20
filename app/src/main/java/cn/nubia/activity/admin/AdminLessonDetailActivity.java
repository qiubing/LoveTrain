package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientEvaluateActivity;
import cn.nubia.activity.client.ClientMyCourseJudgeDetailFillActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.Utils;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminLessonDetailActivity extends Activity implements View.OnClickListener {

    private ImageView backImageView;
    private Button alterLessonBtn;
    private Button deleteLessonBtn;
    private TextView signUpPopulationTextView;
    private TextView mGenerateQRCode;
    private Button mEvaluateTextView;
    private String status = "teacher";
    private TextView sub_page_title;

    private TextView lessonNameTextView;
    private TextView lessDescTextView;
    private TextView lessonInfoTextView;
    private TextView signInPopulationTextView;

    /**从前一个页面传过来的LessonItem对象*/
    LessonItem lessonItem;

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lesson_detail);


        /**获取控件**/
        backImageView = (ImageView) findViewById(R.id.backButton);
        alterLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_alterLessonButton);
        deleteLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_deleteLessonButton);
        signUpPopulationTextView = (TextView) findViewById(R.id.lesson_detail_signIn_textView);
        mGenerateQRCode = (TextView) findViewById(R.id.backupButton);
        mEvaluateTextView = (Button) findViewById(R.id.evaluateTextView);
        sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        sub_page_title.setText("课时管理");
        /**获取相关的TextView*/
        lessonNameTextView=(TextView)findViewById(R.id.lesson_detail_realName_textView);
        lessDescTextView=(TextView)findViewById(R.id.lesson_detail_realDesc_textView);
        lessonInfoTextView=(TextView)findViewById(R.id.lesson_detail_lessonInfo_textView);
        signInPopulationTextView=(TextView)findViewById(R.id.lesson_detail_signIn_textView);

        /**获取启动该Activity的Intent*/
        Intent intent=getIntent();
        lessonItem=(LessonItem)intent.getSerializableExtra("LessonItem");
        if(lessonItem!=null) {
            lessonNameTextView.setText(lessonItem.getName() == null ? "null" : lessonItem.getName());
            lessDescTextView.setText(lessonItem.getDescription() == null ? "null" : lessonItem.getDescription());
            lessonInfoTextView.setText(lessonItem.getTeacherName()+lessonItem.getLocation()+lessonItem.getStartTime());
            /**签到人数怎么获得？*/
//            signInPopulationTextView.setText(lessonItem.getIndex());
        }


        /**设置监听事件**/
        mGenerateQRCode.setVisibility(View.VISIBLE);
        backImageView.setOnClickListener(this);
        alterLessonBtn.setOnClickListener(this);
        deleteLessonBtn.setOnClickListener(this);
        signUpPopulationTextView.setOnClickListener(this);
        mGenerateQRCode.setOnClickListener(this);
        mEvaluateTextView.setOnClickListener(this);


        /**非管理员隐去修改课时和删除课时按钮**/
        if(Constant.IS_ADMIN==false){
            alterLessonBtn.setVisibility(View.GONE);
            deleteLessonBtn.setVisibility(View.GONE);
            mGenerateQRCode.setVisibility(View.GONE);
        }
        /**只有管理员界面才可以查看签到人数的详细信息和生成二维码*/
        else{
            signUpPopulationTextView.setOnClickListener(this);
            mGenerateQRCode.setOnClickListener(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        if(intent.getStringExtra("status") != null) {
            status = intent.getStringExtra("status");
        }
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Intent intent = getIntent();
        if(intent.getStringExtra("status") != null) {
            status = intent.getStringExtra("status");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                //huhu
                /*Intent intentBackImage = new Intent(AdminLessonDetailActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);*/
                finish();
                break;
            case R.id.admin_lesson_detail_alterLessonButton:
                Intent intentAlterLesson = new Intent(AdminLessonDetailActivity.this, AdminAlterLessonActivity.class);
                startActivity(intentAlterLesson);
                finish();
                break;
            case R.id.admin_lesson_detail_deleteLessonButton:
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了删除课时", Toast.LENGTH_LONG).show();
                Intent intentDeleteLesson = new Intent(AdminLessonDetailActivity.this, AdminMainActivity.class);
                startActivity(intentDeleteLesson);
                break;
            case R.id.lesson_detail_signIn_textView:
                Intent intentSignInInfo = new Intent(AdminLessonDetailActivity.this, AdminSignInExamPersonInfoActivity.class);
                startActivity(intentSignInInfo);
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了查看签到人员信息", Toast.LENGTH_LONG).show();
                break;

            case R.id.backupButton:
                /**
                 * 生成二维码，edit by qiubing
                 */
                //TODO:生成具有课程和讲师信息的二维码
                Toast.makeText(this,"二维码生成中",Toast.LENGTH_SHORT).show();
                String contentString = "nubia";
                if (!contentString.equals("")) {
                    //获取需要插入的头像logo
                    Bitmap logo = Utils.getPictureFromSD(Constant.LOCAL_PATH + Constant.PORTRAIT);
                    Bitmap qrCodeBitmap = null;
                    try {
                        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                        //Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
                        qrCodeBitmap = EncodingHandler.createQRImage(contentString, 350, 350, logo);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    //mShowBarCode.setImageBitmap(qrCodeBitmap);
                    String barCodeName = contentString + ".jpg";
                    //保存二维码图片到SD卡中
                    Utils.saveBitmap(barCodeName, qrCodeBitmap);
                } else {
                    Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.evaluateTextView:
                Intent intent = null;
                if(status.equals("teacher")) {
                    intent = new Intent(this, ClientEvaluateActivity.class);
                } else {
                    intent = new Intent(this, ClientMyCourseJudgeDetailFillActivity.class);
                    intent.putExtra("lessonIndex",0);
                }
                startActivity(intent);
                break;

        }

    }


}
