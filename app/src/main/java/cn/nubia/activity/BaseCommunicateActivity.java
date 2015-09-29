package cn.nubia.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.Map;

import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.GestureDetectorManager;

/**
 * Created by JiangYu on 2015/9/23.
 */
public abstract class BaseCommunicateActivity extends Activity{

    public class Inter implements ActivityInter {
        public void handleResponse(Map<String,?> response,String responseURL){
            BaseCommunicateActivity.this.handleResponse(response, responseURL);
        }
    }

    protected CommunicateService.CommunicateBinder mBinder;
    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (CommunicateService.CommunicateBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };
    protected GestureDetectorManager mGestureDetectorManager  = GestureDetectorManager.getInstance();
    protected GestureDetector gestureDetector ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, mGestureDetectorManager);
        mGestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        connectService();
    }

    @Override
    public void onStop(){
        super.onStop();
        disconectService();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return  gestureDetector.onTouchEvent(event);
    }

    protected void connectService(){
        Intent intent = new Intent(
                BaseCommunicateActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    protected void disconectService(){
        unbindService(mConn);
    }

    protected abstract void handleResponse(Map<String,?> response,String responseURL);

}
