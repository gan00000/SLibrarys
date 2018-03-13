package com.starpy.sdk.login.widget.v2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.sdk.R;
import com.starpy.sdk.login.adapter.AccountListViewAdapter;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;
import com.starpy.sql.DaoManager;
import com.starpy.sql.DaoSession;
import com.starpy.sql.StarpyPersionDao;
import com.starpy.sql.bean.StarpyPersion;

import java.util.List;

/**
 * Created by GanYuanrong on 2017/2/6.
 */

public class PyAccountLoginV2 extends SLoginBaseRelativeLayout {

    private View contentView;
    private View saveAccountPwdLayout;

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
    private View goBindPhone;
    private View listAccountViewTips;

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
        goBindPhone = contentView.findViewById(R.id.py_login_go_bind_phone);
        loginMainGoAccountCenter = contentView.findViewById(R.id.py_login_go_account_center);
        loginMainFreeRegLogin = contentView.findViewById(R.id.py_login_free_reg_login);//遊客登錄


        eyeImageView = (ImageView) contentView.findViewById(R.id.py_login_password_eye_v2);

        loginAccountEditText = (EditText) contentView.findViewById(R.id.py_login_account_v2);
        loginPasswordEditText = (EditText) contentView.findViewById(R.id.py_login_password_v2);

        loginMainLoginBtn = (TextView) contentView.findViewById(R.id.v2_member_btn_login);

        leftTopView = contentView.findViewById(R.id.py_left_top_id);
        leftBottomView = contentView.findViewById(R.id.py_left_bottom_id);

//        if (StarPyUtil.isXM(getContext())){//星盟标题
//
//            ((ImageView)contentView.findViewById(R.id.v2_bg_title_login_iv)).setImageResource(R.drawable.bg_xm_title_login);
//        }


//        if (Localization.getSGameLanguage(getTheContext()) == SGameLanguage.en_US){//星盟--星彼英文一样
//            ((ImageView)contentView.findViewById(R.id.v2_bg_title_login_iv)).setImageResource(R.drawable.bg_xm_title_login_en);
//        }

        saveAccountPwdLayout =  contentView.findViewById(R.id.py_save_account_pwd_layout);

        if (StarPyUtil.isMainland(getContext())){
            loginMainFreeRegLogin.setVisibility(View.VISIBLE);
            goBindPhone.setVisibility(VISIBLE);
            backView.setVisibility(GONE);
            loginMainGoAccountCenter.setVisibility(GONE);
            saveAccountPwdLayout.setVisibility(GONE);
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
        goBindPhone.setOnClickListener(new OnClickListener() {
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

        listAccountViewTips = contentView.findViewById(R.id.py_login_account_list_tips);
        listAccountViewTips.setSelected(true);

        listAccountViewTips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null && !listAccountViewTips.isSelected()){
                    pw.dismiss();
                    return;
                }
                showAccount();
            }
        });

        return contentView;
    }

    PopupWindow pw;
    //显示电话号码区号
    private void showAccount() {

        if (pw == null) {
            pw = new PopupWindow(sLoginDialogv2.getActivity());
        }

        if (pw.isShowing()){
            pw.dismiss();
            return;
        }

        final List<StarpyPersion> starpyPersions = findAccountFromSql();

        View listLayout = LayoutInflater.from(sLoginDialogv2.getActivity()).inflate(R.layout.login_account_list,null);
        ListView listView = (ListView) listLayout.findViewById(R.id.login_account_list_id);
        AccountListViewAdapter accountListViewAdapter = new AccountListViewAdapter(sLoginDialogv2.getActivity());

        accountListViewAdapter.setDataModelList(starpyPersions);
        listView.setAdapter(accountListViewAdapter);

        pw.setOutsideTouchable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }else{
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listAccountViewTips.setSelected(true);
            }
        });
        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setContentView(listLayout);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        pw.showAsDropDown((LinearLayout)loginAccountEditText.getParent());
        listAccountViewTips.setSelected(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PL.i(starpyPersions.get(position) + "   position:" + position);
                pw.dismiss();
                loginAccountEditText.setText(starpyPersions.get(position).getName());
                loginPasswordEditText.setText(starpyPersions.get(position).getPwd());
            }
        });
    }

    private List<StarpyPersion> findAccountFromSql(){

        DaoSession daoSession = DaoManager.getDaoManager(sLoginDialogv2.getActivity().getApplicationContext()).getDaoSession();
        StarpyPersionDao starpyPersionDao = daoSession.getStarpyPersionDao();
        List<StarpyPersion> starpyPersions = starpyPersionDao.queryBuilder().list();
        return starpyPersions;
    }

    private void login() {

        account = loginAccountEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }

        password = loginPasswordEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toast(getTheContext(), R.string.py_password_empty);
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

        sLoginDialogv2.getLoginPresenter().starpyAccountLogin(sLoginDialogv2.getActivity(),account,password);
     /*   AccountLoginRequestTask accountLoginCmd = new AccountLoginRequestTask(getTheContext(), account, password);
        accountLoginCmd.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(),"Loading..."));
        accountLoginCmd.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    if (sLoginResponse.isRequestSuccess()) {
                        StarPyUtil.saveAccount(getContext(),account);
                        StarPyUtil.savePassword(getContext(),password);
                        sLoginDialog.handleRegisteOrLoginSuccess(sLoginResponse,rawResult, SLoginType.LOGIN_TYPE_STARPY);
                    }else{

                        ToastUtils.toast(getTheContext(),sLoginResponse.getMessage());
                    }
                }else{
                    ToastUtils.toast(getTheContext(),R.string.py_error_occur);
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
