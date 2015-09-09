package cn.nubia.util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存一些常量数据
 */
public class SpUtil {
    private static SharedPreferences sp;

    private static final String KEY_SETTINGS = "USER_PARAMETER";

    /*private static void initSharedPreferences(Context context) {
        sp = context.getSharedPreferences(KEY_SETTINGS, Context.MODE_PRIVATE);
    }*/

    //胡立加，创建SharedPreferences对象是单例模式吗？如果不是上述用法有问题，修改为单例模式
    private static void initSharedPreferences(Context context) {
        if (sp == null){
            sp = context.getSharedPreferences(KEY_SETTINGS, Context.MODE_PRIVATE);
        }
    }

    //每次都initSharedPreferences(context)逻辑性不好，还是单例模式好些，但未作修改
    public static void putInt(Context context, String key, int value) {
        initSharedPreferences(context);
        sp.edit().putInt(key, value).commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        initSharedPreferences(context);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void putString(Context context, String key, String value) {
        initSharedPreferences(context);
        sp.edit().putString(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        initSharedPreferences(context);
        return sp.getInt(key, 0);
    }

    public static String getString(Context context, String key) {
        initSharedPreferences(context);
        return sp.getString(key, null);
    }

    public static boolean getBoolean(Context context, String key) {
        initSharedPreferences(context);
        return sp.getBoolean(key, true);
    }

    public static boolean isContains(Context context, String key) {
        initSharedPreferences(context);
        return sp.contains(key);
        
    }

    public static void removeSetting(Context context, String key) {
        initSharedPreferences(context);
        sp.edit().remove(key).commit();
    }

}
