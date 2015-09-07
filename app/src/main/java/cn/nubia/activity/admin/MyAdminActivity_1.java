package cn.nubia.activity.admin;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import cn.nubia.activity.R;


public class MyAdminActivity_1 extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "CourseAdminActivity_1", Toast.LENGTH_LONG).show();
    }
}
