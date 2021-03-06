package com.starpy.sdk.plat.data;

import android.app.Activity;

import com.starpy.BaseView;
import com.starpy.IBasePresenter;
import com.starpy.sdk.plat.data.bean.response.PlatMenuAllModel;

/**
 * Created by gan on 2017/8/14.
 */

public class PlatContract {

    public enum RequestType{

        REQ_PLATMENU;

    }

    public interface IPlatView extends BaseView {

        void reqeustPlatMenuDataSuccess(RequestType requestType, PlatMenuAllModel platMenuAllModel);

        void reqeustDataFail(RequestType requestType);

    }


    public interface IPlatPresenter extends IBasePresenter<IPlatView>{

        public void reqeustPlatMenuData(Activity activity);
    }
}
