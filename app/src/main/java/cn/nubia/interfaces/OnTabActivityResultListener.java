package cn.nubia.interfaces;
import cn.nubia.entity.Item;

/**
 * Created by WJ on 2015/9/28.
 */
public interface OnTabActivityResultListener {
    void onTabActivityResult(int requestCode, int resultCode, Item data);
}