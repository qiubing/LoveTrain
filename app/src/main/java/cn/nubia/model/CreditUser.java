package cn.nubia.model;

/**
 * Created by LK on 2015/9/16.
 */
public class CreditUser {
    private String user_id;
    private String user_name;
    private int user_total_credits;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_total_credits() {
        return user_total_credits;
    }

    public void setUser_total_credits(int user_total_credits) {
        this.user_total_credits = user_total_credits;
    }
}
