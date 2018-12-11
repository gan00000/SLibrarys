package com.starpy.data.login.response;

import com.core.base.bean.BaseResponseModel;

import java.util.List;

public class UserListModel extends BaseResponseModel {

    private List<User> users;

    public List<User> getUserList() {
        return users;
    }

    public void setUserList(List<User> userList) {
        this.users = userList;
    }
}
