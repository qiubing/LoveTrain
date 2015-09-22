package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.ShareCourseMsg;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 15:53
 */
public class ClientShareCourseAdapter extends BaseAdapter {
    private final List<ShareCourseMsg> mCourseList;
    private final Context mContext;

    public ClientShareCourseAdapter(List<ShareCourseMsg> courseList,Context context){
        this.mContext = context;
        this.mCourseList = courseList;
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
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
            convertView = View.inflate(mContext, R.layout.activity_user_share_course_detail_item,null);
            viewHold.mLessonName = (TextView) convertView.findViewById(R.id.share_course_title);
            viewHold.mDate = (TextView) convertView.findViewById(R.id.share_course_date);
            viewHold.mTime = (TextView) convertView.findViewById(R.id.share_course_time);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHolder) convertView.getTag();
        }
        viewHold.mLessonName.setText(mCourseList.get(position).getCourseName());
        Date date = new Date();
        date.setTime(mCourseList.get(position).getStartTime());
        viewHold.mDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        viewHold.mTime.setText(new SimpleDateFormat("HH:mm").format(date));
        return convertView;
    }

    public final class ViewHolder{
        TextView mLessonName;
        TextView mDate;
        TextView mTime;
    }
}
