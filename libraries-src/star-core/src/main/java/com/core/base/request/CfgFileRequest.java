package com.core.base.request;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;

public class CfgFileRequest extends AbsHttpRequest {

    private Context context;
   private   BaseReqeustBean baseReqeustBean;

    public CfgFileRequest(Context context) {
        this.context = context;
        setGetMethod(true,false);
    }


    @Override
    public BaseReqeustBean onHttpRequest() {
        return baseReqeustBean;
    }

    @Override
    public <T> void onHttpSucceess(T responseModel) {

    }

    @Override
    public void onTimeout(String result) {

    }

    @Override
    public void onNoData(String result) {

    }

    public void setBaseReqeustBean(BaseReqeustBean baseReqeustBean) {
        this.baseReqeustBean = baseReqeustBean;
    }
}