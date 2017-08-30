package com.starpy.base.bean;

import android.content.Context;

import com.starpy.base.utils.StarPyUtil;

/**
 * <p>Title: AdsRequestBean</p> <p>Description: 接口请求参数实体</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年8月22日
 */
public class AdsRequestBean extends SSdkBaseRequestBean {

    public AdsRequestBean(Context context) {
        super(context);

        referrer = StarPyUtil.getReferrer(context);
    }

    private String spy_platForm = "";//渠道包-所属平台
    private String spy_advertiser = "";//渠道包-所属广告
    private String referrer = "";

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getSpy_platForm() {
        return spy_platForm;
    }

    public void setSpy_platForm(String spy_platForm) {
        this.spy_platForm = spy_platForm;
    }

    public String getSpy_advertiser() {
        return spy_advertiser;
    }

    public void setSpy_advertiser(String spy_advertiser) {
        this.spy_advertiser = spy_advertiser;
    }
}
