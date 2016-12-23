package com.efun.platform.login.comm.execute;

import android.content.Context;
import android.text.TextUtils;

import com.efun.core.db.EfunDatabase;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.platform.login.comm.dao.impl.EfunThirdPlatImpl2;

/**
* <p>Title: EfunThirdPlatLoginOrRegCmd2</p>
* <p>Description: 新三方登陆&注册接口</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年9月16日
*/
public class EfunThirdPlatLoginOrRegCmd2 extends EfunBaseCmd {

	public static final String FB_TOKEN_FOR_BUSINESS = "FB_TOKEN_FOR_BUSINESS";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EfunThirdPlatLoginOrRegCmd2(Context context, String thirdPlateId, String advertisersName, String partnerName, String platForm, String thirdPlate,
			String bussessIds) {
		super(context,new EfunThirdPlatImpl2());
		
		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("mac")){//6.0系统无法获取mac，使用植入SD卡uuid代替
			thirdPlateId = EfunLocalUtil.getEfunUUid(context);
		}
		
		listenerParameters.setThirdPlateId(thirdPlateId);
		
		listenerParameters.setPartner(partnerName);
		
		listenerParameters.setThirdPlate(thirdPlate);
		listenerParameters.setPlatForm(platForm);
		listenerParameters.setAdvertisersName(advertisersName);
		
		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("fb")) {
			if (TextUtils.isEmpty(bussessIds)) {
				//throw new IllegalArgumentException("FB login must have bussess Ids, now Ids is empty");
				EfunLogUtil.logE("FB login must have bussess Ids, now Ids is empty");
			}
			String fbTokenBusiness = EfunDatabase.getSimpleString(context,EfunDatabase.EFUN_FILE,FB_TOKEN_FOR_BUSINESS);
			listenerParameters.setApps(bussessIds);
			listenerParameters.setToken_for_business(fbTokenBusiness);
		}
	
	}

	public EfunThirdPlatLoginOrRegCmd2(Context context, String thirdPlateId, String advertisersName, String partnerName, String platForm, String thirdPlate,
									   String bussessIds,String coveredThirdId,String coveredThirdPlate){
		this(context,thirdPlateId, advertisersName, partnerName, platForm, thirdPlate,bussessIds);

		listenerParameters.setCoveredThirdId(coveredThirdId);
		listenerParameters.setCoveredThirdPlate(coveredThirdPlate);

	}


	@Override
	public void execute() throws Exception {
		super.execute();	
		saveLoginReponse(mResponse);
	}

}
