package cn.nubia.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.CourseAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class AdminSharePassTabActivity extends Activity {

    private ListView mListView;
    private CourseAdapter mCourseAdapter;
    private List<TechnologyShareCourseItem> mCourseList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_approved_share_course);
        initViews();
        initEvents();
        loadData();
    }

    private void initViews(){
        mListView = (ListView) findViewById(R.id.admin_all_approved_share_course);
    }

    private void initEvents(){
        mCourseList = new ArrayList<TechnologyShareCourseItem>();
        mListView.setOnItemClickListener(new CourseListOnItemClickListener());
    }

    private void loadData(){
        //获取请求参数
        HashMap<String,String> params = new HashMap<String,String>();
        RequestParams request = Utils.toParams(params);
        String url = Constant.BASE_URL + "share/list_going_course.do";
        AsyncHttpHelper.post(url, request, mSharePassHandler);
        mCourseAdapter = new CourseAdapter(mCourseList,this);
        mListView.setAdapter(mCourseAdapter);
    }

    MyJsonHttpResponseHandler mSharePassHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.e("onSuccess", response.toString());
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mCourseList.add(AdminShareCheckTabActivity.makeTechnologyShareCourse(obj));
                }
                mCourseAdapter = new CourseAdapter(mCourseList, AdminSharePassTabActivity.this);
                mListView.setAdapter(mCourseAdapter);
                Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("onFailure", throwable.toString());
        }
    };

    /**
    * @ClassName:
    * @Description: 实现点击列表选项监听
    * @Author: qiubing
    * @Date: 2015/9/9 15:12
    */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AdminSharePassTabActivity.this, AdminShareCourseApprovedDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CourseInfo",mCourseList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
