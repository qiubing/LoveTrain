package cn.nubia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.TechnologyShareCourseItem;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/9 14:28
 */
public class CourseAdapter extends BaseAdapter {

    private List<TechnologyShareCourseItem> mCourseList;//考试信息表
    private Context mContext;

    public CourseAdapter(List<TechnologyShareCourseItem> courseList,Context context){
        this.mCourseList = courseList;
        this.mContext = context;
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
            convertView = View.inflate(mContext, R.layout.approved_share_detail_item,null);
            viewHold.mImage = (ImageView) convertView.findViewById(R.id.item_layout_imageview_1);
            viewHold.mTitle = (TextView) convertView.findViewById(R.id.item_layout_title_1);
            viewHold.mCourseInfo = (TextView) convertView.findViewById(R.id.item_layout_content_1);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHolder) convertView.getTag();
        }
        TechnologyShareCourseItem course = mCourseList.get(position);
        viewHold.mTitle.setText(course.getmCourseName());

        //组合课程信息
        Date startTime = new Date();
        startTime.setTime(course.getmStartTime());
        Date endTime = new Date();
        endTime.setTime(course.getmEndTime());
        StringBuilder sb = new StringBuilder();
        sb.append(course.getmLocation());
        sb.append(", ");
        sb.append(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        sb.append(" ");
        sb.append(new SimpleDateFormat("HH:mm").format(startTime));
        sb.append("~");
        sb.append(new SimpleDateFormat("HH:mm").format(endTime));
        sb.append(", ");
        sb.append(course.getmUserName());
        viewHold.mCourseInfo.setText(sb.toString());

        Log.v("course", sb.toString());
        return convertView;
    }

    public final class ViewHolder{
        TextView mTitle,mCourseInfo;
        ImageView mImage;
    }
}
