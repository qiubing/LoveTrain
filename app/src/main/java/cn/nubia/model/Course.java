package cn.nubia.model;

/**
 * Created by LK on 2015/9/15.
 */
public class Course {
    private int course_index;
    private String course_name;
    private double exam_score;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public double getExam_score() {
        return exam_score;
    }

    public void setExam_score(double exam_score) {
        this.exam_score = exam_score;
    }
}
