/*
package com.starpy.sdk.login.auto;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.SignatureUtil;
import com.om.starpy.thirdlib.facebook.FbSp;
import com.starpy.base.bean.SLoginType;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.AccountLoginRequestTask;
import com.starpy.data.login.execute.FBLoginRegRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.login.SLoginActivity;
import com.starpy.sdk.login.fragment.AccountLoginFragment;
import com.starpy.sdk.login.fragment.AccountLoginMainFragment;
import com.starpy.sdk.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

*/
/**
 * Created by gan on 2017/4/13.
 *//*


public class AutoLogin {


    private void login() {

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

        ThirdLoginRegRequestTask cmd = new ThirdLoginRegRequestTask(sLoginActivity,fbScopeId,fbApps, FbSp.getTokenForBusiness(sLoginActivity));
        cmd.setLoadDialog(DialogUtil.createLoadingDialog(sLoginActivity, "Loading..."));
        cmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {

                    if (sLoginResponse.isRequestSuccess()){

//                        ToastUtils.toast(getTheContext(), R.string.py_login_success);
                        sLoginActivity.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_FB);
//                        StarPyUtil.saveSdkLoginData(getContext(),rawResult);
//                        getTheContext().finish();
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
*/
