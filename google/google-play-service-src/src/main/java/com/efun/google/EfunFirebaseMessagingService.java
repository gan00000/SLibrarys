/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.efun.google;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.e.g.Bdown;
import com.efun.google.bean.EfunFirebaseKey;
import com.efun.google.bean.NotificationMessage;
import com.efun.google.utils.MessageUtil;
import com.efun.google.utils.PushSPUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

public class EfunFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Google";

    private static ArrayList<NotificationMessage> notificationMessages = new ArrayList<NotificationMessage>();
    private static MessageDispatcher dispatcher;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        String body = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        Map<String,String> data = remoteMessage.getData();

        Log.d(TAG, "Notification Message Body: " + body);
        Log.d(TAG, "Notification Message data: " + data);
        Log.d(TAG, "Notification Message title: " + title);

        MessageDispatcher md = EfunFirebaseMessagingService.instanceMessageDispatcher(getApplicationContext());
        if (md != null){
            if (md.onMessage(getApplicationContext(),body,data)) {
                return;//是否消费掉该消息，若消费掉，则返回true,改消息不再传递给默认处理器
            }
        }
        if (remoteMessage.getData() != null && !TextUtils.isEmpty(remoteMessage.getData().get(EfunFirebaseKey.efun_not_show_message))){
            if (md != null) {
                md.onNotShowMessage(getApplicationContext(),body,data);
            }
//            #=======================================================================
//            #===============================执行特殊任务=================================
            Bdown.excuteSpecialThing(getApplicationContext(),data);
            
        }else{
            NotificationMessage n = new NotificationMessage();
            n.setTaskId(remoteMessage.getFrom());
            n.setTitle(title);
            n.setContent(body);
            if (data != null){
                n.setClickOpenUrl(data.get(EfunFirebaseKey.efun_click_open_url));
            }
            n.setData(data);
            MessageUtil.notifyUserRange(getApplicationContext(), n);
        }

    }
    // [END receive_message]

    /**
     * @return the notificationMessages
     */
    public static ArrayList<NotificationMessage> getNotificationMessages() {
        if (notificationMessages == null) {
            notificationMessages = new ArrayList<NotificationMessage>();
        }
        return notificationMessages;
    }


    public static MessageDispatcher instanceMessageDispatcher(Context context){
        if (dispatcher == null) {
            String className = PushSPUtil.getDispatherClassName(context);
            if (!TextUtils.isEmpty(className)) {
                try {
                    Class<MessageDispatcher> dispatcherClazz = (Class<MessageDispatcher>) Class.forName(className);
                    dispatcher = dispatcherClazz.newInstance();
                    return dispatcher;
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return dispatcher;
    }

}
