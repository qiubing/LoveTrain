package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nubia.entity.Paramable;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;


/**
 * Created by JiangYu on 2015/9/11.
 */
public class CommunicateService extends Service {
    public enum OperateType{INSERT,UPDATE,QUERY,DELETE}
    private final SyncHttpClient client = new SyncHttpClient();
    public class CommunicateBinder extends Binder {
        public void communicate(Paramable paramable,ActivityInter inter,String URL){
            CommunicateService.this.communicate(paramable, inter, URL);
        }

        public void communicate (List<? extends Paramable> paramList,ActivityInter inter,String URL){
            CommunicateService.this.communicate(paramList, inter, URL);
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

    private void communicate(final Paramable paramable,final ActivityInter inter,final String tagetURL) {
        try {
            Class<? extends HttpHandler> HttpHandlerClass = URLMap.HANDLER_MAPPING.get(tagetURL);
            final HttpHandler httpHandler = HttpHandlerClass.newInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (httpHandler != null) {
                        final AsyncGetResult asyncGetResult = new AsyncGetResult();
                        asyncGetResult.setEntityFactory(new EntityFactoryGenerics(URLMap.ASSEMBLER_MAPPING.get(tagetURL)));
                        asyncGetResult.setMainHandler(new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                Map<String, ?> mResponse = asyncGetResult.getResultList();
                                inter.handleResponse(mResponse, tagetURL);
                            }
                        });
                        RequestParams params = paramable.toParams();

                        httpHandler.setAsyncGetResult(asyncGetResult);
                        client.get(tagetURL, params, httpHandler);
                    }
                }
            }).start();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    private void communicate(final List<? extends Paramable> paramList,final ActivityInter inter,final String tagetURL) {
        try {

            Class<? extends HttpHandler> HttpHandlerClass = URLMap.HANDLER_MAPPING.get(tagetURL);
            final HttpHandler httpHandler = HttpHandlerClass.newInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (httpHandler != null) {
                        final List<Boolean> lastMissionFinishedContain = new ArrayList<Boolean>();
                        lastMissionFinishedContain.add(true);

                        final AsyncGetResult asyncGetResult = new AsyncGetResult();
                        asyncGetResult.setEntityFactory(new EntityFactoryGenerics(URLMap.ASSEMBLER_MAPPING.get(tagetURL)));
                        asyncGetResult.setMainHandler(new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                synchronized (lastMissionFinishedContain) {
                                    Map<String,?> mResponse = asyncGetResult.getResultList();
                                    inter.handleResponse(mResponse, tagetURL);

                                    lastMissionFinishedContain.remove(0);
                                    lastMissionFinishedContain.add(true);
                                    lastMissionFinishedContain.notifyAll();
                                }
                            }
                        });

                        httpHandler.setAsyncGetResult(asyncGetResult);
                        int loopIndex = 0;
                        while(loopIndex<paramList.size()){
                            //noinspection SynchronizationOnLocalVariableOrMethodParameter
                            synchronized (lastMissionFinishedContain) {
                                while (!lastMissionFinishedContain.get(0)) {
                                    try {
                                        lastMissionFinishedContain.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                lastMissionFinishedContain.remove(0);
                                lastMissionFinishedContain.add(false);
                            }

                            Paramable currentParamable = paramList.get(loopIndex);
                            RequestParams params = currentParamable.toParams();
                            client.get(tagetURL, params, httpHandler);

                            loopIndex++;
                        }
                    }
                }
            }).start();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
