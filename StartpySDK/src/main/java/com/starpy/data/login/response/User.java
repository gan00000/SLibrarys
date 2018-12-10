package com.starpy.data.login.response;

public class User {

    private String userId = "";
    private String fbName = "";
    private String fbPictureUrl = "";


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getFbPictureUrl() {
        return fbPictureUrl;
    }

    public void setFbPictureUrl(String fbPictureUrl) {
        this.fbPictureUrl = fbPictureUrl;
    }
}
