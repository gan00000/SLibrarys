package com.starpy.sdk;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * Created by gan on 2017/4/12.
 */

public class SBaseRelativeLayout extends RelativeLayout {

    protected boolean isPortrait;

    public SBaseRelativeLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public SBaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public SBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SBaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout(context);
    }

    private void initLayout(Context context){
        if (context.getResources().getConfiguration().orientation ==  Configuration.ORIENTATION_PORTRAIT){
            isPortrait = true;
        }else {
            isPortrait = false;
        }
    }
}
