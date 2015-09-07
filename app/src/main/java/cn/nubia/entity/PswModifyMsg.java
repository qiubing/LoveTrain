package cn.nubia.entity;

/**
 * Created by JiangYu on 2015/9/7.
 */
public class PswModifyMsg {
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
}
