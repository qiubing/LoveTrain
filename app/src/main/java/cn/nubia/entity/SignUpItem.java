package cn.nubia.entity;

import java.io.Serializable;

/**
 * Created by hexiao on 2015/9/19.
 */
public class SignUpItem implements Serializable {

    private int userID;
    private int courseID;
    private boolean isEnroll;

    public int getUserID(){
        return userID;
    }
    public int getCourseID(){
        return courseID;
    }
    public boolean setEnroll(){
        return isEnroll;
    }
    public void setUserID(int userid){
        userID=userid;
    }
    public void setCourseID(int courseid){
        courseID=courseid;
    }
    public void setEnroll(boolean isenroll){
        isEnroll=isenroll;
    }
}
