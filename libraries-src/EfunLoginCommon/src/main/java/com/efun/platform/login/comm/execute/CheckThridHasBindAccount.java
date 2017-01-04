package com.efun.platform.login.comm.execute;

import com.efun.platform.login.comm.dao.impl.EfunCheckThirdIsBindEfunAccountImpl;

import android.content.Context;

/**
 * 检查第三方是否绑定了efun或者其他第三方账号
 * @author Efun
 *
 */
public class CheckThridHasBindAccount extends EfunBaseCmd {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CheckThridHasBindAccount(Context context, String userId) {
		super(context,new EfunCheckThirdIsBindEfunAccountImpl());

		this.listenerParameters.setEfunUserId(userId);
	}
	
	public CheckThridHasBindAccount(Context context, String userId, String gameCode, String language) {
		super(context,new EfunCheckThirdIsBindEfunAccountImpl());

		this.listenerParameters.setEfunUserId(userId);
		this.language = language;
		this.gameCode = gameCode;
	}


	@Override
	public void execute() throws Exception {
		super.execute();
	}
}
