package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.AdminExamScoreInputAdapter;
import cn.nubia.adapter.CourseLevelSpinnerAdapter;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.ExamMsg;
import cn.nubia.entity.ExamScoreMsg;
import cn.nubia.service.ActivityInter;
import cn.nubia.service.CommunicateService;
import cn.nubia.service.URLMap;
import cn.nubia.util.DialogUtil;

public class AdminExamInputScoreActivity extends Activity {
    private ExamItem mExamItem;
    private List<ExamScoreMsg> mExamScoreList;
    private int mResultNum = 0;
    private boolean mNextPress;

    private ListView mExamScoreListView;
    private AdminExamScoreInputAdapter mExamScoreAdapter;
    private Button button;

    private TextView mManagerTitle;
    private ImageView mGoBack;

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
        public void alter(List<?> list,String URL){
            AdminExamInputScoreActivity.this.showOperateResult(list,URL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_input_score);
        connectService();
        //公共部分
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.title_activity_manager_score_input);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconectService();
                finish();
            }
        });

        holdView();
        setViewLogic();

    }

    @Override
    public void onStart(){
        super.onStart();
        mNextPress = true;
        Intent intent =getIntent();
        mExamItem = (ExamItem) intent.getSerializableExtra("ExamInfo");
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

    private void holdView(){
        mExamScoreListView = (ListView) findViewById(R.id.manager_exam_score_input_listview);
        View view = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.manager_exam_listviewfoot, null, false);
        mExamScoreListView.addFooterView(view);
        button = (Button) view.findViewById(R.id.manager_exam_score_input_button);
    }

    private void setViewLogic(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mNextPress ) {
                    if (checkData()) {
                        scoreUpload();
                    }
                    mNextPress = false;
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

    private void showOperateResult(List<?> list,String tagetURL){
        if(list==null){
            DialogUtil.showDialog(
                    AdminExamInputScoreActivity.this,"操作失败!",false);
        }else{
            if(tagetURL.equals(URLMap.URL_QUE_EXAMENROLLLIST)){
//                mExamScoreList = (List<ExamScoreMsg>) list;
                mExamScoreList = new ArrayList<ExamScoreMsg>();
                for(int i =0;i<10;i++){
                    ExamScoreMsg msg = new ExamScoreMsg();
                    msg.setUserID(String.valueOf(i));
                    msg.setUserName("name"+String.valueOf(i));
                    mExamScoreList.add(msg);
                }
                mExamScoreAdapter = new AdminExamScoreInputAdapter(this,mExamScoreList);
                mExamScoreListView.setAdapter(mExamScoreAdapter);
            }else if(tagetURL.equals(URLMap.URL_ADD_EXAMSCORE)){
                mResultNum++;
                if(mResultNum == mExamScoreList.size()) {
                    DialogUtil.showDialog(
                            AdminExamInputScoreActivity.this, "操作成功!", false);
                    mNextPress = true;
                }
            }
        }
    }
}
