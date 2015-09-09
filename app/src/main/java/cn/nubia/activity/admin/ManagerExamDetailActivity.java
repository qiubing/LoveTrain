package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

/**
 * Created by LK on 2015/9/9.
 */
public class ManagerExamDetailActivity extends Activity implements View.OnClickListener {
    private Button mInputScore;
    private Button mDeleteExam;
    private Button mEditExam;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_detail);

        mInputScore = (Button) findViewById(R.id.manager_exam_inputscorebtn);
        mDeleteExam = (Button) findViewById(R.id.manager_exam_deletebtn);
        mEditExam = (Button) findViewById(R.id.manager_exam_editbtn);
        mImageView = (ImageView) findViewById(R.id.manager_exam_detail_backImage);
        mImageView.setOnClickListener(this);
        mInputScore.setOnClickListener(this);
        mDeleteExam.setOnClickListener(this);
        mEditExam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.manager_exam_detail_backImage:
                finish();
                break;
            case R.id.manager_exam_inputscorebtn:
                intent = new Intent(ManagerExamDetailActivity.this, ManagerExamInputScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.manager_exam_deletebtn:
                new AlertDialog.Builder(ManagerExamDetailActivity.this).setTitle("确认要删除" + "吗?")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DialogUtil.showToast(ManagerExamDetailActivity.this, "你删除了考试:");
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.manager_exam_editbtn:
                intent = new Intent(ManagerExamDetailActivity.this, ManagerEditExamActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
