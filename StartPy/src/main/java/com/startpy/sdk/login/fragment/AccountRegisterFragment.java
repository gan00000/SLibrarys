package com.startpy.sdk.login.fragment;

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
import com.core.base.request.EfunCommandCallBack;
import com.core.base.request.STaskExecutor;
import com.core.base.request.command.abstracts.EfunCommand;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.SLoginResponse;
import com.starpy.model.login.bean.request.AccountRegRequest;
import com.starpy.model.login.execute.AccountRegisterCmd;
import com.startpy.sdk.R;
import com.startpy.sdk.utils.DialogUtil;
import com.startpy.sdk.utils.StarPyUtil;
import com.startpy.sdk.utils.ToastUtils;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountRegisterFragment extends BaseFragment implements View.OnClickListener {

    private View contentView;
    private ImageView termsSelectImageView, eyeImageView;
    private TextView termsTextView,registerConfirm;

    private EditText registerPasswordEditText,registerAccountEditText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.py_account_register, container, false);

        backView = contentView.findViewById(R.id.py_back_button);

        termsSelectImageView = (ImageView) contentView.findViewById(R.id.py_register_account_terms_check);
        termsTextView = (TextView) contentView.findViewById(R.id.py_register_terms_text_id);


        eyeImageView = (ImageView) contentView.findViewById(R.id.py_eye_imageview_id);
        registerPasswordEditText = (EditText) contentView.findViewById(R.id.py_register_password);
        registerAccountEditText = (EditText) contentView.findViewById(R.id.py_register_account);

        registerConfirm = (TextView)contentView.findViewById(R.id.py_register_account_confirm);

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
        switch (v.getId()) {
            case R.id.py_register_account_terms_check:

                if (termsSelectImageView.isSelected()){
                    termsSelectImageView.setSelected(false);
                }else{
                    termsSelectImageView.setSelected(true);
                }

                break;
            case R.id.py_register_terms_text_id:
                sLoginActivity.replaceFragmentBackToStack(new AccountRegisterTermsFragment());
                break;
            case R.id.py_register_account_confirm:
                register();
                break;
            case R.id.py_back_button:
                sLoginActivity.popBackStack();
                break;
            case R.id.py_eye_imageview_id:

                if (eyeImageView.isSelected()){
                    eyeImageView.setSelected(false);
                    // 显示为密码
                    registerPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    eyeImageView.setSelected(true);
                    // 显示为普通文本
                    registerPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // 使光标始终在最后位置
                Editable etable = registerPasswordEditText.getText();
                Selection.setSelection(etable, etable.length());

                break;
            default:
        }
    }

    private void register() {

        String account = registerAccountEditText.getEditableText().toString();
        if (TextUtils.isEmpty(account)){
            ToastUtils.toast(getActivity(),R.string.py_account_empty);
            return;
        }
        account = account.trim();

        String password = registerPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)){
            ToastUtils.toast(getActivity(),R.string.py_password_empty);
            return;
        }
        password = password.trim();

        if (!termsSelectImageView.isSelected()){
            ToastUtils.toast(getActivity(),R.string.py_select_terms);
        }

        if (SStringUtil.isEqual(account, password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_equal_account);
            return;
        }

        if (!StarPyUtil.checkAccount(account)){
            ToastUtils.toast(getActivity(),R.string.py_account_error);
            return;
        }
        if (!StarPyUtil.checkPassword(password)){
            ToastUtils.toast(getActivity(),R.string.py_password_error);
            return;
        }

        AccountRegisterCmd accountRegisterCmd = new AccountRegisterCmd(getActivity(),account,password);
        accountRegisterCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(),"Loading..."));
        accountRegisterCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void callBack(SLoginResponse accountRegRequest) {
                if (accountRegRequest != null && accountRegRequest.isRequestSuccess()){
                    ToastUtils.toast(getActivity(),R.string.py_register_success);
                }else{
                    ToastUtils.toast(getActivity(),accountRegRequest.getMessage());
                }
            }

        });
        accountRegisterCmd.excute(SLoginResponse.class);


    }
}
