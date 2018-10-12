package com.starpy.sdk.out;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.core.base.ObjFactory;
import com.core.base.utils.AppUtil;
import com.core.base.utils.PL;
import com.core.base.utils.PermissionUtil;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.SignatureUtil;
import com.core.base.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.starpy.base.bean.SGameBaseRequestBean;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SPayType;
import com.starpy.base.cfg.ConfigRequest;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.Localization;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.cs.CsReqeustBean;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.execute.QueryFbToStarpyUserIdTask;
import com.starpy.pay.gp.GooglePayActivity2;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.bean.req.WebPayReqBean;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.plugin.PayPluginManger;
import com.starpy.plugin.PluginCallBack;
import com.starpy.sdk.BuildConfig;
import com.starpy.sdk.R;
import com.starpy.sdk.SWebViewDialog;
import com.starpy.sdk.StarpyPermissionActivity;
import com.starpy.sdk.ads.StarEventLogger;
import com.starpy.sdk.login.DialogLoginImpl;
import com.starpy.sdk.login.ILogin;
import com.starpy.sdk.plat.PlatMainActivity;
import com.starpy.thirdlib.facebook.FriendProfile;
import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.thirdlib.google.SGoogleFirebaseProxy;
import com.starpy.thirdlib.google.SGooglePlayGameServices;

import java.net.URLEncoder;
import java.util.List;


public class StarpyImpl implements IStarpy {

    private static final int PERMISSION_REQUEST_CODE = 401;
    private ILogin iLogin;

    private long firstClickTime;

    private static boolean isInitSdk = false;

    private SFacebookProxy sFacebookProxy;
    private SGooglePlayGameServices sGooglePlayGameServices;

    private SWebViewDialog csWebViewDialog;
    private SWebViewDialog otherPayWebViewDialog;

    private SGoogleFirebaseProxy sGoogleFirebaseProxy;

    private PayPluginManger payPluginManger;

    public StarpyImpl() {
        iLogin = ObjFactory.create(DialogLoginImpl.class);
    }

    @Override
    public void initSDK(final Activity activity) {
        PL.i("IStarpy initSDK");
        //清除上一次登录成功的返回值
        StarPyUtil.saveSdkLoginData(activity,"");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                StarEventLogger.registerGoogleAdId(activity);
                StarEventLogger.reportInstallActivation(activity.getApplicationContext());
                try {
                    Fresco.initialize(activity.getApplicationContext());//初始化fb Fresco库
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SStringUtil.isEmpty(ResConfig.getGameLanguage(activity))){
                    if (StarPyUtil.isMainland(activity)) {
                        setGameLanguage(activity,SGameLanguage.zh_CH);
                    }else {

                        setGameLanguage(activity,SGameLanguage.zh_TW);
                    }
                }

                ConfigRequest.requestBaseCfg(activity.getApplicationContext());//下载配置文件
                ConfigRequest.requestTermsCfg(activity.getApplicationContext());//下载服务条款
                // 1.初始化fb sdk
                SFacebookProxy.initFbSdk(activity.getApplicationContext());
                sFacebookProxy = new SFacebookProxy(activity.getApplicationContext());
                isInitSdk = true;
            }
        });

    }

    /*
        语言默认繁体zh-TW，用来设置UI界面语言，提示等
        需要在其他所有方法之前调用
    * */

    @Override
    public void setGameLanguage(final Activity activity, final SGameLanguage gameLanguage) {
        PL.i("IStarpy setGameLanguage:" + gameLanguage);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Localization.gameLanguage(activity, gameLanguage);
            }
        });

    }


    @Override
    public void registerRoleInfo(Activity activity, String roleId, String roleName, String roleLevel, String severCode, String serverName) {
        PL.i("IStarpy registerRoleInfo");
        PL.i("roleId:" + roleId + ",roleName:" + roleName + ",severCode:" + severCode + ",serverName:" + serverName);
        StarPyUtil.saveRoleInfo(activity, roleId, roleName, severCode, serverName);//保存角色信息
    }

    @Override
    public void login(final Activity activity, final ILoginCallBack iLoginCallBack) {
        PL.i("IStarpy login");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PL.i("fb keyhash:" + SignatureUtil.getHashKey(activity, activity.getPackageName()));
                PL.i("google sha1:" + SignatureUtil.getSignatureSHA1WithColon(activity, activity.getPackageName()));
                if (iLogin != null){
                    //清除上一次登录成功的返回值
                    StarPyUtil.saveSdkLoginData(activity,"");

                    iLogin.initFacebookPro(activity,sFacebookProxy);
                    iLogin.startLogin(activity, iLoginCallBack);
                }
            }
        });


    }

    @Override
    public void pay(final Activity activity, final SPayType payType, final String cpOrderId, final String productId, final String roleLevel, final String extra) {
        PL.i("IStarpy pay cpOrderId:" + cpOrderId + ",productId:" + productId + ",roleLevel:" + roleLevel + ",extra:" + extra);
        if ((System.currentTimeMillis() - firstClickTime) < 1000){//防止连续点击
            PL.i("点击过快，无效");
            return;
        }
        firstClickTime = System.currentTimeMillis();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                starPay(activity, payType, cpOrderId, productId, roleLevel, extra);
            }
        });

    }

    @Override
    public void cs(final Activity activity, final String roleLevel, final String roleVipLevel) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                CsReqeustBean csReqeustBean = new CsReqeustBean(activity);
                csReqeustBean.setRoleLevel(roleLevel);
                csReqeustBean.setRoleVipLevel(roleVipLevel);

                csReqeustBean.setRequestUrl(ResConfig.getCsPreferredUrl(activity));
                csReqeustBean.setRequestSpaUrl(ResConfig.getCsSpareUrl(activity));
                csReqeustBean.setRequestMethod(activity.getResources().getString(R.string.star_cs_method));

                csWebViewDialog = new SWebViewDialog(activity, R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);

                csWebViewDialog.setWebUrl(csReqeustBean.createPreRequestUrl());

                csWebViewDialog.show();
            }
        });

    }

    @Override
    public void openWebview(final Activity activity, final String roleLevel, final String roleVipLevel) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SGameBaseRequestBean webviewReqeustBean = new SGameBaseRequestBean(activity);
                webviewReqeustBean.setRoleLevel(roleLevel);
                webviewReqeustBean.setRoleVipLevel(roleVipLevel);

                //设置签名
//        appkey+gameCode+userId+roleId+timestamp
                webviewReqeustBean.setSignature(SStringUtil.toMd5(webviewReqeustBean.getAppKey() + webviewReqeustBean.getGameCode()
                        + webviewReqeustBean.getUserId() + webviewReqeustBean.getRoleId() + webviewReqeustBean.getTimestamp()));

                webviewReqeustBean.setRequestUrl(ResConfig.getActivityPreferredUrl(activity));//活动域名
                webviewReqeustBean.setRequestSpaUrl(ResConfig.getActivitySpareUrl(activity));
                webviewReqeustBean.setRequestMethod(activity.getResources().getString(R.string.star_act_dynamic_method));

                SWebViewDialog sWebViewDialog = new SWebViewDialog(activity, R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);

                sWebViewDialog.setWebUrl(webviewReqeustBean.createPreRequestUrl());

                sWebViewDialog.show();
            }
        });
    }


    @Override
    public void openWebview(final Activity activity, final String url) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                SWebViewDialog sWebViewDialog = new SWebViewDialog(activity, R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);
                sWebViewDialog.setWebUrl(url);
                sWebViewDialog.show();
            }
        });
    }

    @Override
    public void share(Activity activity, ISdkCallBack iSdkCallBack, String shareLinkUrl) {
        share(activity, iSdkCallBack,"","", shareLinkUrl,"");
    }

    @Override
    public void share(Activity activity, final ISdkCallBack iSdkCallBack, String title, String message, String shareLinkUrl, String sharePictureUrl) {
        if (sFacebookProxy != null){

            SFacebookProxy.FbShareCallBack fbShareCallBack = new SFacebookProxy.FbShareCallBack() {
                @Override
                public void onCancel() {
                    if (iSdkCallBack != null){
                        iSdkCallBack.failure();
                    }
                }

                @Override
                public void onError(String message) {
                    if (iSdkCallBack != null){
                        iSdkCallBack.failure();
                    }
                }

                @Override
                public void onSuccess() {
                    if (iSdkCallBack != null){
                        iSdkCallBack.success();
                    }
                }
            };
            if (SStringUtil.isNotEmpty(StarPyUtil.getServerCode(activity)) && SStringUtil.isNotEmpty(StarPyUtil.getRoleId(activity))) {
                if (shareLinkUrl.contains("?")){//userId+||S||+serverCode+||S||+roleId
                    shareLinkUrl = shareLinkUrl + "&campaign=" + URLEncoder.encode(StarPyUtil.getUid(activity)+ "||S||" + StarPyUtil.getServerCode(activity)+"||S||"+StarPyUtil.getRoleId(activity));
                }else {
                    shareLinkUrl = shareLinkUrl + "?campaign=" + URLEncoder.encode(StarPyUtil.getUid(activity)+ "||S||" + StarPyUtil.getServerCode(activity)+"||S||"+StarPyUtil.getRoleId(activity));
                }
            }
            sFacebookProxy.fbShare(activity, fbShareCallBack,title,message,shareLinkUrl,sharePictureUrl);
        }
    }


    @Override
    public void openPlatform(final Activity activity, final String roleLevel, final String roleVipLevel) {
        PL.i("IStarpy pay roleLevel:" + roleLevel + ",roleVipLevel:" + roleVipLevel);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                StarPyUtil.saveRoleLevelVip(activity,roleLevel,roleVipLevel);
                if (StarPyUtil.isLogin(activity)){
                    activity.startActivity(new Intent(activity, PlatMainActivity.class));
                }else {
                    ToastUtils.toast(activity,"please login game first");
                }
            }
        });

    }


    private void starPay(Activity activity, SPayType payType, String cpOrderId, String productId, String roleLevel, String extra) {
        if (payType == SPayType.OTHERS){//第三方储值

            othersPay(activity, cpOrderId,productId, roleLevel, extra);

        }else{//默认Google储值

            if (StarPyUtil.getSdkCfg(activity) != null && StarPyUtil.getSdkCfg(activity).openOthersPay(activity)){//假若Google包侵权被下架，次配置可以启动三方储值
                PL.i("转第三方储值");
                othersPay(activity, cpOrderId,productId, roleLevel, extra);

            }else{

                if (StarPyUtil.isPayPlugin(activity)){

                    googlePayInPlugin(activity, cpOrderId, productId, roleLevel, extra);

                }else {

                    googlePay(activity, cpOrderId, productId, roleLevel, extra);
                }
            }

        }
    }

    private void googlePay(Activity activity, String cpOrderId, String productId, String roleLevel, String extra) {
        GooglePayCreateOrderIdReqBean googlePayCreateOrderIdReqBean = new GooglePayCreateOrderIdReqBean(activity);
        googlePayCreateOrderIdReqBean.setCpOrderId(cpOrderId);
        googlePayCreateOrderIdReqBean.setProductId(productId);
        googlePayCreateOrderIdReqBean.setRoleLevel(roleLevel);
        googlePayCreateOrderIdReqBean.setExtra(extra);

        Intent i = new Intent(activity, GooglePayActivity2.class);
        i.putExtra(GooglePayActivity2.GooglePayReqBean_Extra_Key, googlePayCreateOrderIdReqBean);
        activity.startActivityForResult(i,GooglePayActivity2.GooglePayReqeustCode);
    }

    private void googlePayInPlugin(final Activity activity, final String cpOrderId, final String productId, final String roleLevel,final String extra) {

        if (payPluginManger == null) {
            payPluginManger = new PayPluginManger(activity);
        }
        payPluginManger.setPluginCallBack(new PluginCallBack() {
            @Override
            public void payInapp(String s) {
                googlePay(activity, cpOrderId, productId, roleLevel, extra);
            }

            @Override
            public void payInPlugin(String pluginPkg) {

                GooglePayCreateOrderIdReqBean googlePayCreateOrderIdReqBean = new GooglePayCreateOrderIdReqBean(activity);
                googlePayCreateOrderIdReqBean.setCpOrderId(cpOrderId);
                googlePayCreateOrderIdReqBean.setProductId(productId);
                googlePayCreateOrderIdReqBean.setRoleLevel(roleLevel);
                googlePayCreateOrderIdReqBean.setExtra(extra);

                try {
                    Intent i = new Intent();
                    i.setAction(PayPluginManger.PayPluginAction);
                    i.setPackage(pluginPkg);
                    i.putExtra(GooglePayActivity2.GooglePayReqBean_Extra_Key, googlePayCreateOrderIdReqBean);
                    activity.startActivityForResult(i,GooglePayActivity2.GooglePayReqeustCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        payPluginManger.checkPlugin();

    }

    private void othersPay(Activity activity, String cpOrderId,String productId, String roleLevel, String extra) {
        WebPayReqBean webPayReqBean = PayHelper.buildWebPayBean(activity,cpOrderId,productId,roleLevel,extra);

        String payThirdUrl = null;
        if (StarPyUtil.getSdkCfg(activity) != null) {

            payThirdUrl = StarPyUtil.getSdkCfg(activity).getS_Third_PayUrl();
        }
        if (TextUtils.isEmpty(payThirdUrl)){
            payThirdUrl = ResConfig.getPayPreferredUrl(activity) + ResConfig.getPayThirdMethod(activity);
        }
        webPayReqBean.setCompleteUrl(payThirdUrl);

        String webUrl = webPayReqBean.createPreRequestUrl();

        otherPayWebViewDialog = new SWebViewDialog(activity, R.style.Starpy_Theme_AppCompat_Dialog_Notitle_Fullscreen);

        otherPayWebViewDialog.setWebUrl(webUrl);

        otherPayWebViewDialog.show();
    }

    @Override
    public void onCreate(final Activity activity) {
        PL.i("IStarpy onCreate");
        PL.i("the jar version:" + BuildConfig.JAR_VERSION);//打印版本号
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                activity.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        PL.d("activity onSystemUiVisibilityChange");
                        AppUtil.hideActivityBottomBar(activity);
                    }
                });

                //广告
                StarEventLogger.activateApp(activity);

                if (!isInitSdk){
                    initSDK(activity);
                }
                if (iLogin != null) {
                    iLogin.onCreate(activity);
                }
                sGooglePlayGameServices = new SGooglePlayGameServices(activity);

                if (!StarPyUtil.isMainland(activity)) {
                    sGoogleFirebaseProxy = new SGoogleFirebaseProxy(activity);
                    sGoogleFirebaseProxy.logEvent("starpy_install",null);
                }
                PL.d("sha1:" + SignatureUtil.getSignatureSHA1WithColon(activity,activity.getPackageName()));
                PL.d("md5:" + SignatureUtil.getSignatureMD5(activity,activity.getPackageName()).toLowerCase());

                //permission授权
                if (!PermissionUtil.hasSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Intent intent = new Intent(activity, StarpyPermissionActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume(Activity activity) {
        PL.i("IStarpy onResume");
//        AppUtil.hideActivityBottomBar(activity);
        if (iLogin != null) {
            iLogin.onResume(activity);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        PL.i("IStarpy onActivityResult");
        if (iLogin != null) {
            iLogin.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (sFacebookProxy != null){
            sFacebookProxy.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (csWebViewDialog != null){
            csWebViewDialog.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (otherPayWebViewDialog != null){
            otherPayWebViewDialog.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (requestCode == GooglePayActivity2.GooglePayReqeustCode && resultCode == GooglePayActivity2.GooglePayResultCode){
            if (data != null && data.getExtras() != null){
                Bundle b = data.getExtras();
                GooglePayCreateOrderIdReqBean g = (GooglePayCreateOrderIdReqBean) data.getSerializableExtra("GooglePayCreateOrderIdReqBean");
                if (!StarPyUtil.isMainland(activity) && b.getInt("status") == 93 && g != null){//充值成功

                    if (sGoogleFirebaseProxy != null && SStringUtil.isNotEmpty(g.getPayValue())) {

                        try {
                            Bundle bundle = new Bundle();
                            bundle.putFloat(FirebaseAnalytics.Param.VALUE, Float.parseFloat(g.getPayValue()));
                            sGoogleFirebaseProxy.logEvent("google_pay", bundle);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    PL.i("google pay success");
                }
            }
            return;
        }

        if (sGooglePlayGameServices != null){
            sGooglePlayGameServices.handleActivityResult(activity, requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause(Activity activity) {
        PL.i("IStarpy onPause");
        if (iLogin != null) {
            iLogin.onPause(activity);
        }
    }

    @Override
    public void onStop(Activity activity) {
        PL.i("IStarpy onStop");
        if (iLogin != null) {
            iLogin.onStop(activity);
        }
    }

    @Override
    public void onDestroy(Activity activity) {
        PL.i("IStarpy onDestroy");
        if (iLogin != null) {
            iLogin.onDestroy(activity);
        }
        if (sFacebookProxy != null){
            sFacebookProxy.onDestroy(activity);
        }
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        PL.i("IStarpy onRequestPermissionsResult");
    }

    @Override
    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
        PL.i("IStarpy onWindowFocusChanged: hasFocus -- " + hasFocus);
        if (hasFocus) {
            AppUtil.hideActivityBottomBar(activity);
        }
    }


    @Override
    public void displayingAchievements() {
        if (sGooglePlayGameServices != null) {
            sGooglePlayGameServices.displayingAchievements();
        }
    }

    @Override
    public void displayLeaderboard(String leaderboardID) {
        if (sGooglePlayGameServices != null) {
            sGooglePlayGameServices.displayLeaderboard(leaderboardID);
        }
    }

    @Override
    public void unlockAchievement(String achievementID) {
        if (sGooglePlayGameServices != null){
            sGooglePlayGameServices.unlock(achievementID);
        }
    }

    @Override
    public void submitScore(String leaderboardID, long score) {
        if (sGooglePlayGameServices != null){
            sGooglePlayGameServices.submitScore(leaderboardID, score);
        }
    }


    @Override
    public void getFacebookFriends(final Activity activity, final SFacebookProxy.RequestFriendsCallBack requestFriendsCallBack) {
        QueryFbToStarpyUserIdTask queryFbToStarpyUserIdTask = new QueryFbToStarpyUserIdTask(activity,sFacebookProxy,requestFriendsCallBack);
        queryFbToStarpyUserIdTask.query();
    }

    @Override
    public void inviteFriends(Activity activity, List<FriendProfile> friendProfiles, String message, SFacebookProxy.FbInviteFriendsCallBack fbInviteFriendsCallBack) {
        if (sFacebookProxy != null){
            sFacebookProxy.inviteFriends(activity, friendProfiles, message, fbInviteFriendsCallBack);
        }
    }

}
