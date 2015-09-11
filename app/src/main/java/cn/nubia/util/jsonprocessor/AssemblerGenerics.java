package cn.nubia.util.jsonprocessor;

import java.util.List;

import org.json.JSONArray;

/**
 * Created by JiangYu on 2015/9/10.
 */
interface AssemblerGenerics<T> {
    public boolean assemble(JSONArray jsonArray, List<T> list);
}
