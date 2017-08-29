package com.core.base.request;

import com.core.base.bean.BaseReqeustBean;

/**
 * Created by Efun on 2017/2/11.
 */

public interface ISRqeust {

    <T> void excute(final Class<T> classOfT);

    void excute();

    BaseReqeustBean createRequestBean();

    <T> void onHttpSucceess(T responseModel);

    void onTimeout(String result);

    void onNoData(String result);

}
