package cn.nubia.service;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.activity.admin.AdminCreditsAwardActivity;
import cn.nubia.activity.admin.AdminExamInputScoreActivity;
import cn.nubia.activity.client.ClientMyAccountmanaPswmodifyActivity;
import cn.nubia.activity.client.ClientMyCourseJudgeDetailFillActivity;
import cn.nubia.activity.client.ClientMyShareCourseDetailFillActivity;
import cn.nubia.util.jsonprocessor.ExamEnrollAssembler;
import cn.nubia.util.jsonprocessor.IAssemblerGenerics;
import cn.nubia.util.jsonprocessor.LessonJudgementAssembler;


/**
 * Created by JiangYu on 2015/9/15.
 */
public class URLMap {
    public final static Map<Class<? extends ActivityInter>,String> URL_MAPPING =
            new HashMap<Class<? extends ActivityInter>,String>();
    public final static Map<String,Class<? extends HttpHandler>> HANDLER_MAPPING =
            new HashMap<String,Class<? extends HttpHandler>>();
    public final static Map<String,Class<? extends IAssemblerGenerics<?>>> ASSEMBLER_MAPPING =
            new HashMap<String,Class<? extends IAssemblerGenerics<?>>>();

    public final static String BASE_URL = "http://love-train-dev.nubia.cn/";
    public final static String URL_ADD_SHARE = BASE_URL+"share/add_share_course.do";
    public final static String URL_UPD_SHARE = BASE_URL+"share/edit_going_course.do";
    public final static String URL_UPD_PSW = BASE_URL+"user/modify.do";
    public final static String URL_AWARD_CREDITS = BASE_URL+"credit/give_credits.do";
    public final static String URL_QUE_EXAMENROLLLIST = BASE_URL+"exam/exam_people_list.do";
    public final static String URL_ADD_EXAMSCORE = BASE_URL+"exam/add_score.do";
    public final static String URL_QUE_JUDGEMENT = BASE_URL+"my/find_lesson_judge.do";
    public final static String URL_ADD_JUDGEMENT = BASE_URL+"my/add_lesson_judge.do";

    static {
        URL_MAPPING.put(AdminCreditsAwardActivity.Inter.class,URL_AWARD_CREDITS);
        HANDLER_MAPPING.put(URL_AWARD_CREDITS,NormalHttpHandler.class);

        URL_MAPPING.put(ClientMyAccountmanaPswmodifyActivity.Inter.class,URL_UPD_PSW);
        HANDLER_MAPPING.put(URL_UPD_PSW,NormalHttpHandler.class);

        URL_MAPPING.put(ClientMyShareCourseDetailFillActivity.Inter.class,URL_ADD_SHARE);
        HANDLER_MAPPING.put(URL_ADD_SHARE, NormalHttpHandler.class);

        URL_MAPPING.put(ClientMyShareCourseDetailFillActivity.Inter.class,URL_UPD_SHARE);
        HANDLER_MAPPING.put(URL_UPD_SHARE,NormalHttpHandler.class);

        URL_MAPPING.put(AdminExamInputScoreActivity.Inter.class,URL_QUE_EXAMENROLLLIST);
        HANDLER_MAPPING.put(URL_QUE_EXAMENROLLLIST,NormalHttpHandler.class);
        ASSEMBLER_MAPPING.put(URL_QUE_EXAMENROLLLIST,ExamEnrollAssembler.class);

        URL_MAPPING.put(AdminExamInputScoreActivity.Inter.class,URL_ADD_EXAMSCORE);
        HANDLER_MAPPING.put(URL_ADD_EXAMSCORE,NormalHttpHandler.class);

        URL_MAPPING.put(ClientMyCourseJudgeDetailFillActivity.Inter.class,URL_ADD_JUDGEMENT);
        HANDLER_MAPPING.put(URL_ADD_JUDGEMENT,NormalHttpHandler.class);

//        URL_MAPPING.put(AdminExamInputScoreActivity.Inter.class,URL_QUE_JUDGEMENT);
        HANDLER_MAPPING.put(URL_QUE_JUDGEMENT,NormalHttpHandler.class);
        ASSEMBLER_MAPPING.put(URL_QUE_JUDGEMENT,LessonJudgementAssembler.class);
    }
}
