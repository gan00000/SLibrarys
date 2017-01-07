package com.efun.core.callback;

/**
 * Created by Efun on 2017/1/7.
 */
public interface SavePhotoCallback extends EfunCallBack{

    void onSaveSuccess(String path);
    void onSaveFailure();
}
