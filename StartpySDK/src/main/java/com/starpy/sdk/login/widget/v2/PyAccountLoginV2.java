package com.starpy.sdk.login.widget.v2;

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

import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.utils.Localization;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;

/**
 * Created by Efun on 2017/2/6.
 */

public class PyAccountLoginV2 extends SLoginBaseRelativeLayout {

    private View contentView;

    private TextView loginMainLoginBtn;
    private ImageView eyeImageView, savePwdCheckBox;

    private EditText loginPasswordEditText, loginAccountEditText;
    private String account;

    private String password;

    private View loginMainGoRegisterBtn;
    private View loginMainGoFindPwd;
//    private View loginMainGoChangePwd;
//    private View loginMainGoBindUnique;
//    private View loginMainGoBindFb;
    private View loginMainFreeRegLogin;
    private View loginMainGoAccountCenter;


    private View leftTopView;
    private View leftBottomView;
    private long firstClickTime;
//    private long lastClickTime;
    private int leftTopClickCount = 0;
    private int leftBottomClickCount = 0;

    public PyAccountLoginV2(Context context) {
        super(context);

    }


    public PyAccountLoginV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PyAccountLoginV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    public View onCreateView(LayoutInflater inflater) {

        contentView = inflater.inflate(R.layout.v2_py_account_login, null);

        backView = contentView.findViewById(R.id.py_back_button_v2);
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginDialogv2.toMainLoginView();
            }
        });

        loginMainGoRegisterBtn = contentView.findViewById(R.id.py_login_go_reg_v2);
        loginMainGoFindPwd = contentView.findViewById(R.id.py_login_go_findpwd_v2);
//        loginMainGoChangePwd = contentView.findViewById(R.id.py_login_go_changePwd_v2);
//        loginMainGoBindUnique = contentView.findViewById(R.id.py_login_go_bindUnique_v2);
//        loginMainGoBindFb = contentView.findViewById(R.id.py_login_go_bindFb_v2);
//        loginMainGoBindGoogle = contentView.findViewById(R.id.py_login_go_bindGoogle);
        loginMainGoAccountCenter = contentView.findViewById(R.id.py_login_go_account_center);
        loginMainFreeRegLogin = contentView.findViewById(R.id.py_login_free_reg_login);//遊客登錄


        eyeImageView = (ImageView) contentView.findViewById(R.id.py_login_password_eye_v2);

        loginAccountEditText = (EditText) contentView.findViewById(R.id.py_login_account_v2);
        loginPasswordEditText = (EditText) contentView.findViewById(R.id.py_login_password_v2);

        loginMainLoginBtn = (TextView) contentView.findViewById(R.id.v2_member_btn_login);

        leftTopView = contentView.findViewById(R.id.py_left_top_id);
        leftBottomView = contentView.findViewById(R.id.py_left_bottom_id);

        if (StarPyUtil.isXM(getContext())){//星盟标题

            ((ImageView)contentView.findViewById(R.id.v2_bg_title_login_iv)).setImageResource(R.drawable.bg_xm_title_login);
        }

        if (StarPyUtil.isMainland(getContext())){
            loginMainFreeRegLogin.setVisibility(View.VISIBLE);
            backView.setVisibility(GONE);
        }

        if (Localization.getSGameLanguage(getActivity()) == SGameLanguage.en_US){//星盟--星彼英文一样
            ((ImageView)contentView.findViewById(R.id.v2_bg_title_login_iv)).setImageResource(R.drawable.bg_xm_title_login_en);
        }

        savePwdCheckBox = (ImageView) contentView.findViewById(R.id.py_save_pwd_text_check_id);

        savePwdCheckBox.setSelected(true);

        savePwdCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savePwdCheckBox.isSelected()) {
                    savePwdCheckBox.setSelected(false);
                } else {
                    savePwdCheckBox.setSelected(true);
                }
            }
        });

        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                sLoginActivity.replaceFragment(new AccountLoginFragment());
                sLoginDialogv2.toMainLoginView();
            }
        });

        loginMainGoRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                sLoginActivity.replaceFragmentBackToStack(new AccountRegisterFragment());

                sLoginDialogv2.toRegisterView(2);
            }
        });

        loginMainGoFindPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginDialogv2.toFindPwdView();
            }
        });
//        loginMainGoChangePwd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sLoginDialogv2.toChangePwdView();
//            }
//        });
//
//        loginMainGoBindUnique.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sLoginDialogv2.toBindUniqueView();
//            }
//        });
//        loginMainGoBindFb.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sLoginDialogv2.toBindFbView();
//            }
//        });
//        loginMainGoBindGoogle.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sLoginDialogv2.toBindGoogleView();
//            }
//        });

        loginMainFreeRegLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginDialogv2.getLoginPresenter().macLogin(sLoginDialogv2.getActivity());
            }
        });

        loginMainGoAccountCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sLoginDialogv2.toAccountManagerCenter();
            }
        });

        eyeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eyeImageView.isSelected()) {
                    eyeImageView.setSelected(false);
                    // 显示为普通文本
                    loginPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    eyeImageView.setSelected(true);
                    // 显示为密码
                    loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // 使光标始终在最后位置
                Editable etable = loginPasswordEditText.getText();
                Selection.setSelection(etable, etable.length());
            }
        });

        loginMainLoginBtn.setOnClickListener(new OnClickListener() {
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

        leftTopView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftBottomClickCount > 0){//若下方点击不为0，全部重置
                    leftTopClickCount = 0;
                    leftBottomClickCount = 0;
                    firstClickTime = 0;
                }
                if (System.currentTimeMillis() - firstClickTime < 10 * 1000){//10秒内点击计数
                    leftTopClickCount++;
                    PL.i("leftTopClickCount--- " + leftTopClickCount);
                    return;
                }
                leftTopClickCount = 0;//大于10秒的点击重置
                leftBottomClickCount = 0;
                leftTopClickCount++;
                firstClickTime = System.currentTimeMillis();

            }
        });
        leftBottomView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() - firstClickTime < 10 * 1000){
                    leftBottomClickCount++;
                    PL.i("leftBottomClickCount--- " + leftBottomClickCount);
                    if (leftTopClickCount >= 5 && leftBottomClickCount >= 5){
                        PL.i("open set uid page");
                        leftTopClickCount = 0;
                        leftBottomClickCount = 0;
                        firstClickTime = 0;
                        sLoginDialogv2.toInjectionView();
                    }
                    return;
                }
                leftTopClickCount = 0;
                leftBottomClickCount = 0;
                firstClickTime = 0;

            }
        });

        return contentView;
    }

    private void login() {

        account = loginAccountEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getActivity(), R.string.py_account_empty);
            return;
        }

        password = loginPasswordEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getActivity(), R.string.py_password_empty);
            return;
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

        sLoginDialogv2.getLoginPresenter().starpyAccountLogin(sLoginDialogv2.getActivity(),account,password);
     /*   AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(getActivity(), account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(getActivity(),"Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    if (sLoginResponse.isRequestSuccess()) {
                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
                        sLoginDialog.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                    }else{

                        ToastUtils.toast(getActivity(),sLoginResponse.getMessage());
                    }
                }else{
                    ToastUtils.toast(getActivity(),R.string.py_error_occur);
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
