package com.starpy.thirdlib.wx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.core.base.utils.PL;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by gan on 2018/4/18.
 */

public class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private SWXProxy swxProxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swxProxy = SWXProxy.getSWXProxy();

        try {
//            swxProxy.handleIntent(getIntent(), this);
            swxProxy.createNewIWXAPI(this).handleIntent(getIntent(),this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // bundle = intent.getExtras();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        PL.i("onReq");

    }

    @Override
    public void onResp(BaseResp baseResp) {
        PL.i("onResp");
        int errCode = baseResp.errCode;
        PL.i("errCode:" + errCode + "  errMsg:" + baseResp.errStr);

        if (swxProxy != null) {
            swxProxy.onResp(baseResp);
        }

        finish();

    }


}
