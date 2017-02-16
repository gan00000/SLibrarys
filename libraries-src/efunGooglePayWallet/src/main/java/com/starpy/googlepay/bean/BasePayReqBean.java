package com.starpy.googlepay.bean;

import android.content.Context;

import com.core.base.request.bean.BaseReqeustBean;
import com.starpy.base.cfg.ResConfig;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.googlepay.constants.GooglePayContant;

public class BasePayReqBean extends BaseReqeustBean {

	private String userId;
	private String gameCode;
	private String payFrom = GooglePayContant.PAY_FROM;

	private String serverCode;
	private String serverName;
	private String roleId = "";
	private String roleName = "";

	private String extra = "";

	private String roleLevel = "";
	private String cpOrderId = "";

	private String gameLanguage;
	private String accessToken;

/*	public BasePayReqBean(Context context, String extra, String roleLevel, String cpOrderId) {
		super(context);
		this.extra = extra;
		this.roleLevel = roleLevel;
		this.cpOrderId = cpOrderId;
		init(context);

	}*/

	public BasePayReqBean(Context context) {
		super(context);

		init(context);

	}

	private void init(Context context) {
		userId = StarPyUtil.getUid(context);
		accessToken = StarPyUtil.getSdkAccessToken(context);
		gameCode = ResConfig.getGameCode(context);
		gameLanguage = ResConfig.getGameLanguage(context);

		serverCode = StarPyUtil.getServerCode(context);
		serverName = StarPyUtil.getServerName(context);
		roleName = StarPyUtil.getRoleName(context);
		roleId = StarPyUtil.getRoleId(context);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCpOrderId() {
		return cpOrderId;
	}

	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}


	public String getGameLanguage() {
		return gameLanguage;
	}

	public void setGameLanguage(String gameLanguage) {
		this.gameLanguage = gameLanguage;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
