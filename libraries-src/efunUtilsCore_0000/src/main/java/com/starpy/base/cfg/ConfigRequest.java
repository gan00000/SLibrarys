package com.starpy.base.cfg;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.beans.UrlFileContent;
import com.core.base.callback.ISReqCallBack;
import com.core.base.request.CfgFileRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.PL;
import com.starpy.base.StarPyUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Efun on 2017/2/14.
 */

public class ConfigRequest{

    public static void requestCfg(final Context context){

        CfgFileRequest cfgFileRequest = new CfgFileRequest(context);
        BaseReqeustBean baseReqeustBean = new BaseReqeustBean(context);
        baseReqeustBean.setCompleteUrl("https://www.baidu.com/?tn=47018152_dg");
        cfgFileRequest.setBaseReqeustBean(baseReqeustBean);
        cfgFileRequest.setReqCallBack(new ISReqCallBack() {
            @Override
            public void callBack(Object o,String rawResult) {
                PL.d("sdk cfg:" + rawResult);
                StarPyUtil.saveSdkCfg(context,rawResult);
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
        baseReqeustBean.setCompleteUrl("https://www.baidu.com/?tn=47018152_dg");
        cfgFileRequest.setBaseReqeustBean(baseReqeustBean);
        cfgFileRequest.setReqCallBack(new ISReqCallBack() {
            @Override
            public void callBack(Object o,String rawResult) {
                PL.d("sdk cfg:" + rawResult);
                StarPyUtil.saveSdkLoginTerms(context,rawResult);
            }
        });
        cfgFileRequest.excute();
    }


}
