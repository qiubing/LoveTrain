package cn.nubia.util.jsonprocessor;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class EntityFactoryGenerics {
    public enum ItemType{SIMPLEDATA}
    private IAssemblerGenerics mAssembler;
    private JSONObject mJsonObject;
    private ItemType mType;


    public EntityFactoryGenerics(Class<? extends IAssemblerGenerics> assemblerClass){
        try {
            if(assemblerClass != null)
                mAssembler = assemblerClass.newInstance();
            else
                mType = ItemType.SIMPLEDATA;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setJSON(JSONObject jsonObject){
        mJsonObject = jsonObject;
    }

    public Map<String,?> getResponse(){
        if (mType == ItemType.SIMPLEDATA)
            return getResponseOperateResult();
        else
            return getResponseItemList();
    }

    private Map<String,Object> getResponseItemList(){
        Map<String,Object> response = new HashMap<String,Object>();
        if(JSONResolver.readCode(mJsonObject)==0){
            response.put("operateResult","success");
            response.put("message",JSONResolver.readMessage(mJsonObject));
            response.put("detail",JSONResolver.readAsList(mJsonObject, mAssembler));
            return response;
        }else{
            response.put("operateResult","failure");
            response.put("message",JSONResolver.readMessage(mJsonObject));
            response.put("detail",null);
            return response;
        }
    }

    private Map<String,Object> getResponseOperateResult(){
        Map<String,Object> response = new HashMap<String,Object>();
        if(JSONResolver.readCode(mJsonObject)==0){
            response.put("operateResult","success");
            response.put("message",JSONResolver.readMessage(mJsonObject));
            response.put("detail",JSONResolver.readOperateReult(mJsonObject));
            return response;
        }else{
            response.put("operateResult","failure");
            response.put("message",JSONResolver.readMessage(mJsonObject));
            response.put("detail",JSONResolver.readOperateReult(mJsonObject));
            return response;
        }
    }
}
