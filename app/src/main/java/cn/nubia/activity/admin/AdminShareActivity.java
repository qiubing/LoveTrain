package cn.nubia.activity.admin;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;

/**
 * Created by 胡立 on 2015/9/6.
 */

public class AdminShareActivity extends ActivityGroup {
    private LocalActivityManager manager;
    private TabHost tabHost;
    private ViewPager pager;
    private ImageView loadingWaite;
    private ImageView loadingOk;
    public class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimationDrawable animationDrawableWaite = (AnimationDrawable)loadingWaite.getDrawable();
            AnimationDrawable animationDrawableOk = (AnimationDrawable)loadingOk.getDrawable();
            switch (intent.getStringExtra(Constant.SHARE)) {
                case "visibleWaite":
                    loadingWaite.setVisibility(View.VISIBLE);
                    animationDrawableWaite.start();
                    break;
                case "goneWaite":
                    animationDrawableWaite.stop();
                    loadingWaite.setVisibility(View.GONE);
                    break;
                case "visibleOk":
                    loadingOk.setVisibility(View.VISIBLE);
                    animationDrawableOk.start();
                    break;
                case "goneOk":
                    animationDrawableOk.stop();
                    loadingOk.setVisibility(View.GONE);
                    break;
                default:
                    Toast.makeText(AdminShareActivity.this, "广播异常了", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
        pager = (ViewPager) findViewById(R.id.admin_course_viewpager);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new MyBroadCast(), new IntentFilter(Constant.SHARE_WAITE));
        localBroadcastManager.registerReceiver(new MyBroadCast(), new IntentFilter(Constant.SHARE_OK));

        // 定放一个放view的list，用于存放viewPager用到的view
        List<View> listViews = new ArrayList<View>();
        manager = this.getLocalActivityManager();
        manager.dispatchCreate(savedInstanceState);

        Intent i2 = new Intent(AdminShareActivity.this, AdminShareCheckTabActivity.class);
        listViews.add(getView("AA", i2));
        Intent i3 = new Intent(AdminShareActivity.this, AdminSharePassTabActivity.class);
        listViews.add(getView("BA", i3));

        tabHost = (TabHost) findViewById(R.id.admin_course_tabhost);
        tabHost.setup(AdminShareActivity.this.getLocalActivityManager());


        RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv_title);
        tvTab2.setText("待审核");
        loadingWaite = (ImageView)tabIndicator2.findViewById(R.id.loading_iv);

        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);

        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("已批准");
        loadingOk = (ImageView)tabIndicator3.findViewById(R.id.loading_iv);

        Intent intent = new Intent(AdminShareActivity.this, EmptyActivity.class);

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
            container.removeView(list.get(position));
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

}