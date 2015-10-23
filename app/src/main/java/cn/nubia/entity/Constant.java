package cn.nubia.entity;

import android.os.Environment;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 类说明 一些常量都放在这里
 */
public class Constant {
    /*胡立加*/
    public static final int INTERVAL = 2000;


    public static final String BASE_URL = "http://love-train-dev.nubia.cn/";
    public static UserInfo user = new UserInfo();//登录成功后保存用户信息

    /****
     * 网络类型
     ****/
    public static final int LOADING_SUCCESS = 0;
    public static final int LOADING_FAILED = 1;
    public static final int NETWORK_UNUSABLE = 2;
    public static final boolean SHOW_HEADER = true;
    public static final boolean SHOW_FOOTER = false;

    public static final String COURCE = "CN.NUBIA.COURCE";
    public static final String EXAM = "CN.NUBIA.EXAM";
    public static final String SHARE_WAITE = "CN.NUBIA.SHARE.WAITE";
    public static final String SHARE_OK = "CN.NUBIA.SHARE.OK";
    public static final String MYCOURCE_TEACHER = "CN.NUBIA.MYCOURCE.TEACHER";
    public static final String MYCOURCE_STUDENT = "CN.NUBIA.MYCOURCE.STUDENT";
    public static final String ALLCOURCE = "CN.NUBIA.ALLCOURCE";

    public static final String IS_FIRST_RUN = "isFirstRun";
    /**
     * 设备id
     */
    public static Long systemTime = 0L;
    public static Long loginTime = 0L;
    public static String devideID = null;
    public static String apkVersion = null;
    public static String tokenKep = "sfdgfjh";
    public static boolean IS_ADMIN = false;

    /**
     * 必要的四个参数
     * return
     */
    public static Map<String, String> getRequestParams() {
        Map<String, String> map = new HashMap<>();
        map.put("device_id", devideID);
        map.put("apk_version", apkVersion);
        map.put("token_key", tokenKep);
        map.put("request_time", String.valueOf(getRequestTime()));
        return map;
    }

    private static Long getRequestTime() {
        return (systemTime + System.currentTimeMillis() - loginTime);
    }

    /**
     * 登录的用户是否是管理员
     */
    //public static String USER_ID = "";
    /**
     * 记录用户ID
     */

    /**
     * 下载文件保存的本地路径
     */
    public static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyDownloader" + File.separator;

    /**
     * 二维码保存的本地文件夹
     */
    public static final String BARCODE_PATH = LOCAL_PATH + "barcode" + File.separator;

    /**
     * 头像图片文件名
     */
    public static final String PORTRAIT = "LOVE_TRAIN_PORTAIT.jpg";

    /**
     * 服务器图片地址前缀
     */
    public static final String PICTURE_PREFIX = "http://love-train-dev.nubia.cn/res/";

    /**
     * APP下载地址
     */
    public static final String APP_DOWNLOAD_URL = "http://www.pgyer.com/lovetrain";
}
