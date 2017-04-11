package com.facebook.sfb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

	public static List<InviteFriend> parseInviteFriendsJson(JSONObject inviteFriendsJson){
		
		List<InviteFriend> inviteFriends = new ArrayList<InviteFriend>();
		if (inviteFriendsJson != null) {
			JSONArray jsonArray = inviteFriendsJson.optJSONArray("data");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject friendsItem = jsonArray.optJSONObject(i);
					String name = friendsItem.optString("name", "");
					String id = friendsItem.optString("id", "");
					JSONObject friendsItemPicture = friendsItem.optJSONObject("picture");
					
					InviteFriendPicture inviteFriendPicture = new InviteFriendPicture();
					if (friendsItemPicture != null) {
						JSONObject friendsItemPictureData = friendsItemPicture.optJSONObject("data");
						if (friendsItemPictureData != null) {
							int height = friendsItemPictureData.optInt("height", 0);
							int width = friendsItemPictureData.optInt("width", 0);
							String url = friendsItemPictureData.optString("url", "");
							boolean is_silhouette = friendsItemPictureData.optBoolean("is_silhouette", false);
							
							inviteFriendPicture.setHeight(height);
							inviteFriendPicture.setWidth(width);
							inviteFriendPicture.setIs_silhouette(is_silhouette);
							inviteFriendPicture.setUrl(url);
							
						}
					}
					
					InviteFriend inviteFriend = new InviteFriend();
					inviteFriend.setName(name);
					inviteFriend.setId(id);
					inviteFriend.setInviteFriendPicture(inviteFriendPicture);
					
					inviteFriends.add(inviteFriend);
				}
			}
		}
		return inviteFriends;
	}
}
