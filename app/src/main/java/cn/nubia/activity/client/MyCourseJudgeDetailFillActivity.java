package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;

import cn.nubia.activity.R;
import cn.nubia.entity.LessonJudgement;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class MyCourseJudgeDetailFillActivity extends Activity{
    private Button mConfirmButton;
    private EditText mComprehensiveEvaluationEditText;
    private EditText mSuggestionEditText;
    private ScrollView mContentScrollView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse_judge_detail_fill);

        holdView();
        setViewLogic();
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonJudgement judgement= new LessonJudgement();
                judgement.setContentApplicability(((RatingBar)findViewById(R.id
                        .mycourse_judge_detail_fill_contentapplicability_ratingbar)).getRating());
                judgement.setContentRationality(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_contentRationality_ratingbar)).getRating());
                judgement.setDiscussion(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_discussion_ratingbar)).getRating());
                judgement.setTimeRationality(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_timerationality_ratingbar)).getRating());
                judgement.setContentUnderstanding(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_contentunderstanding_ratingbar)).getRating());
                judgement.setExpressionAbility(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_expressionability_ratingbar)).getRating());
                judgement.setCommunication(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_communication_ratingbar)).getRating());
                judgement.setOrganization(((RatingBar) findViewById(R.id
                        .mycourse_judge_detail_fill_organization_ratingbar)).getRating());
                judgement.setComprehensiveEvaluation(
                        mComprehensiveEvaluationEditText.getText().toString());
                judgement.setSuggestion(
                        mSuggestionEditText.getText().toString());
            }
        };
    }

    private void holdView(){
        mConfirmButton =(Button) findViewById(
                R.id.mycourse_judge_detail_fill_confirmbutton);
        mComprehensiveEvaluationEditText =(EditText) findViewById(
                R.id.mycourse_judge_detail_fill_comprehensiveevaluation_edittext);
        mSuggestionEditText =(EditText) findViewById(
                R.id.mycourse_judge_detail_fill_suggestion_edittext);
        mContentScrollView =(ScrollView) findViewById(
                R.id.mycourse_judge_detail_fill_contentscrollview);
    }

    private void setViewLogic(){
        /**设置当输入框被触碰时，内容滚动界面不响应触碰事件，及手指在输入框上移动时，
         * 内容滚动界面不会滚动，只有输入框才会滚动*/
        mComprehensiveEvaluationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    mContentScrollView.requestDisallowInterceptTouchEvent(false);
                else
                    mContentScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mSuggestionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    mContentScrollView.requestDisallowInterceptTouchEvent(false);
                else
                    mContentScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        /**监听确认按钮，进行提交动作*/
        mConfirmButton.setOnClickListener(makeConfirmOnClickListener());
    }
}
