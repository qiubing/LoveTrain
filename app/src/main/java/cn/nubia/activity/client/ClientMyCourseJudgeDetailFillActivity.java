package cn.nubia.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonJudgementMsg;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;

/**
 * Created by JiangYu on 2015/9/6.
 */
public class ClientMyCourseJudgeDetailFillActivity extends BaseCommunicateActivity{
    private int mLessonIndex;
    private Boolean mNextPressReady;
    private CommunicateService.OperateType mOperateType;
    private Button mConfirmButton;
    private EditText mComprehensiveEvaluationEditText;
    private EditText mSuggestionEditText;
    private ScrollView mContentScrollView;

    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private ImageView loadingView;
    private RotateAnimation refreshingAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycourse_judge_detail_fill);

        //公用部分
        ((TextView) findViewById(R.id.sub_page_title))
                .setText(R.string.activity_mycourse_judge_detail_fill_title_textView);

        holdView();
        setViewLogic();
    }

    @Override
    public void onStart(){
        super.onStart();
        mNextPressReady = true;
        setIsIndicator(false);
        Intent intent = getIntent();
        mOperateType = (CommunicateService.OperateType) intent.getSerializableExtra("operate");
        mLessonIndex = intent.getIntExtra("lessonIndex",-1);

        if(mOperateType == CommunicateService.OperateType.QUERY){
            final LessonJudgementMsg judgementMsg = new LessonJudgementMsg();
            judgementMsg.setLessonIndex(mLessonIndex);
            judgementMsg.setOperateType(CommunicateService.OperateType.QUERY);
            judgementMsg.setUserID(Constant.user.getUserID());

            initLoadingUI();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //noinspection StatementWithEmptyBody
                    while(mBinder==null){}
                    mBinder.communicate(judgementMsg, new Inter(), URLMap.URL_QUE_MYJUDGEMENT);
                }
            }).start();
        }
    }

//    @Override
//    protected void onBinderCompleted() {
//
//    }

    private View.OnClickListener makeConfirmOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperateType == CommunicateService.OperateType.INSERT) {
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


                            initLoadingUI();
                            judgement.setLessonIndex(mLessonIndex);
                            judgement.setUserID(Constant.user.getUserID());
                            judgement.setOperateType(CommunicateService.OperateType.INSERT);
                            mConfirmButton.setText("评价提交中...");
                            mBinder.communicate(judgement, new Inter(), URLMap.URL_ADD_JUDGEMENT);
                            mNextPressReady = false;
                        }
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

        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout) findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
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

        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        (findViewById(R.id.title_back_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientMyCourseJudgeDetailFillActivity.this.finish();
            }
        });
    }

    private void initLoadingUI(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        loadingView = (ImageView)findViewById(R.id.loading_iv);
        loadingView.setVisibility(View.VISIBLE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(ClientMyCourseJudgeDetailFillActivity.this, R.anim.rotating);
        //添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        loadingView.startAnimation(refreshingAnimation);
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

    private void setIsIndicator(boolean isIndicator){
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_contentapplicability_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_contentRationality_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_discussion_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_timerationality_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_contentunderstanding_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_expressionability_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_communication_ratingbar)).setIsIndicator(isIndicator);
        ((RatingBar) findViewById(R.id
                .mycourse_judge_detail_fill_organization_ratingbar)).setIsIndicator(isIndicator);
        mComprehensiveEvaluationEditText.setEnabled(!isIndicator);
        mSuggestionEditText.setEnabled(!isIndicator);
        if(isIndicator){
            mConfirmButton.setVisibility(View.GONE);
        }else{
            mConfirmButton.setVisibility(View.VISIBLE);
        }
    }

    private void initViewData(LessonJudgementMsg msg) {
        if(msg!=null) {
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_contentapplicability_ratingbar)).setRating(msg.getContentApplicability());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_contentRationality_ratingbar)).setRating(msg.getContentRationality());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_discussion_ratingbar)).setRating(msg.getDiscussion());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_timerationality_ratingbar)).setRating(msg.getTimeRationality());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_contentunderstanding_ratingbar)).setRating(msg.getContentUnderstanding());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_expressionability_ratingbar)).setRating(msg.getExpressionAbility());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_communication_ratingbar)).setRating(msg.getCommunication());
            ((RatingBar) findViewById(R.id
                    .mycourse_judge_detail_fill_organization_ratingbar)).setRating(msg.getOrganization());
            mComprehensiveEvaluationEditText.setText(msg.getComprehensiveEvaluation());
            mSuggestionEditText.setText(msg.getSuggestion());
        }
    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        mNextPressReady = true;
        mConfirmButton.setText("评价");
        if(response==null){
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            DialogMaker.finishCurrentDialog(ClientMyCourseJudgeDetailFillActivity.this,
                    ClientMyCourseJudgeDetailFillActivity.this, "连接服务器失败!", false);
        }else{
            if(responseURL.equals(URLMap.URL_ADD_JUDGEMENT)) {
                String operateResult = (String) response.get("operateResult");
                if (operateResult.equals("success")) {
                    DialogMaker.finishCurrentDialog(ClientMyCourseJudgeDetailFillActivity.this,
                            ClientMyCourseJudgeDetailFillActivity.this, "课程评价成功!", true);
                } else if (operateResult.equals("failure")) {
                    String message = (String) response.get("message");
                    DialogMaker.finishCurrentDialog(ClientMyCourseJudgeDetailFillActivity.this,
                            ClientMyCourseJudgeDetailFillActivity.this, "课程评价失败：\n" +
                                    message, false);
                }
            }else if(responseURL.equals(URLMap.URL_QUE_MYJUDGEMENT)){
                String operateResult = (String) response.get("operateResult");
                if (operateResult.equals("success")) {
                    @SuppressWarnings("unchecked")
                    List<LessonJudgementMsg> mExamScoreList = (List<LessonJudgementMsg>) response.get("detail");

                    if (!mExamScoreList.isEmpty()) {
                        if(mExamScoreList.size()>0)
                            initViewData(mExamScoreList.get(0));
                            setIsIndicator(true);
                    }
                } else if (operateResult.equals("failure")) {
                    loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                    String message = (String) response.get("message");
                    DialogMaker.finishCurrentDialog(ClientMyCourseJudgeDetailFillActivity.this,
                            ClientMyCourseJudgeDetailFillActivity.this, "获取评价信息失败：\n" +
                                    message, true);
                }
            }
        }
    }
}
