package cn.nubia.model.admin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LK on 2015/9/14.
 */
public class User {
    @SerializedName("user_index")
    private int user_index;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("user_name")
    private String userName;
    private boolean gender;
    @SerializedName("last_login_time")
    private String lastLoginTime;
    @SerializedName("register_time")
    private String registerTime;
    @SerializedName("UserTotalCredits")
    private int userTotalCredits;
    @SerializedName("icon_url")
    private String userIconURL;

    public User() {

    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public User(String userID, String userName, boolean gender, String lastLoginTime, String registerTime, int userTotalCredits, String userIconURL, int user_index) {

        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
        this.lastLoginTime = lastLoginTime;
        this.registerTime = registerTime;
        this.userTotalCredits = userTotalCredits;
        this.userIconURL = userIconURL;
        this.user_index = user_index;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public void setUserTotalCredits(int userTotalCredits) {
        this.userTotalCredits = userTotalCredits;
    }

    public void setUserIconURL(String userIconURL) {
        this.userIconURL = userIconURL;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isGender() {
        return gender;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public int getUserTotalCredits() {
        return userTotalCredits;
    }

    public String getUserIconURL() {
        return userIconURL;
    }
}
