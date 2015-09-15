package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class EntityFactoryGenerics {
    public enum ItemType{COURSE,EXAM,LESSONJUDGEMENT,USERINFO,SIMPLEDATA,CHECKRECORD}
    private IAssemblerGenerics mAssembler;
    private JSONObject mJsonObject;
    private ItemType mType;

    public EntityFactoryGenerics(ItemType type,JSONObject jsonObject){
        this(type);
        mJsonObject = jsonObject;
    }

    public EntityFactoryGenerics(ItemType type){
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
        if(mType ==ItemType.SIMPLEDATA)
            return getOperateResult();
        else
            return getItemList();
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

    /**
     * 获得作为字符串返回的简单值
     */
    private List<String> getOperateResult(){
        return JSONResolver.readOperateReult(mJsonObject);
    }
}
