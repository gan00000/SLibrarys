package com.starpy.thirdlib.facebook;

import android.text.TextUtils;

public class FriendProfile {

	public FriendProfile() {
		// TODO Auto-generated constructor stub
	}
	
	
	private String name;
	private String id;

	private String iconUrl;
	private String userId;

	private FriendPicture friendPicture;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the friendPicture
	 */
	public FriendPicture getFriendPicture() {
		return friendPicture;
	}

	/**
	 * @param friendPicture the friendPicture to set
	 */
	public void setFriendPicture(FriendPicture friendPicture) {
		this.friendPicture = friendPicture;
		this.iconUrl = friendPicture.getUrl();
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isStarpyUser() {
		return !TextUtils.isEmpty(userId);
	}


}
