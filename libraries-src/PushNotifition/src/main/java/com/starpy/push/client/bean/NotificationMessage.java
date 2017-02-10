package com.starpy.push.client.bean;

import java.io.Serializable;

public class NotificationMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean showMessageContent;
	
	private String content;
	private String gameCode;
	private String messageId;
	private String title;
	private String type;
	private String packageName;
	private String advertiser;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAdvertiser() {
		return advertiser;
	}
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}
	
	public boolean isShowMessageContent() {
		return showMessageContent;
	}
	public void setShowMessageContent(boolean showMessageContent) {
		this.showMessageContent = showMessageContent;
	}

	
}
