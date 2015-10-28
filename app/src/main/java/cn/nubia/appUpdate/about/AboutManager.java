package cn.nubia.appUpdate.about;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cn.nubia.activity.R;
import cn.nubia.upgrade.api.NubiaUpdateConfiguration;
import cn.nubia.upgrade.api.NubiaUpgradeManager;
import cn.nubia.upgrade.http.IDownLoadListener;
import cn.nubia.upgrade.http.IGetVersionListener;
import cn.nubia.upgrade.model.VersionData;

/**
 * Created by WJ on 2015/10/26.
 */
public class AboutManager {
    private static final String AppLogUrl = "";
    private final static String NEW_VERSION = "cn.nubia.lovetrain.update.change";
    private static final String AUTH_ID = "OHxuZVn30b99e477";
    private static final String AUTH_KEY = "025df7336bd7fe24";
    private static final String TAG = "AboutManager";
    private static VersionData mVersionData;

    private static Service mContext = null;
    private AlertDialog connectDialog = null;

    private final static int FLAG_UPDATE_NEW_VERSION = 0;
    private final static int FLAG_UPDATE_QUERYING = 1;
    private final static int FLAG_UPDATE_NETWORK_ERROR = 2;
    private final static int FLAG_UPDATE_NO_NEW_VERSION = 3;
    private final static int FLAG_UPDATE_NOTIFICATION = 4;
    private final static int FLAG_UPDATE_QUERY_TIMEOUT = 5;

    private final static String VERSION_NAME = "verName";
    private final static String UPDATE_CONTENT = "content";
    private final static String VERSION_SIZE = "appsize";

    private final static int TIMEOUT = 10 * 1000;
    private static NubiaUpgradeManager mUpgradeManager;

    public static NubiaUpgradeManager getUpgradeManager() {
        if (null == mUpgradeManager) {
            mUpgradeManager = NubiaUpgradeManager.getInstance(mContext, AUTH_ID, AUTH_KEY);
        }
        return mUpgradeManager;
    }

    private static class MyHandler extends Handler {
        WeakReference<AboutManager> _mgr;

        MyHandler(AboutManager mgr) {
            _mgr = new WeakReference<AboutManager>(mgr);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("wj", "MyHandler  handleMessage");
            super.handleMessage(msg);
            switch (msg.what) {
                case FLAG_UPDATE_NEW_VERSION: {
                    if (hasMessages(FLAG_UPDATE_QUERY_TIMEOUT)) {
                        removeMessages(FLAG_UPDATE_QUERY_TIMEOUT);
                    }
                    AboutManager mgr = _mgr.get();
                    if (mgr != null) {
                        Bundle data = msg.getData();
                        mgr.showDialog(data.getString(VERSION_SIZE),
                                data.getString(VERSION_SIZE),
                                data.getString(UPDATE_CONTENT));
                    }
                    _mgr.clear();
                }
                break;
                case FLAG_UPDATE_QUERYING: {
                    Log.e("wj", "FLAG_UPDATE_QUERYING");
                    AboutManager mgr = _mgr.get();
                    if (null != mgr) {
                        Log.e("wj", "null != mgr");
                        mgr.showProcessDialog();
                    }
                }
                break;
                case FLAG_UPDATE_NETWORK_ERROR: {
                    if (hasMessages(FLAG_UPDATE_QUERY_TIMEOUT)) {
                        removeMessages(FLAG_UPDATE_QUERY_TIMEOUT);
                    }
                    AboutManager mgr = _mgr.get();
                    if (null != mgr) {
                        mgr.showErrorDialog();
                    }
                    _mgr.clear();
                }
                break;
                case FLAG_UPDATE_NO_NEW_VERSION: {
                    if (hasMessages(FLAG_UPDATE_QUERY_TIMEOUT)) {
                        removeMessages(FLAG_UPDATE_QUERY_TIMEOUT);
                    }
                    AboutManager mgr = _mgr.get();
                    if (null != mgr) {
                        Log.e("wj", "if(null != mgr){");
                        mgr.showNoUpdateDialog();
                    }
                    _mgr.clear();
                }
                break;
                case FLAG_UPDATE_QUERY_TIMEOUT: {
                    Log.e("wj", "FLAG_UPDATE_QUERY_TIMEOUT");
                    AboutManager mgr = _mgr.get();
                    if (null != mgr) {
                        Log.e("wj", "ull != mgr showErrorDialog");
                        mgr.showErrorDialog();
                    }
                    _mgr.clear();
                }
                break;
                case FLAG_UPDATE_NOTIFICATION: {
                    AboutManager mgr = _mgr.get();
                    if (null != mgr) {
                        mgr.sendNewVersionNotification();
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    private Handler mHandler = new MyHandler(this);

    public AboutManager(Service context) {
        mContext = context;
    }

    private static void stopUpdateService() {
        mContext.stopSelf();
    }

    private void newAutoSetVersionInfo(Message msg, VersionData data) {
        if (msg != null && data != null) {
            Bundle bundle = new Bundle();
            bundle.putString(UPDATE_CONTENT, data.getUpgradeContent());
            bundle.putString(VERSION_SIZE, "");
            bundle.putString(VERSION_NAME, data.getToVersionCode());
            msg.setData(bundle);
        }
    }

    public void newCheckUpdate() {
        checkVersion(false);
        setConfiguration();
    }

    private void setConfiguration() {
        NubiaUpdateConfiguration.Builder builder = new NubiaUpdateConfiguration.Builder();
        builder.setShowNotification(true);
        builder.setAppName(mContext.getResources().getString(R.string.app_name));
        /****revise here **/
        builder.setIcon(R.drawable.my_list_txt_background);
        getUpgradeManager().setConfiguration(NubiaUpdateConfiguration.Builder
                .getConfiguration(builder));
    }

    private void checkVersion(boolean isAutoDetect) {
        getUpgradeManager().debug(true);
        if (!isAutoDetect) {
            Message msg = Message.obtain();
            msg.what = FLAG_UPDATE_QUERYING;
            mHandler.sendMessage(msg);
        }
        Log.e("wj", "checkVersion");
        getUpgradeManager().getVersion(mContext, new getVersionListener(isAutoDetect));
    }

    private class getVersionListener implements IGetVersionListener {

        boolean isAutoDetect;

        public getVersionListener(boolean isAutoDetect) {
            this.isAutoDetect = isAutoDetect;
        }

        @Override
        public void onGetNewVersion(VersionData versionData) {
            Log.e("wj", "onGetNewVersion");
            if (versionData.isUpdate()) {
                if (isAutoDetect) {
                    Log.e("wj", "onGetNewVersion isAutoDetect");
                    SettingsDataStore store = new SettingsDataStore(mContext);
                    store.setSwitchStatus(SettingsDataStore.DETECT_NEW_VERSION_KEY, true);

                    Intent intent = new Intent(NEW_VERSION);
                    intent.putExtra("new_version", true);
                    mContext.sendBroadcast(intent);
                } else {
                    Log.e("wj", "onGetNewVersion   not  isAutoDetect");
                    Message msg = Message.obtain();
                    msg.what = FLAG_UPDATE_NO_NEW_VERSION;
                    mVersionData = versionData;
                    newAutoSetVersionInfo(msg, versionData);
                    mHandler.sendMessage(msg);
                }
            }
        }

        @Override
        public void onGetNoVersion() {
            Log.e("wj", "onGetNoVersion");
            if (isAutoDetect) {
                Log.e("wj", "onGetNoVersion   not  isAutoDetect");
                SettingsDataStore store = new SettingsDataStore(mContext);
                store.setSwitchStatus(SettingsDataStore.DETECT_NEW_VERSION_KEY, false);
                Intent intent = new Intent(NEW_VERSION);
                intent.putExtra("new_version", false);
                mContext.sendBroadcast(intent);
                stopUpdateService();
            } else {
                Log.e("wj", "onGetNoVersion   not  isAutoDetect");
                Message msg = Message.obtain();
                msg.what = FLAG_UPDATE_NO_NEW_VERSION;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onError(int i) {
            Log.e("wj", "onError" + i);
            if (isAutoDetect) {
                stopUpdateService();
            } else {
                Message msg = Message.obtain();
                msg.what = FLAG_UPDATE_NETWORK_ERROR;
                mHandler.sendMessage(msg);
            }
        }
    }

    private void showProcessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在检查新版本……");

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        /**** revise here ***/
        View view = layoutInflater.inflate(R.layout.setting_detect_process_dialog, null);
        builder.setView(view);
        connectDialog = builder.create();
        connectDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        connectDialog.show();

        Message msg = Message.obtain();
        msg.what = FLAG_UPDATE_QUERY_TIMEOUT;
        mHandler.sendMessageDelayed(msg, TIMEOUT);
    }

    private void showErrorDialog() {
        Log.e("wj", "showErrorDialog");
        if (connectDialog != null && connectDialog.isShowing()) {
            connectDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        /******revise here****/
        builder.setMessage("与服务器连接错误");
        builder.setTitle("检测版本更新");

        AlertDialog errorDialog = builder.create();
        errorDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopUpdateService();
            }
        });
        errorDialog.show();
    }

    private void showNoUpdateDialog() {
        if (connectDialog != null && connectDialog.isShowing()) {
            connectDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        /******revise here****/
        builder.setMessage("没有最新版本");
        builder.setTitle("检测版本更新");
        builder.setPositiveButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stopUpdateService();
                    }
                });
        AlertDialog noUpdateDialog = builder.create();
        noUpdateDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        noUpdateDialog.show();
    }

    private void showDialog(String version, String size, String update) {
        if (connectDialog != null && connectDialog.isShowing()) {
            connectDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

        window.setContentView(R.layout.settings_layout_update_notify_dialog);
        TextView stopUpdate = (TextView) window.findViewById(R.id.settings_update_dialog_not_update);
        stopUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                stopUpdateService();
            }
        });

        TextView nowUpdate = (TextView) window.findViewById(R.id.settings_update_dialog_now_update);
        nowUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getUpgradeManager().startApkDown(mContext, mVersionData);
                getUpgradeManager().addDownLoadListener(new DownLoadListener(mVersionData));
                dialog.dismiss();
            }
        });

        TextView versionTitle = (TextView) window.findViewById(R.id.settings_update_dialog_version_name);
        versionTitle.setText(mContext.getResources().getString(R.string.soft_update_title) + version);

        if (!size.equals("")) {
            TextView sizeTitle = (TextView) window.findViewById(R.id.settings_update_dialog_version_size);
            sizeTitle.setText(mContext.getResources().getString(R.string.soft_update_size) + size);
            sizeTitle.setText("应用大小" + size);
        }

        TextView contentView = (TextView) window.findViewById(R.id.settings_update_dialog_content);
        contentView.setText(mContext.getResources().getString(R.string.soft_update_content) + update);
        contentView.setText("更新" + update);
    }

    private static class DownLoadListener implements IDownLoadListener {

        private VersionData mVersionData;

        public DownLoadListener(VersionData data) {
            mVersionData = data;
        }

        @Override
        public void onStartDownload() {

        }

        @Override
        public void onResumeDownload() {

        }

        @Override
        public void onDownloadError(int i) {
            Toast.makeText(mContext, mContext.getResources()
                    .getString(R.string.soft_update_error), Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onDownloadPause() {

        }

        @Override
        public void onDownloadComplete(String s) {
            getUpgradeManager().install(mContext, mVersionData);
            stopUpdateService();
        }

        @Override
        public void onDownloadProgress(int i) {

        }
    }

    private void sendNewVersionNotification() {
        /******revise here****/
        Intent serviceIntent = new Intent("cn.nubia.appUpdate.newVersion");
        serviceIntent.putExtra("command", "update");

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext)
                .setTicker(mContext.getResources().getString(R.string.soft_update_lovetrain))
                .setContentTitle(mContext.getResources().getString(R.string.soft_update_lovetrain))
                .setContentText(mContext.getResources().getString(R.string.soft_update_info))
                .setSmallIcon(R.drawable.icon_all_selector)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getService(mContext, 0, serviceIntent, 0));
        Notification notification = builder.build();
        notificationManager.notify(R.drawable.my_list_txt_background, notification);

        stopUpdateService();
    }

    public void newAutoDetected() {
        checkVersion(true);
    }
}