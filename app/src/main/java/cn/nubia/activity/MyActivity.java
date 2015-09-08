package cn.nubia.activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import cn.nubia.activity.client.AccountManagementActivity;
import cn.nubia.activity.client.CourseIntegrationRecordActivity;
import cn.nubia.activity.client.ExamScoreActivity;
import cn.nubia.activity.client.MyCheckRecordActivity;
import cn.nubia.activity.client.ShareCourseActivity;

/**
 * @Description:与我相关的
 * @Author: qiubing
 * @Date: 2015/9/6 19:28
 */
public class MyActivity extends BaseActivity implements OnClickListener{
    private ImageView mArrow1;
    private ImageView mArrow2;
    private ImageView mArrow3;
    private ImageView mArrow4;
    private ImageView mArrow5;
    private ImageView mArrow6;



    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_user_my_setting);
        findViews();
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        mArrow1.setOnClickListener(this);
        mArrow2.setOnClickListener(this);
        mArrow3.setOnClickListener(this);
        mArrow4.setOnClickListener(this);
        mArrow5.setOnClickListener(this);
        mArrow6.setOnClickListener(this);
    }

    private void findViews(){
        mArrow1 = (ImageView) findViewById(R.id.arrow_image_1);
        mArrow2 = (ImageView) findViewById(R.id.arrow_image_2);
        mArrow3 = (ImageView) findViewById(R.id.arrow_image_3);
        mArrow4 = (ImageView) findViewById(R.id.arrow_image_4);
        mArrow5 = (ImageView) findViewById(R.id.arrow_image_5);
        mArrow6 = (ImageView) findViewById(R.id.arrow_image_6);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    public void back(View view) {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_image_1:
                startActivity(MyCheckRecordActivity.class);
                break;
            case R.id.arrow_image_2:
                startActivity(CourseIntegrationRecordActivity.class);
                break;
            case R.id.arrow_image_3:
                startActivity(ExamScoreActivity.class);
                break;
            case R.id.arrow_image_4:
                startActivity(ShareCourseActivity.class);
                break;
            case R.id.arrow_image_5:
//                startActivity(MyCheckRecordActivity.class);
                break;
            case R.id.arrow_image_6:
                startActivity(AccountManagementActivity.class);
                break;
        }
    }
}
