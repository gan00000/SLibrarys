package com.starpy.sdk.ads;

import android.app.Activity;
import android.content.Context;

import com.appsflyer.AppsFlyerLib;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;
import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.thirdlib.google.SGoogleProxy;

/**
 * Created by gan on 2017/3/3.
 */

public class StarEventLogger {

    public static void activateApp(Activity activity){

        try {

            AppsFlyerLib.getInstance().startTracking(activity.getApplication(),activity.getString(R.string.star_ads_appflyer_dev_key));

            SFacebookProxy.activateApp(activity.getApplicationContext());

            // Google Android first open conversion tracking snippet
            // Add this code to the onCreate() method of your application activity

            if (SStringUtil.isNotEmpty(activity.getString(R.string.star_ads_adword_conversionId))) {
                AdWordsConversionReporter.reportWithConversionId(activity.getApplicationContext(),
                        activity.getString(R.string.star_ads_adword_conversionId), activity.getString(R.string.star_ads_adword_label), "0.00", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void trackinLoginEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_login_event_android");
    }

    public static void trackinRegisterEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_register_event_android");
    }

    public static void trackinPayEvent(Activity activity, double payVaule){
        SFacebookProxy.trackingEvent(activity,"pay_android", payVaule);
    }

    public static void registerGoogleAdId(final Context context){
        new  Thread(new Runnable() {
            @Override
            public void run() {
                PL.i("save google ad id");
                String googleAdId = SGoogleProxy.getAdvertisingId(context.getApplicationContext());
                StarPyUtil.saveGoogleAdId(context,googleAdId);
            }
        }).start();
    }


}
