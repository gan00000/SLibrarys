package com.starpy.data.login.response;

import com.core.base.bean.BaseResponseModel;

public class AdModel extends BaseResponseModel {

//    fbShareTimes  为0不弹分享
//    adType   0 插屏，1视频  ，满足分享次数弹出
//    ISAdPlacementId  插屏广告编号
//    RVAdPlacementId  视频广告编号

    private String fbShareTimes;
    private String adType;
    private String ISAdPlacementId;
    private String RVAdPlacementId;

    public String getFbShareTimes() {
        return fbShareTimes;
    }

    public void setFbShareTimes(String fbShareTimes) {
        this.fbShareTimes = fbShareTimes;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getISAdPlacementId() {
        return ISAdPlacementId;
    }

    public void setISAdPlacementId(String ISAdPlacementId) {
        this.ISAdPlacementId = ISAdPlacementId;
    }

    public String getRVAdPlacementId() {
        return RVAdPlacementId;
    }

    public void setRVAdPlacementId(String RVAdPlacementId) {
        this.RVAdPlacementId = RVAdPlacementId;
    }
}
