package cn.nubia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.TreeMap;

import cn.nubia.activity.R;
import cn.nubia.entity.ExamItem;

/**
 * Created by WJ on 2015/9/7.
 */
public class ExamAdapter extends BaseAdapter{
    private List<ExamItem> mExamList;
//    private TreeMap<Integer,ExamItem>  mExamList;
    private Context mCtx;
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
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHolder) convertView.getTag();
        }
//        viewHold.mImage.setImageBitmap();  //图片
        viewHold.mTitle.setText(mExamList.get(position).getName());

        StringBuilder strb = new StringBuilder();
        strb.append(mExamList.get(position).getIndex());

        strb.append(mExamList.get(position).getLocale());
        strb.append("  ");
        strb.append(mExamList.get(position).getStartTime());
        strb.append("到");
        strb.append(mExamList.get(position).getEndTime());
        Log.e("test",strb.toString());
        viewHold.mExamInfo.setText(strb.toString());
        return convertView;
    }


    static class ViewHolder{
        TextView mTitle,mExamInfo;
        ImageView mImage;
    }
}