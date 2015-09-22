package cn.nubia.entity;

import java.io.Serializable;

/**
 * Created by hexiao on 2015/9/19.
 */
public class SignUpItem implements Serializable {
    private String userName;
    private String userID;
    private int courseID;
    private boolean isEnroll;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isEnroll() {
        return isEnroll;
    }

    public void setIsEnroll(boolean isEnroll) {
        this.isEnroll = isEnroll;
    }
}
