package cn.nubia.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Map;

import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;

/**
 * Created by JiangYu on 2015/9/23.
 */
public abstract class BaseCommunicateActivity extends BaseGestureActivity{

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

            onBinderCompleted();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

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

    private void connectService(){
        Intent intent = new Intent(
                BaseCommunicateActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void disconectService(){
        unbindService(mConn);
    }

    protected abstract void onBinderCompleted();
    protected abstract void handleResponse(Map<String,?> response,String responseURL);
}
