package cn.nubia.activity;




/*胡立加，用户向导
* activity_guide：相对布局，ViewPager铺满全屏，然后四个dot位于相对布局的底部，局部覆盖ViewPager
* mViewPager用于展示四个layout，使用PagerAdapter为其提供数据，数据源为四个layout的List<View>
* 并为mViewPager绑定监听器，当ViewPager显示的Fragment发生改变时改变相应dot的状态
* */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {


	private final List<View> mViewList = new ArrayList<>();
	// 底部小点图片
	private ImageView[] dots;
	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		//准备数据
		View v1 = View.inflate(this, R.layout.guide_one, null);
		View v2 = View.inflate(this, R.layout.guide_two, null);
		View v3 = View.inflate(this, R.layout.guide_three, null);
		View v4 = View.inflate(this, R.layout.guide_four, null);
		mViewList.add(v1);
		mViewList.add(v2);
		mViewList.add(v3);
		mViewList.add(v4);

		initDots();

		ViewPager mViewPager = (ViewPager)findViewById(R.id.guide_viewPager);
		mViewPager.setAdapter(pagerAdapter);


		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {

			}

			@Override
			//当ViewPager显示的Fragment发生改变时激发该方法
			public void onPageSelected(int arg0) {
				setCurrentDot(arg0);
			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});
	}

	private void initDots() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guide_linearLayout);
		dots = new ImageView[mViewList.size()];
		// 循环取得小点图片
		for (int i = 0; i < mViewList.size(); i++) {
			dots[i] = (ImageView) linearLayout.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > mViewList.size() - 1
				|| currentIndex == position) {
			return;
		}
		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = position;
	}


	private final PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return mViewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView( mViewList.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView( mViewList.get(position));
			if (position ==  mViewList.size() - 1) {
				Button startImg = (Button) findViewById(R.id.guide_logIn);
				startImg.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(GuideActivity.this, LoginActivity1.class);
						startActivity(intent);
						GuideActivity.this.finish();
					}
				});
			}
			return  mViewList.get(position);
		}

	};

}
