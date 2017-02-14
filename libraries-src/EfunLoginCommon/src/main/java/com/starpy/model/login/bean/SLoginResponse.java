package com.starpy.model.login.bean;

import com.core.base.request.bean.BaseResponseModel;

/**
 * Created by Efun on 2017/2/11.
 */

public class SLoginResponse extends BaseResponseModel {

    private String userId = "";
    private String accessTkoen = "";
    /**
     * 登陆成功时间戳
     */
    private String timestamp = "";
    private String freeRegisterName = "";
    private String freeRegisterPwd = "";


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessTkoen() {
        return accessTkoen;
    }

    public void setAccessTkoen(String accessTkoen) {
        this.accessTkoen = accessTkoen;
    }

    public String getFreeRegisterName() {
        return freeRegisterName;
    }

    public void setFreeRegisterName(String freeRegisterName) {
        this.freeRegisterName = freeRegisterName;
    }

    public String getFreeRegisterPwd() {
        return freeRegisterPwd;
    }

    public void setFreeRegisterPwd(String freeRegisterPwd) {
        this.freeRegisterPwd = freeRegisterPwd;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
