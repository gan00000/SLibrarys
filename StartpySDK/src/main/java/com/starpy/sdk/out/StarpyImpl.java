package com.starpy.sdk.out;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.ads.StarEventLogger;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SPayType;
import com.starpy.base.cfg.ConfigRequest;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.Localization;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.cs.CsReqeustBean;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.pay.gp.GooglePayActivity2;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.bean.req.WebPayReqBean;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.sdk.R;
import com.starpy.sdk.SWebViewActivity;
import com.starpy.sdk.SWebViewDialog;
import com.starpy.sdk.login.ILogin;
import com.starpy.sdk.login.ILoginFactory;

/**
 * Created by Efun on 2017/2/13.
 */

public class StarpyImpl implements IStarpy {

    ILogin iLogin;

    private long firstClickTime;

    private static boolean isInitSdk = false;

    public StarpyImpl() {
        iLogin = ILoginFactory.create();
    }

    @Override
    public void initSDK(final Activity activity) {
        PL.i("IStarpy initSDK");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (SStringUtil.isEmpty(ResConfig.getGameLanguage(activity))){
                    setGameLanguage(activity,SGameLanguage.zh_TW);
                }

                ConfigRequest.requestBaseCfg(activity.getApplicationContext());//下载配置文件
                ConfigRequest.requestTermsCfg(activity.getApplicationContext());//下载服务条款
                // 1.初始化fb sdk
                SFacebookProxy.initFbSdk(activity);
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

                if (iLogin != null){
                    //清除上一次登录成功的返回值
                    StarPyUtil.saveSdkLoginData(activity,"");
                    iLogin.startLogin(activity, iLoginCallBack);
                }
            }
        });


    }

    @Override
    public void pay(final Activity activity, final SPayType payType, final String cpOrderId, final String productId, final String roleLevel, final String extra) {
        PL.i("IStarpy pay");
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
    public void cs(Activity activity, String roleLevel, String roleVipLevel) {
        CsReqeustBean csReqeustBean = new CsReqeustBean(activity);
        csReqeustBean.setRoleLevel(roleLevel);
        csReqeustBean.setRoleVipLevel(roleVipLevel);

        // TODO: 2017/4/11 需要设置域名
//        csReqeustBean.setRequestUrl();

        SWebViewDialog sWebViewDialog = new SWebViewDialog(activity, R.style.StarDialogTheme);

        sWebViewDialog.setWebUrl(csReqeustBean.createPreRequestUrl());

        sWebViewDialog.show();
    }

    private void starPay(Activity activity, SPayType payType, String cpOrderId, String productId, String roleLevel, String extra) {
        if (payType == SPayType.OTHERS){//第三方储值

            othersPay(activity, cpOrderId, roleLevel, extra);

        }else{//默认Google储值

            if (StarPyUtil.getSdkCfg(activity) != null && StarPyUtil.getSdkCfg(activity).openOthersPay(activity)){//假若Google包侵权被下架，次配置可以启动三方储值
                PL.i("转第三方储值");
                othersPay(activity, cpOrderId, roleLevel, extra);

            }else{

                googlePay(activity, cpOrderId, productId, roleLevel, extra);
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
        activity.startActivity(i);
    }

    private void othersPay(Activity activity, String cpOrderId, String roleLevel, String extra) {
        WebPayReqBean webPayReqBean = PayHelper.buildWebPayBean(activity,cpOrderId,roleLevel,extra);

        Intent i = new Intent(activity, SWebViewActivity.class);
        String payThirdUrl = null;
        if (StarPyUtil.getSdkCfg(activity) != null) {

            payThirdUrl = StarPyUtil.getSdkCfg(activity).getS_Third_PayUrl();
        }
        if (TextUtils.isEmpty(payThirdUrl)){
            payThirdUrl = ResConfig.getPayPreferredUrl(activity) + ResConfig.getPayThirdMethod(activity);
        }

        String webUrl = payThirdUrl + "?" + SStringUtil.map2strData(webPayReqBean.fieldValueToMap());
//        webUrl = "http://pay.starb168.com/dynamic_change?roleName=dome%E7%8D%85%E5%BF%83%E7%8E%8B&packageName=com.star.mrmmd.tw&versionCode=20001&osLanguage=zh&userId=824&timestamp=1491539387924&gameCode=brmmd&serverName=BRT1&roleId=1902&imei=863125039661377&payFrom=starpy&gameLanguage=zh-TW&serverCode=99&androidid=b847f69119f1c240&iswifi=-111111&signature=3222bf99a5f1bc4e6e2ac5dc3f7a14fb&deviceType=HUAWEI%40%40EVA-AL00&mac=7c%3A11%3Acb%3A84%3Aff%3A2f&accessToken=f4112f436e5dabae183192c12c73d81c&versionName=2.0.1&payType=mobile&extra=11a8bb45d73844dbb2ac7ca7eb21d1f0&systemVersion=7.0&roleLevel=4&psid=62&cpOrderId=11a8bb45d73844dbb2ac7ca7eb21d1f0";

//        i.putExtra(SWebViewActivity.PLAT_WEBVIEW_URL, webUrl);
//        i.putExtra(SWebViewActivity.PLAT_WEBVIEW_TITLE,activity.getString(R.string.py_pay_title));
//        activity.startActivity(i);

//        SWebViewPopu p = new SWebViewPopu(activity);
//        p.showPop(webUrl);

        SWebViewDialog sWebViewDialog = new SWebViewDialog(activity, R.style.StarDialogTheme);

        sWebViewDialog.setWebUrl(webUrl);

        sWebViewDialog.show();
    }

    @Override
    public void onCreate(Activity activity) {
        PL.i("IStarpy onCreate");
        //广告
        StarEventLogger.activateApp(activity);

        if (!isInitSdk){
            initSDK(activity);
        }
        if (iLogin != null) {
            iLogin.onCreate(activity);
        }
    }

    @Override
    public void onResume(Activity activity) {
        PL.i("IStarpy onResume");
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
    }
}
