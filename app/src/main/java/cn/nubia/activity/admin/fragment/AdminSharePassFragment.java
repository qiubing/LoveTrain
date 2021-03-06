package cn.nubia.activity.admin.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientMyShareCourseDetailDisplayActivity;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.entity.Constant;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;

public class AdminSharePassFragment extends Fragment {
	private ListView mListView;
	private CourseAdapter mCourseAdapter;
	private List<TechnologyShareCourseItem> mCourseList;
	private RefreshLayout mRefreshLayout;
	private LoadViewUtil mLoadViewUtil;
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_admin_approved_share_course, container, false);
		initViews();
		initEvents();
		return rootView;
	}


	private void initViews() {
		mListView = (ListView) rootView.findViewById(R.id.admin_all_approved_share_course);
		mRefreshLayout = (RefreshLayout) rootView.findViewById(R.id.evaluate_refreshLayout_pass);
	}

	private void initEvents() {
		mCourseList = new ArrayList<>();
		mLoadViewUtil = new LoadViewUtil(getActivity(), mListView, null);
		mListView.setOnItemClickListener(new CourseListOnItemClickListener());

		mCourseAdapter = new CourseAdapter(mCourseList, getActivity());
		mListView.setAdapter(mCourseAdapter);

		// 设置下拉刷新监听器
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mLoadViewUtil.showShortToast("刷新");
				mRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {

						loadData();
						mRefreshLayout.setRefreshing(false);
						mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,
								mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
					}
				}, 1500);
			}
		});

		// 加载监听器
		mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
			@Override
			public void onLoad() {
				mLoadViewUtil.showShortToast("加载更多");
				mRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						//加载历史数据
						loadData();
						mRefreshLayout.setLoading(false);
						mRefreshLayout.showLoadFailedView(Constant.SHOW_FOOTER,
								mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
					}
				}, 1500);
			}
		});

		loadShow();
		loadData();
	}

	private void loadData() {
		//获取请求参数
		RequestParams params = new RequestParams(Constant.getRequestParams());
		String url = Constant.BASE_URL + "share/list_going_course.do";
		AsyncHttpHelper.post(url, params, mSharePassHandler);
		mCourseAdapter = new CourseAdapter(mCourseList, getActivity());
		mListView.setAdapter(mCourseAdapter);
	}

	private final JsonHttpResponseHandler mSharePassHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			try {
				if (response != null && response.getInt("code") == 0 && response.getJSONArray("data") != null) {
					mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
//					Log.e("SharePass", response.toString());
					JSONArray jsonArray = response.getJSONArray("data");
					AsyncParseJsonTask asyncParseJsonTask = new AsyncParseJsonTask();
					asyncParseJsonTask.execute(jsonArray);
					cancelLoadShow();
				}
			} catch (JSONException e) {
				mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
				e.printStackTrace();
				cancelLoadShow();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
			cancelLoadShow();
			mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.NETWORK_UNUSABLE, true);
		}
	};


	private class AsyncParseJsonTask extends AsyncTask<JSONArray, Void, List<TechnologyShareCourseItem>> {

		@Override
		protected List<TechnologyShareCourseItem> doInBackground(JSONArray... params) {
			List<TechnologyShareCourseItem> courseList = new ArrayList<>();
			for (int i = 0; i < params[0].length(); i++) {
				JSONObject obj;
				try {
					obj = params[0].getJSONObject(i);
					courseList.add(AdminShareCheckFragment.makeTechnologyShareCourse(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Collections.reverse(courseList);
			return courseList;
		}

		@Override
		protected void onPostExecute(List<TechnologyShareCourseItem> technologyShareCourseItems) {
			super.onPostExecute(technologyShareCourseItems);
			mCourseList.clear();
			if (technologyShareCourseItems != null && technologyShareCourseItems.size() != 0) {
				mCourseList.addAll(technologyShareCourseItems);
			}
			mCourseAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * ClassName:
	 * Description: 实现点击列表选项监听
	 * Author: qiubing
	 * Date: 2015/9/9 15:12
	 */
	private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), ClientMyShareCourseDetailDisplayActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("shareType", "share_pass");
			bundle.putSerializable("shareCourse", mCourseList.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	private void loadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.SHARE_OK);
		intent.putExtra(Constant.SHARE_OK, "visible");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

	}
	private void cancelLoadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.SHARE_OK);
		intent.putExtra(Constant.SHARE_OK, "gone");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}
}