package com.starpy.sdk.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starpy.base.utils.Localization;
import com.starpy.sdk.SSdkBaseFragment;
import com.starpy.sdk.login.SLoginActivity;

/**
 * Created by GanYuanrong on 2017/2/6.
 */

public class BaseFragment extends SSdkBaseFragment {

//    protected Dialog loadingDialog;
    protected View backView;
    protected TextView titleTextView;
    SLoginActivity sLoginActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadingDialog = DialogUtil.createLoadingDialog(getActivity(), "Loading...");
        sLoginActivity = (SLoginActivity) getActivity();
        Localization.updateSGameLanguage(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
