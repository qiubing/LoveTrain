package cn.nubia.model;

/**
 * Created by LK on 2015/9/16.
 */
public class CreditCourse {
    private int course_index;
    private String course_name;
    private int course_total_credits;

    public int getCourse_index() {
        return course_index;
    }

    public void setCourse_index(int course_index) {
        this.course_index = course_index;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getCourse_total_credits() {
        return course_total_credits;
    }

    public void setCourse_total_credits(int course_total_credits) {
        this.course_total_credits = course_total_credits;
    }
}
