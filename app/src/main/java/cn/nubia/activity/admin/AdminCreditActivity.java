package cn.nubia.activity.admin;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.EmptyActivity;
import cn.nubia.activity.R;
@SuppressWarnings("deprecation")
public class AdminCreditActivity extends ActivityGroup {


    private LocalActivityManager mManager = null;
    private TabHost mTabHost = null;
    private ViewPager mViewPage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_credit);

        //公用部分
        TextView mManagerTitle = (TextView) findViewById(R.id.manager_head_title);
        mManagerTitle.setText(R.string.title_activity_manager_credit_title);
        ImageView mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<View> mListViews;
        Context mContext = AdminCreditActivity.this;
        mViewPage = (ViewPager) findViewById(R.id.viewpager);

        // 定放一个放view的list，用于存放viewPager用到的view
        mListViews = new ArrayList<>();
        mManager = this.getLocalActivityManager();
        mManager.dispatchCreate(savedInstanceState);

        Intent intent1 = new Intent(mContext, AdminCreditTotalActivity.class);
        mListViews.add(getView("A", intent1));
        Intent intent2 = new Intent(mContext, AdminCreditCourseActivity.class);
        mListViews.add(getView("B", intent2));


        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(mManager);

        // 这儿主要是自定义一下tabhost中的tab的样式
        RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);
        TextView tvTab1 = (TextView) tabIndicator1.findViewById(R.id.tv_title);
        tvTab1.setText("积分总排行");

        RelativeLayout tabIndicator2 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);
        TextView tvTab2 = (TextView) tabIndicator2.findViewById(R.id.tv_title);
        tvTab2.setText("按课程查询");

        Intent intent = new Intent(mContext, EmptyActivity.class);
        // 注意这儿Intent中的activity不能是自身
        // 比如“A”对应的是T1Activity，后面intent就new的T3Activity的。
        mTabHost.addTab(mTabHost.newTabSpec("A").setIndicator(tabIndicator1)
                .setContent(intent));
        mTabHost.addTab(mTabHost.newTabSpec("B").setIndicator(tabIndicator2)
                .setContent(intent));

        mViewPage.setAdapter(new MyPageAdapter(mListViews));
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 当viewPager发生改变时，同时改变tabhost上面的currentTab
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // 点击tabhost中的tab时，要切换下面的viewPager
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if ("A".equals(tabId)) {
                    mViewPage.setCurrentItem(0);
                }
                if ("B".equals(tabId)) {
                    mViewPage.setCurrentItem(1);
                }
                if ("C".equals(tabId)) {
                    mViewPage.setCurrentItem(2);
                }
            }
        });

    }

    private View getView(String id, Intent intent) {
        return mManager.startActivity(id, intent).getDecorView();
    }

    private class MyPageAdapter extends PagerAdapter {

        private final List<View> list;

        private MyPageAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ViewPager pViewPager = ((ViewPager) view);
            pViewPager.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }


}
