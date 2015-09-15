package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class AdminEditExamActivity extends Activity {

    private Button mAddButton;
    private EditText mExamInfo;
    private Spinner mExamOneSpinner;
    private EditText mExamAddress;
    private EditText mExamTime;
    private EditText mExamCredit;

    private TextView mManagerTitle;
    private ImageView mGoBack;

    private List<String> mList;

    private void initSpinner() {
        mList = new ArrayList<>();
        mList.add("Java基础");
        mList.add("Android基础");
        mList.add("面向对象OO");
        mList.add("Java基础");
        mList.add("Android基础");
        mList.add("面向对象OO");
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater = LayoutInflater.from(AdminEditExamActivity.this);
                convertView = layoutInflater.inflate(R.layout.manager_add_exam_item, null);
                if (convertView != null) {
                    TextView coursenameTV = (TextView) convertView.findViewById(R.id.manager_add_exam_coursename);
                    TextView teachernameTV = (TextView) convertView.findViewById(R.id.manager_add_exam_teachername);
                    coursenameTV.setText(mList.get(position));
                    teachernameTV.setText(mList.get(position));
                }
                return convertView;
            }
        };

        mExamOneSpinner.setAdapter(baseAdapter);
        mExamOneSpinner.setSelection(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edit_exam);

        mExamOneSpinner = (Spinner) findViewById(R.id.activity_manager_edit_exam_one);
        mExamInfo = (EditText) findViewById(R.id.activity_manager_edit_exam_info);
        mExamAddress = (EditText) findViewById(R.id.activity_manager_edit_exam_address);
        mExamTime = (EditText) findViewById(R.id.activity_manager_edit_exam_time);
        mExamCredit = (EditText) findViewById(R.id.activity_manager_edit_exam_credit);
        mAddButton = (Button) findViewById(R.id.activity_manager_edit_exam_button);
        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.activity_manager_exam_detail_title);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSpinner();

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showToast(AdminEditExamActivity.this, "edit exam");
            }
        });
    }


}
