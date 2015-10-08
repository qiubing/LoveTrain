package cn.nubia.util.jsonprocessor;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.nubia.entity.Paramable;

/**
 * Created by JiangYu on 2015/9/10.
 */
public interface IAssemblerGenerics<T extends Paramable> {
     List<T> assemble(JSONArray jsonArray);
     T assemble(JSONObject jsonObject);
}
