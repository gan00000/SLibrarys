package com.starpy.sdk.login.widget.v2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;

/**
 * Created by GanYuanrong on 2017/2/6.
 */

public class MainLoginMainLandLayoutV extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;

    private View guestLoginView, regLoginView, loginLoginView, wxLoginView;


    public MainLoginMainLandLayoutV(Context context) {
        super(context);
    }

    public MainLoginMainLandLayoutV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLoginMainLandLayoutV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    public View onCreateView(LayoutInflater inflater) {

        contentView = inflater.inflate(R.layout.v2_main_login_page_mianland, null);

        guestLoginView = contentView.findViewById(R.id.btn_login_guest_id);
        loginLoginView = contentView.findViewById(R.id.btn_login_login_id);
        regLoginView = contentView.findViewById(R.id.btn_login_reg_id);

        wxLoginView = contentView.findViewById(R.id.btn_login_wx_id);

        guestLoginView.setOnClickListener(this);
        loginLoginView.setOnClickListener(this);
        regLoginView.setOnClickListener(this);
        wxLoginView.setOnClickListener(this);

        return contentView;
    }



    @Override
    public void onClick(View v) {

        if (v == loginLoginView){
            sLoginDialogv2.toAccountLoginView();

        }else if (v == regLoginView) {

            sLoginDialogv2.toRegisterView(2);
        }else if(v == guestLoginView){
            sLoginDialogv2.getLoginPresenter().macLogin(sLoginDialogv2.getActivity());
        }else if (v == wxLoginView){
            //微信登录
            sLoginDialogv2.getLoginPresenter().wxLogin(sLoginDialogv2.getActivity());



        }
    }


}
