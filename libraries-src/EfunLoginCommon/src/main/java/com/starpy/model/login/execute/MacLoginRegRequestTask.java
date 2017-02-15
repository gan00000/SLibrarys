package com.starpy.model.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.starpy.base.cfg.SConfig;
import com.starpy.model.login.constant.SLoginType;

/**
 * <p>Title: MacLoginRegRequestTask</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class MacLoginRegRequestTask extends BaseRequestTask {

    com.starpy.model.login.bean.request.MacLoginRegRequest macLoginRegRequest;
    public MacLoginRegRequestTask(Context context) {
        super(context);

        macLoginRegRequest = new com.starpy.model.login.bean.request.MacLoginRegRequest(context);

        baseRequest = macLoginRegRequest;

        macLoginRegRequest.setRegistPlatform(SLoginType.LOGIN_TYPE_MAC);
        String uniqueId = ApkInfoUtil.getCustomizedUniqueIdOrAndroidId(context);
        if(TextUtils.isEmpty(uniqueId)){
            PL.d("uniqueId:" + uniqueId);
            return;
        }
        macLoginRegRequest.setUniqueId(uniqueId);

        macLoginRegRequest.setRequestMethod("login/freeRegister");


    }

    @Override
    public BaseReqeustBean onHttpRequest() {
        super.onHttpRequest();
        macLoginRegRequest.setSignature(SStringUtil.toMd5(macLoginRegRequest.getAppKey() + macLoginRegRequest.getTimestamp() +
                macLoginRegRequest.getUniqueId() + macLoginRegRequest.getGameCode()));
        return macLoginRegRequest;
    }
}
