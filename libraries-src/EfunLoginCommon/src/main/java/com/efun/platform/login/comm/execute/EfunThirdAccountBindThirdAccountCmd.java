package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.text.TextUtils;

import com.efun.core.tools.EfunLocalUtil;
import com.efun.platform.login.comm.dao.impl.EfunThirdAccountBindThirdAccountImpl;


/**
* <p>Title: EfunThirdAccountBindThirdAccountCmd</p>
* <p>Description: 第三方绑定第三方接口封装</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年9月16日
*/
public class EfunThirdAccountBindThirdAccountCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunThirdAccountBindThirdAccountCmd(Context context, String efunUserId, String thirdPlateId, String thirdPlate, String advertiser, String partner) {
		super(context,new EfunThirdAccountBindThirdAccountImpl());
		
		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("mac")){//6.0系统无法获取mac，使用植入SD卡uuid代替
			thirdPlateId = EfunLocalUtil.getEfunUUid(context);
		}
		
		listenerParameters.setThirdPlateId(thirdPlateId);
		listenerParameters.setThirdPlate(thirdPlate);
		listenerParameters.setEfunUserId(efunUserId);
		
		listenerParameters.setAdvertisersName(advertiser);
		listenerParameters.setPartner(partner);
		
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
