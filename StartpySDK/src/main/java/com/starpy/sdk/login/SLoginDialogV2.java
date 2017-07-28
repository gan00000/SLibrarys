package com.starpy.sdk.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.sfb.SFacebookProxy;
import com.starpy.base.bean.SLoginType;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.SBaseDialog;
import com.starpy.sdk.login.p.LoginPresenterImpl;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;
import com.starpy.sdk.login.widget.v2.AccountBindAccountLayoutV2;
import com.starpy.sdk.login.widget.v2.AccountChangePwdLayoutV2;
import com.starpy.sdk.login.widget.v2.AccountFindPwdLayoutV2;
import com.starpy.sdk.login.widget.v2.AccountInjectionLayoutV2;
import com.starpy.sdk.login.widget.v2.AccountRegisterLayoutV2;
import com.starpy.sdk.login.widget.v2.AccountRegisterTermsLayoutV2;
import com.starpy.sdk.login.widget.v2.MainLoginLayoutV2;
import com.starpy.sdk.login.widget.v2.PyAccountLoginV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gan on 2017/4/12.
 */

public class SLoginDialogV2 extends SBaseDialog implements LoginContract.ILoginView{

    private Context context;
    private Activity activity;

    private FrameLayout rootFrameLayout;
    private FrameLayout contentFrameLayout;
    private View autoLoginPage;

    //    自動登錄頁面控件
    private RelativeLayout autoLoginLayout;
    private TextView autoLoginTips;
    private TextView autoLoginWaitTime;
    private TextView autoLoginChangeAccount;

    private SLoginBaseRelativeLayout mainLoginView;
    private SLoginBaseRelativeLayout accountLoginView;
    private SLoginBaseRelativeLayout registerView;
    private SLoginBaseRelativeLayout registerTermsView;
    private SLoginBaseRelativeLayout changePwdView;
    private SLoginBaseRelativeLayout findPwdView;
    private SLoginBaseRelativeLayout bindUniqueView;
    private SLoginBaseRelativeLayout bindFbView;
    private SLoginBaseRelativeLayout injectionView;

    private List<SLoginBaseRelativeLayout> viewPageList;

    private SFacebookProxy sFacebookProxy;

    private LoginContract.ILoginPresenter iLoginPresenter;

    private ILoginCallBack iLoginCallBack;

    public SLoginDialogV2(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SLoginDialogV2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected SLoginDialogV2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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

        contentFrameLayout = new FrameLayout(context);
        contentFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

        autoLoginLayout = new RelativeLayout(context);
        autoLoginPage = getLayoutInflater().inflate(R.layout.v2_py_auto_login_loading,null,false);
        autoLoginLayout.addView(autoLoginPage,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));

        rootFrameLayout = new FrameLayout(context);
        rootFrameLayout.addView(contentFrameLayout);
        rootFrameLayout.addView(autoLoginLayout);

        setContentView(rootFrameLayout);

        viewPageList = new ArrayList<>();

        /*mainLoginView = new MainLoginLayoutV2(context);
        accountLoginView = new PyAccountLoginV2(context);
        registerView = new AccountRegisterLayoutV2(context);
        registerTermsView = new AccountRegisterTermsLayoutV2(context);
        changePwdView = new AccountChangePwdLayoutV2(context);


        viewPageList.add(mainLoginView);
        viewPageList.add(accountLoginView);
        viewPageList.add(registerView);
        viewPageList.add(registerTermsView);
        viewPageList.add(changePwdView);


        for (SLoginBaseRelativeLayout childView : viewPageList) {
            if (childView != null){
                childView.setLoginDialogV2(this);
                contentFrameLayout.addView(childView);
            }
        }*/

        toMainLoginView();

        initAutoLoginView();


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
        super.onBackPressed();

    }

    public void popBackStack(SLoginBaseRelativeLayout baseRelativeLayout) {

    }


    public void toMainLoginView() {
        if (mainLoginView == null || !viewPageList.contains(mainLoginView)){
            mainLoginView = new MainLoginLayoutV2(context);
            mainLoginView.setLoginDialogV2(this);
            contentFrameLayout.addView(mainLoginView);
            viewPageList.add(mainLoginView);
        }
        for (View childView : viewPageList) {
            if (childView == null){
                continue;
            }
            if (childView == mainLoginView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }
    }
    public void toAccountLoginView() {

        if (accountLoginView == null || !viewPageList.contains(accountLoginView)){
            accountLoginView = new PyAccountLoginV2(context);
            accountLoginView.setLoginDialogV2(this);
            contentFrameLayout.addView(accountLoginView);
            viewPageList.add(accountLoginView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == accountLoginView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }
    }

    public void toRegisterView(int from) {

        if (registerView == null || !viewPageList.contains(registerView)){
            registerView = new AccountRegisterLayoutV2(context);
            registerView.setLoginDialogV2(this);
            contentFrameLayout.addView(registerView);
            viewPageList.add(registerView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == registerView){
                if (from > 0) {
                    registerView.from = from;
                }
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }
    }
    public void toRegisterTermsView(int from) {

        if (registerTermsView == null || !viewPageList.contains(registerTermsView)){
            registerTermsView = new AccountRegisterTermsLayoutV2(context);
            registerTermsView.setLoginDialogV2(this);
            contentFrameLayout.addView(registerTermsView);
            viewPageList.add(registerTermsView);
        }
        registerTermsView.from = from;
        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == registerTermsView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }
    }

    public void toChangePwdView() {

        if (changePwdView == null || !viewPageList.contains(changePwdView)){
            changePwdView = new AccountChangePwdLayoutV2(context);
            changePwdView.setLoginDialogV2(this);
            contentFrameLayout.addView(changePwdView);
            viewPageList.add(changePwdView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == changePwdView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }

    }

    public void toFindPwdView() {

        if (findPwdView == null || !viewPageList.contains(findPwdView)){
            findPwdView = new AccountFindPwdLayoutV2(context);
            findPwdView.setLoginDialogV2(this);
            contentFrameLayout.addView(findPwdView);
            viewPageList.add(findPwdView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == findPwdView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }

    }

    public void toInjectionView() {

        if (injectionView == null || !viewPageList.contains(injectionView)){
            injectionView = new AccountInjectionLayoutV2(context);
            injectionView.setLoginDialogV2(this);
            contentFrameLayout.addView(injectionView);
            viewPageList.add(injectionView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == injectionView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }

    }

    public void toBindUniqueView() {

        if (bindUniqueView == null || !viewPageList.contains(bindUniqueView)){
            bindUniqueView = new AccountBindAccountLayoutV2(context);
            ((AccountBindAccountLayoutV2)bindUniqueView).setBindTpye(SLoginType.bind_unique);
            bindUniqueView.setLoginDialogV2(this);
            contentFrameLayout.addView(bindUniqueView);
            viewPageList.add(bindUniqueView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == bindUniqueView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }

    }

    public void toBindFbView() {

        if (bindFbView == null || !viewPageList.contains(bindFbView)){
            bindFbView = new AccountBindAccountLayoutV2(context);
            ((AccountBindAccountLayoutV2)bindFbView).setBindTpye(SLoginType.bind_fb);
            bindFbView.setLoginDialogV2(this);
            contentFrameLayout.addView(bindFbView);
            viewPageList.add(bindFbView);
        }

        for (View childView : viewPageList) {

            if (childView == null){
                continue;
            }

            if (childView == bindFbView){
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }

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
        contentFrameLayout.setVisibility(View.GONE);
        autoLoginLayout.setVisibility(View.VISIBLE);

    }


    @Override
    public void showLoginView() {
        contentFrameLayout.setVisibility(View.VISIBLE);
        autoLoginLayout.setVisibility(View.GONE);
        if (iLoginPresenter.hasAccountLogin()){
            toAccountLoginView();
        }else{
            toMainLoginView();
        }
    }

    @Override
    public void changePwdSuccess(SLoginResponse sLoginResponse) {
        toAccountLoginView();
    }

    @Override
    public void showAutoLoginWaitTime(String time) {
        autoLoginWaitTime.setText(time);
    }

    public void setLoginCallBack(ILoginCallBack iLoginCallBack) {
        this.iLoginCallBack = iLoginCallBack;
    }

    @Override
    public void findPwdSuccess(SLoginResponse sLoginResponse) {

    }

    @Override
    public void accountBindSuccess(SLoginResponse sLoginResponse) {

    }
}