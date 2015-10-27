package cn.nubia.activity.client.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientMainActivity;
import cn.nubia.activity.client.ClientMyShareCourseDetailFillActivity;

/**客户端我的课程界面：顶部滑动导航栏
 * 采用RelativeLayout+ViewPager结构
 * 布局为LinearLayout，上面为两个标题条(每个标题条下面为分隔条，处于隐藏状态)和扫描ImageView，下面为一条分隔条，和标题条的高度一致，
 * 但比标题条下面的分隔条要窄，最下面为ViewPager
 * ViewPager使用了FragmentPagerAdapter(分页数据为旧版本的Fragment)，而FragmentPagerAdapter需要提供旧版本的FragmentManager
 * 作为构造器参数，因此，该Fragment所在的Activity(ClientMainActivity)需要继承FragmentActivity，
 * 标题条与ViewPager的内容为双向关联关系，一个改变时，另一个跟着改变，
 * Created by 胡立加 on 2015/10/22.
 */
public class ClientMyCourceFragment extends Fragment implements View.OnClickListener{
	private ViewPager viewPager;
	private TextView[] mTextViews = new TextView[2];
	private View[] mDividerViews = new View[2];
	private int currentItemId;
	private View rootView;
	private ImageView addShareCource;
//	private ImageView searchView;

	// 重写该方法，该方法返回的View将作为Fragment显示的组件
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_client_my_cource, container, false);
		initViews();
		initEvents();
		return rootView;
	}

	private void initViews() {
		viewPager = (ViewPager) rootView.findViewById(R.id.client_my_course_viewpager);
		mTextViews[0] = (TextView) rootView.findViewById(R.id.client_student);
		mTextViews[1] = (TextView) rootView.findViewById(R.id.client_teacher);
		mDividerViews[0] = rootView.findViewById(R.id.client_student_divider);
		mDividerViews[1] = rootView.findViewById(R.id.client_teacher_divider);
		addShareCource = (ImageView) rootView.findViewById(R.id.item_share_add);
		/*searchView = (ImageView) rootView.findViewById(R.id.client_search);*/
	}

	private void initEvents() {
		for (TextView mTextView : mTextViews) {
			mTextView.setOnClickListener(ClientMyCourceFragment.this);
		}
		addShareCource.setOnClickListener(ClientMyCourceFragment.this);
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
						fragment = new ClientStudentFragment();
						break;
					case 1 :
						fragment = new ClientTeacherFragment();;
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
			case R.id.client_student :
				if(currentItemId != 0) {
					viewPager.setCurrentItem(0);
				}
				break;

			case R.id.client_teacher :
				if(currentItemId != 1) {
					viewPager.setCurrentItem(1);
				}
				break;
			case R.id.item_share_add :
				Intent intent = new Intent(getActivity(), ClientMyShareCourseDetailFillActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type","insert");
				intent.putExtras(bundle);
				startActivity(intent);
		}
	}
}
