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
    public RequestParams toInsertParams() {
        return null;
    }

    @Override
    public RequestParams toUpdateParams() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("oldPsw",mOldPsw);
        param.put("newPsw",mNewPsw);
        return new RequestParams(param);
    }

    @Override
    public RequestParams toQueryParams() {
        return null;
    }

    @Override
    public RequestParams toDeleteParams() {
        return null;
    }
}
