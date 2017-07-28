package com.starpy.data.login.request;

import android.content.Context;

/**
* <p>Title: LoginBaseRequest</p>
* <p>Description: 用户登录请求参数实体</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年8月22日
*/
public class FindPwdRequest extends LoginBaseRequest{

	public FindPwdRequest(Context context) {
		super(context);
	}


	private String name;//用户账号名
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
