package com.starpy.sdk.login.widget.v2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;

/**
 * Created by Efun on 2017/2/6.
 */

public class XMMainLoginLayoutV2 extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;

    private View fbLoginView, starLoginView, macLoginView, googleLoginView;
    private View starpyRegView;

    public XMMainLoginLayoutV2(Context context) {
        super(context);
    }

    public XMMainLoginLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XMMainLoginLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    public View onCreateView(LayoutInflater inflater) {

        contentView = inflater.inflate(R.layout.v2_main_login_page_xm, null);

        fbLoginView = contentView.findViewById(R.id.btn_xm_facebook_login_iv);
        starLoginView = contentView.findViewById(R.id.btn_xm_member_login_iv);
        macLoginView = contentView.findViewById(R.id.btn_xm_guest_login_iv);

        starpyRegView = contentView.findViewById(R.id.btn_xm_member_register_iv);
        googleLoginView = contentView.findViewById(R.id.btn_xm_google_login_iv);

        fbLoginView.setOnClickListener(this);
        starLoginView.setOnClickListener(this);
        macLoginView.setOnClickListener(this);
        starpyRegView.setOnClickListener(this);
        googleLoginView.setOnClickListener(this);

        return contentView;
    }



    @Override
    public void onClick(View v) {

        if (v == fbLoginView){
            sLoginDialogv2.getLoginPresenter().fbLogin(sLoginDialogv2.getActivity(),sLoginDialogv2.getFacebookProxy());
        }else if (v == starLoginView) {

            sLoginDialogv2.toAccountLoginView();
        }else if(v == macLoginView){
            sLoginDialogv2.getLoginPresenter().macLogin(sLoginDialogv2.getActivity());
        }else if (v == starpyRegView){
            sLoginDialogv2.toRegisterView(1);
        }else if (v == googleLoginView){
            //google+登录
            sLoginDialogv2.getLoginPresenter().googleLogin(sLoginDialogv2.getActivity(),sLoginDialogv2.getsGoogleSignIn());

        }
    }


}
