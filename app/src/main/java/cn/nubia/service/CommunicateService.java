package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.RequestParams;

import cn.nubia.entity.Paramable;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class CommunicateService extends Service {
    public enum OperateType{INSERT,UPDATE,QUERY,DELETE}
    public class CommunicateBinder extends Binder {
        public void communicate(Paramable paramable,ActivityInter inter,String URL){
            CommunicateService.this.communicate(paramable,inter,URL);
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

    private void communicate(Paramable paramable,ActivityInter inter,String URL){
        Log.e("jiangyu",inter.getClass().getName());
        try {
            Handler handler = MappingTable.HANDLER_MAPPING.get(URL).newInstance();
            handler.setActivityInter(inter);
            handler.setOperateType(paramable.getOperateType());
            handler.initEntityFactory(new EntityFactoryGenerics(MappingTable.ASSEMBLER_MAPPING.get(URL)));

            RequestParams params = paramable.toParams();

            AsyncHttpHelper.get(URL,params,handler);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
