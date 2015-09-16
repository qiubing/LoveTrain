package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/7.
 */
public class PswModifyMsg extends Paramable{
    private String mOldPsw;
    private String mNewPsw;

    public String getOldPsw() {
        return mOldPsw;
    }

    public void setOldPsw(String oldPsw) {
        this.mOldPsw = oldPsw;
    }

    public String getNewPsw() {
        return mNewPsw;
    }

    public void setNewPsw(String newPsw) {
        this.mNewPsw = newPsw;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toUpdateParams(RequestParams params) {
        params.add("oldPsw", mOldPsw);
        params.add("newPsw", mNewPsw);
        return params;
    }

    @Override
    protected RequestParams toQueryParams(RequestParams params) {
        return null;
    }

    @Override
    protected RequestParams toDeleteParams(RequestParams params) {
        return null;
    }
}
