package com.starpy.sdk.login.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.starpy.sdk.R;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountLoginLayout extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;

    private View fbLoginView, starLoginView, macLoginView;

    public AccountLoginLayout(Context context) {
        super(context);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountLoginLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    public View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.py_account_login, null);
        backView = contentView.findViewById(R.id.py_back_button);
        backView.setVisibility(View.GONE);

        fbLoginView = contentView.findViewById(R.id.py_account_login_fb);
        starLoginView = contentView.findViewById(R.id.py_account_login_star);
        macLoginView = contentView.findViewById(R.id.py_account_login_mac);

        fbLoginView.setOnClickListener(this);
        starLoginView.setOnClickListener(this);
        macLoginView.setOnClickListener(this);

        return contentView;
    }



    @Override
    public void onClick(View v) {

        if (v == fbLoginView){
            sLoginDialog.getLoginPresenter().fbLogin(sLoginDialog.getActivity(),sLoginDialog.getFacebookProxy());
        }else if (v == starLoginView) {

            sLoginDialog.toAccountLoginView();
        }else if(v == macLoginView){
            sLoginDialog.getLoginPresenter().macLogin(sLoginDialog.getActivity());
        }
    }


}
