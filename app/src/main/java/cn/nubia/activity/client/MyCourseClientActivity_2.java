package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import cn.nubia.activity.R;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class MyCourseClientActivity_2 extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "MyCourseClientActivity_2", Toast.LENGTH_LONG).show();
    }
}