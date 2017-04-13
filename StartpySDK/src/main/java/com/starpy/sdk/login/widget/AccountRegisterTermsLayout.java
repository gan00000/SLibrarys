package com.starpy.sdk.login.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;

/**
 * Created by Efun on 2017/2/6.
 */

public class AccountRegisterTermsLayout extends SLoginBaseRelativeLayout {

    private View contentView;
    private TextView termsTextView;

    public AccountRegisterTermsLayout(Context context) {
        super(context);
    }

    public AccountRegisterTermsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountRegisterTermsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.py_account_register_terms, null);
        backView = contentView.findViewById(R.id.py_back_button);
        titleTextView = (TextView) contentView.findViewById(R.id.py_title_id);
        titleTextView.setText(getResources().getText(R.string.py_login_terms));

        termsTextView = (TextView) contentView.findViewById(R.id.py_terms_text_id);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginDialog.popBackStack();
            }
        });

        String serverTermsContent = StarPyUtil.getSdkLoginTerms(getContext());//优先设置服务器获取的配置
        if (!TextUtils.isEmpty(serverTermsContent)){
            termsTextView.setText(serverTermsContent);
        }

        return contentView;
    }



    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }
}
