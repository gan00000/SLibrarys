package com.starpy.sdk.out;

import com.starpy.data.login.response.User;

import java.util.List;

/**
 * Created by gan on 2017/4/14.
 */

public interface IRequestUserCallBack {

    void onFinish(List<User> users);
}
