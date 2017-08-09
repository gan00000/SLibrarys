package com.starpy.sdk.login.p;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.BitmapUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.SignatureUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.thirdlib.facebook.FbSp;
import com.starpy.thirdlib.facebook.SFacebookProxy;
import com.starpy.sdk.ads.StarEventLogger;
import com.starpy.base.bean.SLoginType;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.AccountInjectionRequestTask;
import com.starpy.data.login.execute.AccountLoginRequestTask;
import com.starpy.data.login.execute.AccountRegisterRequestTask;
import com.starpy.data.login.execute.ChangePwdRequestTask;
import com.starpy.data.login.execute.FindPwdRequestTask;
import com.starpy.data.login.execute.MacLoginRegRequestTask;
import com.starpy.data.login.execute.ThirdAccountBindRequestTask;
import com.starpy.data.login.execute.ThirdLoginRegRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.login.LoginContract;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.thirdlib.google.SGoogleSignIn;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gan on 2017/4/13.
 */

public class LoginPresenterImpl implements LoginContract.ILoginPresenter {

    private Activity activity;

    private LoginContract.ILoginView iLoginView;

    private Timer autoLoginTimer;
    int count = 3;

    private Activity getActivity(){
        return activity;
    }

    private Context getContext(){
        return activity.getApplicationContext();
    }

    @Override
    public void autoLogin(Activity activity) {
        this.activity = activity;
        String previousLoginType = StarPyUtil.getPreviousLoginType(activity);

        if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, previousLoginType)) {//自動登錄
            String account = StarPyUtil.getAccount(activity);
            String password = StarPyUtil.getPassword(activity);
            startAutoLogin(activity, SLoginType.LOGIN_TYPE_STARPY, account, password);

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_MAC, previousLoginType)) {//自動登錄
            String account = StarPyUtil.getMacAccount(activity);
            String password = StarPyUtil.getMacPassword(activity);
            startAutoLogin(activity, SLoginType.LOGIN_TYPE_STARPY, account, password);

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, previousLoginType)) {//自動登錄
            String fbScopeId = FbSp.getFbId(activity);
            String fbApps = FbSp.getAppsBusinessId(activity);
//            String fbToken = FbSp.getTokenForBusiness(this);
            if (SStringUtil.hasEmpty(fbScopeId,fbApps)){
                showLoginView();
            }else{
                startAutoLogin(activity, SLoginType.LOGIN_TYPE_FB, "", "");
            }

        }  else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_GOOGLE, previousLoginType)) {//自動登錄
//           thirdPlatLogin(activity,StarPyUtil.getGoogleId(activity),SLoginType.LOGIN_TYPE_GOOGLE);
            startAutoLogin(activity, SLoginType.LOGIN_TYPE_GOOGLE, "", "");

        }else {//進入登錄頁面
            showLoginView();
        }

        PL.i("fb keyhash:" + SignatureUtil.getHashKey(activity, activity.getPackageName()));

    }

    @Override
    public void starpyAccountLogin(Activity activity, String account, String pwd) {
        this.activity = activity;
        login(activity, account, pwd);
    }

    @Override
    public void fbLogin(Activity activity, SFacebookProxy sFacebookProxy) {
        this.activity = activity;
        sFbLogin(activity, sFacebookProxy, new FbLoginCallBack() {
            @Override
            public void loginSuccess(String fbScopeId, String businessId, String tokenForBusiness) {
                fbThirdLogin(fbScopeId, businessId, tokenForBusiness);
            }
        });
    }


    @Override
    public void googleLogin(final Activity activity, SGoogleSignIn sGoogleSignIn) {
        if (sGoogleSignIn == null){
            return;
        }
        sGoogleSignIn.startSignIn(new SGoogleSignIn.GoogleSignInCallBack() {
            @Override
            public void success(String id, String mFullName, String mEmail) {
                PL.i("google sign in : " + id);
                if (SStringUtil.isNotEmpty(id)) {
                    StarPyUtil.saveGoogleId(activity,id);
                    thirdPlatLogin(activity,id,SLoginType.LOGIN_TYPE_GOOGLE);
                }
            }

            @Override
            public void failure() {
                PL.i("google sign in failure");
            }
        });
    }

    @Override
    public void thirdPlatLogin(Activity activity, String thirdPlatId, final String registPlatform) {
        this.activity = activity;

        ThirdLoginRegRequestTask cmd = new ThirdLoginRegRequestTask(getActivity(),thirdPlatId,registPlatform);
        cmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        cmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {

                    if (sLoginResponse.isRequestSuccess()){

                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, registPlatform);
                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }
                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        cmd.excute(SLoginResponse.class);

    }

    @Override
    public void macLogin(Activity activity) {
        this.activity = activity;
        mMacLogin(activity);
    }

    @Override
    public void register(Activity activity, String account, String pwd, String email) {
        this.activity = activity;
        registerAccout(activity, account, pwd, email);
    }


    @Override
    public void setBaseView(LoginContract.ILoginView view) {
        iLoginView = view;
    }

    @Override
    public void autoLoginChangeAccount(Activity activity) {
        this.activity = activity;
        if (autoLoginTimer != null){
            autoLoginTimer.cancel();
        }
        showLoginView();
    }

    @Override
    public boolean hasAccountLogin() {

        String account = StarPyUtil.getAccount(activity);
        String password = StarPyUtil.getPassword(activity);
        if (TextUtils.isEmpty(account)) {
            account = StarPyUtil.getMacAccount(activity);
            password = StarPyUtil.getMacPassword(activity);
        }

        if (SStringUtil.hasEmpty(account,password)) {
            return false;
        }
        return true;
    }

    @Override
    public void destory(Activity activity) {
        this.activity = activity;
        if (autoLoginTimer != null){
            autoLoginTimer.cancel();
        }
    }


    @Override
    public void changePwd(final Activity activity, final String account, String oldPwd, String newPwd) {

        this.activity = activity;
        ChangePwdRequestTask changePwdRequestTask = new ChangePwdRequestTask(activity,account,oldPwd,newPwd);
        changePwdRequestTask.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        changePwdRequestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {

            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                        if (account.equals(StarPyUtil.getAccount(getContext()))){
                            StarPyUtil.savePassword(getContext(),"");
                        }
                        iLoginView.changePwdSuccess(sLoginResponse);

                    }else {

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });

        changePwdRequestTask.excute(SLoginResponse.class);

    }

    @Override
    public void findPwd(Activity activity, String account, String email) {
        this.activity = activity;
        FindPwdRequestTask findPwdRequestTask = new FindPwdRequestTask(getActivity(), account, email);
        findPwdRequestTask.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        findPwdRequestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
                        ToastUtils.toast(getActivity(), R.string.py_findpwd_success);

//                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                        if (iLoginView != null){
                            iLoginView.findPwdSuccess(sLoginResponse);
                        }
                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }

        });
        findPwdRequestTask.excute(SLoginResponse.class);
    }


    @Override
    public void accountBind(Activity activity, String account, String pwd, String email, int bindType, SFacebookProxy sFacebookProxy) {
        this.activity = activity;
        final String mAccount = account;
        final String mPwd = pwd;
        final String mEmail = email;
        if (bindType == SLoginType.bind_unique){

            ThirdAccountBindRequestTask bindRequestTask = new ThirdAccountBindRequestTask(getActivity(), account,pwd, email);
            sAccountBind(bindRequestTask);

        }else if (bindType == SLoginType.bind_fb){
            sFbLogin(activity, sFacebookProxy, new FbLoginCallBack() {
                @Override
                public void loginSuccess(String fbScopeId, String businessId, String tokenForBusiness) {
                    ThirdAccountBindRequestTask bindRequestTask = new ThirdAccountBindRequestTask(getActivity(), mAccount,mPwd, mEmail,fbScopeId,businessId,tokenForBusiness);
                    sAccountBind(bindRequestTask);
                }
            });

        }


    }

    @Override
    public void accountInject(Activity activity, String account, String pwd, String uid) {
        AccountInjectionRequestTask injectionRequestTask = new AccountInjectionRequestTask(getActivity(),account,pwd,uid);
        injectionRequestTask.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        injectionRequestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {

                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, "");

                    }else {

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        injectionRequestTask.excute(SLoginResponse.class);
    }

    private void sAccountBind(ThirdAccountBindRequestTask bindRequestTask) {
        bindRequestTask.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        bindRequestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
                        ToastUtils.toast(getActivity(), R.string.py_success);

//                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                        if (iLoginView != null){
                            iLoginView.accountBindSuccess(sLoginResponse);
                        }
                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }

        });
        bindRequestTask.excute(SLoginResponse.class);
    }

    private void mMacLogin(Activity activity) {

        MacLoginRegRequestTask macLoginRegCmd = new MacLoginRegRequestTask(getActivity());
        macLoginRegCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        macLoginRegCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
//                        ToastUtils.toast(getActivity(), R.string.py_login_success);
//                        StarPyUtil.saveSdkLoginData(getContext(),rawResult);

                        //1001 注册成功    1000登入成功
                        if (SStringUtil.isEqual("1001", sLoginResponse.getCode())) {//注册写一次

                            cteateUserImage(getActivity(),sLoginResponse.getFreeRegisterName(),sLoginResponse.getFreeRegisterPwd());

                            //mac登录第一次写一次
                        }else if(SStringUtil.isEqual("1000", sLoginResponse.getCode()) && SStringUtil.isEmpty(StarPyUtil.getMacAccount(getContext()))) {

                            cteateUserImage(getActivity(),sLoginResponse.getFreeRegisterName(),sLoginResponse.getFreeRegisterPwd());
                        }

                        StarPyUtil.saveMacAccount(getContext(),sLoginResponse.getFreeRegisterName());
                        StarPyUtil.saveMacPassword(getContext(),sLoginResponse.getFreeRegisterPwd());
                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_MAC);

                    }else {

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        macLoginRegCmd.excute(SLoginResponse.class);

    }

    private void cteateUserImage(Context context, String freeRegisterName, String freeRegisterPwd) {
        String appName = ApkInfoUtil.getApplicationName(context);
//        String text = "你登入" + appName + "的帳號和密碼如下:\n帳號:" + freeRegisterName + "\n" + "密碼:" + freeRegisterPwd;
        String text = String.format(context.getResources().getString(R.string.py_login_mac_tips), appName, freeRegisterName, freeRegisterPwd);
        PL.i("cteateUserImage:" + text);
        Bitmap bitmap = BitmapUtil.bitmapAddText(BitmapFactory.decodeResource(context.getResources(),R.drawable.v2_mac_pwd_bg),text);
        BitmapUtil.saveImageToGallery(getContext(),bitmap);
        ToastUtils.toast(context, context.getResources().getString(R.string.py_login_mac_saveimage_tips));
    }


    private void sFbLogin(final Activity activity, final SFacebookProxy sFacebookProxy, final FbLoginCallBack fbLoginCallBack) {
        if (sFacebookProxy == null) {
            return;
        }
        sFacebookProxy.fbLogin(activity, new SFacebookProxy.FbLoginCallBack() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(SFacebookProxy.User user) {
                PL.d("fb uid:" + user.getUserId());

                final String fbScopeId = user.getUserId();
                sFacebookProxy.requestBusinessId(activity, new SFacebookProxy.FbBusinessIdCallBack() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(String businessId) {
                        PL.d("fb businessId:" + businessId);
                        if (fbLoginCallBack != null){
                            fbLoginCallBack.loginSuccess(fbScopeId,businessId,FbSp.getTokenForBusiness(getActivity()));
                        }
                    }
                });

            }
        });
    }

    private void fbThirdLogin(String fbScopeId, String fbApps, String fbTokenBusiness) {

        ThirdLoginRegRequestTask cmd = new ThirdLoginRegRequestTask(getActivity(),fbScopeId,fbApps,fbTokenBusiness);
        cmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        cmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {

                    if (sLoginResponse.isRequestSuccess()){

                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_FB);
                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }
                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        cmd.excute(SLoginResponse.class);

    }

    private void login(Activity activity, final String account, final String password) {

        AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(getActivity(), account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(),"Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    if (sLoginResponse.isRequestSuccess()) {
                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                    }else{

                        ToastUtils.toast(getActivity(),sLoginResponse.getMessage());
                    }
                }else{
                    ToastUtils.toast(getActivity(),R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        accountLoginCmd.excute(SLoginResponse.class);

    }

    private void registerAccout(Activity activity, final String account, final String password, String email){

        AccountRegisterRequestTask accountRegisterCmd = new AccountRegisterRequestTask(getActivity(), account, password,email);
        accountRegisterCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        accountRegisterCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
                        ToastUtils.toast(getActivity(), R.string.py_register_success);

                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
                        handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);

                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }

        });
        accountRegisterCmd.excute(SLoginResponse.class);

    }

    private void startAutoLogin(final Activity activity, final String registPlatform, final String account, final String password) {


        if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, registPlatform)) {

            if (SStringUtil.hasEmpty(account, password)) {

                showLoginView();
                return;
            }
            if (SStringUtil.isEqual(account, password)) {
                showLoginView();
                return;
            }

            if (!StarPyUtil.checkAccount(account)) {
                showLoginView();
                return;
            }
            if (!StarPyUtil.checkPassword(password)) {
                showLoginView();
                return;
            }

            iLoginView.showAutoLoginTips(String.format(activity.getResources().getString(R.string.py_login_autologin_tips),account));
        }else{
            String autoLoginTips = activity.getResources().getString(R.string.py_login_autologin_logining_tips);
            iLoginView.showAutoLoginTips(registPlatform + " " + autoLoginTips);
        }
        iLoginView.showAutoLoginView();

        count = 3;

        iLoginView.showAutoLoginWaitTime("(" + count +  ")");

        autoLoginTimer = new Timer();//delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。

        autoLoginTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iLoginView.showAutoLoginWaitTime("(" + count +  ")");
                        if (count == 0){

                            if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, registPlatform)) {//免注册或者平台用户自动登录
//                                autoLogin22(activity, account, password);

                                starpyAccountLogin(activity,account,password);

                            }else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, registPlatform)){//fb自动的登录
                                String fbScopeId = FbSp.getFbId(activity);
                                String fbApps = FbSp.getAppsBusinessId(activity);
                                fbThirdLogin(fbScopeId, fbApps,FbSp.getTokenForBusiness(activity));

                            }else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_GOOGLE, registPlatform)){//Google登录

                                thirdPlatLogin(activity,StarPyUtil.getGoogleId(activity),SLoginType.LOGIN_TYPE_GOOGLE);
                            }

                            if (autoLoginTimer != null){

                                autoLoginTimer.cancel();
                            }
                        }
                        count--;
                    }
                });
            }
        },300,1000);

    }

    private void showLoginView() {
        if (iLoginView != null){
//            iLoginView.hildAutoLoginView();
            iLoginView.showLoginView();
        }
    }


    private void handleRegisteOrLoginSuccess(SLoginResponse loginResponse, String rawResult, String loginType) {

        if (SStringUtil.isNotEmpty(loginType)) {//loginType为空时是账号注入登录，不能空时是其他普通登入

            StarPyUtil.saveSdkLoginData(getContext(), loginResponse.getRawResponse());
            StarPyUtil.savePreviousLoginType(activity, loginType);
            try {
                if (loginResponse != null) {
                    //1001 注册成功    1000登入成功
                    if (SStringUtil.isEqual("1000", loginResponse.getCode())) {
                        StarEventLogger.trackinLoginEvent(activity);
                    } else if (SStringUtil.isEqual("1001", loginResponse.getCode())) {
                        StarEventLogger.trackinRegisterEvent(activity);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    /*    if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_MAC, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, loginType)) {

        }*/

        ToastUtils.toast(activity, R.string.py_login_success);

        if (iLoginView != null){
            iLoginView.LoginSuccess(loginResponse);
        }
    }


    interface FbLoginCallBack{

        void loginSuccess(String fbScopeId, String businessId, String tokenForBusiness);
    }

}
