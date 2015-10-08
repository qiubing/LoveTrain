package cn.nubia.entity;

/**
 * Created by JiangYu on 2015/9/2.
 */
public class ShareCourseLevel {
    private final String mCourseLevelName;
    private final short mCourseLevelSign;

    public ShareCourseLevel(String courseLevelName,short courseLevelSign){
        this.mCourseLevelName = courseLevelName;
        this.mCourseLevelSign = courseLevelSign;
    }
    public String getmCourseLevelName() {
        return mCourseLevelName;
    }

    public short getmCourseLevelSign() {
        return mCourseLevelSign;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            //目标为空引用，并不指向对象
            return false;
        }else if(this == obj) {
            //引用同一个对象
            return true;
        }else if(getClass() != obj.getClass() ) {
            //目标与本对象不属于同一个类型
            return false;
        }else{
            ShareCourseLevel other = (ShareCourseLevel)obj;
            if(!other.mCourseLevelName.equals(this.mCourseLevelName)) {
                    return false;
            }else if (other.mCourseLevelSign !=this.mCourseLevelSign)
                return false;
        }
        return true;
    }
}
