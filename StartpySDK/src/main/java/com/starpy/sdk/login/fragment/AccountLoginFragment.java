package com.starpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.SStringUtil;
import com.facebook.sfb.FbSp;
import com.facebook.sfb.SFacebookProxy;
import com.core.base.utils.PL;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.FBLoginRegRequestTask;
import com.starpy.data.login.execute.MacLoginRegRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.startpy.sdk.R;
import com.starpy.sdk.utils.DialogUtil;
import com.core.base.utils.ToastUtils;

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

        if (v == fbLoginView){
//            sFbLogin();
            fbThirdLogin(FbSp.getFbId(getActivity()),FbSp.getAppsBusinessId(getActivity()),"");
        }else if (v == starLoginView){

            sLoginActivity.replaceFragment(new AccountLoginMainFragment());

        }else if(v == macLoginView){
            macLogin();
        }
    }

    private void macLogin() {

        MacLoginRegRequestTask macLoginRegCmd = new MacLoginRegRequestTask(getActivity());
        macLoginRegCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        macLoginRegCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void callBack(SLoginResponse sLoginResponse,String rawResult) {
                if (sLoginResponse != null && (sLoginResponse.isRequestSuccess() || SStringUtil.isEqual("1001", sLoginResponse.getCode()))) {
                    ToastUtils.toast(getActivity(), R.string.py_login_success);
                    StarPyUtil.saveSdkLoginData(getContext(),rawResult);

                    sLoginActivity.setResult(sLoginResponse);
                    getActivity().finish();
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
                requestBusinessId(user.getUserId());
            }
        });
    }

    private void requestBusinessId(final String fbScopeId){

        sLoginActivity.getsFacebookProxy().requestBusinessId(sLoginActivity, new SFacebookProxy.EfunFbBusinessIdCallBack() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String businessId) {
                PL.d("fb businessId:" + businessId);
                fbThirdLogin(fbScopeId,businessId, FbSp.getTokenForBusiness(getActivity()));
            }
        });

    }

    private void fbThirdLogin(String fbScopeId, String fbApps, String fbTokenBusiness) {

        FBLoginRegRequestTask cmd = new FBLoginRegRequestTask(getActivity(),fbScopeId,fbApps,fbTokenBusiness);
        cmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        cmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void callBack(SLoginResponse sLoginResponse,String rawResult) {
                if (sLoginResponse != null && (sLoginResponse.isRequestSuccess() || SStringUtil.isEqual("1001", sLoginResponse.getCode()))) {
                    ToastUtils.toast(getActivity(), R.string.py_login_success);
                    sLoginActivity.setResult(sLoginResponse);
                    StarPyUtil.saveSdkLoginData(getContext(),rawResult);
                    getActivity().finish();
                } else {
                    ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                }
            }
        });
        cmd.excute(SLoginResponse.class);

    }

}
