package com.efun.platform.login.comm.execute;

import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunBindCheckMessageImpl;

import android.content.Context;

public class EfunBindCheckMessageCmd extends EfunBaseCmd{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunBindCheckMessageCmd(Context mContext, String userName, String password, String userId, String encode) {
		super(mContext,new EfunBindCheckMessageImpl());
		listenerParameters.setUserName(userName);
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		
		listenerParameters.setEfunUserId(userId);
		listenerParameters.setEncode(encode);
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();		
	}
	
}
