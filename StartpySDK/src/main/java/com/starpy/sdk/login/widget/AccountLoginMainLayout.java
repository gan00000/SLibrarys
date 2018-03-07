package com.starpy.sdk.login.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;

/**
 * Created by GanYuanrong on 2017/2/6.
 */

public class AccountLoginMainLayout extends SLoginBaseRelativeLayout {

    private View contentView;

    private TextView loginMainGoRegisterBtn, loginMainLoginBtn;

    private ImageView eyeImageView;
    private EditText loginPasswordEditText, loginAccountEditText;

    private String account;
    private String password;

    public AccountLoginMainLayout(Context context) {
        super(context);

    }


    public AccountLoginMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountLoginMainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    public View onCreateView(LayoutInflater inflater) {

        contentView = inflater.inflate(R.layout.py_account_login_mian, null);

        backView = contentView.findViewById(R.id.py_back_button);
        loginMainGoRegisterBtn = (TextView) contentView.findViewById(R.id.py_login_main_register);
        eyeImageView = (ImageView) contentView.findViewById(R.id.py_eye_imageview_id);

        loginAccountEditText = (EditText) contentView.findViewById(R.id.py_account_login_main_account);
        loginPasswordEditText = (EditText) contentView.findViewById(R.id.py_account_login_main_password);

        loginMainLoginBtn = (TextView) contentView.findViewById(R.id.py_account_main_login);


        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sLoginActivity.replaceFragment(new AccountLoginFragment());
                sLoginDialog.toLoginView();
            }
        });

        loginMainGoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sLoginActivity.replaceFragmentBackToStack(new AccountRegisterFragment());

                sLoginDialog.toRegisterView();
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
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }
        account = account.trim();

        password = loginPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_empty);
            return;
        }
        password = password.trim();

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

        sLoginDialog.getLoginPresenter().starpyAccountLogin(sLoginDialog.getActivity(),account,password);
     /*   AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(getTheContext(), account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(),"Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    if (sLoginResponse.isRequestSuccess()) {
                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
                        sLoginDialog.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                    }else{

                        ToastUtils.toast(getTheContext(),sLoginResponse.getMessage());
                    }
                }else{
                    ToastUtils.toast(getTheContext(),R.string.py_error_occur);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        accountLoginCmd.excute(SLoginResponse.class);*/

    }


}
