package com.starpy.sdk.plat.data.presenter;

import android.app.Activity;

import com.core.base.callback.ISReqCallBack;
import com.core.base.request.AbsHttpRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.starpy.sdk.R;
import com.starpy.sdk.plat.data.PlatContract;
import com.starpy.sdk.plat.data.bean.response.PlatMenuAllModel;

import static com.starpy.sdk.plat.data.PlatContract.RequestType.REQ_PLATMENU;

/**
 * Created by gan on 2017/8/14.
 */

public class PlatPresenterImpl implements PlatContract.IPlatPresenter {

    private PlatContract.IPlatView iPlatView;

    @Override
    public void reqeustPlatMenuData(final Activity activity) {

        AbsHttpRequest absHttpRequest = new AbsHttpRequest() {
            @Override
            public BaseReqeustBean createRequestBean() {
                BaseReqeustBean baseReqeustBean = new BaseReqeustBean(activity);
                baseReqeustBean.setCompleteUrl(activity.getResources().getString(R.string.star_cdn_url));
                return baseReqeustBean;
            }
        };

        absHttpRequest.setReqCallBack(new ISReqCallBack<PlatMenuAllModel>() {
            @Override
            public void success(PlatMenuAllModel platMenuAllModel, String rawResult) {
                if (iPlatView != null) {
                    iPlatView.reqeustPlatMenuDataSuccess(REQ_PLATMENU, platMenuAllModel);
                }
            }

            @Override
            public void timeout(String code) {

            }

            @Override
            public void noData() {

            }
        });
        absHttpRequest.setGetMethod(true,false);
        absHttpRequest.excute(PlatMenuAllModel.class);
    }

    @Override
    public void setBaseView(PlatContract.IPlatView view) {
        iPlatView = view;
    }
}
