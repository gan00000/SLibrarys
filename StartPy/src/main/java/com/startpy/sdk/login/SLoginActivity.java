package com.startpy.sdk.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

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
//        sFacebookProxy = new SFacebookProxy(this.getApplicationContext());

        fragmentManager.beginTransaction().replace(relativeLayout.getId(),new AccountLoginMainFragment()).commit();

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
