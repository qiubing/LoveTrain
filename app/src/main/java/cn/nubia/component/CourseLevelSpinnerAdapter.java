package cn.nubia.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.nubia.activity.R;
import cn.nubia.entity.ShareCourseLevel;
import cn.nubia.model.ShareCourseLevelModel;

/**
 * Created by JiangYu on 2015/9/1.
 */
public class CourseLevelSpinnerAdapter extends BaseAdapter {
    private List<ShareCourseLevel> mList;
    private Context mContext;

    public CourseLevelSpinnerAdapter(Context context){
        this.mContext = context;
        this.mList = new ArrayList<ShareCourseLevel>();
        Iterator<Map.Entry<Short,String>> iterator = ShareCourseLevelModel.SHARE_COURSE_MODEL.entrySet().iterator();
       while(iterator.hasNext()){
           Map.Entry<Short,String> entry = iterator.next();
           mList.add(new ShareCourseLevel(entry.getValue(),entry.getKey()));

       }
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
        TextView text;
        if(convertView == null){
            text= (TextView) LayoutInflater.from(mContext).inflate(R.layout.component_spinner_content,null);
        }else{
            text = (TextView) convertView;
        }

        text.setText(mList.get(position).getmCourseLevelName());
        return text;
    }
}
