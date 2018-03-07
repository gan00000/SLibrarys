package com.starpy.sdk.login.widget.v2;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.base.utils.ToastUtils;
import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;

/**
 * 账号注入
 */
public class AccountInjectionLayoutV2 extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;

    private TextView injectionTextView;

    private EditText registerPasswordEditText, registerAccountEditText,registerUidEditText;

    private String account;
    private String password;

    public AccountInjectionLayoutV2(Context context) {
        super(context);
    }

    public AccountInjectionLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountInjectionLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    private View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.v2_account_injection, null);

        backView = contentView.findViewById(R.id.py_back_button_v2);

        registerPasswordEditText = (EditText) contentView.findViewById(R.id.py_register_password);
        registerAccountEditText = (EditText) contentView.findViewById(R.id.py_register_account);
        registerUidEditText = (EditText) contentView.findViewById(R.id.py_register_account_uid);

        injectionTextView = (TextView) contentView.findViewById(R.id.py_register_account_confirm);

        backView.setOnClickListener(this);
        injectionTextView.setOnClickListener(this);

        return contentView;
    }


    @Override
    public void onClick(View v) {

        if (v == injectionTextView) {
            injectionAccount();
        } else if (v == backView) {//返回键

            sLoginDialogv2.toAccountLoginView();

        }

    }

    private void injectionAccount() {

        account = registerAccountEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }

        password = registerPasswordEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_empty);
            return;
        }

        String uid = registerUidEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(uid)) {
            ToastUtils.toast(getTheContext(), "user id 不能为空");
            return;
        }

        sLoginDialogv2.getLoginPresenter().accountInject(sLoginDialogv2.getActivity(), account, password, uid);
    }


}
