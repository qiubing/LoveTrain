package cn.nubia.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by WJ on 2015/9/6.
 */
public class UserInfo implements Serializable {
    private String mUserID;
    private String mUserName;
    private String mUserPasswd;
    private Boolean mGender;
    private String mUserIconURL;  //用户图标链接
    private long mLastLoginTime;
    private long mRegisterTime;
    private int mUserTotalCredits;
    private Drawable mUserIcon;

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserPasswd() {
        return mUserPasswd;
    }

    public void setUserPasswd(String mUserPasswd) {
        this.mUserPasswd = mUserPasswd;
    }

    public Boolean getGender() {
        return mGender;
    }

    public void setGender(Boolean mGender) {
        this.mGender = mGender;
    }

    public String getUserIconURL() {
        return mUserIconURL;
    }

    public void setUserIconURL(String mUserIconURL) {
        this.mUserIconURL = mUserIconURL;
    }

    public long getLastLoginTime() {
        return mLastLoginTime;
    }

    public void setLastLoginTime(long mLastLoginTime) {
        this.mLastLoginTime = mLastLoginTime;
    }

    public long getRegisterTime() {
        return mRegisterTime;
    }

    public void setRegisterTime(long mRegisterTime) {
        this.mRegisterTime = mRegisterTime;
    }

    public int getUserTotalCredits() {
        return mUserTotalCredits;
    }

    public void setUserTotalCredits(int userTotalCredits) {
        mUserTotalCredits = userTotalCredits;
    }

    public Drawable getUserIcon() {
        return mUserIcon;
    }

    public void setUserIcon(Drawable userIcon) {
        this.mUserIcon = userIcon;
    }

}
