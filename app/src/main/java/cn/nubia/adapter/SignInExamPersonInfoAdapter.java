package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/14.
 * 显示签到人员的Adapter
 */
public class SignInExamPersonInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public SignInExamPersonInfoAdapter(List<String> list, Context ctx) {
        mContext = ctx;
        mList = list;
    }

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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.activity_admin_person_info_item, null);
            mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.admin_personInfo_headViewImage);
            mViewHolder.mNameStudentNo = (TextView) convertView.findViewById(R.id.admin_personInfo_contentTextView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mNameStudentNo.setText(mList.get(position));
        mViewHolder.mImage.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }

    static class ViewHolder {
        TextView mNameStudentNo;
        ImageView mImage;
    }
}
