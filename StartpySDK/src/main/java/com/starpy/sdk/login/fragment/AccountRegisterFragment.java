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
import com.starpy.base.bean.SLoginType;
import com.starpy.data.login.execute.AccountRegisterRequestTask;
import com.starpy.data.login.response.SLoginResponse;
import com.startpy.sdk.R;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.base.utils.StarPyUtil;
import com.core.base.utils.ToastUtils;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountRegisterFragment extends BaseFragment implements View.OnClickListener {

    private View contentView;
    private ImageView termsSelectImageView, eyeImageView;
    private TextView termsTextView, registerConfirm;

    private EditText registerPasswordEditText, registerAccountEditText;

    private String account;
    private String password;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.py_account_register, container, false);

        backView = contentView.findViewById(R.id.py_back_button);
        titleTextView = (TextView) contentView.findViewById(R.id.py_title_id);
        titleTextView.setText(getText(R.string.py_register_account));

        termsSelectImageView = (ImageView) contentView.findViewById(R.id.py_register_account_terms_check);
        termsTextView = (TextView) contentView.findViewById(R.id.py_register_terms_text_id);


        eyeImageView = (ImageView) contentView.findViewById(R.id.py_eye_imageview_id);
        registerPasswordEditText = (EditText) contentView.findViewById(R.id.py_register_password);
        registerAccountEditText = (EditText) contentView.findViewById(R.id.py_register_account);

        registerConfirm = (TextView) contentView.findViewById(R.id.py_register_account_confirm);

        termsSelectImageView.setSelected(true);

        termsSelectImageView.setOnClickListener(this);
        eyeImageView.setOnClickListener(this);
        termsTextView.setOnClickListener(this);
        backView.setOnClickListener(this);
        registerConfirm.setOnClickListener(this);

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

        if (v == termsSelectImageView) {
            if (termsSelectImageView.isSelected()) {
                termsSelectImageView.setSelected(false);
            } else {
                termsSelectImageView.setSelected(true);
            }
        } else if (v == termsTextView) {
            sLoginActivity.replaceFragmentBackToStack(new AccountRegisterTermsFragment());
        } else if (v == registerConfirm) {
            register();
        } else if (v == backView) {//返回键
            sLoginActivity.popBackStack();
        } else if (v == eyeImageView) {//密码眼睛

            if (eyeImageView.isSelected()) {
                eyeImageView.setSelected(false);
                // 显示为密码
                registerPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                eyeImageView.setSelected(true);
                // 显示为普通文本
                registerPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            // 使光标始终在最后位置
            Editable etable = registerPasswordEditText.getText();
            Selection.setSelection(etable, etable.length());

        }

    }

    private void register() {

        account = registerAccountEditText.getEditableText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getActivity(), R.string.py_account_empty);
            return;
        }
        account = account.trim();

        password = registerPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_empty);
            return;
        }
        password = password.trim();

        if (!termsSelectImageView.isSelected()) {
            ToastUtils.toast(getActivity(), R.string.py_select_terms);
        }

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

        AccountRegisterRequestTask accountRegisterCmd = new AccountRegisterRequestTask(getActivity(), account, password);
        accountRegisterCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(), "Loading..."));
        accountRegisterCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null) {
                    if (sLoginResponse.isRequestSuccess()) {
                        ToastUtils.toast(getActivity(), R.string.py_register_success);
                        sLoginActivity.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);

                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);

                        getActivity().finish();
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
        accountRegisterCmd.excute(SLoginResponse.class);


    }
}
