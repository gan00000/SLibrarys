package com.starpy.sdk.login.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.BitmapUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.facebook.sfb.FbSp;
import com.facebook.sfb.SFacebookProxy;
import com.starpy.base.bean.SLoginType;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.FBLoginRegRequestTask;
import com.starpy.data.login.execute.MacLoginRegRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.utils.DialogUtil;

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
            sFbLogin();
        }else if (v == starLoginView){

//            sLoginActivity.replaceFragment(new AccountLoginMainFragment());
                sLoginDialog.toAccountLoginView();
        }else if(v == macLoginView){
            macLogin();
        }
    }

    private void macLogin() {

        MacLoginRegRequestTask macLoginRegCmd = new MacLoginRegRequestTask(getActivity());
        macLoginRegCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        macLoginRegCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
//                        ToastUtils.toast(getActivity(), R.string.py_login_success);
//                        StarPyUtil.saveSdkLoginData(getContext(),rawResult);

                        //1001 注册成功    1000登入成功
                        if (SStringUtil.isEqual("1001", sLoginResponse.getCode())) {
                            cteateUserImage(getActivity(),sLoginResponse.getFreeRegisterName(),sLoginResponse.getFreeRegisterPwd());
                        }

                        StarPyUtil.saveMacAccount(getContext(),sLoginResponse.getFreeRegisterName());
                        StarPyUtil.saveMacPassword(getContext(),sLoginResponse.getFreeRegisterPwd());
                        sLoginDialog.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_MAC);

                    }else {

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }

                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        macLoginRegCmd.excute(SLoginResponse.class);

    }

    private void cteateUserImage(Context context,String freeRegisterName, String freeRegisterPwd) {
        String appName = ApkInfoUtil.getApplicationName(context);
//        String text = "你登入" + appName + "的帳號和密碼如下:\n帳號:" + freeRegisterName + "\n" + "密碼:" + freeRegisterPwd;
        String text = String.format(context.getResources().getString(R.string.py_login_mac_tips), appName, freeRegisterName, freeRegisterPwd);
        PL.i("cteateUserImage:" + text);
        Bitmap bitmap = BitmapUtil.bitmapAddText(BitmapFactory.decodeResource(getResources(),R.drawable.image_bg),text);
        BitmapUtil.saveImageToGallery(getContext(),bitmap);
        ToastUtils.toast(context, getResources().getString(R.string.py_login_mac_saveimage_tips));
    }


    private void sFbLogin() {
        if (sLoginDialog.getsFacebookProxy() == null) {
            return;
        }
        sLoginDialog.getsFacebookProxy().fbLogin(sLoginDialog.getActivity(), new SFacebookProxy.EfunFbLoginCallBack() {
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

        sLoginDialog.getsFacebookProxy().requestBusinessId(sLoginDialog.getActivity(), new SFacebookProxy.EfunFbBusinessIdCallBack() {
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
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {

                    if (sLoginResponse.isRequestSuccess()){

                        sLoginDialog.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_FB);
                    }else{

                        ToastUtils.toast(getActivity(), sLoginResponse.getMessage());
                    }
                } else {
                    ToastUtils.toast(getActivity(), R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        cmd.excute(SLoginResponse.class);

    }


}
