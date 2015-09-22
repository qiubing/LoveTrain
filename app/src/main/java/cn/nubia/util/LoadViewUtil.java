package cn.nubia.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import cn.nubia.entity.Constant;

/**
 * Created by WJ on 2015/9/9.
 * 刷新页面控制工具
 */
public class LoadViewUtil {
    /** 显示列表 */
    final public static int VIEW_LIST = 1;
    /** 显示断网 **/
    final public static int VIEW_WIFIFAILUER = 2;
    /** 显示加载数据失败 **/
    final public static int VIEW_LOADFAILURE = 3;
    /** 正在加载 */
    final public static int VIEW_LOADING = 4;

    private Context mCtx;
    private boolean mNetworkFailedFlag = true;
    private int mLoadingFailedFlag = Constant.LOADING_SUCCESS;

    public LoadViewUtil(Context ctx,ListView listView,Handler handler){
        this.mCtx = ctx;
        ListView mListView = listView;
        Handler mHandler = handler;
    }

    public void setNetworkFailedViewVisible(boolean visible){
        mNetworkFailedFlag = visible;
    }

    public void setNetworkFailedView(View view){
        View mNetworkFailedView = view;
    }

    public boolean getNetworkFailedFlag(){
        return  mNetworkFailedFlag;
    }

    public int getLoadingFailedFlag() {
        return mLoadingFailedFlag;
    }

    public void setLoadingFailedFlag(int mLoadingFailedFlag) {
        this.mLoadingFailedFlag = mLoadingFailedFlag;
        if(mLoadingFailedFlag == Constant.LOADING_FAILED || mLoadingFailedFlag == Constant.NETWORK_UNUSABLE)
            mNetworkFailedFlag = true;
    }

    /**
     * 等待
     *
     * @param i
     */
   /* public void showLoading(int i) {}
     public void showLoading1(int i) {
        mErrorHintView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        switch (i) {
            case 1:
                mErrorHintView.hideLoading();
                mListView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mErrorHintView.hideLoading();
                mErrorHintView.netError(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(VIEW_LOADING);
                        mHandler.sendEmptyMessage(2);//重新加载数据
//                      loadData(1);
                    }
                });
                break;
            case 3:
                mErrorHintView.hideLoading();
                mErrorHintView.loadFailure(new ErrorHintView.OperateListener() {
                    @Override
                    public void operate() {
                        showLoading(VIEW_LOADING);
                        mHandler.sendEmptyMessage(2);
//                      loadData(1);
                    }
                });
                break;
            case 4:
                mErrorHintView.loadingData();
                break;
        }
    }*/
    /**
     * 短暂显示Toast提示(来自String) *
     */
    public void showShortToast(String text) {
        Toast.makeText(this.mCtx, text, Toast.LENGTH_SHORT).show();
    }

}
