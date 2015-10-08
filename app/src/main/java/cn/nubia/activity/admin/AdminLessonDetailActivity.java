package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.zxing.WriterException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientEvaluateActivity;
import cn.nubia.activity.client.ClientMyCourseJudgeDetailFillActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminLessonDetailActivity extends Activity implements View.OnClickListener {

    private String  status = "";
    /**
     * 从前一个页面传过来的LessonItem对象
     */
    private LessonItem lessonItem;
    private Bundle signUpBundle;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    private GestureDetector gestureDetector;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lesson_detail);
        signUpBundle = new Bundle();
        /**获取控件**/
        Button alterLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_alterLessonButton);
        Button deleteLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_deleteLessonButton);
        TextView signUpPopulationTextView = (TextView) findViewById(R.id.lesson_detail_signIn_textView);
        TextView mGenerateQRCode = (TextView) findViewById(R.id.backupButton);
        Button mEvaluateTextView = (Button) findViewById(R.id.evaluateTextView);
        TextView sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        sub_page_title.setText("课时管理");
        /**获取相关的TextView*/
        TextView lessonNameTextView = (TextView) findViewById(R.id.lesson_detail_realName_textView);
        TextView lessDescTextView = (TextView) findViewById(R.id.lesson_detail_realDesc_textView);
        TextView lessonInfoTextView = (TextView) findViewById(R.id.lesson_detail_lessonInfo_textView);
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
        Intent intent = getIntent();
        lessonItem = (LessonItem) intent.getSerializableExtra("LessonItem");
        String startActivity = intent.getStringExtra("startActivity");

        sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        sub_page_title.setText(lessonItem.getName());
        signUpPopulationTextView.setText(lessonItem.getCheckUsers()+"人签到");

        String teacherID = lessonItem.getTeacherID();
        String myID = Constant.user.getUserID();
        if (myID.equals(teacherID)) {
            status = "teacher";
        } else {
            status = "student";
        }

        switch (startActivity) {
            case "cn.nubia.activity.admin.AdminCourseAddTabActivity":
                mGenerateQRCode.setVisibility(View.VISIBLE);
                alterLessonBtn.setOnClickListener(this);
                deleteLessonBtn.setOnClickListener(this);
                signUpPopulationTextView.setOnClickListener(this);
                mGenerateQRCode.setOnClickListener(this);
                mEvaluateTextView.setOnClickListener(this);
                break;
            case "cn.nubia.activity.client.ClientAllCourseActivity":
                alterLessonBtn.setVisibility(View.GONE);
                deleteLessonBtn.setVisibility(View.GONE);
                signUpPopulationTextView.setVisibility(View.GONE);
                mEvaluateTextView.setVisibility(View.GONE);
                break;
            case "cn.nubia.activity.client.ClientMyCourseActivity":
                if(status.equals("teacher")) {
                    mGenerateQRCode.setVisibility(View.VISIBLE);
//                alterLessonBtn.setOnClickListener(this);
//                deleteLessonBtn.setOnClickListener(this);
                    signUpPopulationTextView.setOnClickListener(this);
                    mGenerateQRCode.setOnClickListener(this);
                    mEvaluateTextView.setOnClickListener(this);
                    alterLessonBtn.setVisibility(View.GONE);
                    deleteLessonBtn.setVisibility(View.GONE);
                } else if(status.equals("student")){
                    mEvaluateTextView.setText("进行评价");
                    mEvaluateTextView.setOnClickListener(this);
                    alterLessonBtn.setVisibility(View.GONE);
                    deleteLessonBtn.setVisibility(View.GONE);
                    signUpPopulationTextView.setVisibility(View.GONE);
                }
                break;
            default:
                Log.i("huhu", "AdminLessonDetail  startActivity异常了");
                break;
        }



        if (lessonItem != null) {
            lessonNameTextView.setText(lessonItem.getName() == null ? "null" : lessonItem.getName());
            lessDescTextView.setText(lessonItem.getDescription() == null ? "null" : lessonItem.getDescription());
            lessonInfoTextView.setText("讲师：" + lessonItem.getTeacherName() +
                            "\n上课地址：" + lessonItem.getLocation() +
                            "\n上课时间：" + TimeFormatConversion.toDateTime(lessonItem.getStartTime()) +

                            "\n下课时间：" + TimeFormatConversion.toDateTime(lessonItem.getEndTime()) +
                            "\n讲师上课可得积分：" + lessonItem.getTeacherCredits() +
                            "\n学员签到可得积分：" + lessonItem.getCheckCredits()
            );
        }
        Log.e("MyTime", lessonItem.getEndTime() + "");

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
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_lesson_detail_alterLessonButton:
                Intent intentAlterLesson = new Intent(AdminLessonDetailActivity.this,
                        AdminAlterLessonActivity.class);
                Bundle alterBundle = new Bundle();
                alterBundle.putSerializable("LessonItem", lessonItem);
                intentAlterLesson.putExtras(alterBundle);
                startActivity(intentAlterLesson);
                break;
            case R.id.admin_lesson_detail_deleteLessonButton:
                final AlertDialog.Builder builderDelete = new AlertDialog.Builder(AdminLessonDetailActivity.this)
                        //设置对话框标题
                        .setTitle("删除课时")
                                //设置图标
                        .setIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                        .setMessage("确认要删除《" + lessonItem.getName() + "》这门课时吗?");
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
            case R.id.lesson_detail_signIn_textView:
                Intent intentSignInInfo = new Intent();
                intentSignInInfo.setClass(AdminLessonDetailActivity.this, AdminSignInLessonPersonInfoActivity.class);
                signUpBundle.putSerializable("LessonItem", lessonItem);
                intentSignInInfo.putExtras(signUpBundle);
                startActivity(intentSignInInfo);
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了查看签到人员信息", Toast.LENGTH_LONG).show();
                break;

            case R.id.backupButton:
                /**
                 * 生成二维码，edit by qiubing
                 */
                //TODO:生成具有课程和讲师信息的二维码
                //获取要生成课程的ID索引
                Toast.makeText(this, "二维码生成中", Toast.LENGTH_SHORT).show();
                //二维码的名称
                String contentString = String.valueOf(lessonItem.getIndex());
                if (!contentString.equals("")) {
                    //获取需要插入的头像logo
                    //Bitmap logo = Utils.getPictureFromSD(Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT);
                    Bitmap qrCodeBitmap = null;
                    try {
                        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                        //qrCodeBitmap = EncodingHandler.createQRImage(contentString, 350, 350, logo);
                        qrCodeBitmap = EncodingHandler.createQRCode(contentString,350);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    final String barCodeName = lessonItem.getTeacherName() + "-" + lessonItem.getName()
                            + "-" + String.valueOf(lessonItem.getIndex()) + ".jpg";
                    final Bitmap bitmap = qrCodeBitmap;
                    ImageView image = new ImageView(this);
                    image.setMaxHeight(350);
                    image.setMaxWidth(350);
                    image.setImageBitmap(qrCodeBitmap);

                    //弹框显示二维码图片
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this).
                            setTitle("保存二维码图片到本地").setView(image);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //保存二维码图片到SD卡中
                            Utils.saveBitmap(barCodeName, bitmap);
                            Toast.makeText(AdminLessonDetailActivity.this,
                                    "二维码保存在/MyDownloader/barcode目录下", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(this, "Lesson id can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.evaluateTextView:
                /**课时尚未开始和结束不能进行评价*/
                if(lessonItem.getStartTime()>System.currentTimeMillis()){
                    Toast.makeText(AdminLessonDetailActivity.this, "该课时尚未开始，不能评价！", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(lessonItem.getStartTime()<System.currentTimeMillis() && lessonItem.getEndTime()>System.currentTimeMillis()){
                    Toast.makeText(AdminLessonDetailActivity.this, "该课时尚未结束，不能评价！", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent ;
                if(Constant.IS_ADMIN == true || status.equals("teacher")) {
                    intent = new Intent(this, ClientEvaluateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("lession_index_ID", lessonItem.getIndex() + "," + lessonItem.getTeacherID());
                    intent.putExtras(bundle);
                } else {
                    intent = new Intent(this, ClientMyCourseJudgeDetailFillActivity.class);
                    Log.e("jiangyu",String.valueOf(lessonItem.isIsJudged()));
                    intent.putExtra("lessonIndex", lessonItem.getIndex());
                    if(lessonItem.isIsJudged()){
                        ((Button)findViewById(R.id.evaluateTextView)).setText("查看评价");
                        intent.putExtra("operate",CommunicateService.OperateType.QUERY);
                    }else{
                        ((Button)findViewById(R.id.evaluateTextView)).setText("进行评价");
                        intent.putExtra("operate",CommunicateService.OperateType.INSERT);
                    }

                }

                /*if (Constant.IS_ADMIN == true || status.equals("teacher")) {
                    intent = new Intent(this, ClientEvaluateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("lession_index_ID", lessonItem.getIndex() + "," + lessonItem.getTeacherID());
                    intent.putExtras(bundle);
                } else {
                    intent = new Intent(this, ClientMyCourseJudgeDetailFillActivity.class);
                    intent.putExtra("lessonIndex", 0);
                }*/
                startActivity(intent);
                break;
        }
    }

    private void deleteData() {
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
        requestParams.put("lesson_index", lessonItem.getIndex());
        String deleteUrl = Constant.BASE_URL + "course/del_lesson.do";
        AsyncHttpHelper.post(deleteUrl, requestParams, myJsonHttpResponseHandler);
    }

    private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                if (code == 0 && isOk) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LessonItem",lessonItem);
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                    Toast.makeText(AdminLessonDetailActivity.this, "删除课时成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminLessonDetailActivity.this, "删除课时失败", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AdminLessonDetailActivity.this, "删除课时失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(AdminLessonDetailActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
            loadingView.clearAnimation();
            loadingView.setVisibility(View.GONE);
        }
    };

    public void back(View view) {
        this.finish();
    }

}
