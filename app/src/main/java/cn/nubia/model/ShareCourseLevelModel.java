package cn.nubia.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ShareCourseLevelModel {
    public static Map<Short,String> SHARE_COURSE_MODEL;

    static{
        SHARE_COURSE_MODEL = new HashMap<Short,String>();
        SHARE_COURSE_MODEL.put((short)1,"部门级");
        SHARE_COURSE_MODEL.put((short)2,"科室级");
        SHARE_COURSE_MODEL.put((short)3,"团队级");
    }
}
