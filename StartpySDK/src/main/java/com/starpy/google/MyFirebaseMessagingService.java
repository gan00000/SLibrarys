package com.starpy.google;

import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.starpy.data.NotificationContent;
import com.starpy.sdk.utils.NotificationHelper;

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

        if (SStringUtil.isEmpty(title)){
            title = getPackageName() + " message";
        }

        NotificationContent notificationContent = new NotificationContent(getApplicationContext());
        notificationContent.setContentTitle(title);
        notificationContent.setContentText(body);

        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        notificationHelper.show(notificationContent);
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
