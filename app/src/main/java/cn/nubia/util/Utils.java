package cn.nubia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: qiubing
 * @Date: 2015/9/6 9:07
 */
public class Utils {
    /**
     * 根据listView大小自动设置高度，用于解决listView与ScrollView冲突的问题
     * @param listView 需要设置的listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 保存文件到SD卡中
     * @param bitmap 需要保存的位图
     * @param fileName 文件名
     * @throws IOException
     */
    public static void saveFile(Bitmap bitmap,String fileName) throws IOException{
        File dirFile = new File(Constant.LOCAL_PATH);
        if (!dirFile.exists()){
            dirFile.mkdir();
        }

        File captureFile = new File(Constant.LOCAL_PATH + fileName);
        //如果文件已经存在，则替换
        if (captureFile.exists()){
            captureFile.delete();
        }
        try{
            captureFile.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(captureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60,bos);
        bos.flush();
        bos.close();
    }

    public static void saveBitmap(String fileName,Bitmap bitmap){
        File pictureDir = new File(Constant.BARCODE_PATH);
        if (!pictureDir.exists()){
            pictureDir.mkdir();
        }

        File bitmapFile = new File(Constant.BARCODE_PATH + fileName);
        //如果文件已经存在，则替换
        if (bitmapFile.exists()){
            bitmapFile.delete();
        }

        try{
            bitmapFile.createNewFile();
            FileOutputStream output = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从内存卡中读取文件
     * @param path 文件路径
     * @return
     */
    public static Bitmap getPictureFromSD(String path){
        try{
            //判断手机是否插入了SD卡,而且应用程序具有访问SD卡的权限
            if (Environment.getExternalStorageState().
                    equals(Environment.MEDIA_MOUNTED)){
                File mFile = new File(path);
                //文件是否存在
                if (mFile.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    return bitmap;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}