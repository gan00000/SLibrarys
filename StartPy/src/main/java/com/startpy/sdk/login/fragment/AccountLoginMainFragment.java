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

import com.core.base.task.EfunCommandCallBack;
import com.core.base.task.STaskExecutor;
import com.core.base.task.command.abstracts.EfunCommand;
import com.starpy.model.login.execute.AccountLoginCmd;
import com.startpy.sdk.R;
import com.startpy.sdk.utils.StarPyUtil;
import com.startpy.sdk.utils.ToastUtils;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountLoginMainFragment extends BaseFragment {

    private View contentView;

    private TextView loginRegisterBtn,loginMainLoginBtn;

    private ImageView eyeImageView;
    private EditText loginPasswordEditText, loginAccountEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.py_account_login_mian,container,false);

        backView = contentView.findViewById(R.id.py_back_button);
        loginRegisterBtn = (TextView) contentView.findViewById(R.id.py_login_main_register);
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

        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginActivity.replaceFragmentBackToStack(new AccountRegisterFragment());
            }
        });

        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeImageView.isSelected()){
                    eyeImageView.setSelected(false);
                    // 显示为密码
                    loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
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
        return contentView;
    }

    private void login() {

            String account = loginAccountEditText.getEditableText().toString();
            if (TextUtils.isEmpty(account)){
                ToastUtils.toast(getActivity(),R.string.py_account_empty);
                return;
            }
            account = account.trim();

            String password = loginPasswordEditText.getEditableText().toString();
            if (TextUtils.isEmpty(password)){
                ToastUtils.toast(getActivity(),R.string.py_password_empty);
                return;
            }
            password = password.trim();

            if (!StarPyUtil.checkAccount(account)){
                ToastUtils.toast(getActivity(),R.string.py_account_error);
                return;
            }
            if (!StarPyUtil.checkPassword(password)){
                ToastUtils.toast(getActivity(),R.string.py_password_error);
                return;
            }

        AccountLoginCmd accountLoginCmd = new AccountLoginCmd(getActivity(),account,password);
        accountLoginCmd.setCallback(new EfunCommandCallBack() {
                @Override
                public void cmdCallBack(EfunCommand command) {

                }
            });
            STaskExecutor.getInstance().asynExecute(getActivity(),accountLoginCmd);


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
