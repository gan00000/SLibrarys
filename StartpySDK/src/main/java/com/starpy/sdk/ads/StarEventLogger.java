package com.starpy.sdk.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.core.base.bean.BaseResponseModel;
import com.core.base.callback.ISReqCallBack;
import com.core.base.request.SimpleHttpRequest;
import com.core.base.utils.PL;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.starpy.base.bean.AdsRequestBean;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;
import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.thirdlib.google.SGoogleProxy;

/**
 * Created by gan on 2017/3/3.
 */

public class StarEventLogger {

    private static boolean needAf(Activity activity){//100标识接入af(其他都标识接入)，101不接入
        return !ResConfig.getConfigInAssets(activity,"star_sdk_af").equals("101");
    }

    public static void activateApp(Activity activity){

        try {
            if (needAf(activity)){
                AFDelegate.activateApp(activity);
            }
            if (!StarPyUtil.isMainland(activity)) {
                SFacebookProxy.activateApp(activity.getApplicationContext());

                // Google Android first open conversion tracking snippet
                // Add this code to the onCreate() method of your application activity
                String star_ads_adword_conversionId = ResConfig.getConfigInAssets(activity,"star_ads_adword_conversionId");
                if (SStringUtil.isNotEmpty(star_ads_adword_conversionId)) {
                    AdWordsConversionReporter.reportWithConversionId(activity.getApplicationContext(),
                            star_ads_adword_conversionId, ResConfig.getConfigInAssets(activity,"star_ads_adword_label"), "0.00", false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void trackinLoginEvent(Activity activity){
        if (needAf(activity)){

            AFDelegate.trackinLoginEvent(activity);
        }

    }

    public static void trackinRegisterEvent(Activity activity){
        if (needAf(activity)){

            AFDelegate.trackinRegisterEvent(activity);
        }

    }

    public static void trackinPayEvent(Activity activity, double payVaule){
        SFacebookProxy.trackingEvent(activity,"pay_android", payVaule);
    }

    public static void registerGoogleAdId(final Context context){
        new  Thread(new Runnable() {
            @Override
            public void run() {
                String googleAdId = SGoogleProxy.getAdvertisingId(context.getApplicationContext());
                StarPyUtil.saveGoogleAdId(context,googleAdId);
                PL.i("save google ad id-->" + googleAdId);
            }
        }).start();
    }

    private static final String STAR_PY_ADSINSTALLACTIVATION = "STAR_PY_ADSINSTALLACTIVATION";
    public static void reportInstallActivation(final Context context){

        if (SStringUtil.isNotEmpty(SPUtil.getSimpleString(context,StarPyUtil.STAR_PY_SP_FILE,STAR_PY_ADSINSTALLACTIVATION))){
            return;
        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adsInstallActivation(context);
            }
        },10 * 1000);
    }
    public static void adsInstallActivation(final Context context){

        final AdsRequestBean adsRequestBean = new AdsRequestBean(context);
        adsRequestBean.setRequestUrl(ResConfig.getAdsPreferredUrl(context));
        adsRequestBean.setRequestMethod(context.getString(R.string.star_ads_install_activation));
        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest();
        simpleHttpRequest.setBaseReqeustBean(adsRequestBean);
        simpleHttpRequest.setReqCallBack(new ISReqCallBack<BaseResponseModel>() {
            @Override
            public void success(BaseResponseModel responseModel, String rawResult) {
                PL.i("ADS rawResult:" + rawResult);
                if (responseModel != null && responseModel.isRequestSuccess()){
                    SPUtil.saveSimpleInfo(context,StarPyUtil.STAR_PY_SP_FILE,STAR_PY_ADSINSTALLACTIVATION,"adsInstallActivation");
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        simpleHttpRequest.excute();
    }

}
