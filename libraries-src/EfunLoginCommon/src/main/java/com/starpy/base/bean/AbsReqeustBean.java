package com.starpy.base.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Efun on 2016/11/24.
 */

public abstract class AbsReqeustBean implements Serializable{

    private String requestUrl = "";
    private String requestMethod = "";
    private String completeUrl = "";

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }



    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getCompleteUrl() {
        if (TextUtils.isEmpty(completeUrl) && !TextUtils.isEmpty(requestUrl) && !TextUtils.isEmpty(requestMethod)){
            completeUrl = requestUrl + requestMethod;
        }
        return completeUrl;
    }

    public void setCompleteUrl(String completeUrl) {
        this.completeUrl = completeUrl;
    }
}
