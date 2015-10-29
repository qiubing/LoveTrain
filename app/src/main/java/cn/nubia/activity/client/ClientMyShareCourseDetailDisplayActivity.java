package cn.nubia.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.nubia.activity.BaseGestureActivity;
import cn.nubia.activity.R;
import cn.nubia.activity.admin.AdminExamDetailActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.ShareCourseMsg;
import cn.nubia.entity.TechnologyShareCourseItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;


/**
 * Created by JiangYu on 2015/9/2.
 */
public class ClientMyShareCourseDetailDisplayActivity extends BaseGestureActivity  implements View.OnClickListener{

    private ShareCourseMsg mShareCourseMsg;
    private Button passButton;
    private Button rejectButton;
    private Button changeButton;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sharecourse_detail_display);

        passButton = (Button) findViewById(R.id.share_pass);
        rejectButton = (Button) findViewById(R.id.share_reject);
        changeButton = (Button) findViewById(R.id.share_change);

        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);

        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String mSourse = bundle.getString("shareType");
        switch (mSourse) {
            case "share_check":
                passButton.setOnClickListener(this);
                rejectButton.setOnClickListener(this);
                mShareCourseMsg = new ShareCourseMsg(
                        (TechnologyShareCourseItem) bundle.getSerializable("shareCourse"));
                break;
            case "share_pass" :
                passButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                mShareCourseMsg = new ShareCourseMsg(
                        (TechnologyShareCourseItem) bundle.getSerializable("shareCourse"));
                break;
            case "share_client" :
                passButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                changeButton.setVisibility(View.VISIBLE);
                changeButton.setOnClickListener(this);
                mShareCourseMsg = (ShareCourseMsg) bundle.getSerializable("shareCourse");
                mShareCourseMsg.setUserId(Constant.user.getUserID());
                mShareCourseMsg.setUserName(Constant.user.getUserName());
        }

        holdView();
    }


    private void holdView() {
        TextView sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        TextView shareNameTextView = (TextView) findViewById(R.id.title_text);
        TextView shareNameDescTextView = (TextView) findViewById(R.id.share_describe);
        TextView shareName_teacher = (TextView) findViewById(R.id.share_teacher);
        TextView shareName_address = (TextView) findViewById(R.id.share_address);
        TextView shareName_start_time = (TextView) findViewById(R.id.share_start_time);
        TextView shareName_time = (TextView) findViewById(R.id.share_time);

        sub_page_title.setText("技术分享课程详情");
        shareNameTextView.setText(mShareCourseMsg.getCourseName());
        shareNameDescTextView.setText("分享简介：" + mShareCourseMsg.getCourseDescription());
        shareName_teacher.setText(mShareCourseMsg.getUserName());
        shareName_address.setText(mShareCourseMsg.getLocale());
        shareName_start_time.setText(TimeFormatConversion.toDateTime(mShareCourseMsg.getStartTime()));
        shareName_time.setText(TimeFormatConversion.toTimeLong(mShareCourseMsg.getStartTime(), mShareCourseMsg.getEndTime()) + "分钟");

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.share_pass :
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("course_index", mShareCourseMsg.getCourseIndex());
                String url = Constant.BASE_URL + "share/apply_to_going.do";
                AsyncHttpHelper.post(url, params, mApprovedHandler);
                loadingFailedRelativeLayout.setVisibility(View.GONE);
                networkUnusableRelativeLayout.setVisibility(View.GONE);
                break;
            case R.id.share_reject :
                RequestParams params2 = new RequestParams(Constant.getRequestParams());
                params2.put("course_index", mShareCourseMsg.getCourseIndex());
                String url2 = Constant.BASE_URL + "share/apply_to_bad.do";
                AsyncHttpHelper.post(url2, params2, mRejectHandler);
                loadingFailedRelativeLayout.setVisibility(View.GONE);
                networkUnusableRelativeLayout.setVisibility(View.GONE);
                break;
            case R.id.share_change:
                Intent intent = new Intent(ClientMyShareCourseDetailDisplayActivity.this
                        , ClientMyShareCourseDetailFillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "update");
                bundle.putSerializable("shareCourse", mShareCourseMsg);
                intent.putExtras(bundle);
                startActivity(intent);
                ClientMyShareCourseDetailDisplayActivity.this.finish();
        }
    }

    private final JsonHttpResponseHandler mApprovedHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response){
            try {
                if (response.getBoolean("data")){
                    Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this, "审核通过",Toast.LENGTH_LONG).show();
                    ClientMyShareCourseDetailDisplayActivity.this.finish();
                }else {
                    Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this,
                            "审核失败",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
        }
    };

    private final JsonHttpResponseHandler mRejectHandler = new JsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (response.getBoolean("data")){
                    Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this,
                            "否决通过",Toast.LENGTH_LONG).show();
                    ClientMyShareCourseDetailDisplayActivity.this.finish();
                }else{
                    Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this,
                            "否决失败",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(ClientMyShareCourseDetailDisplayActivity.this, "网络没有连接，请连接网络", Toast.LENGTH_SHORT).show();
        }
    };

    public void back(View view) {
        this.finish();
    }
}
