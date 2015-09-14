package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.entity.Paramable;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.util.AsyncHttpHelper;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class CommunicateService extends Service {
    private static Map<Class<? extends Paramable>,String> sURLes;
    private static Map<Class<? extends Paramable>,JsonHttpResponseHandler> sHandleres;

    static{
        sURLes = new HashMap<Class<? extends Paramable>,String>();
        sHandleres = new HashMap<Class<? extends Paramable>,JsonHttpResponseHandler>();
        sURLes.put(CreditsAwardMsg.class,"awarded.do");
        sHandleres.put(CreditsAwardMsg.class,new AwardCreditsHandler());
        sURLes.put(PswModifyMsg.class,"modifyPsw.do");
        sHandleres.put(PswModifyMsg.class,new PswModifyHandler());
    }

    @Override
    public void onCreate() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        communicate(intent);
        return 0;
    }

    private void communicate(Intent intent){
        Paramable paramable = (Paramable)intent.getSerializableExtra("data");
        Log.e("jiangyu", paramable.getClass().getName());
        AsyncHttpHelper.get(sURLes.get(paramable.getClass()),
                paramable.toParams(),
                sHandleres.get(paramable.getClass()));
    }
}
