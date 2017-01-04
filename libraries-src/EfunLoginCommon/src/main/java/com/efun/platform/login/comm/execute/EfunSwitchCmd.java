package com.efun.platform.login.comm.execute;

import com.efun.core.tools.EfunResourceUtil;
import com.efun.platform.login.comm.dao.impl.EfunSwitchImpl;

import android.content.Context;

public class EfunSwitchCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public EfunSwitchCmd(Context mContext) {
//		// this(mContext,"sdk");
//		this(mContext, "sdk");
//	}

	public EfunSwitchCmd(Context mContext, String typeNames) {
		super(mContext, new EfunSwitchImpl());
//		listenerParameters.setLanguage(language);
//		listenerParameters.setGameCode(gameCode);
		listenerParameters.setTypeNames(typeNames);
//		listenerParameters.setAppPlatform(appPlatform);
//		listenerParameters.setPackageName(mContext.getPackageName());
//		listenerParameters.setVersionName(versionName);
		// listenerParameters.setRegion(mRegion);
		String multi = EfunResourceUtil.findStringByName(mContext, "efunMultiLanguage");
		if (!"true".equals(multi)) {
			multi = "false";
		}
		listenerParameters.setMultiLanguage(multi);
	}

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
