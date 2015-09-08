package cn.nubia.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;

public class ManagerExamInputScoreActivity extends Activity {

    private ListView mListView;
    private List<String> mDataList;

    private void init() {
        mDataList = new ArrayList<>();
        mDataList.add("zhangsan");
        mDataList.add("lisi");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_exam_input_score);

        mListView = (ListView) findViewById(R.id.manager_exam_score_input_listview);
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
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.usernameTV.setText(mDataList.get(position));
                holder.scoreET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("AAAAAAA", mDataList.get(position)+":"+s.toString());
                    }
                });
                return convertView;
            }
        };
        mListView.setAdapter(adapter);

    }

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        EditText scoreET;
    }
}
