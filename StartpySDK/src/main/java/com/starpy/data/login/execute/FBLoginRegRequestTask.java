package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.data.login.request.ThirdLoginRegRequest;
import com.starpy.data.login.constant.SLoginType;

/**
 * <p>Title: MacLoginRegRequestTask</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class FBLoginRegRequestTask extends BaseRequestTask {
    ThirdLoginRegRequest macLoginRegRequest;

    public FBLoginRegRequestTask(Context context, String fbScopeId, String fbApps, String fbTokenBusiness) {
        super(context);

        macLoginRegRequest = new ThirdLoginRegRequest(context);

        baseRequest = macLoginRegRequest;

        macLoginRegRequest.setRegistPlatform(SLoginType.LOGIN_TYPE_FB);
        macLoginRegRequest.setThirdPlatId(fbScopeId);
        macLoginRegRequest.setApps(fbApps);
        macLoginRegRequest.setTokenBusiness(fbTokenBusiness);

        macLoginRegRequest.setRequestMethod("thirdPartyLogin");


    }

    @Override
    public BaseReqeustBean onHttpRequest() {
        super.onHttpRequest();


        macLoginRegRequest.setSignature(SStringUtil.toMd5(macLoginRegRequest.getAppKey() + macLoginRegRequest.getTimestamp() +
                macLoginRegRequest.getThirdPlatId() + macLoginRegRequest.getGameCode() + macLoginRegRequest.getRegistPlatform()));

        return macLoginRegRequest;
    }
}
