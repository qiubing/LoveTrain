package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 14:11
 */
public class ExamResultItem extends Paramable {
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
