package cn.nubia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/19.
 */
public class SignUpManageAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;

    private TextView name;
    private Button agreeButton;
    private Button disagreeButton;

    public SignUpManageAdapter(List<String> list,Context context){
        this.mList = list;
        this.mContext = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(mContext, R.layout.activity_admin_signin_manage_item, null);
        name = (TextView) convertView.findViewById(R.id.admin_personInfo_nameTextView);
        agreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_agreeButton);
        disagreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_disagreeButton);

        name.setText(mList.get(position));


        /**按钮事件**/
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout=(RelativeLayout)v.getParent();

                /**同意按钮是在位置1*/
                Button agreeBtn=(Button)relativeLayout.getChildAt(1);
                agreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.green));

                /**否决按钮是在位置0*/
                Button disagreeBtn=(Button)relativeLayout.getChildAt(0);
                disagreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));

                Toast.makeText(mContext, "你点击了同意" + position, Toast.LENGTH_LONG).show();
            }
        });

        disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout=(RelativeLayout)v.getParent();
                Button agreeBtn=(Button)relativeLayout.getChildAt(1);
                Button disagreeBtn=(Button)relativeLayout.getChildAt(0);
                disagreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                agreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                Toast.makeText(mContext, "你点击了否决" + position, Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
