package cn.nubia.entity;

/**
 * Created by WJ on 2015/9/8.
 */
public abstract class Item extends Paramable{
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String OPERATOR = "operator";

    private int mIndex;
    private String mOperator;
    private String mType;
    private String mName;
    private String mDescription;

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

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

}
