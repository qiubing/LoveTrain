package cn.nubia.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.RequestParams;

import cn.nubia.entity.Paramable;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class CommunicateService extends Service {
    private static Map<String,EntityFactoryGenerics.ItemType> sURLReturnType;

    private static Map<OperateType,Class<? extends Handler>> sHandleres;

    public enum OperateType{INSERT,UPDATE,QUERY,DELETE}
    static{
        sURLReturnType = new HashMap<String,EntityFactoryGenerics.ItemType>();
        sHandleres = new HashMap<OperateType,Class<? extends Handler>>();

        sURLReturnType.put("creditsaward.do",EntityFactoryGenerics.ItemType.SIMPLEDATA);
        sURLReturnType.put("passwordmodify.do",EntityFactoryGenerics.ItemType.SIMPLEDATA);
        sURLReturnType.put("newsharecourse.do",EntityFactoryGenerics.ItemType.SIMPLEDATA);
        sURLReturnType.put("updatesharecourse.do",EntityFactoryGenerics.ItemType.SIMPLEDATA);
        sURLReturnType.put("add_course_judge.do",EntityFactoryGenerics.ItemType.SIMPLEDATA);

        sHandleres.put(OperateType.INSERT,NormalHandler.class);
        sHandleres.put(OperateType.DELETE,NormalHandler.class);
        sHandleres.put(OperateType.UPDATE,NormalHandler.class);
        sHandleres.put(OperateType.QUERY,NormalHandler.class);
    }

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
        try {
            Handler  handler = sHandleres.get(paramable.getOperateType()).newInstance();
            handler.setActivityInter(inter);
            handler.setOperateType(paramable.getOperateType());
            handler.initEntityFactory(new EntityFactoryGenerics(sURLReturnType.get(URL)));
            RequestParams params = paramable.toParams();
            AsyncHttpHelper.get(URL,params,handler);
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
