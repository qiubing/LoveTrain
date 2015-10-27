package cn.nubia.activity.admin.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.nubia.activity.LoginActivity1;
import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminCreditActivity;
import cn.nubia.activity.admin.AdminCreditsAwardActivity;
import cn.nubia.activity.admin.AdminRateActivity;
import cn.nubia.activity.admin.AdminScoreActivity;
import cn.nubia.activity.admin.AdminUserActivity;
import cn.nubia.activity.client.AboutUsActivity;
import cn.nubia.activity.client.ClientMyAccountmanaPswmodifyActivity;
import cn.nubia.activity.client.ClientUpdateIconActivity;
import cn.nubia.component.CircleImageView;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.ProcessSPData;
import cn.nubia.util.Utils;


public class AdminMyFragment extends Fragment implements View.OnClickListener {
	private static final int GET_PHOTO_CODE_ADMIN = 1010;
	private CircleImageView mCircleImageView;
	private ImageView mQueryScoreTV;
	private ImageView mQueryCreditTV;
	private ImageView mRateManageTV;
	private ImageView mAccountManageTV;
	private ImageView mUserManageTV;
	private ImageView mAboutUsTV;
	private ImageView mChangeAccoutn;
	private ImageView mAwardCreditTV;
	private TextView myUserName;
	private View rootView;

	private RelativeLayout scoreQuery_imageView_layout;
	private RelativeLayout creditQuery_imageView_layout;
	private RelativeLayout creditAward_imageView_layout;
	private RelativeLayout rateManage_imageView_layout;
	private RelativeLayout userManage_imageView_layout;
	private RelativeLayout accountManage_imageView_layout;
	private RelativeLayout about_us_imageView_layout;
	private RelativeLayout change_account_imageView_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_manager_my_setting, container, false);
		initViews();
		initEvents();
		return rootView;
	}

	private void initViews(){
		mQueryScoreTV = (ImageView) rootView.findViewById(R.id.queryscore);
		mQueryCreditTV = (ImageView) rootView.findViewById(R.id.querycredit);
		mRateManageTV = (ImageView) rootView.findViewById(R.id.ratemanage);
		mAccountManageTV = (ImageView) rootView.findViewById(R.id.accountmanage);
		mUserManageTV = (ImageView) rootView.findViewById(R.id.usermanage);
		mAboutUsTV = (ImageView) rootView.findViewById(R.id.about_us);
		mChangeAccoutn = (ImageView) rootView.findViewById(R.id.change_account);
		mAwardCreditTV = (ImageView) rootView.findViewById(R.id.awardcredit);
		myUserName = (TextView) rootView.findViewById(R.id.client_my_userName);
		myUserName.setText(Constant.user.getUserName());
		mCircleImageView = (CircleImageView) rootView.findViewById(R.id.client_my_head_imageView);

		scoreQuery_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.scoreQuery_imageView_layout);
		creditQuery_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.creditQuery_imageView_layout);
		creditAward_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.creditAward_imageView_layout);
		rateManage_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.rateManage_imageView_layout);
		userManage_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.userManage_imageView_layout);
		accountManage_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.accountManage_imageView_layout);
		about_us_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.about_us_imageView_layout);
		change_account_imageView_layout=(RelativeLayout)rootView.findViewById(R.id.change_account_imageView_layout);
	}

	private void initEvents(){
		mCircleImageView.setOnClickListener(this);
		mQueryScoreTV.setOnClickListener(this);
		mQueryCreditTV.setOnClickListener(this);
		mRateManageTV.setOnClickListener(this);
		mAccountManageTV.setOnClickListener(this);
		mUserManageTV.setOnClickListener(this);
		mAboutUsTV.setOnClickListener(this);
		mChangeAccoutn.setOnClickListener(this);
		mAwardCreditTV.setOnClickListener(this);

		scoreQuery_imageView_layout.setOnClickListener(this);
		creditQuery_imageView_layout.setOnClickListener(this);
		creditAward_imageView_layout.setOnClickListener(this);
		rateManage_imageView_layout.setOnClickListener(this);
		userManage_imageView_layout.setOnClickListener(this);
		accountManage_imageView_layout.setOnClickListener(this);
		about_us_imageView_layout.setOnClickListener(this);
		change_account_imageView_layout.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_PHOTO_CODE_ADMIN && resultCode == 3) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				mCircleImageView.setImageDrawable(drawable);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//头像图片存储路径
		String path = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
		Bitmap bitmap = Utils.getPictureFromSD(path);
		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			Log.i("huhu", "Path: " + path);
			mCircleImageView = (CircleImageView) rootView.findViewById(R.id.client_my_head_imageView);
			mCircleImageView.setImageDrawable(drawable);
		}else{//从服务器中加载
			String remotePath = Constant.PICTURE_PREFIX +
					Utils.parseUrlStringFromServer(Constant.user.getUserIconURL());
			Log.i("huhu", "remotePath: " + remotePath);
			//从服务器加载图片，传递url地址过去
			AsyncHttpHelper.get(remotePath, mPictureHandler);
		}
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_PHOTO_CODE_ADMIN && resultCode == 3) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				mCircleImageView.setImageDrawable(drawable);
			}
		}
	}*/
	/*
	@Override
	protected void onResume() {
		super.onResume();
		//头像图片存储路径
		String path = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
		Bitmap bitmap = Utils.getPictureFromSD(path);
		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			mCircleImageView = (CircleImageView) findViewById(R.id.icon1);
			mCircleImageView.setImageDrawable(drawable);
		}else{//从服务器中加载
			String remotePath = Constant.PICTURE_PREFIX +
					Utils.parseUrlStringFromServer(Constant.user.getUserIconURL());
			Log.e("AdminMyTabActivity", "remotePath: " + remotePath);
			//从服务器加载图片，传递url地址过去
			AsyncHttpHelper.get(remotePath, mPictureHandler);
		}
	}*/

	private final AsyncHttpResponseHandler mPictureHandler = new AsyncHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
			Log.i("huhu","onSuccess: 加载成功");
			InputStream input = new ByteArrayInputStream(bytes);
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			Drawable drawable = new BitmapDrawable(bitmap);
			mCircleImageView = (CircleImageView) rootView.findViewById(R.id.client_my_head_imageView);
			mCircleImageView.setImageDrawable(drawable);
			//同时将图片保存到本地，用来下次加载
			try {
				Utils.saveFile(bitmap,Constant.user.getUserID() + Constant.PORTRAIT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
			Log.e("AdminMyTabActivity","onFailure: 加载失败");
		}
	};

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		Intent intent;
		switch (viewId) {
			case R.id.client_my_head_imageView:
				intent = new Intent(getActivity(), ClientUpdateIconActivity.class);
				startActivityForResult(intent, GET_PHOTO_CODE_ADMIN);
//				startActivity(intent);
				break;
			case R.id.queryscore:
			case R.id.scoreQuery_imageView_layout:
				intent = new Intent(getActivity(), AdminScoreActivity.class);
				startActivity(intent);
				break;

			case R.id.querycredit:
			case R.id.creditQuery_imageView_layout:
				intent = new Intent(getActivity(), AdminCreditActivity.class);
				startActivity(intent);
				break;
			case R.id.awardcredit:
			case R.id.creditAward_imageView_layout:
				intent = new Intent(getActivity(), AdminCreditsAwardActivity.class);
				startActivity(intent);
				break;
			case R.id.ratemanage:
			case R.id.rateManage_imageView_layout:
				intent = new Intent(getActivity(), AdminRateActivity.class);
				startActivity(intent);
				break;
			case R.id.usermanage:
			case R.id.userManage_imageView_layout:
				intent = new Intent(getActivity(), AdminUserActivity.class);
				startActivity(intent);
				break;
			case R.id.accountmanage:
			case R.id.accountManage_imageView_layout:
				intent = new Intent(getActivity(), ClientMyAccountmanaPswmodifyActivity.class);
				startActivity(intent);
				break;
			case R.id.about_us:
			case R.id.about_us_imageView_layout:
				//DialogUtil.showDialog(AdminMyTabActivity.this, "LoveTrain!");
				intent = new Intent(getActivity(), AboutUsActivity.class);
				startActivity(intent);
				break;
			case R.id.change_account:
			case R.id.change_account_imageView_layout:
				intent = new Intent(getActivity(), LoginActivity1.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK);
				ProcessSPData.clearSP(getActivity());
				startActivity(intent);
//				this.finish();
			default:
				break;
		}
	}
}