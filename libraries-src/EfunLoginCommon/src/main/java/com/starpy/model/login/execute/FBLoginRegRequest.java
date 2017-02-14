package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.ThirdLoginRegRequest;
import com.starpy.model.login.constant.SLoginType;

/**
 * <p>Title: MacLoginRegRequest</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class FBLoginRegRequest extends BaseRequest {

    public FBLoginRegRequest(Context context, String fbScopeId, String fbApps, String fbTokenBusiness) {
        super(context);

        ThirdLoginRegRequest macLoginRegRequest = new ThirdLoginRegRequest(context);

        baseRequest = macLoginRegRequest;

        macLoginRegRequest.setRegistPlatform(SLoginType.LOGIN_TYPE_FB);
        macLoginRegRequest.setThirdPlatId(fbScopeId);
        macLoginRegRequest.setApps(fbApps);
        macLoginRegRequest.setTokenBusiness(fbTokenBusiness);

        macLoginRegRequest.setCompleteUrl("http://10.10.10.110:8080/login/login");
        macLoginRegRequest.setAppKey("test123");
        macLoginRegRequest.setGameCode("test");
        macLoginRegRequest.setGameLanguage("tw");

        macLoginRegRequest.setSignature(SStringUtil.toMd5(macLoginRegRequest.getAppKey() + macLoginRegRequest.getTimestamp() +
                macLoginRegRequest.getThirdPlatId() + macLoginRegRequest.getGameCode() + macLoginRegRequest.getRegistPlatform()));

    }


}
