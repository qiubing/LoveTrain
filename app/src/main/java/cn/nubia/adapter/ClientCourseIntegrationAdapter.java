package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.entity.CourseIntegrationItem;

/**
 * Description:
 * Author: qiubing
 * Date: 2015/9/15 10:24
 */
public class ClientCourseIntegrationAdapter extends BaseAdapter {
    private final List<CourseIntegrationItem> mIntegrationList;//积分记录表
    private final Context mContext;

    public ClientCourseIntegrationAdapter(List<CourseIntegrationItem> integrationList,Context context){
        this.mIntegrationList = integrationList;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mIntegrationList.size();
    }

    @Override
    public Object getItem(int position) {
        return mIntegrationList.get(position);
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
            convertView = View.inflate(mContext, R.layout.activity_user_course_integration_record_detail_item,null);
            viewHold.mLessonName = (TextView) convertView.findViewById(R.id.course_title);
            viewHold.mIntegrationCause = (TextView) convertView.findViewById(R.id.course_cause);
            viewHold.mCourseIntegration = (TextView) convertView.findViewById(R.id.course_score);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHolder)convertView.getTag();
        }

        viewHold.mLessonName.setText(mIntegrationList.get(position).getmLessonName());
        viewHold.mIntegrationCause.setText(mIntegrationList.get(position).getmCause());
        viewHold.mCourseIntegration.setText("+" + mIntegrationList.get(position).getmCheckCredits());
        return convertView;
    }

    public  final class ViewHolder{
        TextView mLessonName;
        TextView mIntegrationCause;
        TextView mCourseIntegration;
    }
}
