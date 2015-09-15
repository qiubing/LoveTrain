package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.entity.CreditsAwardMsg;
import cn.nubia.entity.LessonJudgement;
import cn.nubia.entity.Paramable;
import cn.nubia.entity.PswModifyMsg;
import cn.nubia.entity.ShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class CommunicateService extends Service {
    private static Map<Class<? extends Paramable>,String> sUpdateURLes;
    private static Map<Class<? extends Paramable>,Class<? extends Handler>> sUpdateHandleres;

    private static Map<Class<? extends Paramable>,String> sInsertURLes;
    private static Map<Class<? extends Paramable>,Class<? extends Handler>> sInsertHandleres;

    public enum OperateType{INSERT,UPDATE}
    static{
        sUpdateURLes = new HashMap<Class<? extends Paramable>,String>();
        sUpdateHandleres = new HashMap<Class<? extends Paramable>,Class<? extends Handler>>();
        sInsertURLes = new HashMap<Class<? extends Paramable>,String>();
        sInsertHandleres = new HashMap<Class<? extends Paramable>,Class<? extends Handler>>();

        sUpdateURLes.put(PswModifyMsg.class,"modifyPsw.do");
        sUpdateHandleres.put(PswModifyMsg.class,PswModifyHandler.class);
        sUpdateURLes.put(ShareCourseItem.class,"insertShareCourse.do");
        sUpdateHandleres.put(ShareCourseItem.class,UpdateShareCourseHandler.class);

        sInsertURLes.put(CreditsAwardMsg.class,"awarded.do");
        sInsertHandleres.put(CreditsAwardMsg.class,AwardCreditsHandler.class);
        sInsertURLes.put(LessonJudgement.class,"lesson.do");
        sInsertHandleres.put(PswModifyMsg.class,LessonJudgementHandler.class);
        sInsertURLes.put(ShareCourseItem.class,"modifyShareCourse.do");
        sInsertHandleres.put(ShareCourseItem.class,InsertShareCourseHandler.class);
    }

    public class CommunicateBinder extends Binder {
        public void communicate(Paramable paramable,ActivityInter inter,OperateType type){
            CommunicateService.this.communicate(paramable,inter,type);
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

    private void communicate(Paramable paramable,ActivityInter inter,OperateType type){
        Handler handler;
        String URL;
        RequestParams params;
        try {
            if (type == OperateType.INSERT){
                handler = sInsertHandleres.get(paramable.getClass()).newInstance();
                handler.setActivityInter(inter);
                URL = sInsertURLes.get(paramable.getClass());
                params = paramable.toInsertParams();
            }else{
                handler = sUpdateHandleres.get(paramable.getClass()).newInstance();
                handler.setActivityInter(inter);
                URL = sUpdateURLes.get(paramable.getClass());
                params = paramable.toUpdateParams();
            }
            AsyncHttpHelper.get(URL,params,handler);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
