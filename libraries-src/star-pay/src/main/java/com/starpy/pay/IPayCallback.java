package com.starpy.pay;

import com.core.base.callback.ISCallBack;

/**
 * Created by Efun on 2017/2/23.
 */

public interface IPayCallBack extends ISCallBack {

    void success();
    void fail();

}
