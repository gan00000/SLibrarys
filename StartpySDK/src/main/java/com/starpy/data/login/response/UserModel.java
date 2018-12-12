package com.starpy.data.login.response;

import com.core.base.bean.BaseResponseModel;

public class UserModel extends BaseResponseModel {

    private String inviteesUserId; //邀请我进游戏的用户id


    public String getInviteesUserId() {
        return inviteesUserId;
    }

    public void setInviteesUserId(String inviteesUserId) {
        this.inviteesUserId = inviteesUserId;
    }
}
