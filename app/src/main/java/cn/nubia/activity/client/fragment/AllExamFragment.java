package cn.nubia.activity.client.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminAddCourseActivity;
import cn.nubia.activity.admin.AdminAddExamActivity;
import cn.nubia.activity.admin.AdminExamDetailActivity;
import cn.nubia.adapter.ExamAdapter;
import cn.nubia.component.RefreshLayout;
import cn.nubia.db.DbUtil;
import cn.nubia.db.SqliteHelper;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.RecordModifyFlag;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.LoadViewUtil;
import cn.nubia.util.UpdateClassListHelper;

public class AllExamFragment extends Fragment{
	private ListView mAllExamListView;
	private ExamAdapter mExamAdapter;
	private RefreshLayout mRefreshLayout;
	private LoadViewUtil mLoadViewUtil;
	private List<ExamItem> mExamList;
	private static final String URL = Constant.BASE_URL + "course/get_all_exams.do";
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_admin_all_exam, container, false);
		initViews();
		initEvents();
		return rootView;
	}

	private void initViews(){
		mAllExamListView = (ListView) rootView.findViewById(R.id.admin_all_exam_list);
		mRefreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
	}

	private void initEvents() {
		if(Constant.IS_ADMIN) {
			ImageView addExamView = (ImageView) rootView.findViewById(R.id.item_exam_add);
			addExamView.setVisibility(View.VISIBLE);
			addExamView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), AdminAddExamActivity.class);
//					startActivityForResult(intent, 1);
					startActivity(intent);
				}
			});
		}
		mExamList = new ArrayList<>();

		mLoadViewUtil = new LoadViewUtil(getActivity(), mAllExamListView, null);
		mExamAdapter = new ExamAdapter(mExamList, getActivity());
		mAllExamListView.setAdapter(mExamAdapter);
		mAllExamListView.setOnItemClickListener(new ExamListOnItemClickListener());

		// 设置下拉刷新监听器
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

      /*  // 加载监听器
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
*/
		AsyncLoadDBTask dbTask = new AsyncLoadDBTask();
		dbTask.execute();

		loadShow();
		loadData();
	}

	private void loadData(){
		RecordModifyFlag.RecordPair recordPair = RecordModifyFlag.getInstance().getRecordModifyMap().get(SqliteHelper.TB_NAME_EXAM);
		RequestParams requestParams = new RequestParams(Constant.getRequestParams());
		requestParams.put("exam_record_modify_time", recordPair.getLastExamModifyTime());
		requestParams.put("exam_index", recordPair.getLastExamIndex());
		AsyncHttpHelper.post(URL, requestParams, myJsonHttpResponseHandler);
	}

	private final JsonHttpResponseHandler myJsonHttpResponseHandler = new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			try {
				if(response.getInt("code") != 0){
					Toast.makeText(getActivity(),
							response.getJSONArray("message").toString(), Toast.LENGTH_SHORT).show();
					return;
				}else{
					mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_SUCCESS);
				}

				JSONArray jsonArray = response.getJSONArray("data");
				cancelLoadShow();
				if(jsonArray!=null && jsonArray.length() > 0){
					AsyncLoadJsonTask asyncLoadJsonTask  = new AsyncLoadJsonTask();
					asyncLoadJsonTask.execute(jsonArray);
				}
			} catch (JSONException e) {
				mLoadViewUtil.setLoadingFailedFlag(Constant.LOADING_FAILED);
				cancelLoadShow();
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			mLoadViewUtil.setLoadingFailedFlag(Constant.NETWORK_UNUSABLE);
			cancelLoadShow();
			mRefreshLayout.showLoadFailedView(Constant.SHOW_HEADER, Constant.NETWORK_UNUSABLE, true);
		}
	};

	private class AsyncLoadJsonTask extends AsyncTask<JSONArray, Void, List<ExamItem>> {
		List<ExamItem> examItemList;
		@Override
		protected List<ExamItem> doInBackground(JSONArray... params) {
			examItemList = new ArrayList<ExamItem>(mExamList);
			try {
				UpdateClassListHelper.updateAllExamData(params[0], examItemList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return examItemList;
		}

		@Override
		protected void onPostExecute(List<ExamItem> examItemList){
			if(examItemList != null){
				mExamList.clear();
				mExamList.addAll(examItemList) ;
			}
			mExamAdapter.notifyDataSetChanged();
		}
	}

	private class AsyncLoadDBTask extends AsyncTask<Void, Void, List<ExamItem>> {
		@Override
		protected List<ExamItem> doInBackground(Void... params) {
			DbUtil dbUtil = DbUtil.getInstance(getActivity());
			return dbUtil.getExamList();
		}

		@Override
		protected void onPostExecute(List<ExamItem> examItemList) {
			if(examItemList != null){
				mExamList.clear();
				mExamList.addAll(examItemList);
			}
			mExamAdapter.notifyDataSetChanged();
		}
	}

	private class ExamListOnItemClickListener implements AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(getActivity(), AdminExamDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ExamInfo", mExamList.get(arg2));
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		}
	}


	/*@Override
	public void onTabActivityResult(int requestCode, int resultCode, Item data) {
		switch (resultCode){
			case 1:
				if(data != null && data instanceof ExamItem){
					mExamList.remove(data);
					DbUtil.getInstance(null).deleteExamItem((ExamItem) data);
				}
				break;
			case 2:
				if(data != null && data instanceof ExamItem){
					mExamList.add(0,(ExamItem) data);
					DbUtil.getInstance(null).insertExamItem((ExamItem) data);
				}
				break;
			default:
				break;
		}
		mExamAdapter.notifyDataSetChanged();
	}*/

	private void loadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.EXAM);
		intent.putExtra(Constant.EXAM, "visible");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}
	private void cancelLoadShow() {
		Intent intent = new Intent();
		intent.setAction(Constant.EXAM);
		intent.putExtra(Constant.EXAM, "gone");
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}

}