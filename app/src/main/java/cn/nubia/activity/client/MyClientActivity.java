package cn.nubia.activity.client;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;

import java.util.List;

import cn.nubia.activity.R;

/**
 * Created by 胡立 on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class MyClientActivity extends ActivityGroup {
    private List<View> listViews;
    LocalActivityManager manager;
    private TabHost tabHost;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
    }

}

