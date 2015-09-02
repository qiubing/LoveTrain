package cn.nubia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.nubia.entity.ShareCourse;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tx = (TextView) findViewById(R.id.go);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyShareCourseDetailDisplayActivity.class);
                ShareCourse shareCourse = new ShareCourse();
                shareCourse.setCourseName("Java");
                shareCourse.setCourseDescription("dgsdfsdfsfsfs f");
                shareCourse.setLocale("C2-6");
                shareCourse.setCourseLevel((short) 2);
                try {
                    shareCourse.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2015-03-11"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("shareCourse",shareCourse);
                intent.putExtras(bundle);

                Log.d("jiangyu",((ShareCourse)(intent.getExtras().get("shareCourse"))).getCourseName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
