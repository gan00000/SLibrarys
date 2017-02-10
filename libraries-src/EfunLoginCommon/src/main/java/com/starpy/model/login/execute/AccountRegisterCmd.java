package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.core.base.utils.SStringUtil;
import com.starpy.model.login.bean.request.AccountRegRequest;

import java.util.Map;
//1000成功
//1001註冊登入成功
public class AccountRegisterCmd extends EfunBaseCmd {

	private AccountRegRequest regRequest;

	private static final long serialVersionUID = 1L;

	public AccountRegisterCmd(Context context, String userName, String password) {
		super(context);
		regRequest = new AccountRegRequest(context);
		baseRequest = regRequest;

		regRequest.setName(userName);

		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		regRequest.setPwd(password);

		regRequest.setCompleteUrl("http://10.10.10.110:8080/login/register");
		regRequest.setAppKey("test123");
		regRequest.setGameCode("test");
		regRequest.setGameLanguage("tw");

		regRequest.setSignature(SStringUtil.toMd5(regRequest.getAppKey() + regRequest.getTimestamp() +
			regRequest.getName() + regRequest.getPwd() + regRequest.getGameCode()));
	}


	@Override
	public void execute() throws Exception {
		super.execute();

		mResponse = doRequest(regRequest);
		handleLoginResult();
		saveLoginReponse(mResponse);
	}

}
