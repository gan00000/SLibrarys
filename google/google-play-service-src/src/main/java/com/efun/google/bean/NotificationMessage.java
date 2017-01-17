package com.efun.google.bean;

import java.io.Serializable;
import java.util.Map;

public class NotificationMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String content = "";
	private String title = "";

	private String gameCode = "";
	private String packageName = "";

	private String taskId = "";

	private String clickOpenUrl = "";


	private Map<String,String> data;
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

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getClickOpenUrl() {
		return clickOpenUrl;
	}

	public void setClickOpenUrl(String clickOpenUrl) {
		this.clickOpenUrl = clickOpenUrl;
	}
}
