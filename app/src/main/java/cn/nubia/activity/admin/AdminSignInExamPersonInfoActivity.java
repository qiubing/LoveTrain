package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import cn.nubia.activity.R;
import cn.nubia.adapter.SignInExamPersonInfoAdapter;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminSignInExamPersonInfoActivity extends Activity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin_exam_person_info);

        ImageView backImageView = (ImageView) findViewById(R.id.admin_signIn_info_back);
        backImageView.setOnClickListener(this);

        /**获取模拟数据**/
        ArrayList<String> listData=getData();

        /**要填充数据的ListView**/
        ListView listView = (ListView) findViewById(R.id.admin_signIn_info_listView);

        /**这里传this参数是否正确?正确**/
        SignInExamPersonInfoAdapter mSignInExamPersonInfoAdapter = new SignInExamPersonInfoAdapter(listData, this);
        /**设置填充ListView的Adapter**/
        listView.setAdapter(mSignInExamPersonInfoAdapter);

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
