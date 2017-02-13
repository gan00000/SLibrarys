package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.MacLoginRegRequest;
import com.starpy.model.login.bean.request.ThirdLoginRegRequest;

/**
 * <p>Title: MacLoginRegCmd</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class FBLoginRegCmd extends EfunBaseCmd {

    public FBLoginRegCmd(Context context, String fbScopeId, String fbApps, String fbTokenBusiness) {
        super(context);

        ThirdLoginRegRequest macLoginRegRequest = new ThirdLoginRegRequest(context);

        baseRequest = macLoginRegRequest;

        macLoginRegRequest.setRegistPlatform("fb");
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
