package cn.nubia.service;

import java.util.Map;

/**
 * Created by JiangYu on 2015/9/14.
 */
public interface ActivityInter{
    void handleResponse(Map<String,?> response,String responseURL);
}
