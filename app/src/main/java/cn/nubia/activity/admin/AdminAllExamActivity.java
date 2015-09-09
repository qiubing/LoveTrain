package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
import cn.nubia.adapter.ExamAdapter;
import cn.nubia.entity.ExamItem;

/**
 * Created by WJ on 2015/9/7.
 */
public class AdminAllExamActivity extends Activity{
    private ListView mAllExamListView;
    private ExamAdapter mExamAdapter;
    private List<ExamItem> mExamList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_all_exam);
        initView();
    }

    public void initView(){
        mAllExamListView = (ListView) findViewById(R.id.admin_all_exam_list);
        mExamAdapter = new ExamAdapter(mExamList,AdminAllExamActivity.this);
        mAllExamListView.setAdapter(mExamAdapter);
        mAllExamListView.setOnClickListener((View.OnClickListener) new ExamListOnItemClickListener());

        //for Debug
        Message msg = hand.obtainMessage();
        msg.what = 1;
        hand.sendMessage(msg);
    }

    Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            /*For DEBUG*/
            if(msg.what == 1)
            {
                for (int i = 0; i<10;i++){
                    ExamItem examItem = new ExamItem();
                    examItem.setName("Java基础");
                    examItem.setLocale("C-3室");
                    examItem.setStartTime("7月8号9点10分");
                    examItem.setStartTime("7月8号10点10分");
                    mExamList.add(0,examItem);
                }
            }
        }
    };

    private class ExamListOnItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(AdminAllExamActivity.this, EmptyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ExamInfo",mExamList.get(arg2));
            intent.putExtra("value",bundle);
            startActivity(intent);
        }
    }
}
