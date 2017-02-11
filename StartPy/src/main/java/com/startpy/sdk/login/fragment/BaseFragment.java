package com.startpy.sdk.login.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startpy.sdk.login.SLoginActivity;
import com.startpy.sdk.utils.DialogUtil;

/**
 * Created by Efun on 2017/2/6.
 */

public class BaseFragment extends Fragment {

    protected Dialog loadingDialog;
    protected View backView;
    SLoginActivity sLoginActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = DialogUtil.createLoadingDialog(getActivity(), "Loading...");
        sLoginActivity = (SLoginActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}