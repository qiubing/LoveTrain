package cn.nubia.activity.admin;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;

/**管理员课程界面：Tab分页导航
 * admin_course_viewpager：布局为TabHost框架，最上面为TabWidget，废弃了FrameLayout，下面为ViewPager(代替FrameLayout，
 * 以提供滑动功能)
 * TabHost的作用仅仅为对标题进行设置
 *ViewPager用于展示多个Activity，使用PagerAdapter为其提供数据，数据源为List<View> listViews
 * 并为TabHost和ViewPager互相绑定监听器，一个改变时，另一个跟着改变，你们这样真的不累吗？
 * ViewPager+PagerTitleStrip  实现相同功能
 * PagerTitleStrip的标题条是动态滑动的，TabHost是静态的
 * 使用PagerTitleStrip可以避免继承废弃类ActivityGroup，但PagerAdapter的分页为View，仍然需要ActivityGroup将Activity转换为View
 * 另外可以使用FragmentPagerAdapter代替PagerAdapter，以彻底避免废弃类，但此时分页内容为Fragment，程序修改力度较大，需讨论决定
 * Created by 胡立 on 2015/9/6.
 */
public class AdminCourseActivity extends ActivityGroup {
    private List<View> listViews;
    private LocalActivityManager manager;
    private TabHost tabHost;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
        pager = (ViewPager) findViewById(R.id.admin_course_viewpager);

        // 定放一个放view的list，用于存放viewPager用到的view
        listViews = new ArrayList<View>();
        manager = this.getLocalActivityManager();
        manager.dispatchCreate(savedInstanceState);


        Intent i3 = new Intent(AdminCourseActivity.this, AdminCourseAddTabActivity.class);
        listViews.add(getView("A", i3));

        tabHost = (TabHost) findViewById(R.id.admin_course_tabhost);
        tabHost.setup(this.getLocalActivityManager());

        RelativeLayout tabIndicator3 = (RelativeLayout) LayoutInflater.from(
                this).inflate(R.layout.layout_tab, null);

        TextView tvTab3 = (TextView) tabIndicator3.findViewById(R.id.tv_title);
        tvTab3.setText("新增课程");


        final Intent intent = new Intent(AdminCourseActivity.this, AdminAddCourseActivity.class);
        tvTab3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                Toast.makeText(AdminCourseActivity.this, "you can do anything", Toast.LENGTH_LONG).show();
            }
        });
        //此处貌似必须有setContent(intent)
        tabHost.addTab(tabHost.newTabSpec("A").setIndicator(tabIndicator3)
                .setContent(intent));
        pager.setAdapter(new MyPageAdapter(listViews));
       /* pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                if ("C".equals(tabId)) {
                    pager.setCurrentItem(2);
                }
            }
        });*/

    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    private class MyPageAdapter extends PagerAdapter {

        private List<View> list;

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

}
