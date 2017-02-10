package com.starpy.model.login.bean;

import com.core.base.beans.EfunOutputParams;

/**
* <p>Title: LoginParameters</p>
* <p>Description: 登入回调返回给厂商的参数</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2015年1月12日
*/
/**
 * @author Efun
 *
 */
/**
 * @author Efun
 *
 */
/**
 * @author Efun
 *
 */
/**
 * @author Efun
 *
 */
public class LoginParameters extends EfunOutputParams {
	
	private static final long serialVersionUID = 1L;

	/**
	 * efun uid
	 */
	private Long userId;
	/**
	 * 时间戳
	 */
	private Long timestamp;
	/**
	 * 邮箱
	 */
	private String emailMsn;
	/**
	 * 签名
	 */
	private String sign;
	
	
	/**
	 * area 游戏区域
	 */
	private String region;
	/**
	 * fbId facebook  id
	 */
	private String fbId;
	/**
	 * 第三方登入的三方id
	 */
	private String thirdPlateId;
	/**
	 * 登入类型
	 */
	private String loginType;
	
	/**
	 * 用户头像url
	 */
	private String userIconUrl;
	
	
	/**
	 * 免注册登入状态；0：无限制，1：警告，2：禁止
	 */
	private int status; 
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the emailMsn
	 */
	public String getEmailMsn() {
		return emailMsn;
	}
	/**
	 * @param emailMsn the emailMsn to set
	 */
	public void setEmailMsn(String emailMsn) {
		this.emailMsn = emailMsn;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the fbId
	 */
	public String getFbId() {
		return fbId;
	}
	/**
	 * @param fbId the fbId to set
	 * 
	 * deprecated, please use setThirdPlateId 
	 */
	@Deprecated 
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	/**
	 * @return the thirdPlateId
	 */
	public String getThirdPlateId() {
		return thirdPlateId;
	}
	/**
	 * @param thirdPlateId the thirdPlateId to set
	 */
	public void setThirdPlateId(String thirdPlateId) {
		this.thirdPlateId = thirdPlateId;
	}
	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}
	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getUserIconUrl() {
		return userIconUrl;
	}
	public void setUserIconUrl(String userIconUrl) {
		this.userIconUrl = userIconUrl;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
