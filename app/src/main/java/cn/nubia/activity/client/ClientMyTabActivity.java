package cn.nubia.activity.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.nubia.activity.LoginActivity1;
import cn.nubia.activity.R;
import cn.nubia.component.CircleImageView;
import cn.nubia.component.PromptDialog;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.ProcessSPData;
import cn.nubia.util.Utils;



/**
 * Description:与我相关的
 * Author: qiubing
 * Date: 2015/9/6 19:28
 */

public class ClientMyTabActivity extends Activity implements OnClickListener {
    private static final String TAG = "UserSetting";
    private static final int GET_PHOTO_CODE = 1;
    private static final String UPDATE_INTENT_ACTON = "cn.nubia.appUpdate.newVersion";
    private CircleImageView mCircleImageView;
    private ImageView mCheckRecord;
    private ImageView mCourseIntergration;
    private ImageView mExamScore;
    private ImageView mCourseShare;
    //    private ImageView mIWantShare;
    private ImageView mPasswordChange;
    //    private ImageView mAboutMe;
    private ImageView mVersion;
    private ImageView mFanKui;
    private ImageView mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_setting);
        initViews();
        initEvents();
    }

    private void initViews() {
        TextView myUserName = (TextView) findViewById(R.id.user_name);
        myUserName.setText(Constant.user.getUserName());

        mCircleImageView = (CircleImageView) findViewById(R.id.client_my_head_imageView);

        mCheckRecord = (ImageView) findViewById(R.id.checkIn_forward);
        mCourseIntergration = (ImageView) findViewById(R.id.points_forward);
        mExamScore = (ImageView) findViewById(R.id.examScores_forward);
        mCourseShare = (ImageView) findViewById(R.id.shareCourses_forward);
//        mIWantShare = (ImageView) findViewById(R.id.i_want_share);
        mPasswordChange = (ImageView) findViewById(R.id.passwordChange_forward);
        mVersion = (ImageView) findViewById(R.id.checkUpdate_forward);
//        mAboutMe = (ImageView) findViewById(R.id.btn_about);
        mFanKui = (ImageView) findViewById(R.id.feedback_forward);
        mLogoutButton = (ImageView) findViewById(R.id.logout_forward);
    }

    private void initEvents() {
        mCircleImageView.setOnClickListener(this);
        mCheckRecord.setOnClickListener(this);
        mCourseIntergration.setOnClickListener(this);
        mCourseShare.setOnClickListener(this);
//        mIWantShare.setOnClickListener(this);
        mExamScore.setOnClickListener(this);
        mPasswordChange.setOnClickListener(this);
//        mAboutMe.setOnClickListener(this);
        mFanKui.setOnClickListener(this);
        mVersion.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon1:
                Intent intent = new Intent(this, ClientUpdateIconActivity.class);
                startActivityForResult(intent, GET_PHOTO_CODE);
                break;
            case R.id.checkIn_forward:
                myStartActivity(ClientMyCheckRecordActivity.class);
                break;
            case R.id.points_forward:
                myStartActivity(ClientCourseIntegrationRecordActivity.class);
                break;
            case R.id.examScores_forward:
                myStartActivity(ClientExamScoreActivity.class);
                break;
            case R.id.shareCourses_forward:
                myStartActivity(ClientShareCourseActivity.class);
                break;
//            case R.id.i_want_share:
//                Intent intent1 = new Intent(this,ClientMyShareCourseDetailFillActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("type","insert");
//                intent1.putExtras(bundle);
//                startActivity(intent1);
//                break;
            case R.id.passwordChange_forward:
                myStartActivity(ClientMyAccountmanaPswmodifyActivity.class);
                break;
//            case R.id.btn_about:
//                myStartActivity(AboutUsActivity.class);
//                break;

            case R.id.checkUpdate_forward:
                Log.e(TAG, "update version");
                Intent service = new Intent(UPDATE_INTENT_ACTON);
                service.putExtra("command", "update");
                this.getApplicationContext().startService(service);
                break;
            case R.id.feedback_forward:
                myStartActivity(FeedBackActivity.class);
                break;
//            case R.id.btn_version:
//                Log.e(TAG,"update version");
//                Intent service = new Intent(UPDATE_INTENT_ACTON);
//                service.putExtra("command","update");
//                this.getApplicationContext().startService(service);
//                //UpdateManager manager = new UpdateManager(ClientMyTabActivity.this);
//                //检查更新
//                //manager.checkUpdate();
//                //showUpdatDialog();
//            break;
            case R.id.logout_forward:
                //注销登录
                Intent logoutIntent = new Intent(this,
                        LoginActivity1.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                ProcessSPData.clearSP(ClientMyTabActivity.this);//清除缓存的数据
                startActivity(logoutIntent);
                break;
        }
    }


    /**
     * 更新对话框
     */
    private void showUpdatDialog() {
        new PromptDialog.Builder(ClientMyTabActivity.this)
                .setMessage("最新版本：1.1\n最新版本已下载，是否安装？\n更新内容\n这只是一个演示\n学习一下也不错",
                        null).setTitle("发现新版本")
                .setButton1("立即更新", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        Toast.makeText(ClientMyTabActivity.this, "只是一个演示而已", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }).setButton2("以后再说", new PromptDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 自定义的startActivity
     *
     * @param cls 需要启动的Activity类
     */
    private void myStartActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PHOTO_CODE && resultCode == 3) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(photo);
                mCircleImageView.setImageDrawable(drawable);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //头像图片存储路径
        /**
         * 先判断本地是否存储了头像，如果本地存储了头像，则使用本地头像；否则从服务器中加载头像
         */
        String localPath = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
        Bitmap bitmap = Utils.getPictureFromSD(localPath);
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            mCircleImageView = (CircleImageView) findViewById(R.id.client_my_head_imageView);
            mCircleImageView.setImageDrawable(drawable);
        } else {//从服务器中加载
            String remotePath = Constant.PICTURE_PREFIX +
                    Utils.parseUrlStringFromServer(Constant.user.getUserIconURL());
            Log.e(TAG, "remotePath: " + remotePath);
            //从服务器加载图片，传递url地址过去
            AsyncHttpHelper.get(remotePath, mPictureHandler);
        }
    }

    private final AsyncHttpResponseHandler mPictureHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
            Log.e(TAG, "onSuccess: 加载成功");
            InputStream input = new ByteArrayInputStream(bytes);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            Drawable drawable = new BitmapDrawable(bitmap);
            mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
            mCircleImageView.setImageDrawable(drawable);
            //同时将图片保存到本地，用来下次加载
            try {
                Utils.saveFile(bitmap, Constant.user.getUserID() + Constant.PORTRAIT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.e(TAG, "onFailure: 加载失败");
        }
    };
}
