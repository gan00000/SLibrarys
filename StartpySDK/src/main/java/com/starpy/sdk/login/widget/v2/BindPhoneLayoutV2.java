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

import com.core.base.bean.BaseReqeustBean;
import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.bean.SSdkBaseRequestBean;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.execute.BaseLoginRequestTask;
import com.starpy.data.login.request.AccountBindPhoneEmailBean;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.R;
import com.starpy.sdk.login.widget.SLoginBaseRelativeLayout;
import com.starpy.sdk.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;


public class BindPhoneLayoutV2 extends SLoginBaseRelativeLayout implements View.OnClickListener {

    private View contentView;
    private ImageView eyeImageView;
    private TextView bindConfirm;
    private TextView getVfCodeTextView, phoneAreaCodeTextView;

    private TextView bindPhoneTitle, bindPhoneTitleEn;

    private EditText bindPhonePasswordEditText, bindPhoneAccountEditText,bindPhoneNumEditText,vfCodeEditText;

    private String account;
    private String password;

    private boolean isUnBindPhone = false;


    public BindPhoneLayoutV2(Context context) {
        super(context);
    }

    public BindPhoneLayoutV2(Context context, boolean isUnBindPhone) {
        super(context);
        this.isUnBindPhone = isUnBindPhone;

        if (isUnBindPhone){
            bindPhoneTitle.setText(R.string.py_unbind_phone_title_hit);
            bindPhoneTitleEn.setText(R.string.py_unbind_phone_title_en_hit);
            bindPhoneNumEditText.setHint(R.string.py_unbind_phone_phonenum_tips);
        }
    }

    public BindPhoneLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindPhoneLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(Context context, LayoutInflater layoutInflater) {
        return onCreateView(layoutInflater);
    }

    private View onCreateView(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.v2_bind_phone, null);

        backView = contentView.findViewById(R.id.py_back_button_v2);


        eyeImageView = (ImageView) contentView.findViewById(R.id.py_bindphone_eye_imageview_id);

        bindPhonePasswordEditText = (EditText) contentView.findViewById(R.id.py_bindphone_password);
        bindPhoneAccountEditText = (EditText) contentView.findViewById(R.id.py_bindphone_account);
        bindPhoneNumEditText = (EditText) contentView.findViewById(R.id.py_bindphone_phonenum);
        vfCodeEditText = (EditText) contentView.findViewById(R.id.py_bindphone_vf_code);

        getVfCodeTextView = contentView.findViewById(R.id.py_bindphone_get_vf_code);
        phoneAreaCodeTextView = contentView.findViewById(R.id.py_bindphone_phonearea);

        bindConfirm = (TextView) contentView.findViewById(R.id.py_bind_account_confirm);

        bindPhoneTitle = (TextView) contentView.findViewById(R.id.bind_phone_title);
        bindPhoneTitleEn = (TextView) contentView.findViewById(R.id.bind_phone_title_en);


        eyeImageView.setOnClickListener(this);
        backView.setOnClickListener(this);
        bindConfirm.setOnClickListener(this);
        getVfCodeTextView.setOnClickListener(this);

        return contentView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (bindPhoneAccountEditText != null){
            bindPhoneAccountEditText.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {

       if (v == bindConfirm) {

            start(2);

        } else if (v == backView) {//返回键

            sLoginDialogv2.toAccountManagerCenter();

        } else if (v == eyeImageView) {//密码眼睛

            if (eyeImageView.isSelected()) {
                eyeImageView.setSelected(false);
                // 显示为普通文本
                bindPhonePasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                eyeImageView.setSelected(true);
                // 显示为密码
                bindPhonePasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // 使光标始终在最后位置
            Editable etable = bindPhonePasswordEditText.getText();
            Selection.setSelection(etable, etable.length());

        }else if (v == getVfCodeTextView){
           start(1);
       }

    }

    private void start(int m) {

        account = bindPhoneAccountEditText.getEditableText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.toast(getTheContext(), R.string.py_account_empty);
            return;
        }

        password = bindPhonePasswordEditText.getEditableText().toString().trim();
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

        String phoneNum = bindPhoneNumEditText.getEditableText().toString().trim();
        String phoneAreaCode = phoneAreaCodeTextView.getText().toString();

        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.toast(getTheContext(), R.string.plat_phone_num_not_empty);
            return;
        }


        if (m == 1) {
            requestVfCode(phoneAreaCode,phoneNum);//获取验证码
        }else {
            String vfCode = vfCodeEditText.getEditableText().toString().trim();
            if (TextUtils.isEmpty(vfCode)) {
                ToastUtils.toast(getTheContext(), R.string.plat_vf_code_not_empty);
                return;
            }
            if (isUnBindPhone) {

                requestUnBindPhone(account,password,phoneNum,phoneAreaCode,vfCode);//解绑手机

            }else {
                requestBindPhone(account,password,phoneNum,phoneAreaCode,vfCode);//绑定手机

            }

        }

    }


    //獲取驗證碼
    private void requestVfCode(String areaCode,String phone) {

        if (SStringUtil.isEmpty(areaCode)){
            areaCode = "86";
        }

        if ("86".equals(areaCode) || "+86".equals(areaCode)) {
            String phonePattern = "^1\\d{10}$";
            if (SStringUtil.isNotEmpty(phonePattern)){
                if (!phone.matches(phonePattern)){
                    ToastUtils.toast(getTheContext(), R.string.plat_phone_num_format_error);
                    return;
                }
            }
        }

        final SSdkBaseRequestBean sSdkBaseRequestBean = new SSdkBaseRequestBean(getTheContext());
        sSdkBaseRequestBean.setPhone(phone);
        sSdkBaseRequestBean.setPhoneAreaCode(areaCode);
        sSdkBaseRequestBean.setRequestMethod("acquireVfCode");

        BaseLoginRequestTask requestTask = new BaseLoginRequestTask(getTheContext()) {
            @Override
            public BaseReqeustBean createRequestBean() {
                super.createRequestBean();
                sSdkBaseRequestBean.setSignature(SStringUtil.toMd5(sSdkBaseRequestBean.getAppKey() + sSdkBaseRequestBean.getTimestamp()
                        + sSdkBaseRequestBean.getGameCode() + sSdkBaseRequestBean.getPhone()));
                return sSdkBaseRequestBean;
            }
        };
        requestTask.setSdkBaseRequestBean(sSdkBaseRequestBean);
        requestTask.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(), "Loading..."));
        requestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    PL.i(sLoginResponse.getMessage());
                    ToastUtils.toast(getTheContext(),sLoginResponse.getMessage());
                    if (sLoginResponse.isRequestSuccess()) {
                        startCountDownTimer();
                    }
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        requestTask.excute(SLoginResponse.class);
    }

    private Timer countdowncTimer;
    private int count = 60;
    //验证码倒计时
    private void startCountDownTimer() {
        //获取验证码
        countdowncTimer = new Timer();//delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。

        countdowncTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                sLoginDialogv2.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getVfCodeTextView.setText("(" + count +  ")");
                        getVfCodeTextView.setClickable(false);
                        if (count <= 0){

                            if (countdowncTimer != null){
                                countdowncTimer.cancel();
                            }
                            getVfCodeTextView.setClickable(true);
                            getVfCodeTextView.setText(getResources().getString(R.string.py_bind_phone_get_vf_code));
                        }
                        count--;
                    }
                });
            }
        },300,1000);
    }


    //綁定手機
    private void requestBindPhone(String account,String pwd,String phone,String areaCode,String vfCode) {


        if (TextUtils.isEmpty(phone)) {
            ToastUtils.toast(getContext(), R.string.plat_phone_num_not_empty);
            return;
        }
        if (SStringUtil.isEmpty(areaCode)){
            areaCode = "86";
        }

        if ("86".equals(areaCode) || "+86".equals(areaCode)) {
            String phonePattern = "^1\\d{10}$";
            if (SStringUtil.isNotEmpty(phonePattern)){
                if (!phone.matches(phonePattern)){
                    ToastUtils.toast(getTheContext(), R.string.plat_phone_num_format_error);
                    return;
                }
            }
        }


        if (TextUtils.isEmpty(vfCode)) {
            ToastUtils.toast(getContext(), R.string.plat_vf_code_not_empty);
            return;
        }

        final AccountBindPhoneEmailBean sSdkBaseRequestBean = new AccountBindPhoneEmailBean(getContext());

        sSdkBaseRequestBean.setName(account);
        sSdkBaseRequestBean.setPwd(SStringUtil.toMd5(pwd));

        sSdkBaseRequestBean.setPhone(phone);
        sSdkBaseRequestBean.setVfCode(vfCode);
        sSdkBaseRequestBean.setPhoneAreaCode(areaCode);

        sSdkBaseRequestBean.setRequestMethod("bindpe_phone_email");
        BaseLoginRequestTask requestTask = new BaseLoginRequestTask(getContext()) {
            @Override
            public BaseReqeustBean createRequestBean() {
                super.createRequestBean();
                sSdkBaseRequestBean.setSignature(SStringUtil.toMd5(sSdkBaseRequestBean.getAppKey() + sSdkBaseRequestBean.getTimestamp()
                        + sSdkBaseRequestBean.getGameCode() + sSdkBaseRequestBean.getPhone()));
                return sSdkBaseRequestBean;
            }
        };
        requestTask.setSdkBaseRequestBean(sSdkBaseRequestBean);
        requestTask.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(), "Loading..."));
        requestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    PL.i(sLoginResponse.getMessage());
                    ToastUtils.toast(getContext(),sLoginResponse.getMessage());
                    if (sLoginResponse.isRequestSuccess()) {

                        ToastUtils.toast(getContext(),sLoginResponse.getMessage());

                        sLoginDialogv2.toAccountManagerCenter();
                    }
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        requestTask.excute(SLoginResponse.class);

    }

    //解綁定手機
    private void requestUnBindPhone(String account,String pwd,String phone,String areaCode,String vfCode) {


        if (TextUtils.isEmpty(phone)) {
            ToastUtils.toast(getContext(), R.string.plat_phone_num_not_empty);
            return;
        }
        if (SStringUtil.isEmpty(areaCode)){
            areaCode = "86";
        }

        if ("86".equals(areaCode) || "+86".equals(areaCode)) {
            String phonePattern = "^1\\d{10}$";
            if (SStringUtil.isNotEmpty(phonePattern)){
                if (!phone.matches(phonePattern)){
                    ToastUtils.toast(getTheContext(), R.string.plat_phone_num_format_error);
                    return;
                }
            }
        }


        if (TextUtils.isEmpty(vfCode)) {
            ToastUtils.toast(getContext(), R.string.plat_vf_code_not_empty);
            return;
        }

        final AccountBindPhoneEmailBean sSdkBaseRequestBean = new AccountBindPhoneEmailBean(getContext());

        sSdkBaseRequestBean.setName(account);
        sSdkBaseRequestBean.setPwd(SStringUtil.toMd5(pwd));

        sSdkBaseRequestBean.setPhone(phone);
        sSdkBaseRequestBean.setVfCode(vfCode);
        sSdkBaseRequestBean.setPhoneAreaCode(areaCode);

        sSdkBaseRequestBean.setRequestMethod("unbindPhone");
        BaseLoginRequestTask requestTask = new BaseLoginRequestTask(getContext()) {
            @Override
            public BaseReqeustBean createRequestBean() {
                super.createRequestBean();
                sSdkBaseRequestBean.setSignature(SStringUtil.toMd5(sSdkBaseRequestBean.getAppKey() + sSdkBaseRequestBean.getTimestamp()
                        + sSdkBaseRequestBean.getGameCode() + sSdkBaseRequestBean.getPhone()));
                return sSdkBaseRequestBean;
            }
        };
        requestTask.setSdkBaseRequestBean(sSdkBaseRequestBean);
        requestTask.setLoadDialog(DialogUtil.createLoadingDialog(getTheContext(), "Loading..."));
        requestTask.setReqCallBack(new ISReqCallBack<SLoginResponse>() {
            @Override
            public void success(SLoginResponse sLoginResponse, String rawResult) {
                if (sLoginResponse != null){
                    PL.i(sLoginResponse.getMessage());
                    ToastUtils.toast(getContext(),sLoginResponse.getMessage());
                    if (sLoginResponse.isRequestSuccess()) {

                        ToastUtils.toast(getContext(),sLoginResponse.getMessage());

                        sLoginDialogv2.toAccountManagerCenter();
                    }
                }

            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        requestTask.excute(SLoginResponse.class);

    }

}
