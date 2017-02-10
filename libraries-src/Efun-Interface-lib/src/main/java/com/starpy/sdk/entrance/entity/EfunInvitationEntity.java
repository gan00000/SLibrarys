package com.starpy.sdk.entrance.entity;

import com.starpy.sdk.entrance.constant.EfunInvitationType;

public final class EfunInvitationEntity extends EfunSocialBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EfunInvitationType invitationType;
	
	private String inviteTemplateId;
	
	private String inviteMessage;

	/**
	 * @return the invitationType
	 */
	public EfunInvitationType getInvitationType() {
		return invitationType;
	}

	/**
	 * @param invitationType the invitationType to set
	 */
	public void setInvitationType(EfunInvitationType invitationType) {
		this.invitationType = invitationType;
	}

	/**
	 * @return the inviteTemplateId
	 */
	public String getInviteTemplateId() {
		return inviteTemplateId;
	}

	/**
	 * @param inviteTemplateId the inviteTemplateId to set
	 */
	public void setInviteTemplateId(String inviteTemplateId) {
		this.inviteTemplateId = inviteTemplateId;
	}

	/**
	 * @return the inviteMessage
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * @param inviteMessage the inviteMessage to set
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}
	
	
	
}
