package cn.nubia.service;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.activity.admin.AdminCreditsAwardActivity;
import cn.nubia.util.jsonprocessor.IAssemblerGenerics;
import cn.nubia.util.jsonprocessor.LessonJudgementAssembler;


/**
 * Created by JiangYu on 2015/9/15.
 */
public class MappingTable {
    public final static Map<Class<? extends ActivityInter>,String> URL_MAPPING =
            new HashMap<Class<? extends ActivityInter>,String>();
    public final static Map<String,Class<? extends Handler>> HANDLER_MAPPING =
            new HashMap<String,Class<? extends Handler>>();
    public final static Map<String,Class<? extends IAssemblerGenerics<?>>> ASSEMBLER_MAPPING =
            new HashMap<String,Class<? extends IAssemblerGenerics<?>>>();

    static {
        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,"creditsaward.do");
        HANDLER_MAPPING.put("creditsaward.do",NormalHandler.class);

        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,"passwordmodify.do");
        HANDLER_MAPPING.put("passwordmodify.do",NormalHandler.class);

        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,"newsharecourse.do");
        HANDLER_MAPPING.put("newsharecourse.do",NormalHandler.class);

        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,"updatesharecourse.do");
        HANDLER_MAPPING.put("updatesharecourse.do",NormalHandler.class);

        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,"queryAllJudgement.do");
        HANDLER_MAPPING.put("queryAllJudgement.do",NormalHandler.class);
        ASSEMBLER_MAPPING.put("queryAllJudgement.do",LessonJudgementAssembler.class);
    }
}
