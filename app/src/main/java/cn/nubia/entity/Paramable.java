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
        RequestParams params = new RequestParams(Constant.getRequestParams());

        switch (mType){
            case INSERT:
                return toInsertParams(params);
            case UPDATE:
                return toUpdateParams(params);
            case DELETE:
                return toDeleteParams(params);
            case QUERY:
                return toQueryParams(params);
            default:
                return null;
        }
    }
    protected abstract RequestParams toInsertParams(RequestParams params);
    protected abstract RequestParams toUpdateParams(RequestParams params);
    protected abstract RequestParams toQueryParams(RequestParams params);
    protected abstract RequestParams toDeleteParams(RequestParams params);
}
