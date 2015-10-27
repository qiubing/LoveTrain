package cn.nubia.appUpdate.wifipush;

import android.content.Context;

import cn.nubia.appUpdate.about.SettingsDataStore;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/10/27 9:07
 */
public class WifiPushSwitch {
    public final static String KEY_WIFI_AUTO_DOWNLOAD_NEW_VERSION = "wifi_auto_download_new_version";
    public final static boolean WIFI_AUTO_DOWNLOAD_SWITCH_DEFAULT = true;

    public static boolean isEnable(Context context){
        SettingsDataStore store = new SettingsDataStore(context);
        return store.getSwitchStatus(KEY_WIFI_AUTO_DOWNLOAD_NEW_VERSION,
                WIFI_AUTO_DOWNLOAD_SWITCH_DEFAULT);
    }
}
