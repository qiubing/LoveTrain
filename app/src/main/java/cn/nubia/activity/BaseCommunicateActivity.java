package cn.nubia.activity;

import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;

/**
 * Created by JiangYu on 2015/9/23.
 */
public abstract class BaseCommunicateActivity extends Activity{

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

    public class Inter implements ActivityInter {
        public void handleResponse(Map<String,?> response,String responseURL){
            BaseCommunicateActivity.this.handleResponse(response, responseURL);
        }
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
