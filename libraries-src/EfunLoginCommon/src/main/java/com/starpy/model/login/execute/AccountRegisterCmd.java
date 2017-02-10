package com.starpy.model.login.execute;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.starpy.model.login.bean.request.AccountRegRequest;

import java.util.Map;

public class AccountRegisterCmd extends EfunBaseCmd {

	private AccountRegRequest regRequest;

	private static final long serialVersionUID = 1L;

	public AccountRegisterCmd(Context context, String userName, String password) {
		super(context);
		regRequest = new AccountRegRequest(context);
		regRequest.setName(userName);
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		regRequest.setPwd(password);
	}


	@Override
	public void execute() throws Exception {
		super.execute();

		Map<String, String> nameValuePairs = regRequest.buildPostMapInField();
		if (nameValuePairs == null) {
			return;
		}

		mResponse = doRequest(regRequest);
		handleLoginResult();
		saveLoginReponse(mResponse);
	}

}
