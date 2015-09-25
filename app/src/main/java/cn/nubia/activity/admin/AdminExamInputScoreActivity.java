package cn.nubia.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import cn.nubia.activity.BaseCommunicateActivity;
import cn.nubia.activity.R;
import cn.nubia.adapter.AdminExamScoreInputAdapter;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.ExamMsg;
import cn.nubia.entity.ExamScoreMsg;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;

public class AdminExamInputScoreActivity extends BaseCommunicateActivity{
//    private ExamItem mExamItem;
//    private List<ExamScoreMsg> mExamScoreList;
    private  List<ExamScoreMsg> mModifiedExamScoreList;
    private int mResultNum = 0;
    private boolean mNextPressReady;

    private ListView mExamScoreListView;
    private AdminExamScoreInputAdapter mExamScoreAdapter;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_input_score);

        //公共部分
        ((TextView) findViewById(R.id.manager_head_title)).setText(R.string.title_activity_manager_score_input);

        holdView();
        setViewLogic();

    }

    @Override
    public void onStart(){
        super.onStart();
        connectService();
        mNextPressReady = true;

        Intent intent =getIntent();
        ExamItem mExamItem = (ExamItem) intent.getSerializableExtra("ExamInfo");

        final ExamMsg examMsg = new ExamMsg();
        examMsg.setExamIndex(mExamItem.getIndex());
        examMsg.setOperateType(CommunicateService.OperateType.QUERY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mBinder==null){}
                mBinder.communicate(examMsg, new Inter(), URLMap.URL_QUE_EXAMENROLLLIST);
            }
        }).start();

    }

    @Override
    public void onStop(){
        super.onStop();
        disconectService();
    }

    private void holdView(){
        mExamScoreListView = (ListView) findViewById(R.id.manager_exam_score_input_listview);
        View view = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.manager_exam_listviewfoot, null, false);
        mExamScoreListView.addFooterView(view);
        button = (Button) view.findViewById(R.id.manager_exam_score_input_button);
    }

    private void setViewLogic(){
        (findViewById(R.id.manager_goback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNextPressReady) {
                    mModifiedExamScoreList = mExamScoreAdapter.getModifiedExamScoreList();
                    if (checkData()) {
                        scoreUpload();
                        mNextPressReady = false;
                    }
                }
            }
        });
    }

    private boolean checkData(){
        Boolean checkResult = true;
        if(mModifiedExamScoreList == null||mModifiedExamScoreList.size()==0) {
            Toast.makeText(
                    AdminExamInputScoreActivity.this,
                    "成绩录入名单为空！",Toast.LENGTH_SHORT).show();
            checkResult = false;
        }else{
            for (ExamScoreMsg msg : mModifiedExamScoreList) {
                if (msg.getExamScore() == -1) {
                    Toast.makeText(
                            AdminExamInputScoreActivity.this,
                            "输入的成绩含有非法数字，请检查后重新输入！",Toast.LENGTH_SHORT).show();
                    checkResult = false;
                    break;
                }
//                else
//                    msg.setExamIndex(mExamItem.getIndex());
            }
        }
        return checkResult;
    }

    private void scoreUpload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBinder.communicate(mModifiedExamScoreList, new Inter(), URLMap.URL_ADD_EXAMSCORE);
                Log.e("jiangyu", mModifiedExamScoreList.toString());
            }
        }).start();
    }

    @Override
    protected void handleResponse(Map<String,?> response,String responseURL){
        if(responseURL.equals(URLMap.URL_QUE_EXAMENROLLLIST)){
            mNextPressReady = true;
            if(response==null){
                DialogMaker.make(AdminExamInputScoreActivity.this,
                        AdminExamInputScoreActivity.this, "连接服务器失败!", true);
            }else {
                String operateResult = (String) response.get("operateResult");
                if (operateResult.equals("success")) {
                    List<ExamScoreMsg> mExamScoreList = (List<ExamScoreMsg>) response.get("detail");

                    if (mExamScoreList != null) {
                        mExamScoreAdapter =
                                new AdminExamScoreInputAdapter(this, mExamScoreList);
                        mExamScoreListView.setAdapter(mExamScoreAdapter);
                    }
                } else if (operateResult.equals("failure")) {
                    String message = (String) response.get("message");
                    DialogMaker.make(AdminExamInputScoreActivity.this,
                            AdminExamInputScoreActivity.this, "获取考试报名名单失败：\n" +
                                    message, true);
                }
            }
        }else if(responseURL.equals(URLMap.URL_ADD_EXAMSCORE)){
            mResultNum++;
            if(response==null){
                Toast.makeText(
                         AdminExamInputScoreActivity.this, "一个成绩录入未响应!", Toast.LENGTH_SHORT).show();
            }else{
                String operateResult = (String)response.get("operateResult");
                if(operateResult.equals("success")) {
                    Toast.makeText(AdminExamInputScoreActivity.this, "一个成绩录入成功！"
                            ,Toast.LENGTH_SHORT).show();
                }else if(operateResult.equals("failure")) {
                    String message = (String) response.get("message");
                    Toast.makeText(AdminExamInputScoreActivity.this, "一个成绩录入失败：\n" +
                            message, Toast.LENGTH_SHORT).show();
                }
            }
            if(mResultNum == mModifiedExamScoreList.size()) {
                mNextPressReady = true;
                 DialogMaker.make(AdminExamInputScoreActivity.this,
                         AdminExamInputScoreActivity.this, "考试成绩录入完成!", true);
            }
        }
    }
}
