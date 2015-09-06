package cn.nubia.entity;

/**
 * Created by JiangYu on 2015/9/2.
 */
public class ShareCourseLevel {
    private String mCourseLevelName;
    private short mCourseLevelSign;

    public ShareCourseLevel(String courseLevelName,short courseLevelSign){
        this.mCourseLevelName = courseLevelName;
        this.mCourseLevelSign = courseLevelSign;
    }
    public String getmCourseLevelName() {
        return mCourseLevelName;
    }

    public void setmCourseLevelName(String mCourseLevelName) {
        this.mCourseLevelName = mCourseLevelName;
    }

    public short getmCourseLevelSign() {
        return mCourseLevelSign;
    }

    public void setmCourseLevelSign(short mCourseLevelSign) {
        this.mCourseLevelSign = mCourseLevelSign;
    }
}
