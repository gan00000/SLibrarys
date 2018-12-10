package com.starpy.data.login.response;

import com.core.base.bean.BaseResponseModel;

import java.util.List;

public class UserListModel extends BaseResponseModel {

    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
