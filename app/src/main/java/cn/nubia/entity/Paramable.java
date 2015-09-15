package cn.nubia.entity;

import com.loopj.android.http.RequestParams;

import java.io.Serializable;

import cn.nubia.service.CommunicateService;

/**
 * Created by JiangYu on 2015/9/11.
 */
public abstract class Paramable implements Serializable {
    private CommunicateService.OperateType mType;

    public CommunicateService.OperateType getOperateType() {
        return mType;
    }

    public void setOperateType(CommunicateService.OperateType Type) {
        this.mType = Type;
    }

    public RequestParams toParams(){
        switch (mType){
            case INSERT:
                return toInsertParams();
            case UPDATE:
                return toUpdateParams();
            case DELETE:
                return toDeleteParams();
            case QUERY:
                return toQueryParams();
            default:
                return null;
        }
    }
    protected abstract RequestParams toInsertParams();
    protected abstract RequestParams toUpdateParams();
    protected abstract RequestParams toQueryParams();
    protected abstract RequestParams toDeleteParams();
}
