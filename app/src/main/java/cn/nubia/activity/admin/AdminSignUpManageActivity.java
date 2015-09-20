package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.nubia.activity.R;
import cn.nubia.adapter.SignUpManageAdapter;

/**
 * Created by hexiao on 2015/9/11.
 */
public class AdminSignUpManageActivity extends Activity {

    private ImageView backImageView;

    private ListView listView;

    ArrayList<String> listData;

    private TextView name;
    private Button agreeButton;
    private Button disagreeButton;

    private SignUpManageAdapter signUpManageAdapter;
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
                Intent intentBackImage = new Intent(AdminSignUpManageActivity.this, AdminCourseDetailActivity.class);
                startActivity(intentBackImage);
                finish();
            }
        });
        listData = getData();
        listView = (ListView) findViewById(R.id.admin_signIn_manage_listView);
        signUpManageAdapter=new SignUpManageAdapter(listData,ctx);
        listView.setAdapter(signUpManageAdapter);

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
