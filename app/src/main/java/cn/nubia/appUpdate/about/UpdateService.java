package cn.nubia.appUpdate.about;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.nubia.appUpdate.about.AboutManager;

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
        Log.e("UpdateService","onStartCommand"+intent.getStringExtra("command"));
        if(null == intent)
            return START_NOT_STICKY;
        String cmd = intent.getStringExtra("command");
        if(null == cmd)
            return START_NOT_STICKY;
        AboutManager aboutManager = new AboutManager(this);
        if(cmd.equals("update")){
            aboutManager.newCheckUpdate();
        }else if(cmd.equals("report")){
            String msg = intent.getStringExtra("message");
            /***  do  nothing ***/
        }else if(cmd.equals("auto_detect")){
           aboutManager.newAutoDetected();
        }
        return START_NOT_STICKY;
    }
}
