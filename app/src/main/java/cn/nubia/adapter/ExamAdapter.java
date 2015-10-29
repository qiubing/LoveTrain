package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.entity.ExamItem;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;

/**
 * Created by WJ on 2015/9/7.
 */
public class ExamAdapter extends BaseAdapter{
    private final List<ExamItem> mExamList;
//    private TreeMap<Integer,ExamItem>  mExamList;
    private final Context mCtx;
    public ExamAdapter(List<ExamItem>  examList, Context ctx) {
        this.mExamList = examList;
        this.mCtx = ctx;
    }

    @Override
    public int getCount() {
        return mExamList.size();
    }

    @Override
    public Object getItem(int position) {
        return mExamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHold;
        if (convertView == null) {
            viewHold = new ViewHolder();
            convertView = View.inflate(mCtx, R.layout.exam_info_item, null);
            viewHold.mImage = (ImageView) convertView.findViewById(R.id.item_layout_imageview);
            viewHold.mTitle = (TextView) convertView.findViewById(R.id.item_layout_title);
            viewHold.mExamInfo = (TextView) convertView.findViewById(R.id.item_layout_content);
            viewHold.mExamTime = (TextView) convertView.findViewById(R.id.item_layout_time);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHolder) convertView.getTag();
        }
//        viewHold.mImage.setImageBitmap();  //图片
        viewHold.mTitle.setText(mExamList.get(position).getName());

        //        strb.append(mExamList.get(position).getIndex());
        viewHold.mExamInfo.setText(
                TimeFormatConversion.toTimeDate(mExamList.get(position).getStartTime(), 0)
                        + "\n" + TimeFormatConversion.toTime(mExamList.get(position).getStartTime())
                        + "-"
                        + TimeFormatConversion.toTime(mExamList.get(position).getEndTime()));
        viewHold.mExamTime.setText(
                mExamList.get(position).getLocale().toUpperCase()
        );

        return convertView;
    }


    static class ViewHolder{
        TextView mTitle,mExamInfo,mExamTime;
        ImageView mImage;
    }
}
