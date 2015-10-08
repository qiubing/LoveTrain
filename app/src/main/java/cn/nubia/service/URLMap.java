package cn.nubia.service;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.util.jsonprocessor.ExamEnrollAssembler;
import cn.nubia.util.jsonprocessor.IAssemblerGenerics;
import cn.nubia.util.jsonprocessor.LessonJudgementAssembler;


/**
 * Created by JiangYu on 2015/9/15.
 */
public class URLMap {
    public final static Map<String,Class<? extends HttpHandler>> HANDLER_MAPPING =
            new HashMap<String,Class<? extends HttpHandler>>();
    public final static Map<String,Class<? extends IAssemblerGenerics<?>>> ASSEMBLER_MAPPING =
            new HashMap<String,Class<? extends IAssemblerGenerics<?>>>();

    private final static String BASE_URL = "http://love-train-dev.nubia.cn/";
    public final static String URL_ADD_SHARE = BASE_URL+"share/add_share_course.do";
    public final static String URL_UPD_SHARE = BASE_URL+"share/edit_going_course.do";
    public final static String URL_UPD_PSW = BASE_URL+"user/modify.do";
    public final static String URL_AWARD_CREDITS = BASE_URL+"credit/give_credits.do";
    public final static String URL_QUE_EXAMENROLLLIST = BASE_URL+"exam/exam_people_list.do";
    public final static String URL_ADD_NORMALEXAMENROLL = BASE_URL+"exam/exam_join.do";
    public final static String URL_ADD_SPECIALEXAMENROLL = BASE_URL+"exam/special_exam_join.do";
    public final static String URL_ADD_EXAMSCORE = BASE_URL+"exam/add_score.do";
    public final static String URL_QUE_JUDGEMENT = BASE_URL+"my/find_lesson_judge.do";
    public final static String URL_ADD_JUDGEMENT = BASE_URL+"my/add_lesson_judge.do";
    public final static String URL_QUE_MYJUDGEMENT = BASE_URL+"my/show_my_judge.do";
    public final static String URL_ADD_SENIORCOURSEENROLL = BASE_URL+"enroll/user_enroll_course.do";


    static {
        HANDLER_MAPPING.put(URL_AWARD_CREDITS,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_UPD_PSW,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_ADD_SHARE, NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_UPD_SHARE,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_QUE_EXAMENROLLLIST,NormalHttpHandler.class);
        ASSEMBLER_MAPPING.put(URL_QUE_EXAMENROLLLIST,ExamEnrollAssembler.class);

        HANDLER_MAPPING.put(URL_ADD_NORMALEXAMENROLL,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_ADD_SPECIALEXAMENROLL,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_ADD_EXAMSCORE,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_ADD_JUDGEMENT,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_ADD_SENIORCOURSEENROLL,NormalHttpHandler.class);

        HANDLER_MAPPING.put(URL_QUE_JUDGEMENT,NormalHttpHandler.class);
        ASSEMBLER_MAPPING.put(URL_QUE_JUDGEMENT,LessonJudgementAssembler.class);

        HANDLER_MAPPING.put(URL_QUE_MYJUDGEMENT,NormalHttpHandler.class);
        ASSEMBLER_MAPPING.put(URL_QUE_MYJUDGEMENT,LessonJudgementAssembler.class);
    }
}
