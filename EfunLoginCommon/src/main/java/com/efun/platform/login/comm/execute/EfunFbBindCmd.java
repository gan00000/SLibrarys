/*package com.efun.platform.login.comm.execute;

import android.content.Context;

import com.efun.platform.login.comm.dao.impl.EfunBindImpl2;


public class EfunFbBindCmd extends EfunBaseCmd {
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;
	
	private Context context;
	
	private String mResponse;
	
	public EfunFbBindCmd(Context context, String thirdPlateId, String userName, String password, String email, String thirdPlate, String platForm, String advertiser, String partner) {
		super(context,new EfunBindImpl2());
		this.context = context;
		listenerParameters.setThirdPlateId(thirdPlateId);
		listenerParameters.setThirdPlate(thirdPlate);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setUserName(userName);
		listenerParameters.setPassword(password);
		listenerParameters.setEmail(email);
		
		listenerParameters.setAdvertisersName(advertiser);
		listenerParameters.setPartner(partner);
		
	}
	
	
	@Override
	public String getResponse(){
		return this.mResponse;
	}

	@Override
	public void execute() throws Exception {
		super.execute();
		this.mResponse = dao.efunBind();
	}

}
*/