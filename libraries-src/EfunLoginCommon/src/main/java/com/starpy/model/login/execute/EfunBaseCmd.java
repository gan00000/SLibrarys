/**
 * 
 */
package com.starpy.model.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.res.SConfig;
import com.core.base.task.command.abstracts.EfunCommand;
import com.core.base.utils.GoogleAdUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.base.bean.BaseReqeustBean;
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
public abstract class EfunBaseCmd extends EfunCommand {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected Context context;

	protected LoginBaseRequest baseRequest;

	protected String mResponse;

	protected CmdResponse cmdResponse;

	public EfunBaseCmd(Context context) {
		super();
		this.context = context;
	}



	/**
	 * @return the cmdResponse
	 */
	public CmdResponse getCmdResponse() {
		return cmdResponse;
	}

	/**
	 * @param cmdResponse the cmdResponse to set
	 */
	public void setCmdResponse(CmdResponse cmdResponse) {
		this.cmdResponse = cmdResponse;
	}

	@Override
	public String getResponse() {
		return mResponse;
	}

	@Override
	public void execute() throws Exception {

		if (this.context == null) {
			PL.d("execute context is null");
			return;
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
			baseRequest.setGameLanguage(SConfig.getSDKLanguage(context));
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
		
	}

	protected void handleLoginResult() {
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

	}

	protected void saveLoginReponse(String reponse){
		PyLoginHelper.saveLoginReponse(context, reponse);
	}


	/**
	 * <p>Title: doRequest</p>
	 * <p>Description: 实际网络请求</p>
	 * @return
	 */
	public String doRequest(BaseReqeustBean baseReqeustBean){
		String efunResponse = "";
		if (SStringUtil.isNotEmpty(baseReqeustBean.getCompleteUrl())) {
			HttpRequest httpRequest = new HttpRequest();
			HttpResponse hr = httpRequest.postReuqest(baseReqeustBean.getCompleteUrl(), baseReqeustBean.buildPostMapInField());
			efunResponse = hr.getResult();
			requestCompleteUrl = hr.getRequestCompleteUrl();
		}
		return efunResponse;
	}


}
