package com.startpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starpy.base.StarPyUtil;
import com.startpy.sdk.R;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountRegisterTermsFragment extends BaseFragment{

    private View contentView;
    private TextView termsTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.py_account_register_terms, container, false);
        backView = contentView.findViewById(R.id.py_back_button);

        termsTextView = (TextView) contentView.findViewById(R.id.py_terms_text_id);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginActivity.popBackStack();
            }
        });

        String serverTermsContent = StarPyUtil.getSdkLoginTerms(getContext());//优先设置服务器获取的配置
        if (!TextUtils.isEmpty(serverTermsContent)){
            termsTextView.setText(serverTermsContent);
        }

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


}
