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
public class AdminShareCheckTabActivity extends Activity  implements AdminShareActivity.OnTabActivityResultListener {
    private static final String TAG = "ShareCheck";
    private ListView mListView;
    private CourseAdapter mCourseAdapter;
    private List<TechnologyShareCourseItem> mCourseList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_unapproved_share_course);
        initViews();
        initEvents();
        loadData();
    }

    private void initViews(){
        mListView = (ListView) findViewById(R.id.admin_all_unapproved_share_course);

    }

    private void initEvents(){
        mCourseList = new ArrayList<TechnologyShareCourseItem>();
        mListView.setOnItemClickListener(new CourseListOnItemClickListener());
    }

    MyJsonHttpResponseHandler mShareCheckHandler = new MyJsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mCourseList.add(makeTechnologyShareCourse(obj));
                }
                mCourseAdapter = new CourseAdapter(mCourseList, AdminShareCheckTabActivity.this);
                mListView.setAdapter(mCourseAdapter);
                Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG, "onFailure: ");
        }
    };

    /**
     * 加载全部数据
     */
    private void loadData(){
        //获取请求参数
        HashMap<String ,String> param = new HashMap<String,String>();
        RequestParams request = Utils.toParams(param);
        String url = Constant.BASE_URL + "share/list_apply_course.do";
        AsyncHttpHelper.post(url, request, mShareCheckHandler);
    }

    public static TechnologyShareCourseItem makeTechnologyShareCourse(JSONObject jsonObject) throws JSONException {
        TechnologyShareCourseItem technology = new TechnologyShareCourseItem();
        technology.setmCourseName(jsonObject.getString("course_name"));
        technology.setmCourseIndex(jsonObject.getInt("course_index"));
        technology.setmCourseDescription(jsonObject.getString("course_description"));
        technology.setmCourseLevel(jsonObject.getInt("course_level"));
        technology.setmLocation(jsonObject.getString("location"));
        technology.setmUserName(jsonObject.getString("user_name"));
        technology.setmUserId(jsonObject.getString("user_id"));
        technology.setmStartTime(jsonObject.getLong("start_time"));
        technology.setmEndTime(jsonObject.getLong("end_time"));
        return technology;
    }


    /**
     * @ClassName:
     * @Description: 实现点击列表选项监听
     * @Author: qiubing
     * @Date: 2015/9/9 15:12
     */
    private class CourseListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AdminShareCheckTabActivity.this, AdminShareCourseUnApprovedDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("CourseInfo", mCourseList.get(position));
            intent.putExtras(bundle);
            Log.e("text", "tedxt" + getParent().getClass().toString());
            getParent().startActivityForResult(intent, 2);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"sddddddd " + requestCode);
        if (resultCode == 1 || resultCode == 2){
            TechnologyShareCourseItem item = (TechnologyShareCourseItem) data.getSerializableExtra("result");
            mCourseList.remove(item);
            mCourseAdapter.notifyDataSetChanged();
        }
    }*/


    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"sddddddd " + requestCode);
        if (requestCode == 2 && resultCode == 1) {
        }
    }
}
