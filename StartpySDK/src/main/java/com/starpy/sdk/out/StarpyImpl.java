package com.starpy.sdk.out;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.ads.StarEventLogger;
import com.starpy.base.cfg.ConfigRequest;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.data.pay.PayType;
import com.starpy.pay.IPay;
import com.starpy.pay.gp.GooglePayActivity2;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.bean.req.WebPayReqBean;
import com.starpy.pay.gp.constants.GooglePayContant;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.sdk.SWebViewActivity;
import com.starpy.sdk.login.SLoginActivity;

/**
 * Created by Efun on 2017/2/13.
 */

public class StarpyImpl implements IStarpy {

    private ILoginCallBack loginCallBack;

    private IPay iPay;

    private long firstClickTime;

    @Override
    public void initSDK(Activity activity) {
        PL.i("IStarpy initSDK");
        ConfigRequest.requestBaseCfg(activity.getApplicationContext());//下载配置文件
        ConfigRequest.requestTermsCfg(activity.getApplicationContext());//下载服务条款
        // 1.初始化fb sdk
        SFacebookProxy.initFbSdk(activity);

    }

    @Override
    public void setGameLanguage(Activity activity, String gameLanguage) {
        PL.i("IStarpy setGameLanguage");
    }

    @Override
    public void registerRoleInfo(Activity activity, String roleId, String roleName, String roleLevel, String severCode, String serverName) {
        PL.i("IStarpy registerRoleInfo");
        StarPyUtil.saveRoleInfo(activity, roleId, roleName, severCode, serverName);//保存角色信息
    }

    @Override
    public void login(Activity activity, ILoginCallBack iLoginCallBack) {
        PL.i("IStarpy login");
        this.loginCallBack = iLoginCallBack;

        Intent intent = new Intent(activity, SLoginActivity.class);

        activity.startActivityForResult(intent, SLoginActivity.S_LOGIN_REQUEST);//開啟登入

    }

    @Override
    public void pay(final Activity activity, PayType payType, String cpOrderId, String productId, String roleLevel, String extra) {
        PL.i("IStarpy pay");
        if ((System.currentTimeMillis() - firstClickTime) < 800){//防止连续点击
            PL.i("点击过快，无效");
            return;
        }
        firstClickTime = System.currentTimeMillis();

        if (payType == PayType.OTHERS){//第三方储值

            othersPay(activity, cpOrderId, roleLevel, extra);

        }else{//默认Google储值

            if (StarPyUtil.getSdkCfg(activity) != null && StarPyUtil.getSdkCfg(activity).isGoogleToOthersPay()){//假若Google包侵权被下架，次配置可以启动三方储值
                PL.i("转第三方储值");
                othersPay(activity, cpOrderId, roleLevel, extra);

            }else{

                googlePay(activity, cpOrderId, productId, roleLevel, extra);
            }
/*

            iPay.setIPayCallBack(new IPayCallBack() {
                @Override
                public void success() {
//                    ToastUtils.toast(activity,"pay success");
                }

                @Override
                public void fail() {
//                    ToastUtils.toast(activity,"pay fail");
                }
            });
            iPay.startPay(activity,googlePayCreateOrderIdReqBean);
*/

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
        i.putExtra(SWebViewActivity.PLAT_WEBVIEW_URL,payThirdUrl + "?" + SStringUtil.map2strData(webPayReqBean.fieldValueToMap()));
        activity.startActivity(i);
    }

    @Override
    public void onCreate(Activity activity) {
        PL.i("IStarpy onCreate");
        //广告
        StarEventLogger.activateApp(activity);
        if (iPay == null){
//            iPay = IPayFactory.create(IPayFactory.PAY_GOOGLE);
        }
        if (iPay != null) {
            iPay.onCreate(activity);
        }
    }

    @Override
    public void onResume(Activity activity) {
        PL.i("IStarpy onResume");
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        PL.i("IStarpy onActivityResult");
        if (requestCode == SLoginActivity.S_LOGIN_REQUEST && resultCode == SLoginActivity.S_LOGIN_RESULT && data != null) {
            SLoginResponse sLoginResponse = (SLoginResponse) data.getSerializableExtra(SLoginActivity.S_LOGIN_RESPONSE_OBJ);
            if (this.loginCallBack != null) {
                this.loginCallBack.onLogin(sLoginResponse);
            }
        }else if (requestCode == GooglePayContant.RC_REQUEST){
            if (iPay != null){
                iPay .onActivityResult(activity, requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onPause(Activity activity) {
        PL.i("IStarpy onPause");
    }

    @Override
    public void onStop(Activity activity) {
        PL.i("IStarpy onStop");
        if (iPay != null){
            iPay .onStop(activity);
        }

    }

    @Override
    public void onDestroy(Activity activity) {
        PL.i("IStarpy onDestroy");
        if (iPay != null){
            iPay .onDestroy(activity);
        }

    }
}
