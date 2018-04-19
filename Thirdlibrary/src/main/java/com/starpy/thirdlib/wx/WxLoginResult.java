package com.starpy.thirdlib.wx;

import com.core.base.utils.SStringUtil;

/**
 * Created by gan on 2018/4/18.
 */

public class WxLoginResult {

    private String access_token;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private int expires_in;
    private long getAccessTokenTime;

    private int errcode;
    private String errmsg;



    public long getGetAccessTokenTime() {
        return getAccessTokenTime;
    }

    public void setGetAccessTokenTime(long getAccessTokenTime) {
        this.getAccessTokenTime = getAccessTokenTime;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isAccessTokenExpires(){

        if (getAccessTokenTime == 0){
            return true;
        }
        if (((System.currentTimeMillis() - getAccessTokenTime) /1000) > (expires_in - 100)){
            return true;
        }

        return false;
    }

    public boolean isRefreshTokenExpires(){

        if (getAccessTokenTime == 0){
            return true;
        }
        if (((System.currentTimeMillis() - getAccessTokenTime) /1000) > (25 * 24 * 60 * 60)){
            return true;
        }

        return false;
    }

    public boolean accessTokenIsOk(){
        return errcode == 0 && SStringUtil.isNotEmpty(access_token) && !isAccessTokenExpires();
    }

    public boolean refreshTokenIsOk(){
        return errcode == 0 && SStringUtil.isNotEmpty(refresh_token) && !isRefreshTokenExpires();
    }


}
