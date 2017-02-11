package com.starpy.model.login.bean.request;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;

/**
 * <p>Title: LoginBaseRequest</p> <p>Description: 接口请求参数实体</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年8月22日
 */
public class LoginBaseRequest extends BaseReqeustBean {

    public LoginBaseRequest(Context context) {
        super(context);
    }


    private String appKey;
    private String gameCode;
    private String operatingSystem = "android";
    private String gameLanguage = "";//游戏语言

    private String advertiser;
    private String referrer;

    private String advertisingId = "";

    private String timestamp = System.currentTimeMillis() + "";

    private String signature = "";

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getGameLanguage() {
        return gameLanguage;
    }

    public void setGameLanguage(String gameLanguage) {
        this.gameLanguage = gameLanguage;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
