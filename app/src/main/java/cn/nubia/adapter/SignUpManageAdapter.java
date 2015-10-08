package cn.nubia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.SignUpItem;
import cn.nubia.util.AsyncHttpHelper;
/**
 * Created by hexiao on 2015/9/19.
 */
public class SignUpManageAdapter extends BaseAdapter {
    private static final String TAG = "SignUpManageAdapter";
    private final List<SignUpItem> mList;
    private final Context mContext;

    public SignUpManageAdapter(List<SignUpItem> list,Context context){
        this.mList = list;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHold;
        if (convertView == null) {
            viewHold = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.activity_admin_signin_manage_item, null);
            viewHold.name = (TextView) convertView.findViewById(R.id.admin_personInfo_nameTextView);
            viewHold.agreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_agreeButton);
            viewHold.disagreeButton = (Button) convertView.findViewById(R.id.admin_signIn_manage_disagreeButton);
            viewHold.result = (TextView) convertView.findViewById(R.id.sign_up_success);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHolder) convertView.getTag();
        }
        final SignUpItem item =  mList.get(position);
        //根据Item中的Is_enroll值来选择显示结果
        viewHold.name.setText(item.getUserName() + "  " + item.getUserID());
        if (item.isEnroll()){
            viewHold.agreeButton.setVisibility(View.GONE);
            viewHold.disagreeButton.setVisibility(View.GONE);
            viewHold.result.setVisibility(View.VISIBLE);
        }


        /**按钮事件**/
        viewHold.agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout = (RelativeLayout) v.getParent();

                /**同意按钮是在位置0*/
                Button agreeBtn = (Button) relativeLayout.getChildAt(0);
                agreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                agreeBtn.setEnabled(false);

                /**否决按钮是在位置1*/
                Button disagreeBtn = (Button) relativeLayout.getChildAt(1);
                disagreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                disagreeBtn.setEnabled(false);

                /**发送请求到服务器*/
                //获取请求参数
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("user_id",item.getUserID());
                params.put("course_index", item.getCourseID());
                params.put("is_enroll", true);
                String url = Constant.BASE_URL + "enroll/check_enroll_course.do";
                AsyncHttpHelper.post(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        Log.e(TAG, "onSuccess: " + response.toString());
                        try {
                            if (response != null && response.getInt("code") == 0){
                                if (response.getBoolean("data")){
                                    viewHold.result.setVisibility(View.VISIBLE);
                                    viewHold.agreeButton.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "审核完成" , Toast.LENGTH_LONG).show();
                                }
                            }else{
                                //报名失败，隐藏图标
                                Log.e(TAG,"报名失败");
                                Toast.makeText(mContext, "积分不够或者其他原因，报名失败!" , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(TAG, "onFailure:");
                        Toast.makeText(mContext,"网络请求失败，请稍后重试!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        viewHold.disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
                Button agreeBtn = (Button) relativeLayout.getChildAt(0);
                Button disagreeBtn = (Button) relativeLayout.getChildAt(1);
                disagreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                agreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                agreeBtn.setEnabled(false);
                disagreeBtn.setEnabled(true);

                /**发送请求到服务器*/
                //获取请求参数
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("user_id", item.getUserID());
                params.put("course_index", item.getCourseID());
                params.put("is_enroll", false);
                String url = Constant.BASE_URL + "enroll/check_enroll_course.do";
                AsyncHttpHelper.post(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e(TAG, "onSuccess: " + response.toString());
                        try {
                            if (response != null && response.getInt("code") == 0){
                                if (response.getBoolean("data")){
                                    Toast.makeText(mContext, "否决完成" , Toast.LENGTH_LONG).show();
                                }
                            }else{
                                //报名失败，隐藏图标
                                Toast.makeText(mContext, "否决失败" , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(mContext,"网络请求失败，请稍后重试!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        private TextView name;
        private Button agreeButton;
        private Button disagreeButton;
        private TextView result;
    }
}
