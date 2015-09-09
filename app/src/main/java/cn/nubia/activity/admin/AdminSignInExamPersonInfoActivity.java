package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.nubia.activity.R;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminSignInExamPersonInfoActivity extends Activity implements View.OnClickListener{

    private ImageView backImageView;

    private ListView listView;

    ArrayList<String> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_exam_person_info);

        backImageView=(ImageView)findViewById(R.id.admin_signIn_info_back);
        backImageView.setOnClickListener(this);

        listData=getData();

        listView=(ListView)findViewById(R.id.admin_signIn_info_listView);
        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return 30;
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
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.activity_admin_person_info_item, null);

                TextView nameStudentNo = (TextView) relativeLayout.findViewById(R.id.admin_personInfo_contentTextView);

                nameStudentNo.setText(listData.get(position).toString());

                ImageView imageView=(ImageView)relativeLayout.findViewById(R.id.admin_personInfo_headViewImage);
                imageView.setImageResource(R.mipmap.ic_launcher);
                return relativeLayout;
            }
        };
        listView.setAdapter(adapter);
        //ListView的条目点击事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_signIn_info_back:
                Intent intentBackImage = new Intent(AdminSignInExamPersonInfoActivity.this,AdminLessonDetailActivity.class);
                startActivity(intentBackImage);
                finish();
                break;
        }
    }

    //初始化数据
    private ArrayList<String> getData(){
        ArrayList<String> listData=new ArrayList<>();
        for(int i=0;i<30;i++){
            listData.add("张" + i + "  001600"+i);
        }
        return listData;
    }
}
