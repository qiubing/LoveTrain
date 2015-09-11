package cn.nubia.entity;

/**
 * Created by JiangYu on 2015/9/11.
 */
public class CreditsAwardMsg {

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
}
