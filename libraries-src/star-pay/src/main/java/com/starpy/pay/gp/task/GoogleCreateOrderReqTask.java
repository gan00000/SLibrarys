package com.starpy.pay.gp.task;

import com.core.base.request.AbsHttpRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;

/**
 * Created by Efun on 2017/2/23.
 */

public class GoogleCreateOrderReqTask extends AbsHttpRequest {

    private GooglePayCreateOrderIdReqBean createOrderIdReqBean;

    public GoogleCreateOrderReqTask(GooglePayCreateOrderIdReqBean createOrderIdReqBean) {
        this.createOrderIdReqBean = createOrderIdReqBean;
    }


    @Override
    public BaseReqeustBean createRequestBean() {
        return createOrderIdReqBean;
    }

    @Override
    public <T> void onHttpSucceess(T responseModel) {

    }

    @Override
    public void onTimeout(String result) {

    }

    @Override
    public void onNoData(String result) {

    }
}
