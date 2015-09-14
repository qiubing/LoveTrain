package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;

/**
 * Created by JiangYu on 2015/9/11.
 */
public interface Paramable extends Serializable {
    public RequestParams toParams();
}
