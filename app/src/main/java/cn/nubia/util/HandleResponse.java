package cn.nubia.util;

import android.content.Context;

/**
 * Created by LK on 2015/9/14.
 * 处理Response返回的Json结果
 */
public class HandleResponse {

    public static void excute(Context context, String code) {
        if (code.equals("1000")) {
            DialogUtil.showToast(context, "未知系统错误！！");
        } else if (code.equals("1001")) {
            DialogUtil.showToast(context, "参数错误！！");
        } else if (code.equals("2001")) {
            DialogUtil.showToast(context, "用户名不存在，请重新输入！");
        } else if (code.equals("2002")) {
            DialogUtil.showToast(context, "用户名密码错误，请重新输入！");
        }
    }
}
