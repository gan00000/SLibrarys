package com.starpy.ads;

import android.app.Activity;
import android.content.Context;

import com.facebook.sfb.SFacebookProxy;
import com.google.ads.conversiontracking.AdWordsConversionReporter;

/**
 * Created by gan on 2017/3/3.
 */

public class StarEventLogger {

    public static void activateApp(Context context){

        try {

            SFacebookProxy.activateApp(context.getApplicationContext());

            // Google Android first open conversion tracking snippet
            // Add this code to the onCreate() method of your application activity

            AdWordsConversionReporter.reportWithConversionId(context.getApplicationContext(),
                    "858913995", "lniQCKDX5G4Qy_nHmQM", "0.00", false);
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
