package cn.nubia.appUpdate.wifipush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/10/26 20:31
 */
public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkRecevier";
    private static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (CONNECTIVITY_CHANGE_ACTION.equals(action)){
            if (isWifiConnected(context) && WifiPushSwitch.isEnable(context)){
                startService(context,
                        WifiUpdateService.COMMAND_CHECK_I_SHARE_VERSION);
            }else {
                stopService(context);
            }
        }else if ("cn.nubia.appUpdate.wifipush.WifiUpdateService".equals(action)){
            startService(context,WifiUpdateService.COMMAND_CHECK_UPDATE_APK);
        }
    }

    private boolean isWifiConnected(Context context){
        boolean isConnected = false;
        ConnectivityManager connManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null){
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null &&
                    networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                isConnected = true;
            }
        }
        return isConnected;
    }

    private void startService(Context context,String value){
        Intent intent = new Intent(context,WifiUpdateService.class);
        intent.putExtra(WifiUpdateService.COMMAND,value);
        context.startService(intent);
    }

    private void stopService(Context context){
        Intent service = new Intent(context,WifiUpdateService.class);
        context.stopService(service);
    }
}
