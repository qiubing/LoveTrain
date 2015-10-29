package cn.nubia.activity.client.fragment;


import android.app.Dialog;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.nubia.activity.LoginActivity1;
import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientCourseIntegrationRecordActivity;
import cn.nubia.activity.client.ClientExamScoreActivity;
import cn.nubia.activity.client.ClientMyAccountmanaPswmodifyActivity;
import cn.nubia.activity.client.ClientMyCheckRecordActivity;
import cn.nubia.activity.client.ClientShareCourseActivity;
import cn.nubia.activity.client.ClientUpdateIconActivity;
import cn.nubia.activity.client.FeedBackActivity;
import cn.nubia.component.CircleImageView;
import cn.nubia.component.PromptDialog;
import cn.nubia.entity.Constant;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.ProcessSPData;
import cn.nubia.util.Utils;

public class ClientMyFragment extends Fragment implements View.OnClickListener {
	private static final String TAG = "UserSetting";
	private static final int GET_PHOTO_CODE = 1;
	private static final String UPDATE_INTENT_ACTON = "cn.nubia.appUpdate.newVersion";
	private CircleImageView mCircleImageView;
	private ImageView mCheckRecord;
	private ImageView mCourseIntergration;
	private ImageView mExamScore;
	private ImageView mCourseShare;

	private ImageView mPasswordChange;
	private ImageView mFanKui;
	private ImageView mVersion;
	private ImageView mLogoutButton;

	private RelativeLayout mCheckIn_layout;
	private RelativeLayout mCourseIntergration_layout;
	private RelativeLayout mExamScore_layout;
	private RelativeLayout mCourseShare_layout;

	private RelativeLayout mPasswordChange_layout;
	private RelativeLayout mFanKui_layout;
	private RelativeLayout mVersion_layout;
	private RelativeLayout mLogoutButton_layout;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_user_my_setting, container, false);
		initViews();
		initEvents();
		return rootView;
	}

	private void initViews(){
		TextView myUserName = (TextView) rootView.findViewById(R.id.client_my_userName);
		myUserName.setText(Constant.user.getUserName());

		mCircleImageView = (CircleImageView) rootView.findViewById(R.id.client_my_head_imageView);

		mCheckRecord = (ImageView) rootView.findViewById(R.id.checkIn_forward);
		mCourseIntergration = (ImageView) rootView.findViewById(R.id.points_forward);
		mExamScore = (ImageView) rootView.findViewById(R.id.examScores_forward);
		mCourseShare = (ImageView) rootView.findViewById(R.id.shareCourses_forward);

		mPasswordChange = (ImageView) rootView.findViewById(R.id.passwordChange_forward);
		mVersion = (ImageView) rootView.findViewById(R.id.checkUpdate_forward);
		mFanKui = (ImageView) rootView.findViewById(R.id.feedback_forward);
		mLogoutButton = (ImageView) rootView.findViewById(R.id.logout_forward);

		mCheckIn_layout=(RelativeLayout)rootView.findViewById(R.id.checkIn_layout);
		mCourseIntergration_layout=(RelativeLayout)rootView.findViewById(R.id.points_layout);
		mExamScore_layout=(RelativeLayout)rootView.findViewById(R.id.myExam_layout);
		mCourseShare_layout=(RelativeLayout)rootView.findViewById(R.id.myShare_layout);
		mPasswordChange_layout=(RelativeLayout)rootView.findViewById(R.id.passwordChange_layout);
		mFanKui_layout=(RelativeLayout)rootView.findViewById(R.id.checkUpdate_layout);
		mVersion_layout=(RelativeLayout)rootView.findViewById(R.id.feedback_layout);
		mLogoutButton_layout=(RelativeLayout)rootView.findViewById(R.id.logout_layout);
	}

	private void initEvents(){
		mCircleImageView.setOnClickListener(ClientMyFragment.this);
		mCheckRecord.setOnClickListener(ClientMyFragment.this);
		mCourseIntergration.setOnClickListener(ClientMyFragment.this);
		mCourseShare.setOnClickListener(ClientMyFragment.this);
		mExamScore.setOnClickListener(ClientMyFragment.this);
		mPasswordChange.setOnClickListener(ClientMyFragment.this);
		mFanKui.setOnClickListener(ClientMyFragment.this);
		mVersion.setOnClickListener(ClientMyFragment.this);
		mLogoutButton.setOnClickListener(ClientMyFragment.this);

		mCheckIn_layout.setOnClickListener(ClientMyFragment.this);
		mCourseIntergration_layout.setOnClickListener(ClientMyFragment.this);
		mExamScore_layout.setOnClickListener(ClientMyFragment.this);
		mCourseShare_layout.setOnClickListener(ClientMyFragment.this);
		mPasswordChange_layout.setOnClickListener(ClientMyFragment.this);
		mFanKui_layout.setOnClickListener(ClientMyFragment.this);
		mVersion_layout.setOnClickListener(ClientMyFragment.this);
		mLogoutButton_layout.setOnClickListener(ClientMyFragment.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.client_my_head_imageView:
				Intent intent = new Intent(getActivity(), ClientUpdateIconActivity.class);
				startActivityForResult(intent, GET_PHOTO_CODE);
				break;
			case R.id.checkIn_forward:
			case R.id.checkIn_layout:
				myStartActivity(ClientMyCheckRecordActivity.class);
				break;
			case R.id.points_forward:
			case R.id.points_layout:
				myStartActivity(ClientCourseIntegrationRecordActivity.class);
				break;
			case R.id.examScores_forward:
			case R.id.myExam_layout:
				myStartActivity(ClientExamScoreActivity.class);
				break;
			case R.id.shareCourses_forward:
			case R.id.myShare_layout:
				myStartActivity(ClientShareCourseActivity.class);
				break;

			case R.id.passwordChange_forward:
			case R.id.passwordChange_layout:
				myStartActivity(ClientMyAccountmanaPswmodifyActivity.class);
				break;

			case R.id.checkUpdate_forward:
			case R.id.checkUpdate_layout:
				Intent service = new Intent(UPDATE_INTENT_ACTON);
				service.putExtra("command","update");
				this.getActivity().getApplicationContext().startService(service);
				break;
			case R.id.feedback_forward:
			case R.id.feedback_layout:
				myStartActivity(FeedBackActivity.class);
				break;

			case R.id.logout_forward:
			case R.id.logout_layout:
				//注销登录
				Intent logoutIntent = new Intent(getActivity(),
						LoginActivity1.class);
				logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK);
				ProcessSPData.clearSP(getActivity());//清除缓存的数据
				startActivity(logoutIntent);
				break;
		}
	}


	/**
	 * 更新对话框
	 */
	private void showUpdatDialog() {
		new PromptDialog.Builder(getActivity())
				.setMessage("最新版本：1.1\n最新版本已下载，是否安装？\n更新内容\n这只是一个演示\n学习一下也不错",
						null).setTitle("发现新版本")
				.setButton1("立即更新", new PromptDialog.OnClickListener() {
					@Override
					public void onClick(Dialog dialog, int which) {
						/**
						 * 开始下载更新呗
						 */
						Toast.makeText(getActivity(), "只是一个演示而已", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				}).setButton2("以后再说", new PromptDialog.OnClickListener() {
			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}


	/**
	 * 自定义的startActivity
	 *
	 * @param cls 需要启动的Activity类
	 */
	private void myStartActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GET_PHOTO_CODE && resultCode == 3) {
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
		String localPath = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
		Bitmap bitmap = Utils.getPictureFromSD(localPath);
		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			mCircleImageView = (CircleImageView) rootView.findViewById(R.id.client_my_head_imageView);
			mCircleImageView.setImageDrawable(drawable);
		}else{//从服务器中加载
			String remotePath = Constant.PICTURE_PREFIX +
					Utils.parseUrlStringFromServer(Constant.user.getUserIconURL());
			Log.e(TAG, "remotePath: " + remotePath);
			//从服务器加载图片，传递url地址过去
			AsyncHttpHelper.get(remotePath, mPictureHandler);
		}
	}


	/*Fragment没有这两个方法，待解决*/
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_PHOTO_CODE && resultCode == 3) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable drawable = new BitmapDrawable(photo);
				mCircleImageView.setImageDrawable(drawable);
			}
		}
	}*/

	/*@Override
	protected void onResume() {
		super.onResume();
		//头像图片存储路径
			*//**
			 * 先判断本地是否存储了头像，如果本地存储了头像，则使用本地头像；否则从服务器中加载头像
			 *//*
			String localPath = Constant.LOCAL_PATH + Constant.user.getUserID() + Constant.PORTRAIT;
		Bitmap bitmap = Utils.getPictureFromSD(localPath);
		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			mCircleImageView = (CircleImageView) rootView.findViewById(R.id.icon1);
			mCircleImageView.setImageDrawable(drawable);
		}else{//从服务器中加载
			String remotePath = Constant.PICTURE_PREFIX +
					Utils.parseUrlStringFromServer(Constant.user.getUserIconURL());
			Log.e(TAG, "remotePath: " + remotePath);
			//从服务器加载图片，传递url地址过去
			AsyncHttpHelper.get(remotePath, mPictureHandler);
		}
	}*/

	private final AsyncHttpResponseHandler mPictureHandler = new AsyncHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
			Log.e(TAG,"onSuccess: 加载成功");
			InputStream input = new ByteArrayInputStream(bytes);
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			Drawable drawable = new BitmapDrawable(bitmap);
			mCircleImageView = (CircleImageView) rootView.findViewById(R.id.icon1);
			if (mCircleImageView != null && drawable != null)
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
			Log.e(TAG,"onFailure: 加载失败");
		}
	};
}