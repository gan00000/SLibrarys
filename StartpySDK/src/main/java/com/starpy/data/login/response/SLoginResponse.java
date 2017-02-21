package com.starpy.data.login.response;

import com.core.base.request.bean.BaseResponseModel;

/**
 * Created by Efun on 2017/2/11.
 */

public class SLoginResponse extends BaseResponseModel {

    private String userId = "";

    public boolean isRequestSuccess(){
        return SUCCESS_CODE.equals(getCode()) || "1001".equals(getCode());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken = "";
    /**
     * 登陆成功时间戳
     */
    private String timestamp = "";
    private String freeRegisterName = "";
    private String freeRegisterPwd = "";

    private String gameCode = "";

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
