package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.util.DialogUtil;

public class ManagerUserActivity extends Activity {

    private ListView mListView;
    private List<String> mNameList;
    private List<String> mIdList;
    private ImageView mGoBack;
    private TextView mTitle;

    private void init() {
        mNameList = new ArrayList<>();
        mIdList = new ArrayList<>();

        String[] mUserNames = new String[]{"张三1", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰",
                "张三丰", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰", "张三丰", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰",
                "张三丰", "李四", "王五", "王老五", "隔壁老王", "张三丰", "张三丰", "张三丰", "张三丰", "张三丰"};
        String[] mIds = new String[]{"0016003347", "0016003348", "0016003349", "0016003350", "0016003351",
                "0016003352", "0016003353", "0016003354", "0016003355", "0016003356", "0016003357", "0016003358",
                "0016003359", "0016003360", "0016003361", "0016003362",
                "0016003363", "0016003364", "0016003365", "0016003366", "0016003367", "0016003368", "0016003369", "0016003348", "0016003349", "0016003350", "0016003351",
                "0016003352", "0016003353", "0016003354", "0016003355", "0016003356", "0016003357", "0016003358",
                "0016003359", "0016003360", "0016003361", "0016003362",
                "0016003363", "0016003364", "0016003365", "0016003366", "0016003367", "0016003368", "0016003369"};
        for (int i = 0; i < mUserNames.length; i++) {
            mNameList.add(mUserNames[i]);
            mIdList.add(mIds[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
        mTitle = (TextView) findViewById(R.id.manager_head_title);
        mTitle.setText(R.string.activity_manager_user);
        mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();

        mListView = (ListView) findViewById(R.id.manager_user_listview);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mNameList.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                final String userName = mNameList.get(position);
                final String userId = mIdList.get(position);
                final ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.manager_user_item, null);
                    holder.usernameTV = (TextView) convertView.findViewById(R.id.manager_username);
                    holder.idTV = (TextView) convertView.findViewById(R.id.manager_id);
                    holder.resetPWD = (TextView) convertView.findViewById(R.id.manager_resetpwd);
                    holder.delete = (TextView) convertView.findViewById(R.id.manager_delete);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.usernameTV.setText(userName);
                holder.idTV.setText(userId);
                holder.resetPWD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(ManagerUserActivity.this).setTitle("确认要重置" + userName + "的密码吗?")
                                .setPositiveButton("重置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DialogUtil.showToast(ManagerUserActivity.this, "你重置了密码:" + userName + userId);
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(ManagerUserActivity.this).setTitle("确认要删除" + userName + "吗?")
                                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mNameList.remove(position);
                                        mIdList.remove(position);
                                        notifyDataSetChanged();
                                        DialogUtil.showToast(ManagerUserActivity.this, "你删除了用户:" + userName + userId);
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                });
                return convertView;
            }
        };

        mListView.setAdapter(adapter);
    }

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        TextView resetPWD;
        TextView delete;
    }

}
