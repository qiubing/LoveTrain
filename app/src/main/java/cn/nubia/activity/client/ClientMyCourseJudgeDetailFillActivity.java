package cn.nubia.activity.client;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.LessonJudgement;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyCourseJudgeDetailFillActivity extends Activity{
    private Button mConfirmButton;
    private Button mBackButton;
    private EditText mComprehensiveEvaluationEditText;
    private EditText mSuggestionEditText;
    private ScrollView mContentScrollView;

    private CommunicateService.CommunicateBinder mBinder;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (CommunicateService.CommunicateBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    public class Inter implements ActivityInter {
        public void alter(List<?> list,CommunicateService.OperateType type){
            ClientMyCourseJudgeDetailFillActivity.this.showOperateResult((List<String>)list,type);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse_judge_detail_fill);

        connectService();
        holdView();
        setViewLogic();
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    LessonJudgement judgement = new LessonJudgement();
                    judgement.setContentApplicability(((RatingBar) findViewById(R.id
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

                    mBinder.communicate(judgement, new Inter(),CommunicateService.OperateType.INSERT);
                }
            }
        };
    }

    private void holdView(){
        mConfirmButton =(Button) findViewById(
                R.id.mycourse_judge_detail_fill_confirmbutton);
        mBackButton =(Button) findViewById(
                R.id.mycourse_judge_detail_fill_backbutton);
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
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientMyCourseJudgeDetailFillActivity.this.finish();
            }
        });
    }

    private boolean checkData(){
        if(mComprehensiveEvaluationEditText.getText().toString()==null){
            Toast.makeText(this, "课程综合评价不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mSuggestionEditText.getText().toString()==null){
            Toast.makeText(this, "课程建议不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void connectService(){
        Intent intent = new Intent(
                ClientMyCourseJudgeDetailFillActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void showOperateResult(List<String> list,CommunicateService.OperateType type) {
        Boolean result = Boolean.getBoolean(list.get(0));
        if(result)
            DialogUtil.showDialog(
                    ClientMyCourseJudgeDetailFillActivity.this, "课程评价成功!", false);
        else
            DialogUtil.showDialog(
                    ClientMyCourseJudgeDetailFillActivity.this,"课程评价失败!",false);

    }
}
