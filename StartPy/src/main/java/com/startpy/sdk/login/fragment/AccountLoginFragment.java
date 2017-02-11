package com.startpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.SStringUtil;
import com.facebook.s.SFacebookProxy;
import com.core.base.utils.PL;
import com.starpy.model.login.bean.SLoginResponse;
import com.starpy.model.login.bean.request.MacLoginRegRequest;
import com.starpy.model.login.execute.MacLoginRegCmd;
import com.startpy.sdk.R;
import com.startpy.sdk.utils.DialogUtil;
import com.startpy.sdk.utils.ToastUtils;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountLoginFragment extends BaseFragment implements View.OnClickListener {

    private View contentView;

    private View fbLoginView, starLoginView, macLoginView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.py_account_login, container, false);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.py_account_login_fb:
                sFbLogin();
                break;
            case R.id.py_account_login_star:
                sLoginActivity.replaceFragment(new AccountLoginMainFragment());
                break;
            case R.id.py_account_login_mac:
                macLogin();
                break;
            default:
        }
    }

    private void macLogin() {

        MacLoginRegCmd macLoginRegCmd = new MacLoginRegCmd(getActivity());
        macLoginRegCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        macLoginRegCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void callBack(SLoginResponse sLoginResponse) {
                if (sLoginResponse != null && (sLoginResponse.isRequestSuccess() || SStringUtil.isEqual("1001", sLoginResponse.getCode()))) {
                    ToastUtils.toast(getActivity(), R.string.py_login_success);
                } else {
                    ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                }
            }
        });
        macLoginRegCmd.excute(SLoginResponse.class);

    }

    private void sFbLogin() {
        if (sLoginActivity.getsFacebookProxy() == null) {
            return;
        }
        sLoginActivity.getsFacebookProxy().fbLogin(sLoginActivity, new SFacebookProxy.EfunFbLoginCallBack() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(SFacebookProxy.User user) {
                PL.d("fb uid:" + user.getUserId());
                requestBusinessId();
            }
        });
    }

    private void requestBusinessId(){

        sLoginActivity.getsFacebookProxy().requestBusinessId(sLoginActivity, new SFacebookProxy.EfunFbBusinessIdCallBack() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String businessId) {
                PL.d("fb businessId:" + businessId);
            }
        });

    }
}
