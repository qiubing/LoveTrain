package cn.nubia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/14.
 * 显示签到人员的Adapter
 */
public class SignInExamPersonInfoAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Map.Entry<String,String>> mList;

    public SignInExamPersonInfoAdapter(List<Map.Entry<String,String>> list, Context ctx) {
        mContext = ctx;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.score_course_item, null);
            mViewHolder.mNameTextView = (TextView) convertView.findViewById(R.id.score_course_coursename);
            mViewHolder.mIdTextView = (TextView) convertView.findViewById(R.id.score_course_address);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Log.e("wj getKey",mList.get(position).getKey());
        Log.e("wj getValue",mList.get(position).getValue());
        mViewHolder.mNameTextView.setText(mList.get(position).getKey());
        mViewHolder.mIdTextView.setText(mList.get(position).getValue());
        return convertView;
    }

    static class ViewHolder {
        TextView mNameTextView;
        TextView mIdTextView;
    }
}
