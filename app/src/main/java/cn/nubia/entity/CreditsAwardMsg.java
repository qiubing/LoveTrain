package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/11.
 */
public class CreditsAwardMsg extends Paramable{

    private String mAwardedName;
    private int mAwardedCredits;
    private String mAwardedCause;

    public String getAwardedName() {
        return mAwardedName;
    }

    public void setAwardedName(String awardedName) {
        mAwardedName = awardedName;
    }

    public int getAwardedCredits() {
        return mAwardedCredits;
    }

    public void setAwardedCredits(int awardedCredits) {
        mAwardedCredits = awardedCredits;
    }

    public String getAwardedCause() {
        return mAwardedCause;
    }

    public void setAwardedCause(String awardedCause) {
        mAwardedCause = awardedCause;
    }

    @Override
    protected RequestParams toInsertParams(RequestParams params) {
        params.add("awardedName", mAwardedName);
        params.add("awardedCredits", String.valueOf(mAwardedCredits));
        params.add("awaredCause", mAwardedCause);
        return params;
    }

    @Override
    protected RequestParams toUpdateParams(RequestParams params) {
        return null;
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
