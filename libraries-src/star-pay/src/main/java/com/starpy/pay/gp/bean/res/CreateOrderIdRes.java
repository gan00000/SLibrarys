package com.starpy.pay.gp.bean.res;

import com.core.base.request.bean.BaseResponseModel;

/**
 * Created by Efun on 2017/2/23.
 */

public class CreateOrderIdRes extends BaseResponseModel {

    private String orderId;
    private String paygpid;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaygpid() {
        return paygpid;
    }

    public void setPaygpid(String paygpid) {
        this.paygpid = paygpid;
    }
}
