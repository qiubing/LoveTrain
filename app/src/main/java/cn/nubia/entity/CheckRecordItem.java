package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Description: 签到记录信息,包含课程信息、签到时间
 * Author: qiubing
 * Date: 2015/9/14 14:48
 */
public class CheckRecordItem extends Paramable{
    private String mLessonName;
    private long mCheckTime;

    public String getmLessonName() {
        return mLessonName;
    }

    public void setmLessonName(String mLessonName) {
        this.mLessonName = mLessonName;
    }

    public long getmCheckTime() {
        return mCheckTime;
    }

    public void setmCheckTime(long mCheckTime) {
        this.mCheckTime = mCheckTime;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toUpdateParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toQueryParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toDeleteParams(RequestParams params) {
        return null;
    }
}
