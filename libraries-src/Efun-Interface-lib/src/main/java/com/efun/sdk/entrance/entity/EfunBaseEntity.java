package com.efun.sdk.entrance.entity;

public class EfunBaseEntity extends EfunEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String userId;
	
	private String serverCode;
	private String serverName;
	
	private String roleId;
	private String roleName;
	private String roleLevel;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the serverCode
	 */
	public String getServerCode() {
		return serverCode;
	}
	/**
	 * @param serverCode the serverCode to set
	 */
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return the roleLevel
	 */
	public String getRoleLevel() {
		return roleLevel;
	}
	/**
	 * @param roleLevel the roleLevel to set
	 */
	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	
	
}
