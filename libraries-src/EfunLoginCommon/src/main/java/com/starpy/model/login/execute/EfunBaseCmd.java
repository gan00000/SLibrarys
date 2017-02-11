/**
 * 
 */
package com.starpy.model.login.execute;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.request.AbsHttpRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.res.SConfig;
import com.core.base.utils.GoogleAdUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.CmdResponse;
import com.starpy.model.login.bean.request.LoginBaseRequest;
import com.starpy.model.login.utils.PyLoginHelper;

/**
 * <p>Title: EfunBaseCmd</p>
 * <p>Description: 请求接口对象封装类</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月10日
 */
public abstract class EfunBaseCmd extends AbsHttpRequest {

	protected Context context;

	protected LoginBaseRequest baseRequest;

	protected String mResponse;

	public EfunBaseCmd(Context context) {
		super();
		this.context = context;
	}


	@Override
	public BaseReqeustBean onHttpRequest() {
		if (this.context == null) {
			PL.d("execute context is null");
			return null;
		}

		if (SStringUtil.isEmpty(baseRequest.getRequestUrl())) {
			baseRequest.setRequestUrl(SConfig.getLoginPreferredUrl(context));
		}
		/*if (SStringUtil.isEmpty(sparedUrl)) {
			sparedUrl = SConfig.getLoginSpareUrl(context);
		}*/
		if (SStringUtil.isEmpty(baseRequest.getGameCode())) {
			baseRequest.setGameCode(SConfig.getGameCode(context));
		}
		if (SStringUtil.isEmpty(baseRequest.getAppKey())) {
			baseRequest.setAppKey(SConfig.getAppKey(context));
		}


		if(SStringUtil.isEmpty(baseRequest.getGameLanguage())){
			baseRequest.setGameLanguage(SConfig.getGameLanguage(context));
		}

		baseRequest.setReferrer(PyLoginHelper.takeReferrer(context, ""));

		//获取广告
		String advertisingId = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GOOGLE_ADVERTISING_ID);
		try {
			if (TextUtils.isEmpty(advertisingId)) {
				if (PyLoginHelper.existClass("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {//判断google-play-server.jar是否存在
					advertisingId = GoogleAdUtil.getAdvertisingId(context);
					if (!TextUtils.isEmpty(advertisingId)) {
						SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GOOGLE_ADVERTISING_ID, advertisingId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		baseRequest.setAdvertisingId(advertisingId);

		return baseRequest;
	}

	@Override
	public <T> BaseReqeustBean onHttpSucceess(T responseModel) {
		return null;
	}

	@Override
	public void onTimeout(String result) {

	}

	@Override
	public void onNoData(String result) {

	}


	public String getResponse() {
		return mResponse;
	}


	/*protected void handleLoginResult() {
		this.cmdResponse  = PyLoginHelper.parseResult(mResponse);

		if(cmdResponse == null){
			return;
		}
		this.cmdResponse.setRawResponse(mResponse);
		if (!TextUtils.isEmpty(this.cmdResponse.getUserId())) {
			//保存用戶uid
			SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_USER_ID, this.cmdResponse.getUserId());
			//SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_LOGIN_SERVER_RETURN_DATA, mResponse);

		}

	}*/

	protected void saveLoginReponse(String reponse){
		PyLoginHelper.saveLoginReponse(context, reponse);
	}


}
