package cn.nubia.service;

import android.os.Handler;
import android.os.Message;
import org.json.JSONObject;
import java.util.Map;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * Created by JiangYu on 2015/9/19.
 */
class AsyncGetResult implements Runnable {
    private EntityFactoryGenerics mFactoryGenerics;
    private JSONObject mJSONObject;
    private Map<String,?> mResponse;
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

    public Map<String,?> getResultList(){
        return mResponse;
    }

    @Override
    public void run() {
        if((mJSONObject!=null)&&(mFactoryGenerics!=null)){
            mFactoryGenerics.setJSON(mJSONObject);
            mResponse = mFactoryGenerics.getResponse();
            mResultHandler.sendMessage(new Message());
        }else{
            mResultHandler.sendMessage(new Message());
        }
    }
}
