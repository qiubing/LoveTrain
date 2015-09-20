package cn.nubia.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by JiangYu on 2015/9/19.
 */
public class AsyncGetResult implements Runnable {
    private EntityFactoryGenerics mFactoryGenerics;
    private JSONObject mJSONObject;
    private List<?> mResultList;
    private Handler mResultHandler;

    public void setEntityFactory(EntityFactoryGenerics entityFactoryGenerics){
        mFactoryGenerics = entityFactoryGenerics;
    }

    public void setMainHandler(Handler handler){
        mResultHandler = handler;
    }

    public void setJSON(JSONObject object){
        mJSONObject = object;
    }

    public List<?> getResultList(){
        return mResultList;
    }

    @Override
    public void run() {
        Log.e("jiangyu", "result Thread begin");
        if((mJSONObject!=null)&&(mFactoryGenerics!=null)){
            mFactoryGenerics.setJSON(mJSONObject);
            mResultList = mFactoryGenerics.get();
            mResultHandler.sendMessage(new Message());
            mResultHandler = null;
            Log.e("jiangyu", "result Thread success end");
        }
    }
}
