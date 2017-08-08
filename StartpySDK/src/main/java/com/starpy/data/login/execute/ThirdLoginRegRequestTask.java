package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.SStringUtil;
import com.starpy.base.bean.SLoginType;
import com.starpy.data.login.request.ThirdLoginRegRequest;

/**
 * <p>Title: MacLoginRegRequestTask</p> <p>Description: 新三方登陆&注册接口</p> <p>Company: EFun</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class ThirdLoginRegRequestTask extends BaseRequestTask {

    ThirdLoginRegRequest thirdLoginRegRequest;


    /**
     *  fb登录使用
     * @param context
     * @param fbScopeId
     * @param fbApps
     * @param fbTokenBusiness
     */
    public ThirdLoginRegRequestTask(Context context, String fbScopeId, String fbApps, String fbTokenBusiness) {
        super(context);

        thirdLoginRegRequest = new ThirdLoginRegRequest(context);

        baseRequest = thirdLoginRegRequest;

        thirdLoginRegRequest.setRegistPlatform(SLoginType.LOGIN_TYPE_FB);
        thirdLoginRegRequest.setThirdPlatId(fbScopeId);
        thirdLoginRegRequest.setApps(fbApps);
        thirdLoginRegRequest.setTokenBusiness(fbTokenBusiness);

        thirdLoginRegRequest.setRequestMethod("thirdPartyLogin");


    }

    /**
     * 其他第三方登录使用
     * @param context
     * @param thirdPlatId
     * @param registPlatform
     */
    public ThirdLoginRegRequestTask(Context context, String thirdPlatId, String registPlatform) {
        super(context);

        thirdLoginRegRequest = new ThirdLoginRegRequest(context);

        baseRequest = thirdLoginRegRequest;

        thirdLoginRegRequest.setRegistPlatform(registPlatform);
        thirdLoginRegRequest.setThirdPlatId(thirdPlatId);

        thirdLoginRegRequest.setRequestMethod("thirdPartyLogin");


    }

    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();


        thirdLoginRegRequest.setSignature(SStringUtil.toMd5(thirdLoginRegRequest.getAppKey() + thirdLoginRegRequest.getTimestamp() +
                thirdLoginRegRequest.getThirdPlatId() + thirdLoginRegRequest.getGameCode() + thirdLoginRegRequest.getRegistPlatform()));

        return thirdLoginRegRequest;
    }
}
