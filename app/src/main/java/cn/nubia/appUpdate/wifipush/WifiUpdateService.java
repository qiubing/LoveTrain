package cn.nubia.appUpdate.wifipush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import cn.nubia.activity.R;
import cn.nubia.upgrade.api.NubiaUpgradeManager;
import cn.nubia.upgrade.http.IDownLoadListener;
import cn.nubia.upgrade.http.IGetVersionListener;
import cn.nubia.upgrade.model.VersionData;

/**
 * Created by WJ on 2015/10/26.
 */
public class WifiUpdateService extends Service implements IDownLoadListener {
    private final static String TAG = "WifiUpdateService";
    public static final String COMMAND = "command";
    public static final String COMMAND_CHECK_ISHARE_VERSION = "cn.nubia.lovetrain.update.change";
    public static final String COMMAND_CHECK_INSTALL_APK = "command_check_install_apk";
    private static final String AUTH_ID = "OHxuZVn30b99e477";
    private static final String AUTH_KEY = "025df7336bd7fe24";
    private static NubiaUpgradeManager mUpgradeManager;
    private static VersionData mVersionData;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null == intent)
            return START_NOT_STICKY;
        String cmd = intent.getStringExtra(COMMAND);
        if(null == cmd || cmd.isEmpty())
            return START_NOT_STICKY;
        if(cmd.equals(COMMAND_CHECK_ISHARE_VERSION)){
            Log.e(TAG,"onStartCommand"+COMMAND_CHECK_ISHARE_VERSION);
            getUpgradeManager().getVersion(this.getApplicationContext(),
                    new getVersionListener());
        }else if(cmd.equals(COMMAND_CHECK_INSTALL_APK)){
            Log.e(TAG,"onStartCommand"+COMMAND_CHECK_INSTALL_APK);
            getUpgradeManager().install(WifiUpdateService.this.getApplicationContext(),
                    mVersionData);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    public NubiaUpgradeManager getUpgradeManager() {
        if( null == mUpgradeManager){
            mUpgradeManager = NubiaUpgradeManager.getInstance(this,AUTH_ID,AUTH_KEY);
        }
        return mUpgradeManager;
    }

    private class getVersionListener implements IGetVersionListener {

        @Override
        public void onGetNewVersion(VersionData versionData) {
            if(versionData.isUpdate()){
                mVersionData = versionData;
                getUpgradeManager().startApkDown(WifiUpdateService.this.getApplicationContext(), versionData);
            }
        }

        @Override
        public void onGetNoVersion() {
        }

        @Override
        public void onError(int i) {
            WifiUpdateService.this.stopSelf();
        }
    }

    @Override
    public void onStartDownload() {
    }

    @Override
    public void onResumeDownload() {
    }

    @Override
    public void onDownloadError(int i) {
        WifiUpdateService.this.stopSelf();
    }

    @Override
    public void onDownloadPause() {
    }

    @Override
    public void onDownloadComplete(String s) {
        sendNewVersionNotification();
    }

    @Override
    public void onDownloadProgress(int i) {
    }


    private void sendNewVersionNotification(){
        RemoteViews view = new RemoteViews(getPackageName(),
                R.layout.settings_wifi_update_notification);
        view.setImageViewResource(R.id.notification_large_icon,
                R.drawable.my_list_txt_background);
        view.setTextViewText(R.id.notification_text,this.getResources()
                .getString(R.string.soft_update_info));

        Intent serviceIntent = new Intent("cn.nubia.appUpdate.newVersion");
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.evaluate_icon);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker(this.getResources().getString(R.string.soft_update_lovetrain))
                .setContentTitle(this.getResources().getString(R.string.soft_update_lovetrain))
                .setContentText(this.getResources().getString(R.string.soft_update_info))
                .setSmallIcon(R.mipmap.evaluate_icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getService(this, 0, serviceIntent, 0))
                .setContent(view);

        Notification notification = builder.build();
        notificationManager.notify(R.mipmap.evaluate_icon, notification);
        largeIcon.recycle();
    }
}
