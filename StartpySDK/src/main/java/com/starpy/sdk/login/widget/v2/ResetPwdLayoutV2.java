package com.starpy.sdk.login.widget.v2;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.base.bean.BaseReqeustBean;
import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.BaseLoginRequestTask;
import com.starpy.data.login.request.AccountBindPhoneEmailBean;
import com.starpy.data.login.request.AccountLoginRequestBean;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;
import com.starpy.sdk.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;


public class ResetPwdLayoutV2 extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;
    private TextView confirmTextView;
    private TextView getVfCodeTextView;

    private EditText newPasswordEditText, accountEditText,vfCodeEditText;

    private String account;
    private String password;


    public ResetPwdLayoutV2(Context context) {
        super(context);
    }

    public ResetPwdLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResetPwdLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    private View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.v2_set_new_password, null);

        backView = contentView.findViewById(R.id.py_back_button_v2);



        accountEditText = (EditText) contentView.findViewById(R.id.py_set_new_pwd_account);
        newPasswordEditText = (EditText) contentView.findViewById(R.id.py_set_new_pwd_password);
        vfCodeEditText = (EditText) contentView.findViewById(R.id.py_set_new_pwd_vf_code);

        getVfCodeTextView = contentView.findViewById(R.id.py_set_new_pwd_get_vf_code);

        confirmTextView = (TextView) contentView.findViewById(R.id.py_set_new_pwd_confirm);


        backView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);
        getVfCodeTextView.setOnClickListener(this);

        return contentView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (accountEditText != null){
            accountEditText.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {

       if (v == confirmTextView) {

            start(2);

        } else if (v == backView) {//返回键

            sLoginDialogv2.toAccountLoginView();

        } else if (v == getVfCodeTextView){
           start(1);
       }

    }

    private void start(int m) {

        account = accountEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }

        password = newPasswordEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_empty);
            return;
        }


        if (SStringUtil.isEqual(account, password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_equal_account);
            return;
        }

        if (!StarPyUtil.checkAccount(account)) {
            ToastUtils.toast(getTheContext(), R.string.py_account_error);
            return;
        }
        if (!StarPyUtil.checkPassword(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_error);
            return;
        }


        if (m == 1) {
            requestVfCode(account);//获取验证码
        }else {
            String vfCode = vfCodeEditText.getEditableText().toString().trim();
            if (TextUtils.isEmpty(vfCode)) {
                ToastUtils.toast(getTheContext(), R.string.plat_vf_code_not_empty);
                return;
            }
            requestResetPwd(account,password,vfCode);
        }

    }


    //獲取驗證碼
    private void requestVfCode(String name) {


        final AccountLoginRequestBean sSdkBaseRequestBean = new AccountLoginRequestBean(getTheContext());
        sSdkBaseRequestBean.setRequestMethod("acquireVfCode");

        sSdkBaseRequestBean.setName(name);

        BaseLoginRequestTask requestTask = new BaseLoginRequestTask(getTheContext()) {
            @Override
            public BaseReqeustBean createRequestBean() {
                super.createRequestBean();
                sSdkBaseRequestBean.setSignature(SStringUtil.toMd5(sSdkBaseRequestBean.getAppKey() + sSdkBaseRequestBean.getTimestamp()
                        + sSdkBaseRequestBean.getGameCode() + sSdkBaseRequestBean.getPhone()));
                return sSdkBaseRequestBean;
            }
        };
        requestTask.setSdkBaseRequestBean(sSdkBaseRequestBean);
        requestTask.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(), "Loading..."));
        requestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    PL.i(sLoginResponse.getMessage());
                    ToastUtils.toast(getTheContext(),sLoginResponse.getMessage());
                    if (sLoginResponse.isRequestSuccess()) {
                        startCountDownTimer();
                    }
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        requestTask.excute(SLoginResponse.class);
    }

    private Timer countdowncTimer;
    private int count = 60;
    //验证码倒计时
    private void startCountDownTimer() {
        //获取验证码
        countdowncTimer = new Timer();//delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。

        countdowncTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                sLoginDialogv2.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getVfCodeTextView.setText("(" + count +  ")");
                        getVfCodeTextView.setClickable(false);
                        if (count <= 0){

                            if (countdowncTimer != null){
                                countdowncTimer.cancel();
                            }
                            getVfCodeTextView.setClickable(true);
                            getVfCodeTextView.setText(getResources().getString(R.string.py_bind_phone_get_vf_code));
                        }
                        count--;
                    }
                });
            }
        },300,1000);
    }


    //綁定手機
    private void requestResetPwd(String account,String newPwd,String vfCode) {

        if (TextUtils.isEmpty(vfCode)) {
            ToastUtils.toast(getContext(), R.string.plat_vf_code_not_empty);
            return;
        }

        final AccountBindPhoneEmailBean sSdkBaseRequestBean = new AccountBindPhoneEmailBean(getContext());

        sSdkBaseRequestBean.setName(account);
        sSdkBaseRequestBean.setNewPwd(SStringUtil.toMd5(newPwd));

        sSdkBaseRequestBean.setVfCode(vfCode);

        sSdkBaseRequestBean.setRequestMethod("resetPwd");
        BaseLoginRequestTask requestTask = new BaseLoginRequestTask(getContext()) {
            @Override
            public BaseReqeustBean createRequestBean() {
                super.createRequestBean();
                sSdkBaseRequestBean.setSignature(SStringUtil.toMd5(sSdkBaseRequestBean.getAppKey() + sSdkBaseRequestBean.getTimestamp()
                        + sSdkBaseRequestBean.getName()
                        + sSdkBaseRequestBean.getGameCode()));
                return sSdkBaseRequestBean;
            }
        };
        requestTask.setSdkBaseRequestBean(sSdkBaseRequestBean);
        requestTask.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(), "Loading..."));
        requestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    PL.i(sLoginResponse.getMessage());
                    ToastUtils.toast(getContext(),sLoginResponse.getMessage());
                    if (sLoginResponse.isRequestSuccess()) {

                        ToastUtils.toast(getContext(),sLoginResponse.getMessage());

                        sLoginDialogv2.toAccountLoginView();
                    }
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        requestTask.excute(SLoginResponse.class);

    }

}
