package com.starpy.sdk.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.FragmentUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.SignatureUtil;
import com.core.base.utils.ToastUtils;
import com.facebook.sfb.FbSp;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.ads.StarEventLogger;
import com.starpy.base.bean.SLoginType;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.AccountLoginRequestTask;
import com.starpy.data.login.execute.FBLoginRegRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.login.fragment.AccountLoginFragment;
import com.starpy.sdk.login.fragment.AccountLoginMainFragment;
import com.starpy.sdk.login.fragment.BaseFragment;
import com.starpy.sdk.utils.DialogUtil;
import com.startpy.sdk.R;

import java.util.Timer;
import java.util.TimerTask;

public class SLoginActivity extends BaseLoginActivity {

    public static final int S_LOGIN_REQUEST = 600;
    public static final int S_LOGIN_RESULT = 700;
    public static final String S_LOGIN_RESPONSE_OBJ = "S_LOGIN_RESPONSE_OBJ";

    private FragmentManager fragmentManager;
    private RelativeLayout relativeLayout;
    private SFacebookProxy sFacebookProxy;

    //    自動登錄頁面控件
    private RelativeLayout autoLoginLayout;
    private TextView autoLoginTips;
    private TextView autoLoginWaitTime;
    private TextView autoLoginChangeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.py_login_activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.fragment_content);

        initAutoLoginView();

        fragmentManager = getSupportFragmentManager();

        // 2.实例EfunFacebookProxy
        sFacebookProxy = new SFacebookProxy(this.getApplicationContext());

        String previousLoginType = StarPyUtil.getPreviousLoginType(this);

        if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, previousLoginType)) {//自動登錄
            String account = StarPyUtil.getAccount(this);
            String password = StarPyUtil.getPassword(this);
            initAutoLogin(this, SLoginType.LOGIN_TYPE_STARPY, account, password);

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_MAC, previousLoginType)) {//自動登錄
            String account = StarPyUtil.getMacAccount(this);
            String password = StarPyUtil.getMacPassword(this);
            initAutoLogin(this, SLoginType.LOGIN_TYPE_STARPY, account, password);

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, previousLoginType)) {//自動登錄
            String fbScopeId = FbSp.getFbId(this);
            String fbApps = FbSp.getAppsBusinessId(this);
//            String fbToken = FbSp.getTokenForBusiness(this);
            if (SStringUtil.hasEmpty(fbScopeId,fbApps)){
                goCommomLogin();
            }else{
                initAutoLogin(this, SLoginType.LOGIN_TYPE_FB, fbScopeId, fbApps);
            }

        } else {//進入登錄頁面
            goCommomLogin();
        }

        PL.i("fb keyhash:" + SignatureUtil.getHashKey(this, getPackageName()));

    }

    private void goCommomLogin() {

        relativeLayout.setVisibility(View.VISIBLE);
        autoLoginLayout.setVisibility(View.GONE);

        String account = StarPyUtil.getAccount(this);
        String password = StarPyUtil.getPassword(this);
        if (TextUtils.isEmpty(account)) {
            account = StarPyUtil.getMacAccount(this);
            password = StarPyUtil.getMacPassword(this);
        }

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            fragmentManager.beginTransaction().replace(relativeLayout.getId(), new AccountLoginMainFragment()).commit();
        } else {

            fragmentManager.beginTransaction().replace(relativeLayout.getId(), new AccountLoginFragment()).commit();
        }
    }

    private void initAutoLoginView() {
        autoLoginLayout = (RelativeLayout) findViewById(R.id.py_auto_login_page);
        autoLoginTips = (TextView) findViewById(R.id.py_auto_login_tips);
        autoLoginWaitTime = (TextView) findViewById(R.id.py_auto_login_wait_time);
        autoLoginChangeAccount = (TextView) findViewById(R.id.py_auto_login_change);
        autoLoginChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0){
                   if (autoLoginTimer != null){
                       autoLoginTimer.cancel();
                   }
                   goCommomLogin();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        onActivityResult调用onActivityResult
        sFacebookProxy.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        onDestroy调用onDestroy
        sFacebookProxy.onDestroy(this);
        if (autoLoginTimer != null){
            autoLoginTimer.cancel();
        }
    }

    public void handleRegisteOrLoginSuccess(SLoginResponse loginResponse, String rawResult, String loginType) {
        try {
            StarPyUtil.saveSdkLoginData(this, loginResponse.getRawResponse());

            if (loginResponse != null) {
                //1001 注册成功    1000登入成功
                if (SStringUtil.isEqual("1000", loginResponse.getCode())) {
                    StarEventLogger.trackinLoginEvent(this);
                } else if (SStringUtil.isEqual("1001", loginResponse.getCode())) {
                    StarEventLogger.trackinRegisterEvent(this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setResult(loginResponse);

        StarPyUtil.savePreviousLoginType(this, loginType);

    /*    if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_MAC, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, loginType)) {

        }*/

        ToastUtils.toast(this, R.string.py_login_success);

        this.finish();
    }

    public void setResult(SLoginResponse loginResponse) {
        if (loginResponse != null) {
            loginResponse.setGameCode(ResConfig.getGameCode(this));
            Intent data = new Intent();

            data.putExtra(S_LOGIN_RESPONSE_OBJ, loginResponse);
            setResult(S_LOGIN_RESULT, data);
        }
    }

    public void replaceFragment(BaseFragment newFragment) {
        FragmentUtil.replace(fragmentManager, relativeLayout.getId(), newFragment);
    }

    public void replaceFragmentBackToStack(BaseFragment newFragment) {
        FragmentUtil.replaceBackStack(fragmentManager, relativeLayout.getId(), newFragment);
    }

    public void popBackStack() {
        FragmentUtil.popBackStack(fragmentManager);
    }

    public SFacebookProxy getsFacebookProxy() {
        return sFacebookProxy;
    }

    private Timer autoLoginTimer;
    int count = 3;


    private void initAutoLogin(final SLoginActivity sLoginActivity, final String loginType, final String account, final String password) {

        if (SStringUtil.hasEmpty(account, password)) {

            goCommomLogin();
            return;
        }
        if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, loginType)) {
            if (SStringUtil.isEqual(account, password)) {
                goCommomLogin();
                return;
            }

            if (!StarPyUtil.checkAccount(account)) {
                goCommomLogin();
                return;
            }
            if (!StarPyUtil.checkPassword(password)) {
                goCommomLogin();
                return;
            }

            autoLoginTips.setText(String.format(getResources().getString(R.string.py_login_autologin_tips),account));
        }else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, loginType)){
            autoLoginTips.setText("facebook帳號正在登錄");
        }

        relativeLayout.setVisibility(View.GONE);
        autoLoginLayout.setVisibility(View.VISIBLE);
        count = 3;
        autoLoginWaitTime.setText("(" + count +  ")");

        autoLoginTimer = new Timer();//delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。

        autoLoginTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                sLoginActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        autoLoginWaitTime.setText("(" + count +  ")");

                        if (count == 0){

                            if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, loginType)) {
                                autoLogin(sLoginActivity, account, password);

                            }else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, loginType)){
                                fbThirdLogin(sLoginActivity, account, password);
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

    private void autoLogin(final SLoginActivity sLoginActivity, final String account, final String password) {


        AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(sLoginActivity, account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(sLoginActivity, "Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
//                        ToastUtils.toast(sLoginActivity,R.string.py_login_success);
                        StarPyUtil.saveAccount(sLoginActivity, account);
                        StarPyUtil.savePassword(sLoginActivity, password);
//                        StarPyUtil.saveSdkLoginData(sLoginActivity,sLoginResponse.getRawResponse());

                        sLoginActivity.handleRegisteOrLoginSuccess(sLoginResponse, rawResult, SLoginType.LOGIN_TYPE_STARPY);
//                        sLoginActivity.finish();
                    } else {
                        goCommomLogin();
                    }
                } else {

                    goCommomLogin();
                }
            }

            @Override
            public void timeout(String code) {

                goCommomLogin();

            }

            @Override
            public void noData() {

                goCommomLogin();

            }
        });
        accountLoginCmd.excute(SLoginResponse.class);

    }


    private void fbThirdLogin(final SLoginActivity sLoginActivity, String fbScopeId, String fbApps) {

        FBLoginRegRequestTask cmd = new FBLoginRegRequestTask(sLoginActivity,fbScopeId,fbApps,FbSp.getTokenForBusiness(sLoginActivity));
        cmd.setLoadDialog(DialogUtil.createLoadingDialog(sLoginActivity, "Loading..."));
        cmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {

                    if (sLoginResponse.isRequestSuccess()){

//                        ToastUtils.toast(getActivity(), R.string.py_login_success);
                          sLoginActivity.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_FB);
//                        StarPyUtil.saveSdkLoginData(getContext(),rawResult);
//                        getActivity().finish();
                    }else{
                        goCommomLogin();
                    }
                } else {
                    goCommomLogin();
                }
            }

            @Override
            public void timeout(String code) {
                goCommomLogin();
            }

            @Override
            public void noData() {
                goCommomLogin();
            }
        });
        cmd.excute(SLoginResponse.class);

    }
}
