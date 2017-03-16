package com.starpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.SStringUtil;
import com.starpy.data.login.constant.SLoginType;
import com.starpy.data.login.execute.AccountLoginRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.startpy.sdk.R;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.base.utils.StarPyUtil;
import com.core.base.utils.ToastUtils;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountLoginMainFragment extends BaseFragment {

    private View contentView;

    private TextView loginMainGoRegisterBtn, loginMainLoginBtn;

    private ImageView eyeImageView;
    private EditText loginPasswordEditText, loginAccountEditText;

    private String account;
    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.py_account_login_mian, container, false);

        backView = contentView.findViewById(R.id.py_back_button);
        loginMainGoRegisterBtn = (TextView) contentView.findViewById(R.id.py_login_main_register);
        eyeImageView = (ImageView) contentView.findViewById(R.id.py_eye_imageview_id);

        loginAccountEditText = (EditText) contentView.findViewById(R.id.py_account_login_main_account);
        loginPasswordEditText = (EditText) contentView.findViewById(R.id.py_account_login_main_password);

        loginMainLoginBtn = (TextView) contentView.findViewById(R.id.py_account_main_login);


        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginActivity.replaceFragment(new AccountLoginFragment());
            }
        });

        loginMainGoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginActivity.replaceFragmentBackToStack(new AccountRegisterFragment());
            }
        });

        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeImageView.isSelected()) {
                    eyeImageView.setSelected(false);
                    // 显示为密码
                    loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    eyeImageView.setSelected(true);
                    // 显示为普通文本
                    loginPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // 使光标始终在最后位置
                Editable etable = loginPasswordEditText.getText();
                Selection.setSelection(etable, etable.length());
            }
        });

        loginMainLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        account = StarPyUtil.getAccount(getContext());
        password = StarPyUtil.getPassword(getContext());
        if (TextUtils.isEmpty(account)){
            account = StarPyUtil.getMacAccount(getContext());
            password = StarPyUtil.getMacPassword(getContext());
        }
        if (!TextUtils.isEmpty(account)){
            loginAccountEditText.setText(account);
            loginPasswordEditText.setText(password);
        }

        return contentView;
    }

    private void login() {

        account = loginAccountEditText.getEditableText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getActivity(), R.string.py_account_empty);
            return;
        }
        account = account.trim();

        password = loginPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_empty);
            return;
        }
        password = password.trim();

        if (SStringUtil.isEqual(account, password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_equal_account);
            return;
        }

        if (!StarPyUtil.checkAccount(account)) {
            ToastUtils.toast(getActivity(), R.string.py_account_error);
            return;
        }
        if (!StarPyUtil.checkPassword(password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_error);
            return;
        }

        AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(getActivity(), account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(),"Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    if (sLoginResponse.isRequestSuccess()) {
//                        ToastUtils.toast(getActivity(),R.string.py_login_success);
                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
//                        StarPyUtil.saveSdkLoginData(getActivity(),sLoginResponse.getRawResponse());
//
                        sLoginActivity.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
//                        getActivity().finish();
                    }else{

                        ToastUtils.toast(getActivity(),sLoginResponse.getMessage());
                    }
                }else{
                    ToastUtils.toast(getActivity(),R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        accountLoginCmd.excute(SLoginResponse.class);

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


}
