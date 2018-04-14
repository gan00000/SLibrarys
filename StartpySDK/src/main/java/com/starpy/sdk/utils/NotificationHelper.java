/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.starpy.sdk.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.core.base.utils.SStringUtil;
import com.starpy.data.NotificationContent;

import java.util.List;

/**
 * Helper class to manage notification channels, and create notifications.
 */
public class NotificationHelper {

    private NotificationManager manager;
    public static final String STARPY_CHANNE = "STARPY_CHANNE";

    private Context context;

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param ctx The application context
     */
    public NotificationHelper(Context ctx) {
        context = ctx;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(STARPY_CHANNE, "PRIMARY_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel);
        }

    }

    /**
     * Get a notification of type 1
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @return the builder as it keeps a reference to the notification (since API 24)
     *
     * 必需的通知内容
    Notification 对象必须包含以下内容：

    小图标，由 setSmallIcon() 设置
    标题，由 setContentTitle() 设置
    详细文本，由 setContentText() 设置


     */
    public boolean show(NotificationContent notificationContent) {

        if (notificationContent == null || SStringUtil.isEmpty(notificationContent.getContentTitle()) ||
                SStringUtil.isEmpty(notificationContent.getContentText())){
            return false;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,STARPY_CHANNE)
                .setSmallIcon(notificationContent.getSmallIcon())
                .setContentTitle(notificationContent.getContentTitle())
                .setContentText(notificationContent.getContentText());

        if (notificationContent.getLargeIcon() != null){
            mBuilder.setLargeIcon(notificationContent.getLargeIcon());
        }

        //处理多行消息
        List<String> lineStrings = notificationContent.getLineStrings();
        if (lineStrings != null && !lineStrings.isEmpty()){
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(notificationContent.getBigContentTitle());

            for (String line:lineStrings) {
                inboxStyle.addLine(line);
            }
            mBuilder.setNumber(lineStrings.size());
            mBuilder.setStyle(inboxStyle);

        }else {
            //单行消息
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(notificationContent.getBigContentTitle());
            bigTextStyle.bigText(notificationContent.getContentText());
            mBuilder.setStyle(bigTextStyle);
        }

        mBuilder.setAutoCancel(false);
        //mBuilder.setGroupSummary(true);
        mBuilder.setLights(0xff00ff00, 300, 1000);

        if (notificationContent.getClickIntent() != null){//设置点击事件

            PendingIntent clickPendIntent = PendingIntent.getBroadcast(context, 0, notificationContent.getClickIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(clickPendIntent);
        }

        if (notificationContent.getDeleteIntent() != null){//设置删除事件

            PendingIntent deletePendIntent = PendingIntent.getBroadcast(context, 0, notificationContent.getDeleteIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setDeleteIntent(deletePendIntent);
        }
        getManager().notify(notificationContent.getNotifyId(), mBuilder.build());

        return true;
    }


    /**
     * Get the notification manager.
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
