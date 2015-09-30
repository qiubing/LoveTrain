package cn.nubia.activity.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.Utils;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/11 15:32
 */
public class AboutUsActivity extends Activity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.about_us_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("关于我们");

        ImageView QRImage = (ImageView) findViewById(R.id.qr_download);

        //TODO:生成APP下载链接的二维码
        String downloadUrl = Constant.APP_DOWNLOAD_URL;
        if (!downloadUrl.equals("")){
            try {
                Bitmap qrBitmap = EncodingHandler.createQRCode(downloadUrl,500);
                QRImage.setImageBitmap(qrBitmap);
                String saveImage = "LoveTrain-V1.0" + ".jpg";
                Utils.saveBitmap(saveImage,qrBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }


    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
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

    public void back(View view) {
        this.finish();
    }
}
