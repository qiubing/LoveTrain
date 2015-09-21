package cn.nubia.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.ExamScoreMsg;
import cn.nubia.service.CommunicateService;

/**
 * Created by JiangYu on 2015/9/19.
 */
public class AdminExamScoreInputAdapter extends BaseAdapter{
    private List<ExamScoreMsg> mExamScoreList;
    private Context mContext;

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        EditText scoreET;
    }

    public AdminExamScoreInputAdapter(Context context,List<ExamScoreMsg> examScoreList){
        super();
        mContext = context;
        mExamScoreList = examScoreList;
    }

    public List<ExamScoreMsg> getExamScoreList(){
        return mExamScoreList;
    }

    @Override
    public int getCount() {
        return mExamScoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return mExamScoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_exam_inputscore_item, null);

            holder.usernameTV = (TextView) convertView.findViewById(R.id.manager_exam_inputscore_username);
            holder.idTV = (TextView) convertView.findViewById(R.id.manager_exam_inputscore_id);
            holder.scoreET = (EditText) convertView.findViewById(R.id.manager_exam_inputscore_score);

            holder.scoreET.setId(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.usernameTV.setText(mExamScoreList.get(position).getUserName());
        holder.idTV.setText(mExamScoreList.get(position).getUserID());

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
                    mExamScoreList.get(position).setExamScore(Float.parseFloat(input));
                    mExamScoreList.get(position).setOperateType(CommunicateService.OperateType.INSERT);
                } catch (Exception e) {
                    e.printStackTrace();
                    mExamScoreList.get(position).setExamScore(-1);
                }
            }
        });

        return convertView;
    }
}
