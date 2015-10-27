package cn.nubia.appUpdate.wifipush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/10/27 9:15
 */
public class WifiUpdateService extends Service {
    private static final String TAG = "WifiUpdateService";
    public static final String COMMAND = "command";
    public static final String COMMAND_CHECK_I_SHARE_VERSION = "check_i_share_version";
    public static final String COMMAND_CHECK_UPDATE_APK = "check_update_apk";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
