package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.dao.impl.EfunBindImpl2;


public class EfunBindCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunBindCmd(Context context, String thirdPlateId, String userName, String password, String email, String thirdPlate, String platForm, String advertiser, String partner) {
		super(context,new EfunBindImpl2());
		
		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("mac")){//6.0系统无法获取mac，使用植入SD卡uuid代替
			thirdPlateId = EfunLocalUtil.getEfunUUid(context);
		}
		
		listenerParameters.setThirdPlateId(thirdPlateId);
		listenerParameters.setThirdPlate(thirdPlate);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setUserName(userName);
		
		if (password.length() > 32) {
			Toast.makeText(context, "password length must be less than 32", Toast.LENGTH_SHORT).show();
			Log.e("efunLog", "密码不能大于等于32位");
			return;
		}
		listenerParameters.setPassword(EfunStringUtil.toMd5(password, false));
		listenerParameters.setEmail(email);
		
		listenerParameters.setAdvertisersName(advertiser);
		listenerParameters.setPartner(partner);
		
	}
	

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
