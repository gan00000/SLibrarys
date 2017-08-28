package com.starpy.sdk.plat.data.bean.reqeust;

import android.content.Context;

import com.starpy.data.SGameBaseRequestBean;

/**
 * Created by gan on 2017/8/21.
 */

public class ReceiveGiftBean extends SGameBaseRequestBean {
    public ReceiveGiftBean(Context context) {
        super(context);
    }

    private String activityCode;
    private String giftbagGameCode;

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getGiftbagGameCode() {
        return giftbagGameCode;
    }

    public void setGiftbagGameCode(String giftbagGameCode) {
        this.giftbagGameCode = giftbagGameCode;
    }
}
