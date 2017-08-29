package com.starpy.data.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.bean.BaseReqeustBean;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.starpy.base.bean.SLoginType;
import com.starpy.data.login.request.MacLoginRegRequestBean;

/**
 * <p>Title: MacLoginRegRequestTask</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class MacLoginRegRequestTask extends BaseRequestTask {

    MacLoginRegRequestBean macLoginRegRequestBean;
    public MacLoginRegRequestTask(Context context) {
        super(context);

        macLoginRegRequestBean = new MacLoginRegRequestBean(context);

        sdkBaseRequestBean = macLoginRegRequestBean;

        macLoginRegRequestBean.setRegistPlatform(SLoginType.LOGIN_TYPE_UNIQUE);
        String uniqueId = ApkInfoUtil.getCustomizedUniqueIdOrAndroidId(context);
        if(TextUtils.isEmpty(uniqueId)){
            PL.d("uniqueId:" + uniqueId);
            return;
        }
        macLoginRegRequestBean.setUniqueId(uniqueId);

        macLoginRegRequestBean.setRequestMethod("freeRegister");


    }

    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();
        macLoginRegRequestBean.setSignature(SStringUtil.toMd5(macLoginRegRequestBean.getAppKey() + macLoginRegRequestBean.getTimestamp() +
                macLoginRegRequestBean.getUniqueId() + macLoginRegRequestBean.getGameCode()));
        return macLoginRegRequestBean;
    }
}
