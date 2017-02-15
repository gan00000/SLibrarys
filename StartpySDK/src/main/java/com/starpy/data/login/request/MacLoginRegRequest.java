package com.starpy.data.login.request;

import android.content.Context;

/**
* <p>Title: LoginBaseRequest</p>
* <p>Description: 接口请求参数实体</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年8月22日
*/
public class MacLoginRegRequest extends LoginBaseRequest{

	public MacLoginRegRequest(Context context) {
		super(context);
	}

	private String uniqueId;


	/**
	 * registPlatform 第三方登陆平台的标识符
	 */
	private String registPlatform;


	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getRegistPlatform() {
		return registPlatform;
	}

	public void setRegistPlatform(String registPlatform) {
		this.registPlatform = registPlatform;
	}
}
