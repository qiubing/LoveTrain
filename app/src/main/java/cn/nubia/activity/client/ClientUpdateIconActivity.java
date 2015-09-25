package cn.nubia.activity.client;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.nubia.activity.R;
import cn.nubia.component.CircleImageView;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Description: 更新图像的类
 * Author: qiubing
 * Date: 2015/9/6 20:23
 */
public class ClientUpdateIconActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "UpdateIconActivity";

    private CircleImageView mCircleImageView;
    private Button mEditButton;
    private Button mUpLoadButton;
    private Bitmap mPhoto;
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int RETURN_PHOTO_CODE = 3;
    private static Uri photoUri;// 照相之后的数据


    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_photo);
        findViews();
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        mUpLoadButton.setOnClickListener(this);
        mEditButton.setOnClickListener(this);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    public void back(View view) {

        this.finish();
    }

    private void findViews() {
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_check_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("头像设置");
        mCircleImageView = (CircleImageView) findViewById(R.id.image_view);
        mEditButton = (Button) findViewById(R.id.btn_edit_head_portrait);
        mUpLoadButton = (Button) findViewById(R.id.btn_upload);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_head_portrait:
                new PopupWindows(ClientUpdateIconActivity.this, mCircleImageView);
                break;
            case R.id.btn_upload:
                //TODO:上传图像到服务器
                if (mPhoto != null){
                    uploadFile(mPhoto);
                }else{
                    Toast.makeText(this,"头像不能为空",Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    /**
     * 上传文件到服务器
     */
    private void uploadFile(Bitmap bitmap) {
        try {
            String path = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
            File file = new File(path);
            if (file.exists() && file.length() > 0) {
                //上传地址以及请求参数
                Log.e("UpdateIconActivity","file: " + file.toString() +", user_id: " + Constant.user.getUserID());
                String url = Constant.BASE_URL + "user/icon_set.do";
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("icon_type","jpg");
                params.put("icon", file);
                params.put("user_id", Constant.user.getUserID());
                AsyncHttpHelper.post(url, params, mUpdateIconHandler);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final MyJsonHttpResponseHandler mUpdateIconHandler = new MyJsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) throws JSONException {
            super.onSuccess(statusCode, headers, response);
            Log.e("UpdateIconActivity", "onSuccess" + response.toString());
            if (response.getString("code").equals("0") && response.getInt("data") == 0) {
                showShortToast("长传头像成功");
            } else {
                showShortToast("长传头像失败");
            }
            ClientUpdateIconActivity.this.finish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            showShortToast("长传头像失败,请稍后重试");
        }
    };


    /**
     * 处理头像的内部类
     */
    public class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent) {
            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.pop_up_in));
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    takePhoto();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    getPhoto();
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

    }

    /**
     * 系统相机拍照
     *
     * return
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            photoUri = this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new ContentValues());
            if (photoUri != null) {
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                Log.v(TAG, photoUri.toString());
                Log.e("takePhoto", "takePhoto");
            } else {
                Toast.makeText(this, "发生意外，无法写入相册", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(this, "发生意外，无法写入相册", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 从相册取照片
     */
    private void getPhoto() {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
    }

    /**
     * 回调函数处理
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != FragmentActivity.RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    Log.v(TAG, data.getData().toString());
                    break;
                case CAMERA_REQUEST_CODE:
                    Log.e("CAMERA_REQUEST_CODE", "CAMERA_REQUEST_CODE");
                    startPhotoZoom(photoUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        try {
                            getImageToView(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /*返回照片给上一级显示*/
                        ClientUpdateIconActivity.this.setResult(RETURN_PHOTO_CODE, data);
                    }
                    break;
            }
        }
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * param
     */
    @SuppressWarnings("deprecation")
    private void getImageToView(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mPhoto = extras.getParcelable("data");
            //保存文件到SD卡中
            Utils.saveFile(mPhoto, Constant.user.getUserID() + Constant.PORTRAIT);
            Drawable drawable = new BitmapDrawable(mPhoto);
            mCircleImageView.setImageDrawable(drawable);

            // 上传头像到服务器上去
            //String imageStrData = "";
            // bitmap 转换 String
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            //byte[] b = stream.toByteArray();
            // 将图片流以字符串形式存储下来
            //imageStrData = new String(Base64Coder.encodeLines(b));
            //uploadImage(imageStrData);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

}
