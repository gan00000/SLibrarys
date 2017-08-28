package com.starpy.sdk.plat.data.bean.reqeust;

import android.content.Context;

import com.starpy.data.SGameBaseRequestBean;

/**
 * Created by gan on 2017/8/22.
 */

public class MessageReadBean extends SGameBaseRequestBean {
    public MessageReadBean(Context context) {
        super(context);
    }

    private String readStatus;

    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }



}
