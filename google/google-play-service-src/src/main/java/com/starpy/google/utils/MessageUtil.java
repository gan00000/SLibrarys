package com.starpy.google.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.core.base.utils.PL;
import com.starpy.google.EfunFirebaseMessagingService;
import com.starpy.google.EfunPushReceiver;
import com.starpy.google.bean.NotificationMessage;

/**
 * Created by Efun on 2016/6/27.
 */
public class MessageUtil {

    public static final String EFUN_PUSH_OPEN_BROWSER_URL = "EFUN_PUSH_OPEN_BROWSER_URL";

    public static final String EFUN_PUSH_MESSAGE_ACTION_KEY = "EFUN_PUSH_MESSAGE_ACTION_KEY";

    public static final int CLICK_INTENT_NOTIFICATION = 201;
    public static final int DELETE_INTENT_NOTIFICATION = 202;

    public static void notifyUserRange(Context context, NotificationMessage notificationMessage) {

        String messageInfo = notificationMessage.getContent();
        String title = notificationMessage.getTitle();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(messageInfo)) {
            return;
        }

        EfunFirebaseMessagingService.getNotificationMessages().add(notificationMessage);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationMessage.getTitle())
                .setContentText(notificationMessage.getContent());

        int mIcon = PushSPUtil.takePullIcon(context, -1);

        if (mIcon != -1 && mIcon != 0) {
            mBuilder.setSmallIcon(mIcon);
        } else {
            PL.d("没有设置推送图标，使用app icon图标");
            mBuilder.setSmallIcon(context.getApplicationInfo().icon);
        }

        if (EfunFirebaseMessagingService.getNotificationMessages() != null && EfunFirebaseMessagingService.getNotificationMessages().size() == 1) {
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.bigText(notificationMessage.getContent());
            bigTextStyle.setBigContentTitle(notificationMessage.getTitle());
            mBuilder.setStyle(bigTextStyle);
        }else if(EfunFirebaseMessagingService.getNotificationMessages() != null){

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            // Sets a title for the Inbox in expanded layout
            inboxStyle.setBigContentTitle(EfunFirebaseMessagingService.getNotificationMessages().size() + " new messages:");
            // Moves events into the expanded layout
            for (int i = 0; i < EfunFirebaseMessagingService.getNotificationMessages().size(); i++) {

                inboxStyle.addLine(EfunFirebaseMessagingService.getNotificationMessages().get(i).getTitle());

            }
            // Moves the expanded layout object into the notification object.
            mBuilder.setStyle(inboxStyle);

        }
        mBuilder.setAutoCancel(true);
        //mBuilder.setGroupSummary(true);
        //mBuilder.setLights(0xff00ff00, 300, 1000);
        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        mBuilder.setNumber(EfunFirebaseMessagingService.getNotificationMessages().size());

        //设置清除消息的intent
        Intent deleteIntent = new Intent(EfunPushReceiver.NOTIFICATION_DELETE);
        deleteIntent.setPackage(context.getPackageName());
        deleteIntent.putExtra(EFUN_PUSH_MESSAGE_ACTION_KEY, DELETE_INTENT_NOTIFICATION);
        PendingIntent deletePendIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setDeleteIntent(deletePendIntent);

        Intent clickIntent = new Intent(EfunPushReceiver.NOTIFICATION_CLICK);
        clickIntent.putExtra(EFUN_PUSH_MESSAGE_ACTION_KEY, CLICK_INTENT_NOTIFICATION);
        clickIntent.setPackage(context.getPackageName());
        PendingIntent clickPendIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(clickPendIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(100, mBuilder.build());

    }


    public static void openBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        // Uri 是统一资源标识符
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void startAppLaunchActivity(Context context) {

        String pkName = context.getPackageName();

        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);

        Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(pkName);
        if (mainIntent != null && mainIntent.getComponent() != null) {
            String className = mainIntent.getComponent().getClassName();
            it.setClassName(context, className);
        } else if (mainIntent != null) {
            it = mainIntent;
        }

        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        context.startActivity(it);
    }
}
