package cn.nubia.activity.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.adapter.ClientExamScoreAdapter;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ExamResultItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.EntityFactoryGenerics;

/**
 * @Author: qiubing
 * @Date: 2015/9/6 9:19
 */

public class ClientExamScoreActivity extends Activity {
    private static final String TAG = "ExamScore";
    private ListView mListView;
    private List<ExamResultItem> mResultList;
    private ClientExamScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam_score);
        initEvents();
    }

    private void initEvents(){
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.user_exam_score_title);
        TextView text = (TextView) linear.findViewById(R.id.sub_page_title);
        text.setText("考试成绩");
        mListView = (ListView)findViewById(R.id.exam_score_detail);

        //请求参数
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("user_id","0001");
        RequestParams request = Utils.toParams(params);
        String url = Constant.BASE_URL ;
        AsyncHttpHelper.post(url, request, mClientExamScoreHandler);
    }

    AsyncHttpResponseHandler mClientExamScoreHandler = new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            Log.e(TAG, "onSuccess");
            String strJson = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"lesson_name\":\"Java基础一\",\"lesson_index\":1,\"exam_score\":30}," +
                    "{\"lesson_name\":\"Java基础二\",\"lesson_index\":2,\"exam_score\":40}," +
                    "{\"lesson_name\":\"Java基础三\",\"lesson_index\":3,\"exam_score\":50}," +
                    "{\"lesson_name\":\"Java基础四\",\"lesson_index\":4,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础五\",\"lesson_index\":5,\"exam_score\":70}," +
                    "{\"lesson_name\":\"Java基础六\",\"lesson_index\":6,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础七\",\"lesson_index\":7,\"exam_score\":90}," +
                    "{\"lesson_name\":\"Java基础八\",\"lesson_index\":8,\"exam_score\":60}," +
                    "{\"lesson_name\":\"Java基础九\",\"lesson_index\":9,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础十\",\"lesson_index\":10,\"exam_score\":60}," +
                    "{\"lesson_name\":\"Java基础十一\",\"lesson_index\":11,\"exam_score\":70}," +
                    "{\"lesson_name\":\"Java基础十二\",\"lesson_index\":12,\"exam_score\":80}," +
                    "{\"lesson_name\":\"Java基础十三\",\"lesson_index\":13,\"exam_score\":80}]}";
            try {
                JSONObject result = new JSONObject(strJson);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.EXAMRESULT, result);
                int code = factoryGenerics.getCode();
                //成功返回
                if(code == 0){
                    mResultList = (List<ExamResultItem>) factoryGenerics.get();
                    mAdapter = new ClientExamScoreAdapter(mResultList,ClientExamScoreActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            Log.e(TAG, "onFailure");
           /* String strJson = "{\"code\":0,\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[]," +
                    "\"data\":[{\"lesson_name\":\"Java基础一\",\"lesson_index\":1,\"exam_score\":30}," +
                    "{\"lesson_name\":\"Java基础二\",\"lesson_index\":2,\"exam_score\":40}," +
                    "{\"lesson_name\":\"Java基础三\",\"lesson_index\":3,\"exam_score\":50}," +
                    "{\"lesson_name\":\"Java基础四\",\"lesson_index\":4,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础五\",\"lesson_index\":5,\"exam_score\":70}," +
                    "{\"lesson_name\":\"Java基础六\",\"lesson_index\":6,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础七\",\"lesson_index\":7,\"exam_score\":90}," +
                    "{\"lesson_name\":\"Java基础八\",\"lesson_index\":8,\"exam_score\":60}," +
                    "{\"lesson_name\":\"Java基础九\",\"lesson_index\":9,\"exam_score\":-1}," +
                    "{\"lesson_name\":\"Java基础十\",\"lesson_index\":10,\"exam_score\":60}," +
                    "{\"lesson_name\":\"Java基础十一\",\"lesson_index\":11,\"exam_score\":70}," +
                    "{\"lesson_name\":\"Java基础十二\",\"lesson_index\":12,\"exam_score\":80}," +
                    "{\"lesson_name\":\"Java基础十三\",\"lesson_index\":13,\"exam_score\":80}]}";
            try {
                JSONObject result = new JSONObject(strJson);
                EntityFactoryGenerics factoryGenerics =
                        new EntityFactoryGenerics(EntityFactoryGenerics.ItemType.EXAMRESULT, result);
                int code = factoryGenerics.getCode();
                //成功返回
                if(code == 0){
                    mResultList = (List<ExamResultItem>) factoryGenerics.get();
                    mAdapter = new ClientExamScoreAdapter(mResultList,ClientExamScoreActivity.this);
                    mListView.setAdapter(mAdapter);
                    Utils.setListViewHeightBasedOnChildren(mListView);//自适应ListView的高度
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    };

    /**
     * 返回箭头绑定事件，即退出该页面
     * @param view
     */
    public void back(View view){
        this.finish();
    }
}
