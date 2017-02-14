/**
 * 
 */
package com.starpy.model.login.execute;

import android.content.Context;

import com.core.base.request.AbsHttpRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.res.SConfig;
import com.core.base.utils.GoogleUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.LoginBaseRequest;

/**
 * <p>Title: BaseRequest</p>
 * <p>Description: 请求接口对象封装类</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月10日
 */
public abstract class BaseRequest extends AbsHttpRequest {

	protected Context context;

	protected LoginBaseRequest baseRequest;

	public BaseRequest(Context context) {
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

//		baseRequest.setReferrer(PyLoginHelper.takeReferrer(context, ""));


		baseRequest.setAdvertisingId(GoogleUtil.getAdvertisingId(this.context));

		return baseRequest;
	}

	@Override
	public <T> void onHttpSucceess(T responseModel) {

	}

	@Override
	public void onTimeout(String result) {

	}

	@Override
	public void onNoData(String result) {

	}


}
