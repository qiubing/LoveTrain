package cn.nubia.entity;

/**
 * Created by WJ on 2015/9/8.
 */
public class Item {
    private int mIndex;
    private String mOperator;

    public String getOperator() {
        return mOperator;
    }

    public void setOperator(String mOperator) {
        this.mOperator = mOperator;
    }


    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    private String mName;
    private String mDescription;
}
