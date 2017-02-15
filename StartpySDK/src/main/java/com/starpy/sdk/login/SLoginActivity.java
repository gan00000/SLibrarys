package com.starpy.sdk.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

import com.core.base.utils.PL;
import com.core.base.utils.SignatureUtil;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.base.cfg.SConfig;
import com.starpy.data.login.response.SLoginResponse;
import com.startpy.sdk.R;
import com.starpy.sdk.login.fragment.AccountLoginMainFragment;
import com.starpy.sdk.login.fragment.BaseFragment;
import com.core.base.utils.FragmentUtil;

public class SLoginActivity extends BaseLoginActivity {

    public static final int S_LOGIN_REQUEST = 600;
    public static final int S_LOGIN_RESULT = 700;
    public static final String S_LOGIN_RESPONSE_OBJ = "S_LOGIN_RESPONSE_OBJ";

//    private SLoginResponse loginResponse;

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

//        setResult();
    }

    public void setResult(SLoginResponse loginResponse) {
        if (loginResponse != null){
            loginResponse.setGameCode(SConfig.getGameCode(this));
            Intent data = new Intent();

            data.putExtra(S_LOGIN_RESPONSE_OBJ, loginResponse);
            setResult(S_LOGIN_RESULT,data);
        }
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
