package com.starpy.sdk.login;

import android.os.Bundle;

import com.core.base.SBaseActivity;
import com.starpy.base.utils.Localization;

/**
 * Created by Efun on 2017/2/7.
 */

public class BaseLoginActivity extends SBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Localization.updateSGameLanguage(this);
    }
}
