package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class EntityFactoryGenerics {
    public enum ItemType{COURSE,EXAM,LESSONJUDGEMENT,USERINFO,SIMPLEDATA,
        CHECKRECORD,COURSEINTEGRATION,EXAMRESULT,SHARECOURSE}
    private IAssemblerGenerics mAssembler;
    private JSONObject mJsonObject;
    private ItemType mType;

    public EntityFactoryGenerics(ItemType type,JSONObject jsonObject){
        this(type);
        mJsonObject = jsonObject;
    }

    private EntityFactoryGenerics(ItemType type){
        mType = type;
        switch (mType){
            case COURSE:
                mAssembler = new CourseItemAssembler();
                break;
            case EXAM:
                mAssembler = new ExamItemAssembler();
                break;
            case LESSONJUDGEMENT:
                mAssembler = new LessonJudgementAssembler();
                break;
            case USERINFO:
                mAssembler = new UserInfoAssembler();
                break;
            case CHECKRECORD:
                mAssembler = new CheckRecordAssembler();
                break;
            case COURSEINTEGRATION:
                mAssembler = new CourseIntegrationAssembler();
                break;
            case EXAMRESULT:
                mAssembler = new ExamResultAssembler();
                break;
            case SHARECOURSE:
                mAssembler = new ShareCourseAssembler();
                break;
        }
    }

    public EntityFactoryGenerics(){
        mType = ItemType.SIMPLEDATA;
    }

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

    /**
     * 获得返回结果中的code值
     */
    public int getCode(){
        return JSONResolver.readCode(mJsonObject);
    }

    public List<?> get(){
        if(getCode()==0) {
            if (mType == ItemType.SIMPLEDATA)
                return getOperateResult();
            else
                return getItemList();
        }else
            return null;
    }

    public Map<String,?> getResponse(){
        if (mType == ItemType.SIMPLEDATA)
            return getResponseOperateResult();
        else
            return getResponseItemList();
    }

    /**
     * 获得作为列表返回的实体值
     */
    private List<?> getItemList(){
        JSONArray jsonArray = JSONResolver.readArray(mJsonObject);
        if (jsonArray == null){
            return null;
        }else {
            return mAssembler.assemble(jsonArray);
        }
    }

    private Map<String,Object> getResponseItemList(){
        Map<String,Object> response = new HashMap<String,Object>();
        if(JSONResolver.readCode(mJsonObject)==0){
            JSONArray jsonArray = JSONResolver.readArray(mJsonObject);
            if (jsonArray == null) {
                return null;
            } else {
                response.put("operateResult","success");
                response.put("message",JSONResolver.readMessage(mJsonObject));
                response.put("detail",mAssembler.assemble(jsonArray));
                return response;
            }
        }else{
            response.put("operateResult","failure");
            response.put("message",JSONResolver.readMessage(mJsonObject));
            response.put("detail",null);
            return response;
        }
    }

    /**
     * 获得作为字符串返回的简单值
     */
    private List<String> getOperateResult(){
        return JSONResolver.readOperateReult(mJsonObject);
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
