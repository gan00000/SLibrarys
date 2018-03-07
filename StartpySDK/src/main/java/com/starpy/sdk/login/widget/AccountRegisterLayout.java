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

public class AccountRegisterLayout extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;
    private ImageView termsSelectImageView, eyeImageView;
    private TextView termsTextView, registerConfirm;

    private EditText registerPasswordEditText, registerAccountEditText;

    private String account;
    private String password;

    public AccountRegisterLayout(Context context) {
        super(context);
    }

    public AccountRegisterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountRegisterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    private View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.py_account_register, null);

        backView = contentView.findViewById(R.id.py_back_button);
        titleTextView = (TextView) contentView.findViewById(R.id.py_title_id);
        titleTextView.setText(getResources().getText(R.string.py_register_account));

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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (registerAccountEditText != null){
            registerAccountEditText.requestFocus();
        }
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

            sLoginDialog.toRegisterTermsView();

        } else if (v == registerConfirm) {
            register();
        } else if (v == backView) {//返回键
//            sLoginActivity.popBackStack();
            sLoginDialog.popBackStack();
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
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }
        account = account.trim();

        password = registerPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_empty);
            return;
        }
        password = password.trim();

        if (!termsSelectImageView.isSelected()) {
            ToastUtils.toast(getTheContext(), R.string.py_select_terms);
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

        sLoginDialog.getLoginPresenter().register(sLoginDialog.getActivity(), account, password,"");
    }


}
