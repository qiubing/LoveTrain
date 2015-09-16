package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class AdminExamInputScoreActivity extends Activity {

    private ListView mListView;
    private List<String> mDataList;
    private float[] scores;
    private Button button;

    private TextView mManagerTitle;
    private ImageView mGoBack;

    private void init() {
        mDataList = new ArrayList<>();
        mDataList.add("张三");
        mDataList.add("李四");
        mDataList.add("王五");
        mDataList.add("张三1");
        mDataList.add("李四1");
        mDataList.add("王五1");
        mDataList.add("张三2");
        mDataList.add("李四2");
        mDataList.add("王五2");
        mDataList.add("张三3");
        mDataList.add("李四3");
        mDataList.add("王五3");
        mDataList.add("张三4");
        mDataList.add("李四4");
        mDataList.add("王五4");

        scores = new float[mDataList.size()];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_input_score);

        mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.title_activity_manager_score_input);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.manager_exam_score_input_listview);
        init();
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.manager_exam_inputscore_item, null);
                    holder.usernameTV = (TextView) convertView.findViewById(R.id.manager_exam_inputscore_username);
                    holder.idTV = (TextView) convertView.findViewById(R.id.manager_exam_inputscore_id);

                    holder.scoreET = (EditText) convertView.findViewById(R.id.manager_exam_inputscore_score);
                    holder.scoreET.setId(position);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.usernameTV.setText(mDataList.get(position));
                holder.idTV.setText(mDataList.get(position));
                holder.scoreET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString().trim();
                        try {
                            scores[position] = Float.parseFloat(input);
                        } catch (Exception e) {
                            e.printStackTrace();
                            scores[position] = -1;
                        }
                    }
                });
                return convertView;
            }
        };

//        button = (Button) findViewById(R.id.manager_exam_score_input_button);
        View view = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.manager_exam_listviewfoot, null, false);
        button = (Button) view.findViewById(R.id.manager_exam_score_input_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (float score : scores) {
                    if (score == -1) {
                        DialogUtil.showToast(AdminExamInputScoreActivity.this, "输入的分数含非数字，请检查后重新输入！");
                        return;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < scores.length; i++) {
                    sb.append(mDataList.get(i) + "-" + scores[i] + ";");
                }
                DialogUtil.showToast(AdminExamInputScoreActivity.this, sb.toString());
            }
        });
        mListView.addFooterView(view);
        mListView.setAdapter(adapter);

    }

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        EditText scoreET;
    }
}
