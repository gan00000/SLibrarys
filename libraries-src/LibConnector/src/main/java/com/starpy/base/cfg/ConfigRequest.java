package com.starpy.base.cfg;

import android.content.Context;

import com.core.base.callback.ISReqCallBack;
import com.core.base.request.CfgFileRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.PL;
import com.starpy.base.utils.StarPyUtil;

/**
 * Created by Efun on 2017/2/14.
 */

public class ConfigRequest{

    public static void requestCfg(final Context context){

        CfgFileRequest cfgFileRequest = new CfgFileRequest(context);
        BaseReqeustBean baseReqeustBean = new BaseReqeustBean(context);
        baseReqeustBean.setCompleteUrl("http://sdkconf.starb168.com/sdkconf/s_sdk_config.txt");
        cfgFileRequest.setBaseReqeustBean(baseReqeustBean);
        cfgFileRequest.setReqCallBack(new ISReqCallBack() {
            @Override
            public void success(Object o, String rawResult) {
                PL.d("sdk cfg:" + rawResult);
                StarPyUtil.saveSdkCfg(context,rawResult);
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        cfgFileRequest.excute();
    }


    /**
     * 服务条款配置
     * @param context
     */
    public static void requestTermsCfg(final Context context){

        CfgFileRequest cfgFileRequest = new CfgFileRequest(context);
        BaseReqeustBean baseReqeustBean = new BaseReqeustBean(context);
        baseReqeustBean.setCompleteUrl("http://sdkconf.starb168.com/sdkconf/s_sdk_login_terms.txt");
        cfgFileRequest.setBaseReqeustBean(baseReqeustBean);
        cfgFileRequest.setReqCallBack(new ISReqCallBack() {
            @Override
            public void success(Object o, String rawResult) {
                PL.d("sdk cfg:" + rawResult);
                StarPyUtil.saveSdkLoginTerms(context,rawResult);
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        cfgFileRequest.excute();
    }


}
