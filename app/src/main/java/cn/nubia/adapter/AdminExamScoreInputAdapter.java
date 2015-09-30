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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        MyTextWatcher textWatcher;
        TextView usernameTV;
        TextView idTV;
        EditText scoreET;
    }
    class MyTextWatcher implements TextWatcher{
        private EditText mEditText;
        private int mPosition;

        public MyTextWatcher(EditText editText,int position){
            mEditText = editText;
            mPosition = position;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText.getText().toString().contains(".")) {
                if (mEditText.getText().toString().indexOf(".", mEditText.getText().toString().indexOf(".") + 1) > 0) {
                    Toast.makeText(mContext, "已经输入\".\"不能重复输入", Toast.LENGTH_SHORT);
                    mEditText.setText(mEditText.getText().toString().substring(0, mEditText.getText().toString().length() - 1));
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = mEditText.getText().toString().trim();
            Log.e("jiangyu",input);
            Log.e("jiangyu",String.valueOf(mPosition));

            ExamScoreMsg modifiedMsg = mExamScoreList.get(mPosition);

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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.scoreET.removeTextChangedListener(holder.textWatcher);
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

        holder.textWatcher = new MyTextWatcher(holder.scoreET,position);
        holder.scoreET.addTextChangedListener(holder.textWatcher);
        holder.scoreET.clearFocus();
        return convertView;
    }
}
