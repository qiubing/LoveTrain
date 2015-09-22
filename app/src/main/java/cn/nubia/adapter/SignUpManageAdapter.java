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
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.entity.SignUpItem;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.MyJsonHttpResponseHandler;

/**
 * Created by hexiao on 2015/9/19.
 */
public class SignUpManageAdapter extends BaseAdapter {
    private static final String TAG = "SignUpManageAdapter";
    private List<SignUpItem> mList;
    private Context mContext;

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

                /**同意按钮是在位置1*/
                Button agreeBtn = (Button) relativeLayout.getChildAt(1);
                agreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                agreeBtn.setEnabled(true);

                /**否决按钮是在位置0*/
                Button disagreeBtn = (Button) relativeLayout.getChildAt(0);
                disagreeBtn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                disagreeBtn.setEnabled(false);

                /**发送请求到服务器*/
                //获取请求参数
                RequestParams params = new RequestParams(Constant.getRequestParams());
                params.put("user_id",item.getUserID());
                params.put("course_index", item.getCourseID());
                params.put("is_enroll", true);
                String url = Constant.BASE_URL + "enroll/check_enroll_course.do";
                AsyncHttpHelper.post(url, params, mSignUpHandler);
            }
        });

        viewHold.disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
                Button agreeBtn = (Button) relativeLayout.getChildAt(1);
                Button disagreeBtn = (Button) relativeLayout.getChildAt(0);
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
                AsyncHttpHelper.post(url, params, mSignUpHandler);
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

    MyJsonHttpResponseHandler mSignUpHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) throws JSONException {
            Log.e(TAG, "onSuccess: " + response.toString());
            if (response != null && response.getInt("code") == 0){
                if (response.getBoolean("data")){
                    Toast.makeText(mContext, "审核完成" , Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext, "审核失败" , Toast.LENGTH_LONG).show();
                }
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG, "onFailure:");
        }
    };
}
