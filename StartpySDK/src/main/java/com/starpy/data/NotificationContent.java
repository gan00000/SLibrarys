package com.starpy.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gan on 2018/4/14.
 */

public class NotificationContent {

    private Context context;

    private int notifyId = 1100;

    private String contentTitle;
    private String contentText;
    private int smallIcon;

    private Bitmap largeIcon;//下拉通知下来需要显示的大图标

    private String bigContentTitle;
    private List<String> lineStrings;

    private Intent deleteIntent;

    private Intent clickIntent;

    public NotificationContent(Context context) {
        this.context = context;
    }


    public void addLine(String lineText){
        if (lineStrings == null){
            lineStrings = new ArrayList<>();
        }
        lineStrings.add(lineText);
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public int getSmallIcon() {

        if (smallIcon <= 0){
            smallIcon = context.getApplicationInfo().icon;
        }
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getBigContentTitle() {
        return bigContentTitle;
    }

    public void setBigContentTitle(String bigContentTitle) {
        this.bigContentTitle = bigContentTitle;
    }

    public List<String> getLineStrings() {
        return lineStrings;
    }

    public void setLineStrings(List<String> lineStrings) {
        this.lineStrings = lineStrings;
    }

    public Intent getDeleteIntent() {
        return deleteIntent;
    }

    public void setDeleteIntent(Intent deleteIntent) {
        this.deleteIntent = deleteIntent;
    }

    public Intent getClickIntent() {
        return clickIntent;
    }

    public void setClickIntent(Intent clickIntent) {
        this.clickIntent = clickIntent;
    }
}
