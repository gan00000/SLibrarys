/*
package com.core.base.beans;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

*/
/**
 * 记录日志的类
 * Created by Efun on 2016/10/25.
 *//*


public class EfunLogEntity implements Serializable{

    */
/** 日志时间戳 *//*

    private String timestamp;
    */
/** 事件 *//*

    private String event;
    */
/** 日志信息 *//*

    private Map<String, String> info;
    */
/** 附加信息 *//*

    private String remark;

    */
/**
     * 初始化一个日志的实体，该实体会自动获取系统时间戳，用于标识日志时间
     * @param event 事件
     * @param info 信息
     * @param remark 附加信息
     *//*

    public EfunLogEntity(String event, Map<String, String> info, String remark){
        this.timestamp = getTimestampText();
        this.event = event;
        this.info = info;
        this.remark = remark;
    }

    public String getTimestamp() {
        return timestamp;
    }

    */
/**
     * 获取固定时间格式的时间串<br>
     *     时间格式：yyyy-MM-dd hh:mm:ss
     * @return 固定时间格式的时间串
     *//*

    public String getTimestampText(){
        long timestamp = System.currentTimeMillis();
        String formatDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if(timestamp > 0){
            formatDate = formatter.format(new Date(timestamp));
        }
        return formatDate;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toString(){
        String json = "";
        try {
            json = new Gson().toJson(this);
        }catch (Exception | Error ex){
            ex.printStackTrace();
            json = "timestamp=" + timestamp + ", event=" + event +
                    ", info:{" + (info == null ? "null" : info.toString()) + ", remark=" + remark;
        }
        return json + "\n";
    }
}
*/
