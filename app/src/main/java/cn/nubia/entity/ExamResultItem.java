package cn.nubia.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/15 14:11
 */
public class ExamResultItem implements Serializable {
    private String mLessonName;
    private int mLessonIndex;
    private double mExamScore;

    public String getmLessonName() {
        return mLessonName;
    }

    public void setmLessonName(String mLessonName) {
        this.mLessonName = mLessonName;
    }

    public int getmLessonIndex() {
        return mLessonIndex;
    }

    public void setmLessonIndex(int mLessonIndex) {
        this.mLessonIndex = mLessonIndex;
    }

    public double getmExamScore() {
        return mExamScore;
    }

    public void setmExamScore(double mExamScore) {
        this.mExamScore = mExamScore;
    }
}
