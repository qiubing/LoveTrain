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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.LessonJudgementMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyCourseJudgeDetailFillActivity extends Activity{
    private int mLessonIndex;
    private Boolean mNextPressReady;

    private Button mConfirmButton;
    private EditText mComprehensiveEvaluationEditText;
    private EditText mSuggestionEditText;
    private ScrollView mContentScrollView;

    private CommunicateService.CommunicateBinder mBinder;
    private final ServiceConnection mConn = new ServiceConnection() {
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
        public void handleResponse(Map<String,?> response,String responseURL){
            ClientMyCourseJudgeDetailFillActivity.this.handleResponse(response,responseURL);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse_judge_detail_fill);

        //公用部分
        TextView mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.activity_mycourse_judge_detail_fill_title_textView);

        connectService();
        holdView();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        connectService();
        mNextPressReady = true;

        Intent intent = getIntent();
        mLessonIndex = intent.getIntExtra("lessonIndex",-1);
    }

    @Override
    public void onStop(){
        super.onStop();
        disconectService();
    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNextPressReady) {
                   if (checkData()) {
                        LessonJudgementMsg judgement = new LessonJudgementMsg();
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
                                mComprehensiveEvaluationEditText.getText().toString().trim());
                        judgement.setSuggestion(
                                mSuggestionEditText.getText().toString().trim());
                        judgement.setLessonIndex(mLessonIndex);

                        judgement.setOperateType(CommunicateService.OperateType.INSERT);
                        mBinder.communicate(judgement, new Inter(), URLMap.URL_JUDGE_LESSON);
                       mNextPressReady = false;
                    }
                }
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
        (findViewById(R.id.manager_goback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientMyCourseJudgeDetailFillActivity.this.finish();
            }
        });
    }

    private boolean checkData(){
        if(mComprehensiveEvaluationEditText.getText().toString().trim().equals("")){
            Toast.makeText(this, "课程综合评价不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mSuggestionEditText.getText().toString().trim().equals("")){
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

    private void disconectService(){
        unbindService(mConn);
    }

    private void handleResponse(Map<String,?> response,String responseURL){
        mNextPressReady = true;
        if(response==null){
            DialogUtil.showDialog(
                    ClientMyCourseJudgeDetailFillActivity.this,"操作失败!",false);
        }else{
            String operateResult = (String)response.get("operateResult");
            if(operateResult.equals("success")) {
                DialogUtil.showDialog(
                        ClientMyCourseJudgeDetailFillActivity.this, "课程评价成功!", true);
            }else if(operateResult.equals("failure")) {
                String message = (String) response.get("message");
                DialogUtil.showDialog(
                        ClientMyCourseJudgeDetailFillActivity.this,"课程评价失败：\n"+
                                message,false);
            }
        }
    }
}
