package com.startpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.startpy.sdk.R;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountLoginMainFragment extends BaseFragment {

    private View contentView;

    private View loginView;

    private TextView loginRegisterBtn;

    private ImageView eyeImageView;
    private EditText loginPasswordEditText, loginAccountEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.py_account_login_mian,container,false);

        loginView = contentView.findViewById(R.id.doLogin);
        backView = contentView.findViewById(R.id.py_back_button);
        loginRegisterBtn = (TextView) contentView.findViewById(R.id.py_login_register);
        eyeImageView = (ImageView) contentView.findViewById(R.id.py_eye_imageview_id);
        loginPasswordEditText = (EditText) contentView.findViewById(R.id.py_account_login_main_password);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingDialog != null){
                    loadingDialog.show();
                }
            }
        });

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


}
