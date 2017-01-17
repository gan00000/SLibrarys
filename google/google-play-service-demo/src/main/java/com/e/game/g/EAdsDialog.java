package com.e.game.g;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Efun on 2016/7/19.
 */
public class EAdsDialog extends Dialog {
    public EAdsDialog(Context context) {
        super(context);
    }

    public EAdsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public EAdsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }



}
