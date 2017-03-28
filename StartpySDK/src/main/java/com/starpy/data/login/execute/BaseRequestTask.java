/**
 * 
 */
package com.starpy.data.login.execute;

import android.content.Context;

import com.core.base.request.AbsHttpRequest;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.utils.PL;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.cfg.ResConfig;
import com.starpy.data.login.request.LoginBaseRequest;

/**
 * <p>Title: BaseRequestTask</p>
 * <p>Description: 请求接口对象封装类</p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月10日
 */
public abstract class BaseRequestTask extends AbsHttpRequest {

	protected Context context;

	protected LoginBaseRequest baseRequest;

	public BaseRequestTask(Context context) {
		this.context = context;
	}


	@Override
	public BaseReqeustBean createRequestBean() {
		if (this.context == null) {
			PL.d("execute context is null");
			return null;
		}

		if (SStringUtil.isEmpty(baseRequest.getRequestUrl())) {
			baseRequest.setRequestUrl(ResConfig.getLoginPreferredUrl(context));
		}
		if (SStringUtil.isEmpty(baseRequest.getRequestSpaUrl())) {
			baseRequest.setRequestSpaUrl(ResConfig.getLoginSpareUrl(context));
		}
		if (SStringUtil.isEmpty(baseRequest.getGameCode())) {
			baseRequest.setGameCode(ResConfig.getGameCode(context));
		}
		if (SStringUtil.isEmpty(baseRequest.getAppKey())) {
			baseRequest.setAppKey(ResConfig.getAppKey(context));
		}


		if(SStringUtil.isEmpty(baseRequest.getGameLanguage())){
			baseRequest.setGameLanguage(ResConfig.getGameLanguage(context));
		}

//		baseRequest.setReferrer(PyLoginHelper.takeReferrer(context, ""));


//		baseRequest.setAdvertisingId(GoogleUtil.getAdvertisingId(this.context));

		return baseRequest;
	}

	@Override
	public <T> void onHttpSucceess(T responseModel) {

	}

	@Override
	public void onTimeout(String result) {
		PL.i("onTimeout");
		ToastUtils.toast(context, "connect timeout, please try again");
	}

	@Override
	public void onNoData(String result) {
		PL.i("onNoData");
	}


}
