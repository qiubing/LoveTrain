package cn.nubia.util.jsonprocessor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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
        Log.e("jiangyu", "factory get");
        if(getCode()==0) {
            if (mType == ItemType.SIMPLEDATA)
                return getOperateResult();
            else
                return getItemList();
        }else
            return null;
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
