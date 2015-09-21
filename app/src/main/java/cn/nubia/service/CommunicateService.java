package cn.nubia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

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

        public void loopCommunicate (List<? extends Paramable> paramList,ActivityInter inter,String URL){
            CommunicateService.this.loopCommunicate(paramList, inter, URL);
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
        Log.e("jiangyu", "service communicate function start");
        try {
            Class<? extends HttpHandler> HttpHandlerClass = URLMap.HANDLER_MAPPING.get(tagetURL);
            final HttpHandler httpHandler = HttpHandlerClass.newInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("jiangyu", "communicate thread begin");
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
                        Log.e("jiangyu", params.toString());

                        httpHandler.setAsyncGetResult(asyncGetResult);
                        client.get(tagetURL, params, httpHandler);
                        Log.e("jiangyu", "communicate thread end");
                    }
                }
            }).start();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        Log.e("jiangyu", "service communicate function end");
    }

    private void loopCommunicate(final List<? extends Paramable> paramList,final ActivityInter inter,final String tagetURL) {
        Log.e("jiangyu", "service loopCommunicate function start");
        try {

            Class<? extends HttpHandler> HttpHandlerClass = URLMap.HANDLER_MAPPING.get(tagetURL);
            final HttpHandler httpHandler = HttpHandlerClass.newInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("jiangyu", "loopCommunicate thread begin");
                    if (httpHandler != null) {

                        final List<Boolean> lastMissionFinishedContain = new ArrayList<Boolean>();
                        lastMissionFinishedContain.add(true);

                        final AsyncGetResult asyncGetResult = new AsyncGetResult();
                        asyncGetResult.setEntityFactory(new EntityFactoryGenerics(URLMap.ASSEMBLER_MAPPING.get(tagetURL)));
                        asyncGetResult.setMainHandler(new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                synchronized (lastMissionFinishedContain) {
                                    lastMissionFinishedContain.remove(0);
                                    lastMissionFinishedContain.add(true);

                                    Map<String,?> mResponse = asyncGetResult.getResultList();
                                    inter.handleResponse(mResponse, tagetURL);
                                    Log.e("jiangyu", "loopCommunicate handled, lastMissionFinished = "+lastMissionFinishedContain.get(0));
                                    lastMissionFinishedContain.notifyAll();
                                }
                            }
                        });

                        httpHandler.setAsyncGetResult(asyncGetResult);
                        int loopIndex = 0;
                        while(loopIndex<paramList.size()){
                            synchronized (lastMissionFinishedContain) {
                                while(!lastMissionFinishedContain.get(0)){
                                    try {
                                        lastMissionFinishedContain.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Paramable currentParamable = paramList.get(loopIndex);
                                RequestParams params = currentParamable.toParams();
                                Log.e("jiangyu", params.toString());
                                client.get(tagetURL, params, httpHandler);

                                lastMissionFinishedContain.remove(0);
                                lastMissionFinishedContain.add(false);
                                Log.e("jiangyu", "loopCommunicate sended, lastMissionFinished = "+lastMissionFinishedContain.get(0));
                                loopIndex++;
                            }

                        }
                        Log.e("jiangyu", "loopCommunicate thread begin");
                    }
                }
            }).start();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

        Log.e("jiangyu", "service loopCommunicate function end");
    }
}
