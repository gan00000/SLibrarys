package com.starpy.model.login.execute;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.utils.SPUtil;
import com.core.base.utils.ApkInfoUtil;
import com.core.base.utils.EfunLogUtil;

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

	public EfunThirdPlatLoginOrRegCmd2(Context context, String thirdPlateId, String thirdPlate,	String bussessIds) {
		super(context);
		
		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("mac")){//6.0系统无法获取mac，使用植入SD卡uuid代替
			thirdPlateId = ApkInfoUtil.getEfunUUid(context);
		}
		

		if (!TextUtils.isEmpty(thirdPlate) && thirdPlate.equals("fb")) {
			if (TextUtils.isEmpty(bussessIds)) {
				//throw new IllegalArgumentException("FB login must have bussess Ids, now Ids is empty");
				EfunLogUtil.logE("FB login must have bussess Ids, now Ids is empty");
			}
			String fbTokenBusiness = SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,FB_TOKEN_FOR_BUSINESS);
		}
	
	}



	@Override
	public void execute() throws Exception {
		super.execute();	
		saveLoginReponse(mResponse);
	}

}
