package com.starpy.model.login.bean.request;

import android.content.Context;

import com.core.base.utils.SStringUtil;

/**
* <p>Title: LoginBaseRequest</p>
* <p>Description: 用户登录请求参数实体</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年8月22日
*/
public class AccountLoginRequest extends LoginBaseRequest{

	public AccountLoginRequest(Context context) {
		super(context);
	}

	private String name;//用户账号名
	private String pwd;
	private String newPwd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = SStringUtil.toMd5(pwd);
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = SStringUtil.toMd5(newPwd);
	}
}
