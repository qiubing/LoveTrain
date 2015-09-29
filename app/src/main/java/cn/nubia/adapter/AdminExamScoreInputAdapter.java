package cn.nubia.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List<ExamScoreMsg> mModifiedExamScoreList;

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        EditText scoreET;
    }

    public AdminExamScoreInputAdapter(Context context,List<ExamScoreMsg> examScoreList){
        super();
        mContext = context;
        mExamScoreList = examScoreList;
        mModifiedExamScoreList = new ArrayList<ExamScoreMsg>();
    }

    public List<ExamScoreMsg> getExamScoreList(){
        return mExamScoreList;
    }

    public List<ExamScoreMsg> getModifiedExamScoreList(){
        return mModifiedExamScoreList;
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
        holder.scoreET.setText(String.valueOf(mExamScoreList.get(position).getExamScore()));

        boolean isInital = new BigDecimal(mExamScoreList.get(position).getExamScore()).compareTo(new BigDecimal("0.0"))==0;
        if(isInital){
            holder.scoreET.setFocusableInTouchMode(true);
        }else{
            holder.scoreET.setFocusableInTouchMode(false);
        }

        holder.scoreET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.scoreET.getText().toString().contains(".")) {
                    if (holder.scoreET.getText().toString().indexOf(".", holder.scoreET.getText().toString().indexOf(".") + 1) > 0) {
                        Toast.makeText(mContext, "已经输入\".\"不能重复输入", Toast.LENGTH_SHORT);
                        holder.scoreET.setText(holder.scoreET.getText().toString().substring(0, holder.scoreET.getText().toString().length() - 1));
                        holder.scoreET.setSelection(holder.scoreET.getText().toString().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String input = s.toString().trim();
                String input = holder.scoreET.getText().toString().trim();
                ExamScoreMsg modifiedMsg = mExamScoreList.get(position);
                if (mModifiedExamScoreList.contains(modifiedMsg)) {
                    mModifiedExamScoreList.remove(modifiedMsg);
                    Log.e("jiangyu", "nomal remove " + mModifiedExamScoreList.toString());
                }

                ExamScoreMsg newMsg = new ExamScoreMsg();
                newMsg.setExamIndex(modifiedMsg.getExamIndex());
                newMsg.setUserID(modifiedMsg.getUserID());
                newMsg.setUserName(modifiedMsg.getUserName());
                newMsg.setOperateType(CommunicateService.OperateType.INSERT);

                try {
                    float newScore = Float.valueOf(input);
                    if (newScore != modifiedMsg.getExamScore()) {
                        newMsg.setExamScore(newScore);
                        mModifiedExamScoreList.add(newMsg);
                        Log.e("jiangyu", "nomal add " + mModifiedExamScoreList.toString());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    newMsg.setExamScore(-1);
                    mModifiedExamScoreList.add(newMsg);
                    Log.e("jiangyu", "error add " + mModifiedExamScoreList.toString());
                }
            }
        });

        return convertView;
    }
}
