package cn.nubia.activity.client.fragment;


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
import android.widget.ExpandableListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseExpandableListAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.db.DbUtil;
import cn.nubia.db.SqliteHelper;
import cn.nubia.entity.Constant;
import cn.nubia.entity.CourseItem;
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

public class ClientStudentFragment extends Fragment {
	private RefreshLayout mRefreshLayout;
	private LoadViewUtil mLoadViewUtil;
	/**expandableListView*/
	private ExpandableListView mExpandableListView;
	/**adapter*/
	private CourseExpandableListAdapter mCourseExpandableListAdapter;
	/**用来存储courseItem的List*/
	private List<CourseItem> mCourseItemList;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_admin_all_course, container, false);
		initViews();
		initEvents();
//		Log.i("huhu", "student");
		return rootView;
	}

	private void initViews() {
		mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.allCourse_ExpandableListView);
		mRefreshLayout = (RefreshLayout) rootView.findViewById(R.id.admin_all_course_refreshLayout);
	}

	private  void initEvents() {
		mCourseItemList = new ArrayList<>();
		mLoadViewUtil = new LoadViewUtil(getActivity(), mExpandableListView, null);
		/**生成ExpandableListAdapter*/
		mCourseExpandableListAdapter = new CourseExpandableListAdapter(mCourseItemList, getActivity(), Constant.user.getUserID());
		/**为ExpandableListView指定填充数据的adapter*/
		mExpandableListView.setAdapter(mCourseExpandableListAdapter);
		/**去掉箭头*/
		mExpandableListView.setGroupIndicator(null);

		/**设置下拉刷新监听器*/
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mLoadViewUtil.showShortToast("刷新");
				mRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						// 更新最新数据
						loadData();
						mRefreshLayout.setRefreshing(false);
						mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER,
								mLoadViewUtil.getLoadingFailedFlag(), mLoadViewUtil.getNetworkFailedFlag());
					}
				}, 1500);
			}
		});

  /*      *//** 加载监听器*//*
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
        });*/

		/****先从数据库中加载数据**/
		AsyncLoadDBTask mAsyncTask = new AsyncLoadDBTask();
		mAsyncTask.execute();
		/****然后从网络中获取数据**/
		loadShow();
		loadData();
	}

	/**
	 * 从网络加载数据
	 */
	private void loadData() {
		RecordModifyFlag.RecordPair recordPair = RecordModifyFlag.getInstance().getRecordModifyMap().get(SqliteHelper.TB_NAME_MY_CLASS_STU);
		/**请求课程数据*/
		RequestParams requestParams = new RequestParams(Constant.getRequestParams());

		requestParams.put("user_id", Constant.user.getUserID());
		requestParams.put("course_index", recordPair.getLastCourseIndex());
		requestParams.put("course_record_modify_time",recordPair.getLastCourseModifyTime());
		requestParams.put("lesson_index", recordPair.getLastLessonIndex());
		requestParams.put("lesson_record_modify_time", recordPair.getLastLessonModifyTime());

		Log.e("XXXX", "parameter" + requestParams.toString());
		String url = Constant.BASE_URL + "course/get_relation_courses.do";
		AsyncHttpHelper.post(url, requestParams, jsonHttpResponseHandler);
	}

	/**请求课程数据服务器数据的Handler*/
	private final JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

			Log.e("XXXX","response"+response.toString());
			try {
				Log.e("XXXX","responseDataLength"+response.getJSONArray("data").length());
				mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
				if(response.getInt("code") != 0){
					return;
				}
				cancelLoadShow();
				Log.i("huhu", "student" + response.toString());
				if(response.getString("data") != null) {
					JSONArray jsonArray = response.getJSONArray("data");
					Log.e("XXXX","jsonArray"+jsonArray.toString());
					AsyncLoadHttpTask mLoadHttpTask = new AsyncLoadHttpTask();
					mLoadHttpTask.execute(jsonArray);
				}
			} catch (JSONException e) {
				Log.e("XXXX","JsonExecption"+e.toString());
				e.printStackTrace();
				mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
				cancelLoadShow();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Log.e("jsonArray", "" + statusCode);
			mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
			cancelLoadShow();
			mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.NETWORK_UNUSABLE, true);
		}
	};

	/**异步加载数据*/
	private class AsyncLoadHttpTask extends AsyncTask<JSONArray, Void, List<CourseItem>> {
		List<CourseItem> courseItemList;
		@Override
		protected List<CourseItem> doInBackground(JSONArray... params) {
			courseItemList = new ArrayList<CourseItem>(mCourseItemList);
			try {
				UpdateClassListHelper.updateAllClassData(params[0], courseItemList, SqliteHelper.TB_NAME_MY_CLASS_STU);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e("courseItemList",courseItemList.size()+"");
			return courseItemList;
		}


		@Override
		protected void onPostExecute(List<CourseItem> courseList){
			if(courseList != null){
				mCourseItemList.clear();
				mCourseItemList.addAll(courseList);
			}
			mCourseExpandableListAdapter.notifyDataSetChanged();
		}
	}

	/**加载数据库数据*/
	private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<CourseItem>> {
		@Override
		protected List<CourseItem> doInBackground(Void... params) {
			return DbUtil.getInstance(getActivity())
					.getCourseList(SqliteHelper.TB_NAME_MY_CLASS_STU);
		}

		@Override
		protected void onPostExecute(List<CourseItem> courseList) {
			if(courseList != null){
				mCourseItemList.addAll(courseList);
			}
			mCourseExpandableListAdapter.notifyDataSetChanged();
		}

	}

	private void loadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.MYCOURCE_STUDENT);
		intent.putExtra(Constant.MYCOURCE_STUDENT, "visible");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}
	private void cancelLoadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.MYCOURCE_STUDENT);
		intent.putExtra(Constant.MYCOURCE_STUDENT, "gone");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}
}