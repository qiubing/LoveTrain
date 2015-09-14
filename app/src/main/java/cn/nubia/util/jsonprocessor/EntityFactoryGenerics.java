package cn.nubia.util.jsonprocessor;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class EntityFactoryGenerics {
    public enum ItemType{COURSE,EXAM,LESSONJUDGEMENT,USERINFO,SIMPLEDATA}
    private AssemblerGenerics mAssembler;

    public EntityFactoryGenerics(ItemType type){
        switch (type){
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
        }
    }

    public  boolean getUpdate(JSONObject jsonObject,List<?> List){
        JSONArray jsonArray = JSONResolver.readArray(jsonObject);
        if (jsonArray == null){
            return false;
        }else {
            mAssembler.assemble(jsonArray,List);
            return true;
        }
    }

    /**
     * 处理作为字符串返回的简单值类型
     *
     * @param jsonObject    请求返回的JSON字符串
     */
    public static String getResult(JSONObject jsonObject){
        return JSONResolver.readString(jsonObject);
    }
}
