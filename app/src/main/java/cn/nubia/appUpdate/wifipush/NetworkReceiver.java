package cn.nubia.appUpdate.wifipush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/10/26 20:31
 */
public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkRecevier";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("wj","onReceive"+action);
        if (WifiUpdateService.COMMAND_CHECK_ISHARE_VERSION.equals(action)
                ||"android.net.conn.CONNECTIVITY_CHANGE".equals(action)){
            Log.e("wj","ONNECTIVITY_CHANGE_ACTION.equals(action)");
            if (isWifiConnected(context) && WifiPushSwitch.isEnable(context)){
                Log.e("wj","isWifiConnected(context) && WifiPushSwitch.isEnable(context)");
                startService(context,
                        WifiUpdateService.COMMAND_CHECK_ISHARE_VERSION);
            }else {
                Log.e("wj","stopService");
                stopService(context);
            }
        }else if (WifiUpdateService.COMMAND_CHECK_INSTALL_APK.equals(action)){
            Log.e("wj","cn.nubia.appUpdate.wifipush.WifiUpdateService.equals(acti");
            startService(context,WifiUpdateService.COMMAND_CHECK_INSTALL_APK);
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
