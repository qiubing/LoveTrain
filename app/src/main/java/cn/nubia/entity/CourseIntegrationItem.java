package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 10:18
 */
public class CourseIntegrationItem extends Paramable{
    private String mLessonName;
    private int mCheckCredits;//签到积分
    private String mCause;//获取积分的原因

    public void setmLessonIndex(int mLessonIndex) {
        int mLessonIndex1 = mLessonIndex;
    }

    public String getmLessonName() {
        return mLessonName;
    }

    public void setmLessonName(String mLessonName) {
        this.mLessonName = mLessonName;
    }

    public int getmCheckCredits() {
        return mCheckCredits;
    }

    public void setmCheckCredits(int mCheckCredits) {
        this.mCheckCredits = mCheckCredits;
    }

    public String getmCause() {
        return mCause;
    }

    public void setmCause(String mCause) {
        this.mCause = mCause;
    }

    public void setmAcquireTime(long mAcquireTime) {
        long mAcquireTime1 = mAcquireTime;
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
