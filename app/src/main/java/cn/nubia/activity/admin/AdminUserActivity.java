package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.entity.Constant;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.model.User;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.DialogUtil;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.HandleResponse;

@SuppressWarnings("deprecation")
public class AdminUserActivity extends Activity {

    private List<User> list;
    private GestureDetector gestureDetector;

    private void init() {
        list = new ArrayList<>();
        String url = Constant.BASE_URL + "user/manage.do";

        RequestParams params = new RequestParams(Constant.getRequestParams());
//        params.put("device_id", Constant.devideID);
//        params.put("request_time", System.currentTimeMillis());
//        params.put("apk_version", Constant.apkVersion);
//        params.put("token_key", Constant.tokenKep);

        AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String s = new String(bytes, "UTF-8");
                    handleData(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtil.showToast(AdminUserActivity.this, "服务器返回异常！");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                DialogUtil.showToast(AdminUserActivity.this, "连接服务器发生异常！");
            }
        });
    }

    private void handleData(JSONObject response) throws JSONException {
        String code = response.getString("code");
        if (code.equals("0")) {
            String data = response.getString("data");
            Gson gson = new Gson();
            final Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            list = gson.fromJson(data, listType);

            ListView mListView = (ListView) findViewById(R.id.manager_user_listview);
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final String userName = list.get(position).getUserName();
                    final String userId = list.get(position).getUserID();
                    final ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.manager_user_item, null);
                        holder.usernameTV = (TextView) convertView.findViewById(R.id.manager_username);
                        holder.idTV = (TextView) convertView.findViewById(R.id.manager_id);
                        holder.resetPWD = (TextView) convertView.findViewById(R.id.manager_resetpwd);
                        holder.delete = (TextView) convertView.findViewById(R.id.manager_delete);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.usernameTV.setText(userName);
                    holder.idTV.setText(userId);
                    holder.resetPWD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(AdminUserActivity.this).setTitle("确认要重置" + userName + "的密码吗?")
                                    .setPositiveButton("重置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            RequestParams params = new RequestParams();
                                            params.put("device_id", Constant.devideID);
                                            params.put("request_time", System.currentTimeMillis());
                                            params.put("apk_version", Constant.apkVersion);
                                            params.put("token_key", Constant.tokenKep);
                                            params.put("user_id", list.get(position).getUserID());
                                            Log.e("LK", Constant.devideID + "-" + Constant.tokenKep + "-" + System.currentTimeMillis());
                                            String url = Constant.BASE_URL + "user/reset.do";
                                            AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                    try {
                                                        String s = new String(bytes, "UTF-8");
                                                        JSONObject jsonObject = new JSONObject(s);
                                                        String code = jsonObject.getString("code");
                                                        if (code.equals("0")) {
                                                            DialogUtil.showToast(AdminUserActivity.this, "你重置了" + userName + userId + "的密码！");
                                                        } else {
                                                            DialogUtil.showToast(AdminUserActivity.this, "重置密码失败" + jsonObject.getString("message"));
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        DialogUtil.showToast(AdminUserActivity.this, "服务器返回异常,重置密码失败！");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                    DialogUtil.showToast(AdminUserActivity.this, "连接服务器发生异常,重置密码失败！");
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    });
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(AdminUserActivity.this).setTitle("确认要删除" + userName + "吗?")
                                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RequestParams params = new RequestParams();
                                            params.put("device_id", Constant.devideID);
                                            params.put("request_time", System.currentTimeMillis());
                                            params.put("apk_version", Constant.apkVersion);
                                            params.put("token_key", Constant.tokenKep);
                                            params.put("user_id", list.get(position).getUserID());
                                            String url = Constant.BASE_URL + "user/delete.do";
                                            AsyncHttpHelper.get(url, params, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                    try {
                                                        String s = new String(bytes, "UTF-8");
                                                        JSONObject jsonObject = new JSONObject(s);
                                                        String code = jsonObject.getString("code");
                                                        if (code.equals("0")) {
                                                            list.remove(position);
                                                            notifyDataSetChanged();
                                                            DialogUtil.showToast(AdminUserActivity.this, "你删除了用户:" + userName + userId);
                                                        } else {
                                                            DialogUtil.showToast(AdminUserActivity.this, "删除失败" + jsonObject.getString("message"));
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        DialogUtil.showToast(AdminUserActivity.this, "服务器返回异常,删除失败！");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                    DialogUtil.showToast(AdminUserActivity.this, "连接服务器发生异常,删除失败！");
                                                }
                                            });


                                        }
                                    })
                                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    });
                    return convertView;
                }
            };

            mListView.setAdapter(adapter);

        } else {
            HandleResponse.excute(AdminUserActivity.this, code, response.getString("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
        TextView mTitle = (TextView) findViewById(R.id.manager_head_title);
        mTitle.setText(R.string.activity_manager_user);
        ImageView mGoBack = (ImageView) findViewById(R.id.manager_goback);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
        GestureDetectorManager gestureDetectorManager = GestureDetectorManager.getInstance();
        //指定Context和实际识别相应手势操作的GestureDetector.OnGestureListener类
        gestureDetector = new GestureDetector(this, gestureDetectorManager);

        //传入实现了IOnGestureListener接口的匿名内部类对象，此处为多态
        gestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

    class ViewHolder {
        TextView usernameTV;
        TextView idTV;
        TextView resetPWD;
        TextView delete;
    }
}
