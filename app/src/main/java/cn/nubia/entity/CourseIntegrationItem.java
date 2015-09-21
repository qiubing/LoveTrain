package cn.nubia.entity;

import java.io.Serializable;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 10:18
 */
public class CourseIntegrationItem implements Serializable{
    private String mLessonName;
    private int mLessonIndex;
    private int mCheckCredits;//签到积分
    private String mCause;//获取积分的原因
    private long mAcquireTime;//获取积分的时间

    public int getmLessonIndex() {
        return mLessonIndex;
    }

    public void setmLessonIndex(int mLessonIndex) {
        this.mLessonIndex = mLessonIndex;
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

    public long getmAcquireTime() {
        return mAcquireTime;
    }

    public void setmAcquireTime(long mAcquireTime) {
        this.mAcquireTime = mAcquireTime;
    }

}
