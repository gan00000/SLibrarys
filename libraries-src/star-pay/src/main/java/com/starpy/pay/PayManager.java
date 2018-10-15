package com.starpy.pay;

import android.app.Activity;

import com.core.base.bean.BaseResponseModel;
import com.core.base.callback.ISReqCallBack;
import com.core.base.request.SimpleHttpRequest;
import com.starpy.base.bean.SGameBaseRequestBean;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.pay.gp.bean.res.GPExchangeRes;
import com.starpy.pay.gp.util.PayHelper;

public class PayManager {

    public static void checkOnlyThirdPay(final Activity activity){

        SGameBaseRequestBean gameBaseRequestBean = new SGameBaseRequestBean(activity);

        gameBaseRequestBean.setRequestUrl(PayHelper.getPreferredUrl(activity));
        gameBaseRequestBean.setRequestSpaUrl(PayHelper.getSpareUrl(activity));
        gameBaseRequestBean.setRequestMethod("dynamic_pswc");


        SimpleHttpRequest httpRequest = new SimpleHttpRequest();
        httpRequest.setBaseReqeustBean(gameBaseRequestBean);
        httpRequest.setReqCallBack(new ISReqCallBack<BaseResponseModel>() {

            @Override
            public void success(BaseResponseModel baseResponseModel, String rawResult) {

                if (baseResponseModel != null) {
                    StarPyUtil.setOnlyThirdPay(activity,baseResponseModel.getCode());
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {
            }
        });
        httpRequest.excute(GPExchangeRes.class);
    }
}
