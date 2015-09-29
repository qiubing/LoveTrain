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
import cn.nubia.activity.ExamAddTabActivity;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;

/**
 * Created by 胡立 on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class ClientAllCourseActivity extends ActivityGroup {
    private LocalActivityManager manager;
    private TabHost tabHost;
    private ViewPager pager;
    private ImageView loadingCource;
    private ImageView loadingExam;
    private MyBroadCastCource myBroadCastCource;
    private MyBroadCastExam myBroadCastExam;
    private LocalBroadcastManager localBroadcastManager;
    private class MyBroadCastCource extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimationDrawable animationDrawable = (AnimationDrawable)loadingCource.getDrawable();
            if(intent.getStringExtra(Constant.ALLCOURCE).equals("visible")) {
                loadingCource.setVisibility(View.VISIBLE);
                animationDrawable.start();
            } else if(intent.getStringExtra(Constant.ALLCOURCE).equals("gone")){
                animationDrawable.stop();
                loadingCource.setVisibility(View.GONE);
            }

        }
    }

    private class MyBroadCastExam extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimationDrawable animationDrawable = (AnimationDrawable)loadingExam.getDrawable();
            if(intent.getStringExtra(Constant.EXAM).equals("visible")) {
                loadingExam.setVisibility(View.VISIBLE);
                animationDrawable.start();
            } else if(intent.getStringExtra(Constant.EXAM).equals("gone")){
                animationDrawable.stop();
                loadingExam.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
        pager = (ViewPager) findViewById(R.id.admin_course_viewpager);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        myBroadCastCource = new MyBroadCastCource();
        myBroadCastExam = new MyBroadCastExam();
        localBroadcastManager.registerReceiver(myBroadCastCource, new IntentFilter(Constant.ALLCOURCE));
        localBroadcastManager.registerReceiver(myBroadCastExam, new IntentFilter(Constant.EXAM));

        // 定放一个放view的list，用于存放viewPager用到的view
        List<View> listViews = new ArrayList<View>();
        manager = this.getLocalActivityManager();
        manager.dispatchCreate(savedInstanceState);

        Intent i3 = new Intent(ClientAllCourseActivity.this, ClientAllCourseTabActivity.class);
        listViews.add(getView("A", i3));

        Intent i4 = new Intent(ClientAllCourseActivity.this, ExamAddTabActivity.class);
        listViews.add(getView("B", i4));

        tabHost = (TabHost) findViewById(R.id.admin_course_tabhost);
        tabHost.setup(ClientAllCourseActivity.this.getLocalActivityManager());

        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);

        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("全部课程");
        loadingCource = (ImageView)tabIndicator3.findViewById(R.id.loading_iv);

        RelativeLayout tabIndicator4 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);

        TextView tvTab4 = (TextView) tabIndicator4.findViewById(R.id.tv_title);
        tvTab4.setText("考试");
        loadingExam = (ImageView)tabIndicator4.findViewById(R.id.loading_iv);

        Intent intent = new Intent(ClientAllCourseActivity.this, EmptyActivity.class);

        tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator3)
                .setContent(intent));
        tabHost.addTab(tabHost.newTabSpec("B").setIndicator(tabIndicator4)
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
        localBroadcastManager.unregisterReceiver(myBroadCastCource);
        localBroadcastManager.unregisterReceiver(myBroadCastExam);
    }
}

