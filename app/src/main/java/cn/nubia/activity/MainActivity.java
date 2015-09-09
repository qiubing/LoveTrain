package cn.nubia.activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.currentTimeMillis();





//        Intent intent = new Intent(MainActivity.this,MyShareCourseDetailDisplayActivity.class);
//        ShareCourse shareCourse = new ShareCourse();
//        shareCourse.setCourseName("Java");
//        shareCourse.setCourseDescription("本书赢得了全球程序员的广泛赞誉，即使是最晦涩的概念，" +
//                "在Bruce Eckel的文字亲和力和小而直接的编程示例面前也会化解于无形。从Java的基" +
//                "础语法到最高级特性（深入的面向对象概念、多线程、自动项目构建、单元测试和调试等" +
//                "），本书都能逐步指导你轻松掌握。从本书获得的各项大奖以及来自世界各地的读者评论" +
//                "中，不难看出这是一本经典之作。本书的作者拥有多年教学经验，对C、C++以及Java语言都" +
//                "有独到、深入的见解，以通俗易懂及小而直接的示例解释了一个个晦涩抽象的概念。本书共22" +
//                "章，包括操作符、控制执行流程、访问权限控制、复用类、多态、接口、通过异常处理错误、" +
//                "字符串、泛型、数组、容器深入研究、JavaI/O系统、枚举类型、并发以及图形化用户界面等" +
//                "内容。这些丰富的内容，包含了Java语言基础语法以及高级特性，适合各个层次的Java程序员" +
//                "阅读，同时也是高等院校讲授面向对象程序设计语言以及Java语言的绝佳教材和参考书。");
//        shareCourse.setLocale("C2-6");
//        shareCourse.setCourseLevel((short) 2);
//        try {
//            shareCourse.setStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2015-03-11"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("shareCourse",shareCourse);
//        intent.putExtras(bundle);

//        startActivity(intent);

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
