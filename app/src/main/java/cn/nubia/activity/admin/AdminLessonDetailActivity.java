package cn.nubia.activity.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.nubia.activity.R;
import cn.nubia.activity.client.ClientEvaluateActivity;
import cn.nubia.activity.client.ClientMyCourseJudgeDetailFillActivity;
import cn.nubia.entity.Constant;
import cn.nubia.entity.LessonItem;
import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.AsyncHttpHelper;
import cn.nubia.util.GestureDetectorManager;
import cn.nubia.util.MyJsonHttpResponseHandler;
import cn.nubia.util.Utils;
import cn.nubia.util.jsonprocessor.TimeFormatConversion;
import cn.nubia.zxing.encoding.EncodingHandler;

/**
 * Created by hexiao on 2015/9/8.
 */
public class AdminLessonDetailActivity extends Activity implements View.OnClickListener {

    private Button alterLessonBtn;
    private Button deleteLessonBtn;
    private TextView signUpPopulationTextView;
    private TextView mGenerateQRCode;
    private Button mEvaluateTextView;
    private String status = "student";
    private TextView sub_page_title;

    private TextView lessonNameTextView;
    private TextView lessDescTextView;
    private TextView lessonInfoTextView;
    private TextView signInPopulationTextView;

    /**从前一个页面传过来的LessonItem对象*/
    private LessonItem lessonItem;
    private Bundle bundle;
    private Bundle signUpBundle;
    private SignUpData mSignUpData;
    private RelativeLayout loadingFailedRelativeLayout;
    private RelativeLayout networkUnusableRelativeLayout;

    /**保存签到人员信息*/
//    private List<String> mList;

    private String signUpUrl = Constant.BASE_URL + "exam/check_list.do";
    private String deleteUrl = Constant.BASE_URL + "/course/del_lesson.do";

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lesson_detail);
        bundle = new Bundle();
        signUpBundle = new Bundle();

        /**获取控件**/
        alterLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_alterLessonButton);
        deleteLessonBtn = (Button) findViewById(R.id.admin_lesson_detail_deleteLessonButton);
        signUpPopulationTextView = (TextView) findViewById(R.id.lesson_detail_signIn_textView);
        mGenerateQRCode = (TextView) findViewById(R.id.backupButton);
        mEvaluateTextView = (Button) findViewById(R.id.evaluateTextView);
        sub_page_title = (TextView) findViewById(R.id.sub_page_title);
        sub_page_title.setText("课时管理");
        /**获取相关的TextView*/
        lessonNameTextView=(TextView)findViewById(R.id.lesson_detail_realName_textView);
        lessDescTextView=(TextView)findViewById(R.id.lesson_detail_realDesc_textView);
        lessonInfoTextView=(TextView)findViewById(R.id.lesson_detail_lessonInfo_textView);
        loadingFailedRelativeLayout = (RelativeLayout)findViewById(R.id.loading_failed);
        networkUnusableRelativeLayout = (RelativeLayout)findViewById(R.id.network_unusable);
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        /**获取启动该Activity的Intent*/
        Intent intent = getIntent();
        /**此处的lessonItem会不会是null*/
        lessonItem = (LessonItem)intent.getSerializableExtra("LessonItem");
        String teacherID = lessonItem.getTeacherID();
        String myID = Constant.user.getUserID();
        String statusTemp = intent.getStringExtra("status");
        Log.e("huhu", lessonItem + statusTemp + "lessonItem");

        if(statusTemp != null) {
            status = statusTemp;
        }

        if(Constant.IS_ADMIN == true || status.equals("teacher")){
            mGenerateQRCode.setVisibility(View.VISIBLE);
            alterLessonBtn.setOnClickListener(this);
            deleteLessonBtn.setOnClickListener(this);
            signUpPopulationTextView.setOnClickListener(this);
            mGenerateQRCode.setOnClickListener(this);
            mEvaluateTextView.setOnClickListener(this);
        }

        else{
            alterLessonBtn.setVisibility(View.GONE);
            deleteLessonBtn.setVisibility(View.GONE);
            signUpPopulationTextView.setVisibility(View.GONE);
            mEvaluateTextView.setVisibility(View.GONE);
            //mGenerateQRCode.setVisibility(View.GONE);
        }

        if(lessonItem != null) {
            lessonNameTextView.setText(lessonItem.getName() == null ? "null" : lessonItem.getName());
            lessDescTextView.setText(lessonItem.getDescription() == null ? "null" : lessonItem.getDescription());
            lessonInfoTextView.setText("讲师：" + lessonItem.getTeacherName() +
                            "\n上课地址：" + lessonItem.getLocation() +
                            "\n上课时间：" + TimeFormatConversion.toDateTime(lessonItem.getStartTime()) +

                            "\n下课时间：" + TimeFormatConversion.toDateTime(lessonItem.getEndTime()) +
                            "\n讲师上课可得积分：" + lessonItem.getTeacherCredits() +
                            "\n学员签到可得积分：" + lessonItem.getCheckCredits() +
                            "\n课程评价分数：" + lessonItem.getJudgeScore()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //创建手势管理单例对象
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
        loadData();
    }

    //将Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return  gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_lesson_detail_alterLessonButton:
                Intent intentAlterLesson = new Intent(AdminLessonDetailActivity.this,
                        AdminAlterLessonActivity.class);
                Bundle alterBundle=new Bundle();
                alterBundle.putSerializable("LessonItem", lessonItem);
                intentAlterLesson.putExtras(alterBundle);
                startActivity(intentAlterLesson);
                break;
            case R.id.admin_lesson_detail_deleteLessonButton:
                final AlertDialog.Builder builderDelete = new AlertDialog.Builder(AdminLessonDetailActivity.this)
                        //设置对话框标题
                        .setTitle("删除课时")
                                //设置图标
                        .setIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                        .setMessage("确认要删除《" + lessonItem.getName()+ "》这门课时吗?");
                builderDelete.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                });
                builderDelete.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderDelete.create().show();
                break;
            case R.id.lesson_detail_signIn_textView:
                Intent intentSignInInfo = new Intent(AdminLessonDetailActivity.this, AdminSignInExamPersonInfoActivity.class);
                signUpBundle.putSerializable("LessonItem", lessonItem);
                intentSignInInfo.putExtras(signUpBundle);
                startActivity(intentSignInInfo);
                Toast.makeText(AdminLessonDetailActivity.this, "你点击了查看签到人员信息", Toast.LENGTH_LONG).show();
                break;

            case R.id.backupButton:
                /**
                 * 生成二维码，edit by qiubing
                 */
                //TODO:生成具有课程和讲师信息的二维码
                //获取要生成课程的ID索引
                Toast.makeText(this,"二维码生成中",Toast.LENGTH_SHORT).show();
                int lesson_index = lessonItem.getIndex();
                String contentString = String.valueOf(lesson_index);
                if (!contentString.equals("")) {
                    //获取需要插入的头像logo
                    Bitmap logo = Utils.getPictureFromSD(Constant.LOCAL_PATH + Constant.PORTRAIT);
                    Bitmap qrCodeBitmap = null;
                    try {
                        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                        //Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
                        qrCodeBitmap = EncodingHandler.createQRImage(contentString, 350, 350, logo);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    //mShowBarCode.setImageBitmap(qrCodeBitmap);
                    final String barCodeName = contentString + ".jpg";
                    final Bitmap bitmap = qrCodeBitmap;
                    ImageView image = new ImageView(this);
                    image.setMaxHeight(350);
                    image.setMaxWidth(350);
                    image.setImageBitmap(qrCodeBitmap);

                    //弹框显示二维码图片
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this).
                            setTitle("保存二维码图片到本地").setView(image);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //保存二维码图片到SD卡中
                            Utils.saveBitmap(barCodeName, bitmap);
                            Toast.makeText(AdminLessonDetailActivity.this,
                                    "二维码保存在/MyDownloader/barcode目录下",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(this, "Lesson id can not be empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.evaluateTextView:
                Intent intent ;
                if(status.equals("teacher")) {
                    intent = new Intent(this, ClientEvaluateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("lession_index_ID", lessonItem.getIndex()+"," + lessonItem.getTeacherID());
                    intent.putExtras(bundle);
                    Log.i("huhu", "lession_index_ID" + lessonItem.getIndex() + "," + lessonItem.getTeacherID());
                    Log.i("huhu", "lession_index_ID" + lessonItem);
                } else {
                    intent = new Intent(this, ClientMyCourseJudgeDetailFillActivity.class);
                    intent.putExtra("lessonIndex", 0);
                }
                startActivity(intent);
                break;

        }

    }

    private void loadData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");

        requestParams.add("lesson_index", lessonItem.getIndex()+"");
        Log.e("hexiao", lessonItem.getIndex() + "+loadData");
        String signUpUrl = Constant.BASE_URL + "exam/check_list.do";
        AsyncHttpHelper.post(signUpUrl, requestParams, jsonHttpResponseHandler);
    }

    /**请求课程数据服务器数据的Handler*/
    private MyJsonHttpResponseHandler jsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        @SuppressWarnings("deprecation")
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                Log.e("hexiao", response.toString());
                if(response.getInt("code") != 0){
                    Log.e("hexiao", "responseCode!=0");
                    return;
                }
                if(response.getInt("code")==0 && response.getString("data")!=null) {
                    Log.e("hexiao","signUpInfoSuccess");
                    JSONArray jsonArray = response.getJSONArray("data");
                    SignUpData mSignUpData=new SignUpData(jsonArray);
                    signUpPopulationTextView.setText("签到"+mSignUpData.getSize()+"人");
                    signUpBundle.putSerializable("SignUpData", mSignUpData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.e("hexiao","signUpInfoFailure");
        }
    };

    class SignUpData implements Serializable {
        private final static long serialVersionUID = 1234567890L;
        private List<String> mList;
        private int mSize;
        public SignUpData(JSONArray jsonArray){
            this.mList=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    mList.add(jsonObject.getString("user_name")+","+jsonObject.getString("user_id")+","+jsonObject.getString("check_time"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mSize=mList.size();
        }
        public List<String> getList(){
            return mList;
        }
        public void setList(List<String> list){
            mList.addAll(list);
        }
        public int getSize(){
            return mSize;
        }
    }

    private void deleteData(){
        loadingFailedRelativeLayout.setVisibility(View.GONE);
        networkUnusableRelativeLayout.setVisibility(View.GONE);

        RequestParams requestParams = new RequestParams();
        requestParams.add("device_id", "MXJSDLJFJFSFS");
        requestParams.add("request_time","1445545456456");
        requestParams.add("apk_version","1");
        requestParams.add("token_key","wersdfffthnjimhtrfedsaw");
        requestParams.add("record_modify_time_course", "1435125456111");

        requestParams.put("lesson_index", lessonItem.getIndex());

        AsyncHttpHelper.post(deleteUrl, requestParams, myJsonHttpResponseHandler);
    }

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler = new MyJsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i("huhu", "addExam" + "onSuccess");
            try {
                int code = response.getInt("code");
                boolean isOk = response.getBoolean("data");
                //JSONArray jsonArray = response.getJSONArray("data");
                Log.i("huhu", "addExam" + code + "," + isOk);
                if( code == 0 && isOk) {
                    Toast.makeText(AdminLessonDetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminLessonDetailActivity.this, "该课程不存在", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                loadingFailedRelativeLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            networkUnusableRelativeLayout.setVisibility(View.VISIBLE);
        }
    };



    public void back(View view) {
        // TODO Auto-generated method stub
        this.finish();
    }

}
