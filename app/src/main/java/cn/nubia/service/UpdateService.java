package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by WJ on 2015/10/26.
 */
public class UpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null == intent)
            return START_NOT_STICKY;
        String cmd = intent.getStringExtra("command");
        if(null == cmd)
            return START_NOT_STICKY;
        if(cmd.equals("update")){
//            AboutManager
            Log.e("test","");
        }else if(cmd.equals("report")){
            Log.e("test","");
        }else if(cmd.equals("auto_detect")){
            Log.e("test","");
        }
        return START_NOT_STICKY;
    }
}
