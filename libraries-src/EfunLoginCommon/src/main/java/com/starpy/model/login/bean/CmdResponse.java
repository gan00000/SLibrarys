package com.starpy.model.login.bean;

public class CmdResponse {
	
	private String code = "";
	private String message = "";
	private String rawResponse;//原始数据
	
	
	

	//7)查询第三方帐号绑定状态(hasBindEfun)(適用於免註冊)
	private String loginTimes = "";//oginTimes  免注册登陆的当下次数（0表示没有登陆过）
	private String userName = "";//免注册绑定的名字（没有绑定的用户名是空的）
	
	/**
	 * @return the rawResponse
	 */
	public String getRawResponse() {
		return rawResponse;
	}
	/**
	 * @param rawResponse the rawResponse to set
	 */
	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}
	
	/**
	 * efun uid
	 */
	private String userId = "";
	/**
	 * 时间戳
	 */
	private String timestamp = "";

	/**
	 * 签名
	 */
	private String sign = "";
	
	
	/**
	 * area 游戏区域
	 */
	private String region = "";
	
	/**
	 * 第三方登入的三方id
	 */
	private String thirdPlateId = "";
	/**
	 * 登入类型
	 */
	private String loginType = "";
	/**
	 * @return the userId
	 */
	
	
	/**
	 * @return the code 获取服务端返回的code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return the message 获取服务端返回的message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the loginTimes mac登录次数
	 */
	public String getLoginTimes() {
		return loginTimes;
	}
	/**
	 * @return the userName  mac绑定的用户名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @param loginTimes the loginTimes to set
	 */
	public void setLoginTimes(String loginTimes) {
		this.loginTimes = loginTimes;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the userId 用户uid
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the timestamp 时间戳
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @return the sign 签名信息
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @return the region 地区
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @return the thirdPlateId 三方id
	 */
	public String getThirdPlateId() {
		return thirdPlateId;
	}
	/**
	 * @return the loginType 登录类型
	 */
	public String getLoginType() {
		return loginType;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @param thirdPlateId the thirdPlateId to set
	 */
	public void setThirdPlateId(String thirdPlateId) {
		this.thirdPlateId = thirdPlateId;
	}
	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	/**
	 * <p>Description: 判断是否绑定efun账号成功</p>
	 * @return
	 * @date 2016年1月9日
	 */
	public boolean isBindEfunAccount(){
		return code.equals("1015");
	}
	
	/**
	 * <p>Description: 曾经是否已使用过mac登录</p>
	 * @return
	 * @date 2016年1月9日
	 */
	public boolean alreadyMacLogin(){
		return code.equals("1000");
	}
	
	@Override
	public String toString() {
		return "CmdReponse [code=" + code + ", message=" + message + ", loginTimes=" + loginTimes + ", userName=" + userName + ", userId=" + userId
				+ ", timestamp=" + timestamp + ", sign=" + sign + ", region=" + region + ", thirdPlateId=" + thirdPlateId + ", loginType=" + loginType
				+ "]";
	}

	
	
}
