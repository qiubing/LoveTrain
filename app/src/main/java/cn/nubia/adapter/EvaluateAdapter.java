package cn.nubia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.LessonJudgementMsg;
import cn.nubia.util.MyViewHolder;

/**
 * Created by 胡立 on 2015/9/14.
 */
public class EvaluateAdapter extends BaseExpandableListAdapter {

    private final List<LessonJudgementMsg> mList = new ArrayList<>();;
    private final Context mContext;

    public EvaluateAdapter(Context context){
        this.mContext = context;
    }

    public void updateData(List<LessonJudgementMsg> newData) {
        mList.clear();
        mList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.evaluate_item, null);
            holder = new ViewHolder();
            holder.name = MyViewHolder.get(convertView, R.id.evaluate_name);
            holder.evaluate = MyViewHolder.get(convertView, R.id.evaluate_all);
            holder.suggest = MyViewHolder.get(convertView, R.id.evaluate_suggest);
            holder.mExpendedIV = MyViewHolder.get(convertView, R.id.admin_all_course_courseDetailTextView);
//            holder.mExpendedIV = (ImageView) convertView.findViewById(R.id.admin_all_course_courseDetailTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(groupPosition).getUserName());
        holder.evaluate.setText("综合评价：" + mList.get(groupPosition).getComprehensiveEvaluation());
        holder.suggest.setText("诚恳建议：" + mList.get(groupPosition).getSuggestion());

        if(isExpanded){
            Bitmap img = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.new_go);
            Matrix matrix = new Matrix();
            matrix.postRotate(90); /*翻转90度*/
            int width = img.getWidth();
            int height =img.getHeight();
            img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
            holder.mExpendedIV.setImageBitmap(img);
        }
        else {
            holder.mExpendedIV.setImageResource(R.mipmap.new_go);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        ViewHolderSub holderSub;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.evaluate_sub_item, null);
            holderSub = new  ViewHolderSub();
            holderSub.star_communication = MyViewHolder.get(convertView, R.id.star_communication);
            holderSub.star_contentApplicability = MyViewHolder.get(convertView, R.id.star_contentApplicability);
            holderSub.star_contentRationality = MyViewHolder.get(convertView, R.id.star_contentRationality);
            holderSub.star_contentUnderstanding = MyViewHolder.get(convertView, R.id.star_contentUnderstanding);
            holderSub.star_discussion = MyViewHolder.get(convertView, R.id.star_discussion);
            holderSub.star_expressionAbility = MyViewHolder.get(convertView, R.id.star_expressionAbility);
            holderSub.star_timeRationality = MyViewHolder.get(convertView, R.id.star_timeRationality);
            holderSub.star_organization = MyViewHolder.get(convertView, R.id.star_organization);
            convertView.setTag(holderSub);
        } else {
            holderSub = (ViewHolderSub) convertView.getTag();
        }
        setPraiseRate(holderSub.star_communication, mList.get(groupPosition).getCommunication());
        setPraiseRate(holderSub.star_contentApplicability, mList.get(groupPosition).getContentApplicability());
        setPraiseRate(holderSub.star_contentRationality, mList.get(groupPosition).getContentRationality());
        setPraiseRate(holderSub.star_contentUnderstanding, mList.get(groupPosition).getContentUnderstanding());
        setPraiseRate(holderSub.star_discussion, mList.get(groupPosition).getDiscussion());
        setPraiseRate(holderSub.star_expressionAbility, mList.get(groupPosition).getExpressionAbility());
        setPraiseRate(holderSub.star_timeRationality, mList.get(groupPosition).getTimeRationality());
        setPraiseRate(holderSub.star_organization, mList.get(groupPosition).getOrganization());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class ViewHolder {
        TextView name;
        //ImageView icon;
        TextView evaluate;
        TextView suggest;
        ImageView mExpendedIV;

    }

    static class ViewHolderSub {
        ImageView star_communication;
        ImageView star_contentApplicability;
        ImageView star_contentRationality ;
        ImageView star_contentUnderstanding;
        ImageView star_discussion;
        ImageView star_expressionAbility;
        ImageView star_timeRationality;
        ImageView star_organization;

    }

    /**
     * 评分星级
     *
     * param img
     * param score
     */
    private void setPraiseRate(ImageView img, float score) {
        if (score == 0) {
            img.setImageResource(R.mipmap.start0);
        } else if (score== 0.5) {
            img.setImageResource(R.mipmap.start0_5);
        } else if (score == 1) {
            img.setImageResource(R.mipmap.start1);
        } else if (score == 1.5) {
            img.setImageResource(R.mipmap.start1_5);
        } else if (score == 2) {
            img.setImageResource(R.mipmap.start2);
        } else if (score == 2.5) {
            img.setImageResource(R.mipmap.start2_5);
        } else if (score == 3) {
            img.setImageResource(R.mipmap.start3);
        } else if (score == 3.5) {
            img.setImageResource(R.mipmap.start3_5);
        } else if (score == 4) {
            img.setImageResource(R.mipmap.start4);
        } else if (score == 4.5) {
            img.setImageResource(R.mipmap.start4_5);
        } else if (score == 5) {
            img.setImageResource(R.mipmap.start5);
        } else {
            img.setImageResource(R.mipmap.start5);
            //img.setImageAlpha(5);
        }
    }
}
