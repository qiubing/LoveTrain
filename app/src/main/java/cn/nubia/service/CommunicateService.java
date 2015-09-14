package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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
    private static Map<Class<? extends Paramable>,Class<? extends Handler>> sHandleres;

    static{
        sURLes = new HashMap<Class<? extends Paramable>,String>();
        sHandleres = new HashMap<Class<? extends Paramable>,Class<? extends Handler>>();
        sURLes.put(CreditsAwardMsg.class,"awarded.do");
        sHandleres.put(CreditsAwardMsg.class,AwardCreditsHandler.class);
        sURLes.put(PswModifyMsg.class,"modifyPsw.do");
        sHandleres.put(PswModifyMsg.class,PswModifyHandler.class);
    }

    public class CommunicateBinder extends Binder {
        public void communicate(Paramable paramable,ActivityInter inter){
            CommunicateService.this.communicate(paramable,inter);
        }
    }

    @Override
    public void onCreate() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new CommunicateBinder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }
    private void communicate(Paramable paramable,ActivityInter inter){
        try {
            Handler handler = sHandleres.get(paramable.getClass()).newInstance();
            handler.setActivityInter(inter);
            AsyncHttpHelper.get(sURLes.get(paramable.getClass()),
                    paramable.toParams(),
                    handler);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
