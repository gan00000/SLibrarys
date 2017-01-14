package com.efun.sdk.entrance.entity;

public final class EfunPlatformEntity extends EfunBaseEntity {
	private static final long serialVersionUID = 1L;

	/*flag statu: 1. 登陆成功后
    2. 切换服务器登出
    3. 切换角色登出
    4. home键切换到后台
    5. 后台切换到游戏里
    6. 退出游戏
    7. 切换服务器登陆
    8. 切换角色登陆*/
	
	public final int LoginSuccess 					= 1;
	public final int LogoutChangeServer				= 2;
	public final int LogoutChangeRole 				= 3;
	public final int ToBackgroundByHome 			= 4;
	public final int ToGameFromBackground 			= 5;
	public final int ExitGameApp 					= 6;
	public final int LoginSuccessChangeServer 		= 7;
	public final int LoginSuccessChangeRole 		= 8;
	
	/**
	 * 
	 */
	
	private String creditId;
	private String remark;
	private int platformStatu;
	/**
	 * @return the creditId
	 */
	public String getCreditId() {
		return creditId;
	}
	/**
	 * @param creditId the creditId to set
	 */
	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getPlatformStatu() {
		return platformStatu;
	}
	public void setPlatformStatu(int platformStatu) {
		this.platformStatu = platformStatu;
	}

	
}
