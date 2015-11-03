package cn.nubia.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.loopj.android.http.RequestParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nubia.entity.Constant;

/**
 * Author: qiubing
 * Date: 2015/9/6 9:07
 */
public class Utils {
    /**
     * 根据listView大小自动设置高度，用于解决listView与ScrollView冲突的问题
     *
     * @param listView 需要设置的listView
     */
    /*public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            Log.e("qiubing", listItem == null ? "kong" : "feikong");
            if (null == listItem)
                continue;
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }*/

    /**
     * 保存文件到SD卡中
     *
     * @param bitmap   需要保存的位图
     * @param fileName 文件名
     * @throws IOException
     */
    public static void saveFile(Bitmap bitmap, String fileName) throws IOException {
        if(bitmap == null)
            return;
        File dirFile = new File(Constant.LOCAL_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File captureFile = new File(Constant.LOCAL_PATH + fileName);
        //如果文件已经存在，则替换
        if (captureFile.exists()) {
            captureFile.delete();
        }
        try {
            captureFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(captureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        bos.flush();
        bos.close();
    }

    public static void saveBitmap(String fileName, Bitmap bitmap) {
        if (bitmap == null)
            return;
        File pictureDir = new File(Constant.BARCODE_PATH);
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }

        File bitmapFile = new File(Constant.BARCODE_PATH + fileName);
        //如果文件已经存在，则替换
        if (bitmapFile.exists()) {
            bitmapFile.delete();
        }

        try {
            bitmapFile.createNewFile();
            FileOutputStream output = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从内存卡中读取文件
     * param path 文件路径
     * return
     */
    public static Bitmap getPictureFromSD(String path) {
        try {
            //判断手机是否插入了SD卡,而且应用程序具有访问SD卡的权限
            if (Environment.getExternalStorageState().
                    equals(Environment.MEDIA_MOUNTED)) {
                File mFile = new File(path);
                //文件是否存在
                if (mFile.exists()) {
                    return BitmapFactory.decodeFile(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取屏幕分辨率宽
     */

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return dm.widthPixels;
    }

    /**
     * 封装Map参数到HTTP请求参数中
     * param param
     * return
     */
    public static RequestParams toParams(HashMap<String, String> param) {
        param.put("device_id", "87654321");
        param.put("request_time", "1444444444444");
        param.put("apk_version", "1.0");
        param.put("token_key", "123456789");
        return new RequestParams(param);
    }

    /**
     * 将从服务器返回的user_icon String 转换成图片网络地址
     * @param urlString 需要转换的url string
     * @return 一个String对象
     */
    public static String parseUrlStringFromServer(String urlString) {
        if (urlString != null){
            Pattern pattern = Pattern.compile("\\\\+");
            Matcher matcher = pattern.matcher(urlString);
            return matcher.replaceAll("");
        }
        return "";
    }

}
