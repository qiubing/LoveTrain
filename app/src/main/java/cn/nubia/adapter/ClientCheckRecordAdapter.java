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
import cn.nubia.entity.CheckRecordItem;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/14 15:17
 */
public class ClientCheckRecordAdapter extends BaseAdapter {
    private List<CheckRecordItem> mCheckList;//签到记录表
    private Context mContext;

    public ClientCheckRecordAdapter(List<CheckRecordItem> checkList,Context context){
        this.mCheckList = checkList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mCheckList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCheckList.get(position);
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
            convertView = View.inflate(mContext, R.layout.activity_user_check_record_detail_item,null);
            viewHold.mLessonName = (TextView) convertView.findViewById(R.id.check_course_title);
            viewHold.mDate = (TextView) convertView.findViewById(R.id.check_record_date);
            viewHold.mTime = (TextView) convertView.findViewById(R.id.check_record_time);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHolder)convertView.getTag();
        }
        CheckRecordItem checkItem = mCheckList.get(position);
        viewHold.mLessonName.setText(checkItem.getmLessonName());
        //格式化时间
        //long l = System.currentTimeMillis();
        //Log.v("current time","" + l);
        Date date = new Date();
        date.setTime(checkItem.getmCheckTime());
//            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(checkItem.getmCheckTime());
        viewHold.mDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        viewHold.mTime.setText(new SimpleDateFormat("HH:mm").format(date));

        //Log.v("bing",checkItem.toString());
        return convertView;
    }

    public final class ViewHolder{
        TextView mLessonName;
        TextView mDate;
        TextView mTime;
    }
}
