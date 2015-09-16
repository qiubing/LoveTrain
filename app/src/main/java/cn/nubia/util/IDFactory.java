package cn.nubia.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.Random;

/**
 * Created by LK on 2015/9/14.
 * 获取设备ID,软件版本号
 */
public class IDFactory {
    protected static final String DEVIDE_ID = "DEVIDE_ID";
    private Context context;

    public IDFactory(Context context) {
        this.context = context;
    }

    public String getVersionCode() {
        String result = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            result = String.valueOf(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getDevideID() {
        String result = SpUtil.getString(context, DEVIDE_ID);
        if (null != result) {
            return result;
        }
        String androidID = Settings.Secure
                .getString(context.getContentResolver(), DEVIDE_ID);
        if (null != androidID && !"9774d56d682e549c".equals(androidID)) {
            SpUtil.putString(context, DEVIDE_ID, androidID);
            return androidID;
        } else {
            String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (null != deviceId) {
                SpUtil.putString(context, DEVIDE_ID, deviceId);
                return deviceId;
            } else {
                Random random = new Random(100000);
                deviceId = String.valueOf(System.currentTimeMillis()) + String.valueOf(random.nextInt());
                SpUtil.putString(context, DEVIDE_ID, deviceId);
                return deviceId;
            }
        }
    }
}
