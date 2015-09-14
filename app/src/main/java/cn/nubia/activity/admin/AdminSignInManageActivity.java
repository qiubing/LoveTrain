package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/11.
 */
public class AdminSignInManageActivity extends Activity {

    private ImageView backImageView;

    private ListView listView;

    ArrayList<String> listData;

    private TextView name;
    private Button agreeButton;
    private Button disagreeButton;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_manage);

        ctx = this;

        backImageView = (ImageView) findViewById(R.id.admin_signIn_manage_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackImage = new Intent(AdminSignInManageActivity.this, AdminCourseDetailActivity.class);
                startActivity(intentBackImage);
                finish();
            }
        });

        listData = getData();

        listView = (ListView) findViewById(R.id.admin_signIn_manage_listView);

        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listData.size();
            }

            @Override
            public Object getItem(int position) {
                return listData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(ctx, R.layout.activity_admin_signin_manage_item, null);
                name = (TextView) convertView.findViewById(R.id.admin_personInfo_nameTextView);
                agreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_agreeButton);
                disagreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_disagreeButton);
                name.setText(listData.get(position));


                /**同意或不同意**/
                agreeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout relativeLayout=(RelativeLayout)v.getParent();

                        /**获取第一个button，第一个button是1*/
                        Button agreeBtn=(Button)relativeLayout.getChildAt(1);
                        agreeBtn.setBackgroundColor(getResources().getColor(R.color.green));

                        /**获取第二个button，第二个button是0*/
                        Button disagreeBtn=(Button)relativeLayout.getChildAt(0);
                        disagreeBtn.setBackgroundColor(getResources().getColor(R.color.white));

                        Toast.makeText(AdminSignInManageActivity.this, "你点击了同意" + position, Toast.LENGTH_LONG).show();
                    }
                });

                disagreeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout relativeLayout=(RelativeLayout)v.getParent();

                        Button agreeBtn=(Button)relativeLayout.getChildAt(1);
                        agreeBtn.setBackgroundColor(getResources().getColor(R.color.white));

                        Button disagreeBtn=(Button)relativeLayout.getChildAt(0);
                        disagreeBtn.setBackgroundColor(getResources().getColor(R.color.red));
                        Toast.makeText(AdminSignInManageActivity.this, "你点击了否决" + position, Toast.LENGTH_LONG).show();
                    }
                });
                return convertView;
            }
        };
        listView.setAdapter(adapter);

    }

    //初始化数据
    private ArrayList<String> getData() {
        ArrayList<String> listData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            listData.add("张" + i);
        }
        return listData;
    }


    static class ViewHolder {
        Button agreeButton;
        Button disagreeButton;
        TextView name;
    }
}
