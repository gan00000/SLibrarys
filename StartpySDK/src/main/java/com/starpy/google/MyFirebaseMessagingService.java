package com.starpy.google;

import com.core.base.utils.PL;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by gan on 2018/4/11.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null){
            return;
        }
        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        Map<String, String> data = remoteMessage.getData();
        PL.d("Notification Message Body: " + body);
        PL.d("Notification Message data: " + data);
        PL.d("Notification Message title: " + title);
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
