package com.core.base.callback;

/**
 * Created by Efun on 2017/1/7.
 */
public interface SavePhotoCallback extends ISCallBack {

    void onSaveSuccess(String path);
    void onSaveFailure();
}
