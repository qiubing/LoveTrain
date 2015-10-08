package cn.nubia.model;

/**
 * Created by LK on 2015/9/16.
 */
public class Exam {
    private int exam_index;
    private String exam_name;
    private double exam_score;

    public int getExam_index() {
        return exam_index;
    }

    public void setExam_index(int exam_index) {
        this.exam_index = exam_index;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public double getExam_score() {
        return exam_score;
    }

    public void setExam_score(double exam_score) {
        this.exam_score = exam_score;
    }

}
