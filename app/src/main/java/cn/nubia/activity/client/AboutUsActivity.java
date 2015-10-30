package cn.nubia.activity.client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import cn.nubia.upgrade.api.NubiaUpdateConfiguration;
import cn.nubia.upgrade.api.NubiaUpgradeManager;
import cn.nubia.upgrade.http.IGetVersionListener;
import cn.nubia.upgrade.model.VersionData;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.Utils;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/11 15:32
 */
public class AboutUsActivity extends Activity {
    private static final String TAG = "AboutUsActivity";
    private GestureDetector gestureDetector;
    private String mDownloadUrl;
    private String mVersionInfo;
    private static VersionData mVersionData;//返回给应用的使用的版本信息
    private static final String AUTH_ID = "OHxuZVn30b99e477";
    private static final String AUTH_KEY = "025df7336bd7fe24";
    private static final String QRCODE_SAVE_NAME ="IShare_Download_QR_Code.jpg";
    private static final String QRCODE_SAVE_PATH = Constant.BARCODE_PATH + QRCODE_SAVE_NAME;

    private ImageView QRImage;
    private TextView  versionText;

    private static NubiaUpgradeManager mUpgradeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.about_us_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("关于我们");

        QRImage = (ImageView) findViewById(R.id.qr_download);
        versionText = (TextView) findViewById(R.id.about_us_version);

        //TODO:生成APP下载链接的二维码
        getVersionData(this);

        /*if (mVersionData != null){
            mDownloadUrl = mVersionData.getApkUrl();
            mVersionInfo = mVersionData.getVersion();
        }
        if (!mDownloadUrl.equals("")){
            try {
                Bitmap qrBitmap = EncodingHandler.createQRCode(mDownloadUrl, 500);
                QRImage.setImageBitmap(qrBitmap);
                String saveImage = "IShare_Download_QR_Code" + ".jpg";
                Utils.saveBitmap(saveImage, qrBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        versionText.setText("爱分享 V" + mVersionInfo);*/
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

    /**
     *
     * @return 返回服务器中的App版本信息，包括软件版本号以及软件下载链接
     */
    public void getVersionData(Context context){
        if (null == mUpgradeManager) {
            mUpgradeManager = NubiaUpgradeManager.getInstance(context, AUTH_ID, AUTH_KEY);
        }
        //set configuration
        mUpgradeManager.debug(true);
        NubiaUpdateConfiguration.Builder builder = new NubiaUpdateConfiguration.Builder();
        builder.setShowNotification(true);
        builder.setAppName(context.getResources().getString(R.string.app_name));
        builder.setIcon(R.mipmap.evaluate_icon);
        mUpgradeManager.setConfiguration(NubiaUpdateConfiguration.Builder
                .getConfiguration(builder));

        //get Version data
        mUpgradeManager.getVersion(context, new IGetVersionListener() {
            @Override
            public void onGetNewVersion(VersionData versionData) {
                Log.e(TAG, "onGetNewVersion" + " versionData: downloadUrl " + versionData.getApkUrl()
                        + "version: " + versionData.getVersion());
                //生成二维码信息
                mVersionData = versionData;
                if (mVersionData != null){
                    mDownloadUrl = mVersionData.getApkUrl();
                    mVersionInfo = mVersionData.getVersion();
                }
                if (!mDownloadUrl.equals("")){
                    try {
                        Bitmap qrBitmap = EncodingHandler.createQRCode(mDownloadUrl, 400);
                        QRImage.setImageBitmap(qrBitmap);
                        //String saveImage = "IShare_Download_QR_Code" + ".jpg";
                        Utils.saveBitmap(QRCODE_SAVE_NAME, qrBitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                versionText.setText("爱分享 V" + mVersionInfo);

            }

            @Override
            public void onGetNoVersion() {
                Log.e(TAG, "onGetNoVersion()");
            }

            @Override
            public void onError(int i) {
                Log.e(TAG, "onError()" + i);
                //网络连接失败，则用本地保存的二维码下载图片
                Bitmap qrBitmap = Utils.getPictureFromSD(QRCODE_SAVE_PATH);
                if (qrBitmap != null){
                    Log.e(TAG, "setImageBitmap");
                    QRImage.setImageBitmap(qrBitmap);
                }
            }
        });
    }
}
