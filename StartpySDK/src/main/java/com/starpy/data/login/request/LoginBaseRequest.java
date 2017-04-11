package com.starpy.data.login.request;

import android.content.Context;

import com.starpy.data.SSdkBaseRequestBean;

/**
 * <p>Title: LoginBaseRequest</p> <p>Description: 接口请求参数实体</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年8月22日
 */
public class LoginBaseRequest extends SSdkBaseRequestBean {

    public LoginBaseRequest(Context context) {
        super(context);

        setAccessToken("");
        setLoginTimestamp("");

    }

    private String advertiser;
    private String referrer;

    private String advertisingId = "";


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

}
