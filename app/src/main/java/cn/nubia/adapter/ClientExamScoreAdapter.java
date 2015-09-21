package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.ExamResultItem;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 14:53
 */
public class ClientExamScoreAdapter extends BaseAdapter{
    private final List<ExamResultItem> mResultList;
    private final Context mContext;
    public ClientExamScoreAdapter(List<ExamResultItem> resultList,Context context){
        this.mResultList = resultList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHold;
        if (convertView == null){
            viewHold = new ViewHolder();
            convertView = View.inflate(mContext,R.layout.activity_user_exam_score_detail_item,null);
            viewHold.mLessonName = (TextView) convertView.findViewById(R.id.exam_course_title);
            viewHold.mExamScore = (TextView) convertView.findViewById(R.id.exam_score_one);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHolder)convertView.getTag();
        }
        viewHold.mLessonName.setText(mResultList.get(position).getmLessonName());
        if (mResultList.get(position).getmExamScore() < 0){
            viewHold.mExamScore.setText("未考试");
        }else {
            viewHold.mExamScore.setText(String.valueOf(mResultList.get(position).getmExamScore()));
        }

        return convertView;
    }

    public final class ViewHolder{
        TextView mLessonName;
        TextView mExamScore;
    }
}
