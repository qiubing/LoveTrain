package cn.nubia.entity;

import java.io.Serializable;

/**
 * Description: 签到记录信息,包含课程信息、签到时间
 * Author: qiubing
 * Date: 2015/9/14 14:48
 */
public class CheckRecordItem implements Serializable{
    private String mLessonName;
    private int mLessonIndex;
    private long mCheckTime;

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

    public long getmCheckTime() {
        return mCheckTime;
    }

    public void setmCheckTime(long mCheckTime) {
        this.mCheckTime = mCheckTime;
    }
}
