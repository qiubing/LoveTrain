package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
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
    private Button mGenerateQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lesson_detail);

        backImageView = (ImageView) findViewById(R.id.admin_lesson_detail_backImageView);
        alterLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_alterLessonButton);
        deleteLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_deleteLessonButton);
        signUpPopulationTextView = (TextView) findViewById(R.id.lesson_detail_signIn_textView);
        mGenerateQRCode = (Button) findViewById(R.id.lesson_QRCodeImage_generate_button);

        /**设置监听事件**/
        backImageView.setOnClickListener(this);
        alterLessonBtn.setOnClickListener(this);
        deleteLessonBtn.setOnClickListener(this);


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_lesson_detail_backImageView:
                Intent intentBackImage = new Intent(AdminLessonDetailActivity.this, AdminMainActivity.class);
                startActivity(intentBackImage);
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

            case R.id.lesson_QRCodeImage_generate_button:
                /**
                 * 生成二维码，edit by qiubing
                 */
                //TODO:生成具有课程和讲师信息的二维码
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
        }

    }
}
