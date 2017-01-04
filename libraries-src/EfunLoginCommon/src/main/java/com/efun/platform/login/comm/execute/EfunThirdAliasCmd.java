package com.efun.platform.login.comm.execute;

import android.content.Context;

import com.efun.platform.login.comm.dao.impl.EfunBaseLoginDao;
import com.efun.platform.login.comm.dao.impl.EfunThirdAliasImpl;

public class EfunThirdAliasCmd extends EfunBaseCmd {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EfunThirdAliasCmd(Context context, String gameCode, String userId, String appPlatform, String language) {
		// TODO Auto-generated constructor stub
		super(context, new EfunThirdAliasImpl());
		listenerParameters.setGameCode(gameCode);
		listenerParameters.setAppPlatform(appPlatform);
		listenerParameters.setLanguage(language);
		listenerParameters.setEfunUserId(userId);
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		super.execute();
	}
	
}
