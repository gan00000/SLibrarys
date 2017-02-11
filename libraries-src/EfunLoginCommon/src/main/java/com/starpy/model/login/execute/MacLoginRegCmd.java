package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.MacLoginRegRequest;

/**
 * <p>Title: MacLoginRegCmd</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class MacLoginRegCmd extends EfunBaseCmd {

    public MacLoginRegCmd(Context context) {
        super(context);

        MacLoginRegRequest macLoginRegRequest = new MacLoginRegRequest(context);

        baseRequest = macLoginRegRequest;

        macLoginRegRequest.setRegistPlatform("mac");
        macLoginRegRequest.setUniqueId(ApkInfoUtil.getCustomizedUniqueId(context));

        macLoginRegRequest.setCompleteUrl("http://10.10.10.110:8080/login/login");
        macLoginRegRequest.setAppKey("test123");
        macLoginRegRequest.setGameCode("test");
        macLoginRegRequest.setGameLanguage("tw");

        macLoginRegRequest.setSignature(SStringUtil.toMd5(macLoginRegRequest.getAppKey() + macLoginRegRequest.getTimestamp() +
                macLoginRegRequest.getUniqueId() + macLoginRegRequest.getGameCode() + macLoginRegRequest.getRegistPlatform()));

    }


}
