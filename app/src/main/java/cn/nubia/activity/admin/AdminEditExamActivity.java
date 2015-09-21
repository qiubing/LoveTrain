package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

public class AdminEditExamActivity extends Activity {

    private Button mAddButton;
    private EditText mExamInfo;
    private EditText mExamTitle;
    private EditText mExamAddress;
    private EditText mExamStartTime;
    private EditText mExamEndTime;
    private EditText mExamCredit;
    private TextView mTitleText;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    private static final String URL = Constant.BASE_URL + "/exam/add.do";
    private ExamItem mExamItemExamEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_exam);

        mExamItemExamEdit = (ExamItem) getIntent().getSerializableExtra("ExamInfo");


        mExamTitle = (EditText) findViewById(R.id.activity_manager_add_exam_one);
        mExamInfo = (EditText) findViewById(R.id.activity_manager_add_exam_info);
        mExamAddress = (EditText) findViewById(R.id.activity_manager_add_exam_address);
        mExamStartTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_start);
        mExamEndTime = (EditText) findViewById(R.id.activity_manager_add_exam_time_end);
        mExamCredit = (EditText) findViewById(R.id.activity_manager_add_exam_credit);
        mAddButton = (Button) findViewById(R.id.activity_manager_add_exam_button);

        mTitleText = (TextView) findViewById(R.id.sub_page_title);
        mTitleText.setText("修改考试");
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        mAddButton.setText("确认修改");

        mExamTitle.setText(mExamItemExamEdit.getName());
        mExamInfo.setText(mExamItemExamEdit.getDescription());
        mExamAddress.setText(mExamItemExamEdit.getLocale());
        mExamStartTime.setText(mExamItemExamEdit.getStartTime()+"");
        mExamEndTime.setText(mExamItemExamEdit.getEndTime()+"");
        mExamCredit.setText(mExamItemExamEdit.getExamCredits());

        //initSpinner();

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upData();

            }
        });
    }


    void upData(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.add("course_index", mExamItemExamEdit.getCourseIndex() + "");
        requestParams.add("exam_index", mExamItemExamEdit.getIndex() + "");
        requestParams.add("exam_name",  mExamItemExamEdit.getName());
        requestParams.add("exam_description",  mExamItemExamEdit.getDescription());
        requestParams.add("locale",  mExamItemExamEdit.getLocale());
        requestParams.add("start_time",  mExamItemExamEdit.getStartTime()+"");
        requestParams.add("end_time", mExamItemExamEdit.getEndTime()+"");
        requestParams.add("exam_credits", mExamItemExamEdit.getExamCredits()+"");

        AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
    }

    MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "addExam" + "onSuccess");
            try {
                int code = response.getInt("code");
                Log.i("huhu", "addExam" + code);
                boolean result = response.getBoolean("result");
                Log.i("huhu", "addExam" + result);
                boolean isOk = response.getBoolean("data");
                Log.i("huhu", "addExam" + isOk);
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + ","+result + "," +isOk);
                if(result && code == 0 && isOk) {
                    Toast.makeText(AdminEditExamActivity .this, "success", Toast.LENGTH_SHORT).show();
                    mExamTitle.setText("");
                    mExamInfo.setText("");
                    mExamAddress.setText("");
                    mExamStartTime.setText("");
                    mExamEndTime.setText("");
                    mExamCredit.setText("");
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            //mExamAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };

    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }


}
