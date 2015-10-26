package cn.nubia.appUpdate;

import android.app.AlertDialog;
import android.app.Service;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import cn.nubia.upgrade.api.NubiaUpgradeManager;

/**
 * Created by WJ on 2015/10/26.
 */
public class AboutManager {
    private static final String AppLogUrl = "";
    private final static String NEW_VERSION = "cn.nubia.appUpdate.change";
    private static final String AUTH_ID = "";
    private static final String AUTH_KEY = "";
    private static NubiaUpgradeManager mNubiaUpgradeManager;
    private static final String TAG = "AboutManager";
    private static Service mContext = null;
    private AlertDialog connectDialog = null;

    private final static int FLAG_UPDATE_NEW_VERSION = 0;
    private final static int FLAG_UPDATE_QUERYING = 1;
    private final static int FLAG_UPDATE_NETWORK_ERROR = 2;
    private final static int FLAG_UPDATE_NO_NEW_VERSION = 3;
    private final static int FLAG_UPDATE_QUERY_TIMEOUT = 5;

    private final static String VERSION_NAME = "verName";
    private final static String UPDATE_CONTENT = "content";
    private final static String VERSION_SIZE = "appsize";

    private final static int TIMEOUT = 5*1000;

    private static class DialogHandler extends Handler {
        WeakReference<AboutManager> _mgr;
        DialogHandler(AboutManager mgr){
            _mgr = new WeakReference<AboutManager>(mgr);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FLAG_UPDATE_NEW_VERSION:
                    AboutManager mgr = _mgr.get();
            }
        }
    }




}
