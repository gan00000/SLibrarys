package com.startpy.sdk.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

import com.core.base.utils.PL;
import com.core.base.utils.SignatureUtil;
import com.facebook.s.SFacebookProxy;
import com.startpy.sdk.R;
import com.startpy.sdk.login.fragment.AccountLoginMainFragment;
import com.startpy.sdk.login.fragment.BaseFragment;
import com.startpy.sdk.utils.FragmentUtil;

public class SLoginActivity extends BaseLoginActivity {

    private FragmentManager fragmentManager;
    private RelativeLayout relativeLayout;
    private SFacebookProxy sFacebookProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.py_login_activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.fragment_content);

        fragmentManager = getSupportFragmentManager();

        //广告
        SFacebookProxy.activateApp(this);
        // 1.实例EfunFacebookProxy
        sFacebookProxy = new SFacebookProxy(this.getApplicationContext());
        // 2.初始化fb sdk
        sFacebookProxy.initFbSdk(this);

        fragmentManager.beginTransaction().replace(relativeLayout.getId(),new AccountLoginMainFragment()).commit();

        PL.i(SignatureUtil.getHashKey(this,getPackageName()));

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
    }

    public void replaceFragment(BaseFragment newFragment){
        FragmentUtil.replace(fragmentManager,relativeLayout.getId(),newFragment);
    }

    public void replaceFragmentBackToStack(BaseFragment newFragment){
        FragmentUtil.replaceBackStack(fragmentManager,relativeLayout.getId(),newFragment);
    }
    public void popBackStack(){
        FragmentUtil.popBackStack(fragmentManager);
    }

    public SFacebookProxy getsFacebookProxy() {
        return sFacebookProxy;
    }
}
