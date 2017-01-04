package com.efun.ads.event;

import java.io.Serializable;

public class SdkEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @return the serverCode
	 */
	public String getServerCode() {
		return serverCode;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param serverCode the serverCode to set
	 */
	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public SdkEvent() {
		
	}
	
	private String gameCode = "";
	private String serverCode = "";
	private String parentEvent = "";
	private String childEvent = "";
	private String userId = "";
	private String roleId = "";
	private String roleName = "";
	private String roleLevel = "";
	/**
	 * @return the parentEvent
	 */
	public String getParentEvent() {
		return parentEvent;
	}
	/**
	 * @param parentEvent the parentEvent to set
	 */
	public void setParentEvent(String parentEvent) {
		this.parentEvent = parentEvent;
	}
	/**
	 * @return the childEvent
	 */
	public String getChildEvent() {
		return childEvent;
	}
	/**
	 * @param childEvent the childEvent to set
	 */
	public void setChildEvent(String childEvent) {
		this.childEvent = childEvent;
	}
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
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	
	
}
