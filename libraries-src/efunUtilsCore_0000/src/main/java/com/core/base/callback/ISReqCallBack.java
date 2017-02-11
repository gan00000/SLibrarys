package com.core.base.callback;

/**
 * Created by Efun on 2017/2/11.
 */
public interface ISReqCallBack<T> extends ISCallBack<T> {

    void callBack(T t);

}
