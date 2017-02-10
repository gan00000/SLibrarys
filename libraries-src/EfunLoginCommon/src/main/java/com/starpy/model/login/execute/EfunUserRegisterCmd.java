package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.starpy.base.utils.EfunLogUtil;
import com.starpy.base.utils.SStringUtil;
import com.starpy.model.login.constant.DomainSuffix;
import com.starpy.model.login.constant.SLoginType;
import com.starpy.model.login.efun.EfunParamsBuilder;

import java.util.Map;

public class EfunUserRegisterCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EfunUserRegisterCmd(Context context, String userName, String password, String advertisersName, String partnerName, String platForm) {
		super(context);

		listenerParameters.setUserName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(SStringUtil.toMd5(password, false));
		listenerParameters.setPartner(partnerName);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setAdvertisersName(advertisersName);
		this.loginType = SLoginType.LOGIN_TYPE_EFUN;
	}


	@Override
	public void execute() throws Exception {
		super.execute();

		if (listenerParameters == null) {
			return;
		}

		EfunLogUtil.logD(listenerParameters.toString());
		Map<String, String> nameValuePairs = EfunParamsBuilder.buildRegister(listenerParameters);
		if (nameValuePairs == null) {
			return;
		}

		mResponse = doRequest(DomainSuffix.URL_LOGIN_REGISTER, nameValuePairs);
		handleLoginResult();
		saveLoginReponse(mResponse);
	}

}
