package com.e.game.g;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;

import com.starpy.google.EfunGoogleProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Efun on 2016/7/19.
 */
public class EAds {

    public static final String TAG = "EADS";

    public static void initEAds(Activity activity){
        if (activity == null)
            return;
//        EfunGoogleProxy.EfunGoogleAnalytics.initDefaultTracker()
        EfunGoogleProxy.onCreateMainActivity(activity);
        EfunGoogleProxy.initPush(activity.getApplicationContext());
        showEads(activity);
        EfunGoogleProxy.EfunFirebaseAnalytics.logEvent(activity.getApplicationContext(),activity.getPackageName() + "_" + Constant.appStart);//firebase分析，记录激活
    }

    /**
     * 展示广告
     * @param activity
     */
    public static void showEads(final Activity activity){
        EAdsDialog ed = new EAdsDialog(activity);
        ed.setContentView(R.layout.eads_dialog_layout);
        ed.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d(TAG,"onDismiss");
                Map<String,String> data = new HashMap<String, String>();
//                Bdown.excuteSpecialThing(activity.getApplicationContext(),data);
            }
        });
        ed.setCanceledOnTouchOutside(true);
        ed.show();
    }


}
