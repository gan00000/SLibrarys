package com.starpy.base.bean;

import android.content.Context;

/**
 * <p>Title: AdsRequestBean</p> <p>Description: 接口请求参数实体</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年8月22日
 */
public class AdsRequestBean extends SSdkBaseRequestBean {

    public AdsRequestBean(Context context) {
        super(context);

        setAccessToken("");
        setLoginTimestamp("");

    }

    private String advertiser;
    private String referrer;


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

}
