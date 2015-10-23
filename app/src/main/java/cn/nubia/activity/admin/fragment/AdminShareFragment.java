package cn.nubia.activity.admin.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.nubia.activity.R;

public class AdminShareFragment extends Fragment implements View.OnClickListener{
	private ViewPager viewPager;
	private TextView[] mTextViews = new TextView[2];
	private View[] mDividerViews = new View[2];
	private int currentItemId;
	private View rootView;
//	private ImageView searchView;

	// 重写该方法，该方法返回的View将作为Fragment显示的组件
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_admin_share_cource, container, false);
		initViews();
		initEvents();
		Log.i("huhu", "AdminShareFragment,onCreateView");
		return rootView;
	}

	void initViews() {
		viewPager = (ViewPager) rootView.findViewById(R.id.admin_share_viewpager);
		mTextViews[0] = (TextView) rootView.findViewById(R.id.admin_check_share);
		mTextViews[1] = (TextView) rootView.findViewById(R.id.admin_pass_share);
		mDividerViews[0] = rootView.findViewById(R.id.admin_check_share_divider);
		mDividerViews[1] = rootView.findViewById(R.id.admin_pass_share_divider);
		/*searchView = (ImageView) rootView.findViewById(R.id.client_search);*/
	}

	void initEvents() {
		for (TextView mTextView : mTextViews) {
			mTextView.setOnClickListener(AdminShareFragment.this);
		}
//		searchView.setOnClickListener(ClientMyCourceFragment.this);
		updataItemUI(0, R.color.toolbar_bg);
		FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
				((FragmentActivity)getActivity()).getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTextViews.length;
			}

			@Override
			public android.support.v4.app.Fragment getItem(int index) {
				android.support.v4.app.Fragment fragment = null;
				switch (index) {
					case 0 :
						fragment = new AdminShareCheckFragment();
						break;
					case 1 :
						fragment = new AdminSharePassFragment();
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
		mTextViews[index].setTextColor(getActivity().getResources().getColorStateList(textColorResId));
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
			case R.id.admin_check_share :
				if(currentItemId != 0) {
					viewPager.setCurrentItem(0);
				}
				break;

			case R.id.admin_pass_share :
				if(currentItemId != 1) {
					viewPager.setCurrentItem(1);
				}
				break;
			/*case R.id.client_search :
				Toast.makeText(getActivity(), "下个版本完成，敬请期待", Toast.LENGTH_SHORT).show();*/
		}
	}
}
