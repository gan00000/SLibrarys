package com.starpy.sdk.ads;

import android.app.Activity;

import com.appsflyer.AppsFlyerLib;
import com.starpy.base.cfg.ResConfig;
import com.starpy.thirdlib.facebook.SFacebookProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gan on 2018/5/4.
 */

public class AFDelegate {


    public static void activateApp(Activity activity){

        try {

            AppsFlyerLib.getInstance().startTracking(activity.getApplication(), ResConfig.getConfigInAssets(activity,"star_ads_appflyer_dev_key"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void trackinLoginEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_login_event_android");

        Map<String, Object> eventValue = new HashMap<String, Object>();
        AppsFlyerLib.getInstance().trackEvent(activity.getApplicationContext(),"starpy_login_event_android",eventValue);

    }

    public static void trackinRegisterEvent(Activity activity){
        SFacebookProxy.trackingEvent(activity,"starpy_register_event_android");

        Map<String, Object> eventValue = new HashMap<String, Object>();
//        eventValue.put(AFInAppEventParameterName.REVENUE,1);
        AppsFlyerLib.getInstance().trackEvent(activity.getApplicationContext(),"starpy_register_event_android",eventValue);

    }
}
