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

import com.starpy.base.task.EfunCommandCallBack;
import com.starpy.base.task.STaskExecutor;
import com.starpy.base.task.command.abstracts.EfunCommand;
import com.starpy.model.login.execute.EfunUserRegisterCmd;
import com.startpy.sdk.R;

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
        termsSelectImageView = (ImageView) contentView.findViewById(R.id.py_register_account_terms_check);
        termsTextView = (TextView) contentView.findViewById(R.id.py_register_terms_text_id);

        backView = contentView.findViewById(R.id.py_back_button);
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

            return;
        }
        account = account.trim();

        String password = registerPasswordEditText.getEditableText().toString();
        if (TextUtils.isEmpty(password)){

            return;
        }
        password = password.trim();

        EfunUserRegisterCmd efunUserRegisterCmd = new EfunUserRegisterCmd(getActivity(),account,password,"","","android");
        efunUserRegisterCmd.setAppKey("dajajd");
        efunUserRegisterCmd.setGameCode("aaa");
        efunUserRegisterCmd.setPreferredUrl("10:10:10:106:8080/");
        efunUserRegisterCmd.setCallback(new EfunCommandCallBack() {
            @Override
            public void cmdCallBack(EfunCommand command) {

            }
        });
        STaskExecutor.getInstance().asynExecute(getActivity(),efunUserRegisterCmd);


    }
}
