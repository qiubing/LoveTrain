package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.adapter.AdminExamScoreInputAdapter;
import cn.nubia.component.DialogMaker;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.ExamMsg;
import cn.nubia.entity.ExamScoreMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

public class AdminExamInputScoreActivity extends Activity {

    private List<ExamScoreMsg> mExamScoreList;
    private int mResultNum = 0;
    private boolean mNextPressReady;

    private ListView mExamScoreListView;
    private Button button;

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
            AdminExamInputScoreActivity.this.handleResponse(response,responseURL);;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        ExamItem examItem = (ExamItem) intent.getSerializableExtra("ExamInfo");

        final ExamMsg examMsg = new ExamMsg();
        examMsg.setExamIndex(examItem.getIndex());
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
                if(mNextPressReady) {
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
        for (ExamScoreMsg msg : mExamScoreList) {
            if (msg.getExamScore() == -1) {
                DialogUtil.showToast(AdminExamInputScoreActivity.this, "输入的分数含非数字，请检查后重新输入！");
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }

    private void connectService(){
        Intent intent = new Intent(
                AdminExamInputScoreActivity.this, CommunicateService.class);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    private void disconectService(){
        unbindService(mConn);
    }

    private void scoreUpload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mBinder==null){}
                mBinder.loopCommunicate(mExamScoreList, new Inter(), URLMap.URL_ADD_EXAMSCORE);
            }
        }).start();
    }

    private void handleResponse(Map<String,?> response,String responseURL){
        if(response==null){
            mNextPressReady = true;
            DialogMaker.make(
                    AdminExamInputScoreActivity.this, "操作失败!", true).show();
        }else{
            if(responseURL.equals(URLMap.URL_QUE_EXAMENROLLLIST)){
                mNextPressReady = true;
                String operateResult = (String)response.get("operateResult");
                if(operateResult.equals("success")) {
                    mExamScoreList = (List<ExamScoreMsg>) response.get("detail");

//                    mExamScoreList = new ArrayList<ExamScoreMsg>();
//                    for(int i =0;i<5;i++){
//                        ExamScoreMsg msg = new ExamScoreMsg();
//                        msg.setUserID(String.valueOf(i));
//                        msg.setUserName("name" + String.valueOf(i));
//                        mExamScoreList.add(msg);
//                    }
                    if(mExamScoreList!=null){
                        AdminExamScoreInputAdapter mExamScoreAdapter =
                                new AdminExamScoreInputAdapter(this,mExamScoreList);
                        mExamScoreListView.setAdapter(mExamScoreAdapter);
                    }
                }else if(operateResult.equals("failure")) {
                    String message = (String) response.get("message");
                    DialogMaker.make(
                            AdminExamInputScoreActivity.this, "获取考试报名名单失败：\n" +
                                    message, true).show();
                }
            }else if(responseURL.equals(URLMap.URL_ADD_EXAMSCORE)){
                mResultNum++;
                String operateResult = (String)response.get("operateResult");

                if(operateResult.equals("success")) {
                    Toast.makeText(AdminExamInputScoreActivity.this, "一个成绩添加成功：\n"
                            ,Toast.LENGTH_SHORT);
                  }else if(operateResult.equals("failure")) {
                    String message = (String) response.get("message");
                    Toast.makeText(AdminExamInputScoreActivity.this, "一个成绩添加失败：\n" +
                            message, Toast.LENGTH_SHORT);
                }

                if(mResultNum == mExamScoreList.size()) {
                    mNextPressReady = true;
                    DialogMaker.make(
                           AdminExamInputScoreActivity.this, "操作完成!", true).show();
                }
            }
        }
    }
}
