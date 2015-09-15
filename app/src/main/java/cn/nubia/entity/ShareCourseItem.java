package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/15.
 */
public class ShareCourseItem extends CourseItem implements Paramable {
    @Override
    public RequestParams toInsertParams() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("awardedName",getName());
        param.put("awardedName",getDescription());
        param.put("awardedName",String.valueOf(getIndex()));
        param.put("awardedName",getType());
        param.put("awardedName",String.valueOf(getLessones()));
        param.put("awardedName",String.valueOf(getCourseStatus()));
        param.put("awardedName",String.valueOf(hasExam()));
        param.put("awardedCredits",String.valueOf(getShareType()));
        LessonItem lessonItem= getLessonList().get(0);
        param.put("awardedCredits",String.valueOf(lessonItem.getStartTime()));
        param.put("awaredCause",String.valueOf(lessonItem.getEndTime()));
        return new RequestParams(param);
    }

    @Override
    public RequestParams toUpdateParams() {
        return null;
    }
}
