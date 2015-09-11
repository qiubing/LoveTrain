package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cn.nubia.activity.R;

/**
 * @Description:
 * @Author: qiubing
 * @Date: 2015/9/11 15:32
 */
public class AboutUsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void back(View view) {
        this.finish();
    }
}
