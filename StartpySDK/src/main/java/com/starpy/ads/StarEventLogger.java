package com.starpy.ads;

import android.app.Activity;
import android.content.Context;

import com.core.base.utils.SStringUtil;
import com.facebook.sfb.SFacebookProxy;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.startpy.sdk.R;

/**
 * Created by gan on 2017/3/3.
 */

public class StarEventLogger {

    public static void activateApp(Context context){

        try {

            SFacebookProxy.activateApp(context.getApplicationContext());

            // Google Android first open conversion tracking snippet
            // Add this code to the onCreate() method of your application activity

            if (SStringUtil.isNotEmpty(context.getString(R.string.star_ads_adword_conversionId))) {
                AdWordsConversionReporter.reportWithConversionId(context.getApplicationContext(),
                        context.getString(R.string.star_ads_adword_conversionId), context.getString(R.string.star_ads_adword_label), "0.00", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void trackinLoginEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_login_event");
    }

    public static void trackinRegisterEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_register_event");
    }
}
