package cn.nubia.util;

import android.content.Context;

import cn.nubia.entity.UserInfo;
import cn.nubia.util.SpUtil;

/**
 * Created by LK on 2015/9/19.
 * 处理SharePreferences中的数据
 */
public class ProcessSPData {
    public static void putIntoSP(Context context, UserInfo user) {
        SpUtil.putString(context, "userName", user.getUserName());
        SpUtil.putString(context, "userID", user.getUserID());
        SpUtil.putBoolean(context, "gender", user.getGender());
        SpUtil.putString(context, "lastLoginTime", String.valueOf(user.getLastLoginTime()));
    }

    public static void putIntoSP(Context context, String name, Boolean value) {
        SpUtil.putBoolean(context, name, value);
    }

    public static void putIntoSP(Context context, String name, String value) {
        SpUtil.putString(context, name, value);
    }

    public static UserInfo getUserFromSP(Context context) {
        UserInfo user = new UserInfo();
        user.setUserName(SpUtil.getString(context, "userName"));
        user.setUserID(SpUtil.getString(context, "userID"));
        user.setGender(SpUtil.getBoolean(context, "gender"));
        String lastLoginTime = SpUtil.getString(context, "lastLoginTime");
        try {
            user.setLastLoginTime(Long.valueOf(lastLoginTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getStringFromSP(Context context, String key) {
        return SpUtil.getString(context, key);
    }

    public static Boolean getBoolenFromSP(Context context, String key) {
        return SpUtil.getBoolean(context, key);
    }

    public static void clearSP(Context ctx) {
        SpUtil.removeSetting(ctx, "userName");
        //SpUtil.removeSetting(ctx, "userID");
        SpUtil.removeSetting(ctx, "gender");
        SpUtil.removeSetting(ctx, "lastLoginTime");
        SpUtil.removeSetting(ctx, "isLogin");
        SpUtil.removeSetting(ctx, "isAdmin");
        SpUtil.removeSetting(ctx, "tokenKey");
    }
}
