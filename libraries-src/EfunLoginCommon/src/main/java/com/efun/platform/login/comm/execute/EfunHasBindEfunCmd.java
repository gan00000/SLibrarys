package com.efun.platform.login.comm.execute;

import com.efun.core.tools.EfunLocalUtil;
import com.efun.platform.login.comm.dao.impl.EfunHasBindEfunImpl;

import android.content.Context;


/**
* <p>Title: EfunHasBindEfunCmd</p>
* <p>Description: 检查免注册登陆次数，用来提示或者强制玩家绑定efun账号</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年12月14日
*/
public class EfunHasBindEfunCmd extends EfunBaseCmd {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EfunHasBindEfunCmd(Context context,String partner) {
		super(context,new EfunHasBindEfunImpl());
		
		//6.0系统无法获取mac，使用植入SD卡uuid代替
		String thirdPlateId = EfunLocalUtil.getEfunUUid(context);
		listenerParameters.setThirdPlateId(thirdPlateId);
		listenerParameters.setThirdPlate("mac");
		listenerParameters.setPartner(partner);
		
	}
	

	@Override
	public void execute() throws Exception {
		super.execute();
	}

}
