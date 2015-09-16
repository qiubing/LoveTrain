package cn.nubia.model.admin;

/**
 * Created by LK on 2015/9/16.
 */
public class ExamUser {
    private String user_id;
    private String user_name;
    private double exam_score;

    public ExamUser() {
    }

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

    public double getExam_score() {
        return exam_score;
    }

    public void setExam_score(double exam_score) {
        this.exam_score = exam_score;
    }
}
