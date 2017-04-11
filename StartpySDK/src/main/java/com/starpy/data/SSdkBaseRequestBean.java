package com.starpy.data;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;

/**
 * <p>Title: SSdkBaseRequestBean</p> <p>Description: SDK接口请求参数实体</p>
 *
 * @author GanYuanrong
 * @date 2014年8月22日
 */
public class SSdkBaseRequestBean extends BaseReqeustBean {

    private String appKey;
    private String gameCode;
    private String operatingSystem = "android";
    private String gameLanguage = "";//游戏语言

    private String accessToken;
    private String loginTimestamp;

    private String uniqueId;

    private String timestamp = System.currentTimeMillis() + "";

    private String signature = "";

    public SSdkBaseRequestBean(Context context) {
        super(context);
        initSdkField(context);
    }


    private void initSdkField(Context context) {
        appKey = ResConfig.getAppKey(context);
        accessToken = StarPyUtil.getSdkAccessToken(context);
        loginTimestamp = StarPyUtil.getSdkTimestamp(context);
        gameCode = ResConfig.getGameCode(context);
        gameLanguage = ResConfig.getGameLanguage(context);

        uniqueId = ApkInfoUtil.getCustomizedUniqueIdOrAndroidId(context);

        if (SStringUtil.isEmpty(signature)) {
            signature = SStringUtil.toMd5(ResConfig.getAppKey(context) + gameCode + timestamp);
        }
    }

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

    public String getGameLanguage() {
        return gameLanguage;
    }

    public void setGameLanguage(String gameLanguage) {
        this.gameLanguage = gameLanguage;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(String loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
