package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.sfb.SFacebookProxy;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.FixedSpeedScroller;
import com.starpy.sdk.R;
import com.starpy.sdk.SBaseDialog;
import com.starpy.sdk.login.adapter.LoginAdapter;
import com.starpy.sdk.login.p.LoginPresenterImpl;
import com.starpy.sdk.login.widget.AccountLoginLayout;
import com.starpy.sdk.login.widget.AccountLoginMainLayout;
import com.starpy.sdk.login.widget.AccountRegisterLayout;
import com.starpy.sdk.login.widget.AccountRegisterTermsLayout;
import com.starpy.sdk.DepthPageTransformer;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;
import com.starpy.sdk.utils.ViewPageUitl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gan on 2017/4/12.
 */

public class SLoginDialog extends SBaseDialog implements LoginContract.ILoginView{

    private Context context;
    private Activity activity;

    private ViewPager loginViewPager;
    private View autoLoginPage;

    //    自動登錄頁面控件
    private RelativeLayout autoLoginLayout;
    private TextView autoLoginTips;
    private TextView autoLoginWaitTime;
    private TextView autoLoginChangeAccount;

    private SLoginBaseRelativeLayout loginView;
    private SLoginBaseRelativeLayout accountLoginView;
    private SLoginBaseRelativeLayout registerView;
    private SLoginBaseRelativeLayout registerTermsView;
    private List<SLoginBaseRelativeLayout> viewPageList;

    private SFacebookProxy sFacebookProxy;

    private LoginContract.ILoginPresenter iLoginPresenter;

    private ILoginCallBack iLoginCallBack;

    public SLoginDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SLoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected SLoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
        setCanceledOnTouchOutside(false);
        setFullScreen();
        iLoginPresenter = new LoginPresenterImpl();
        iLoginPresenter.setBaseView(this);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                iLoginPresenter.destory(activity);
            }
        });
    }

    public LoginContract.ILoginPresenter getLoginPresenter() {
        return iLoginPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.py_login_activity_main_2);

        initAutoLoginView();

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
        FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(context,new LinearOutSlowInInterpolator());
        fixedSpeedScroller.setmDuration(1000);

        ViewPageUitl.setScrollDuration(activity,loginViewPager,fixedSpeedScroller);

        loginViewPager.setAdapter(loginAdapter);

        iLoginPresenter.autoLogin(activity);

    }

    private void initAutoLoginView() {
        autoLoginLayout = (RelativeLayout) findViewById(R.id.py_auto_login_page);
        autoLoginTips = (TextView) findViewById(R.id.py_auto_login_tips);
        autoLoginWaitTime = (TextView) findViewById(R.id.py_auto_login_wait_time);
        autoLoginChangeAccount = (TextView) findViewById(R.id.py_auto_login_change);
        autoLoginChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iLoginPresenter.autoLoginChangeAccount(activity);
            }
        });
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

    public SFacebookProxy getFacebookProxy() {
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

    @Override
    public void LoginSuccess(SLoginResponse sLoginResponse) {
        if (iLoginCallBack != null){
            iLoginCallBack.onLogin(sLoginResponse);
        }
        this.dismiss();
    }

    @Override
    public void showAutoLoginTips(String tips) {
        autoLoginTips.setText(tips);
    }

    @Override
    public void showAutoLoginView() {
        loginViewPager.setVisibility(View.GONE);
        autoLoginLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoginView() {
        loginViewPager.setVisibility(View.VISIBLE);
        autoLoginLayout.setVisibility(View.GONE);
        if (iLoginPresenter.hasAccountLogin()){
           toAccountLoginView();
        }else{
            toLoginView();
        }
    }

    @Override
    public void showAutoLoginWaitTime(String time) {
        autoLoginWaitTime.setText(time);
    }

    public void setLoginCallBack(ILoginCallBack iLoginCallBack) {
        this.iLoginCallBack = iLoginCallBack;
    }
}
