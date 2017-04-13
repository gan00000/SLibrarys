package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.ads.StarEventLogger;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.SBaseDialog;
import com.starpy.sdk.login.adapter.LoginAdapter;
import com.starpy.sdk.login.widget.AccountLoginLayout;
import com.starpy.sdk.login.widget.AccountLoginMainLayout;
import com.starpy.sdk.login.widget.AccountRegisterLayout;
import com.starpy.sdk.login.widget.AccountRegisterTermsLayout;
import com.starpy.sdk.login.widget.DepthPageTransformer;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gan on 2017/4/12.
 */

public class SLoginDialog extends SBaseDialog {

    private Context context;
    private Activity activity;

    private ViewPager loginViewPager;
    private View autoLoginPage;


    private SLoginBaseRelativeLayout loginView;
    private SLoginBaseRelativeLayout accountLoginView;
    private SLoginBaseRelativeLayout registerView;
    private SLoginBaseRelativeLayout registerTermsView;
    private List<SLoginBaseRelativeLayout> viewPageList;

    private SFacebookProxy sFacebookProxy;

    public SLoginDialog(@NonNull Context context) {
        super(context);
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
        this.context = context;
    }

    public SLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
    }

    protected SLoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.py_login_activity_main_2);

        loginViewPager = (ViewPager) findViewById(R.id.py_login_viewpage_id);
        autoLoginPage = findViewById(R.id.py_auto_login_page);
        autoLoginPage.setVisibility(View.GONE);

        accountLoginView = new AccountLoginMainLayout(getContext());//starpy账号登录页面
        accountLoginView.setLoginDialog(this);

        loginView = new AccountLoginLayout(getContext());//多种账号登录页面
        loginView.setLoginDialog(this);

        registerView = new AccountRegisterLayout(getContext());//注册页面
        registerView.setLoginDialog(this);

        registerTermsView = new AccountRegisterTermsLayout(getContext());//服务条款页面
        registerTermsView.setLoginDialog(this);

        viewPageList = new ArrayList<>();
        viewPageList.add(loginView);
        viewPageList.add(accountLoginView);
        viewPageList.add(registerView);
        viewPageList.add(registerTermsView);

        LoginAdapter loginAdapter = new LoginAdapter(context, viewPageList);
        loginViewPager.setPageTransformer(true,new DepthPageTransformer());
        loginViewPager.setAdapter(loginAdapter);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        int currentIndex = loginViewPager.getCurrentItem();
        if (currentIndex > 0){
            loginViewPager.setCurrentItem(currentIndex - 1,true);
        }else{
            super.onBackPressed();
        }

    }


    public void popBackStack() {
        int currentIndex = loginViewPager.getCurrentItem();
        if (currentIndex > 0){
            loginViewPager.setCurrentItem(currentIndex - 1,true);
        }
    }

    public void toLoginView() {
        loginViewPager.setCurrentItem(viewPageList.indexOf(loginView), true);
    }
    public void toAccountLoginView() {
        loginViewPager.setCurrentItem(viewPageList.indexOf(accountLoginView), true);
    }
    public void toRegisterView() {
        loginViewPager.setCurrentItem(viewPageList.indexOf(registerView), true);
    }
    public void toRegisterTermsView() {
        loginViewPager.setCurrentItem(viewPageList.indexOf(registerTermsView), true);
    }

    public SFacebookProxy getsFacebookProxy() {
        return sFacebookProxy;
    }

    public void setsFacebookProxy(SFacebookProxy sFacebookProxy) {
        this.sFacebookProxy = sFacebookProxy;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void handleRegisteOrLoginSuccess(SLoginResponse loginResponse, String rawResult, String loginType) {
        try {
            StarPyUtil.saveSdkLoginData(getContext(), loginResponse.getRawResponse());

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

//        setResult(loginResponse);

        StarPyUtil.savePreviousLoginType(activity, loginType);

    /*    if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_STARPY, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_MAC, loginType)) {

        } else if (SStringUtil.isEqual(SLoginType.LOGIN_TYPE_FB, loginType)) {

        }*/

        ToastUtils.toast(activity, R.string.py_login_success);

        this.dismiss();
    }
}
