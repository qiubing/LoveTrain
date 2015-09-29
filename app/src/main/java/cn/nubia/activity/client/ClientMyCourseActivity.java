package cn.nubia.activity.client;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;

/**
 * Created by 胡立 on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class ClientMyCourseActivity extends ActivityGroup {
    private LocalActivityManager manager;
    private TabHost tabHost;
    private ViewPager pager;
    private ImageView loadingTeacher;
    private ImageView loadingStudent;
    private MyBroadCastTeacher myBroadCastTeacher;
    private MyBroadCastStudent myBroadCastStudent;
    private LocalBroadcastManager localBroadcastManager;
    private class MyBroadCastTeacher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimationDrawable animationDrawable = (AnimationDrawable)loadingTeacher.getDrawable();
            if(intent.getStringExtra(Constant.MYCOURCE_TEACHER).equals("visible")) {
                loadingTeacher.setVisibility(View.VISIBLE);
                animationDrawable.start();
            } else if(intent.getStringExtra(Constant.MYCOURCE_TEACHER).equals("gone")){
                animationDrawable.stop();
                loadingTeacher.setVisibility(View.GONE);
            }

        }
    }

    private class MyBroadCastStudent extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimationDrawable animationDrawable = (AnimationDrawable)loadingStudent.getDrawable();
            if(intent.getStringExtra(Constant.MYCOURCE_STUDENT).equals("visible")) {
                loadingStudent.setVisibility(View.VISIBLE);
                animationDrawable.start();
            } else if(intent.getStringExtra(Constant.MYCOURCE_STUDENT).equals("gone")){
                animationDrawable.stop();
                loadingStudent.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
        pager = (ViewPager) findViewById(R.id.admin_course_viewpager);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        myBroadCastTeacher = new MyBroadCastTeacher();
        myBroadCastStudent = new MyBroadCastStudent();
        localBroadcastManager.registerReceiver(myBroadCastTeacher, new IntentFilter(Constant.MYCOURCE_TEACHER));
        localBroadcastManager.registerReceiver(myBroadCastStudent, new IntentFilter(Constant.MYCOURCE_STUDENT));

        // 定放一个放view的list，用于存放viewPager用到的view
        List<View> listViews = new ArrayList<View>();
        manager = this.getLocalActivityManager();
        manager.dispatchCreate(savedInstanceState);

        Intent i2 = new Intent(this, ClientMyCourseStudentTabActivity.class);
        listViews.add(getView("A", i2));
        Intent i3 = new Intent(this, ClientMyCourseTeacherTabActivity.class);
        listViews.add(getView("B", i3));

        tabHost = (TabHost) findViewById(R.id.admin_course_tabhost);
        tabHost.setup(this.getLocalActivityManager());


        RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv_title);
        tvTab2.setText("我是学员");
        loadingStudent = (ImageView)tabIndicator2.findViewById(R.id.loading_iv);


        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);

        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("我是讲师");
        loadingTeacher = (ImageView)tabIndicator3.findViewById(R.id.loading_iv);

        Intent intent = new Intent(this, EmptyActivity.class);

        // 注意这儿Intent中的activity不能是自身
        // 比如“A”对应的是T1Activity，后面intent就new的T3Activity的。

        tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator2)
                .setContent(intent));
        tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator3)
                .setContent(intent));
        pager.setAdapter(new MyPageAdapter(listViews));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 当viewPager发生改变时，同时改变tabhost上面的currentTab
                tabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // 点击tabhost中的tab时，要切换下面的viewPager
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if ("A".equals(tabId)) {
                    pager.setCurrentItem(0);
                }
                if ("B".equals(tabId)) {
                    pager.setCurrentItem(1);
                }
            }
        });

    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    private class MyPageAdapter extends PagerAdapter {

        private final List<View> list;

        private MyPageAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView( list.get(position));
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadCastTeacher);
        localBroadcastManager.unregisterReceiver(myBroadCastStudent);
    }
}