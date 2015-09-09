package cn.nubia.activity.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import cn.nubia.activity.BaseActivity;
import cn.nubia.activity.R;
import cn.nubia.util.Constant;
import cn.nubia.util.Utils;
import cn.nubia.zxing.barcode.CaptureActivity;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * @Description:与我相关的
 * @Author: qiubing
 * @Date: 2015/9/6 19:28
 */

public class MyClientActivity_1 extends BaseActivity implements OnClickListener {
    private ImageView mArrow1;
    private ImageView mArrow2;
    private ImageView mArrow3;
    private ImageView mArrow4;
    private ImageView mArrow5;
    private ImageView mArrow6;

    private Button mScanBarCode;
    private Button mGenerateBarCode;
    private TextView mScanResult;
    private ImageView mShowBarCode;


    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_user_my_setting);
        findViews();
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        mArrow1.setOnClickListener(this);
        mArrow2.setOnClickListener(this);
        mArrow3.setOnClickListener(this);
        mArrow4.setOnClickListener(this);
        mArrow5.setOnClickListener(this);
        mArrow6.setOnClickListener(this);
        mScanBarCode.setOnClickListener(this);
        mGenerateBarCode.setOnClickListener(this);
    }

    private void findViews() {
        mArrow1 = (ImageView) findViewById(R.id.arrow_image_1);
        mArrow2 = (ImageView) findViewById(R.id.arrow_image_2);
        mArrow3 = (ImageView) findViewById(R.id.arrow_image_3);
        mArrow4 = (ImageView) findViewById(R.id.arrow_image_4);
        mArrow5 = (ImageView) findViewById(R.id.arrow_image_5);
        mArrow6 = (ImageView) findViewById(R.id.arrow_image_6);
        mScanBarCode = (Button) findViewById(R.id.btn_check_in);
        mScanResult = (TextView) findViewById(R.id.check_in_result);
        mGenerateBarCode = (Button) findViewById(R.id.btn_genrate_barcode);
        mShowBarCode = (ImageView) findViewById(R.id.iv_qr_image_show);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    public void back(View view) {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_image_1:
                startActivity(MyCheckRecordActivity.class);
                break;
            case R.id.arrow_image_2:
                startActivity(CourseIntegrationRecordActivity.class);
                break;
            case R.id.arrow_image_3:
                startActivity(ExamScoreActivity.class);
                break;
            case R.id.arrow_image_4:
                startActivity(ShareCourseActivity.class);
                break;
            case R.id.arrow_image_5:
                startActivity(MyShareCourseDetailFillActivity.class);
                break;
            case R.id.arrow_image_6:
                startActivity(AccountManagementActivity.class);
                break;
            case R.id.btn_check_in:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(MyClientActivity_1.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.btn_genrate_barcode:
                //TODO:生成具有课程和讲师信息的二维码

                String contentString = "hello_nubia";
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
                    mShowBarCode.setImageBitmap(qrCodeBitmap);
                    String barCodeName = contentString + ".jpg";
                    //保存二维码图片到SD卡中
                    Utils.saveBitmap(barCodeName, qrCodeBitmap);
                } else {
                    Toast.makeText(MyClientActivity_1.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            mScanResult.setText(scanResult);
        }
    }
}
