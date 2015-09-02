package cn.nubia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 连网状态判断工具类
 * Created by LK on 2015/8/31.
 */
public class IsNet {
    private Context ctx;

    public IsNet(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 判断客户端是否连接网络
     * @return
     */
    public boolean isConnect() {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo.State state = null;
        if (info != null) {
            state = info.getState();
            if (state == state.CONNECTED)
                return true;
        }
        info = null;
        info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        state = null;
        if (info != null) {
            state = info.getState();
            if (state == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }
}
