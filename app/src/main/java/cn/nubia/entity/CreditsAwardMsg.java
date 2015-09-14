package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiangYu on 2015/9/11.
 */
public class CreditsAwardMsg implements Paramable{

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
    public RequestParams toParams() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("awardedName",mAwardedName);
        param.put("awardedCredits",String.valueOf(mAwardedCredits));
        param.put("awaredCause",mAwardedCause);
        return new RequestParams(param);
    }
}
