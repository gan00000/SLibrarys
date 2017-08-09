package com.starpy.data.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.starpy.base.bean.SLoginType;
import com.starpy.data.login.request.ThirdAccountBindRequest;

/**
 * <p>Title: 三方綁定</p>
 *
 * @author GanYuanrong
 * @date 2014年9月16日
 */
public class ThirdAccountBindRequestTask extends BaseRequestTask {

    ThirdAccountBindRequest thirdAccountBindRequest;

    /**
     *   fb綁定
     * @param context
     * @param name
     * @param pwd
     * @param email
     * @param fbScopeId
     * @param fbApps
     * @param fbTokenBusiness
     */
    public ThirdAccountBindRequestTask(Context context, String name, String pwd, String email, String fbScopeId, String fbApps, String fbTokenBusiness) {
        super(context);

        thirdAccountBindRequest = new ThirdAccountBindRequest(context);

        baseRequest = thirdAccountBindRequest;

        thirdAccountBindRequest.setRegistPlatform(SLoginType.LOGIN_TYPE_FB);
        thirdAccountBindRequest.setThirdPlatId(fbScopeId);
        thirdAccountBindRequest.setApps(fbApps);
        thirdAccountBindRequest.setTokenBusiness(fbTokenBusiness);

        thirdAccountBindRequest.setName(name);
        thirdAccountBindRequest.setPwd(SStringUtil.toMd5(pwd));
        thirdAccountBindRequest.setEmail(email);

        thirdAccountBindRequest.setRequestMethod("bind_thirdParty");


    }

    /**
     * 免註冊綁定
     * @param context
     * @param name
     * @param pwd
     * @param email
     */
    public ThirdAccountBindRequestTask(Context context, String name, String pwd, String email, String sLoginType, String thirdPlatId) {
        super(context);

        if(TextUtils.isEmpty(thirdPlatId)){
            PL.d("thirdPlatId:" + thirdPlatId);
            return;
        }

        thirdAccountBindRequest = new ThirdAccountBindRequest(context);

        baseRequest = thirdAccountBindRequest;

        thirdAccountBindRequest.setRegistPlatform(sLoginType);
        thirdAccountBindRequest.setThirdPlatId(thirdPlatId);
        thirdAccountBindRequest.setName(name);
        thirdAccountBindRequest.setPwd(SStringUtil.toMd5(pwd));
        thirdAccountBindRequest.setEmail(email);

        thirdAccountBindRequest.setRequestMethod("bind_thirdParty");


    }

    @Override
    public BaseReqeustBean createRequestBean() {
        super.createRequestBean();


        thirdAccountBindRequest.setSignature(SStringUtil.toMd5(thirdAccountBindRequest.getAppKey() + thirdAccountBindRequest.getTimestamp() +
                        thirdAccountBindRequest.getName() + thirdAccountBindRequest.getPwd() +
                thirdAccountBindRequest.getGameCode() + thirdAccountBindRequest.getThirdPlatId() + thirdAccountBindRequest.getRegistPlatform()));

        return thirdAccountBindRequest;
    }
}
