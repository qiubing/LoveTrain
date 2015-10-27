package cn.nubia.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

public class LoginActivity1 extends FragmentActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private TextView[] mTextViews = new TextView[3];
    private View[] mDividerViews = new View[3];
    private int currentItemId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        initViews();
        initEvents();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.login_viewpage);
        mTextViews[0] = (TextView) findViewById(R.id.login_client);
        mTextViews[1] = (TextView) findViewById(R.id.login_admin);
        mTextViews[2] = (TextView) findViewById(R.id.login_sign_in);
        mDividerViews[0] = findViewById(R.id.login_client_divider);
        mDividerViews[1] = findViewById(R.id.login_admin_divider);
        mDividerViews[2] = findViewById(R.id.login_sign_in_divider);
    }

    private void initEvents() {
        for (TextView mTextView : mTextViews) {
            mTextView.setOnClickListener(this);
        }

        updataItemUI(0, R.color.toolbar_bg);
        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTextViews.length;
            }

            @Override
            public android.support.v4.app.Fragment getItem(int index) {
                android.support.v4.app.Fragment fragment = null;
                switch (index) {
                    case 0 :
                        fragment = new ClientFragment();
                        break;
                    case 1 :
                        fragment = new AdminFragment();
                        break;
                    case 2 :
                        fragment = new SignInFragment();
                        break;
                }
                return fragment;
            }
        };

        viewPager.setAdapter(mFragmentPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (currentItemId == position) {
                    return;
                }
                updataItemUI(position, R.color.toolbar_bg);
                updataItemUI(currentItemId, R.color.toolbar_text_color_normal);
                currentItemId = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    void updataItemUI(int index, int textColorResId) {
        mTextViews[index].setTextColor(this.getResources().getColorStateList(textColorResId));
        switch (textColorResId) {
            case R.color.toolbar_bg :
                mDividerViews[index].setVisibility(View.VISIBLE);
                break;
            case R.color.toolbar_text_color_normal :
                mDividerViews[index].setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_client :
                if(currentItemId != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;

            case R.id.login_admin :
                if(currentItemId != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.login_sign_in :
                if(currentItemId != 2) {
                    viewPager.setCurrentItem(2);
                }
                break;
        }
    }
}
