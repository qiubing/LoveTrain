package cn.nubia.util;

import android.content.Context;
import android.util.Log;

/**
 * 日志工具类
 * Created by LK on 2015/8/31.
 */
public class Logger {
    public static void Log(final Context ctx, final String msg) {
        Log.d("Log-"+ctx.getClass().toString(), msg);
    }
}
