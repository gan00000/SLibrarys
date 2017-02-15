package com.core.base.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class JsonUtil {


    public static String getValueByKey(Context context, String jsonStr, String key) {
        return  getValueByKey(context, jsonStr, key,"");
    }


    public static String getValueByKey(Context context, String jsonStr, String key, String defaultValue) {

        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (jsonObject != null && jsonObject.has(key)) {
                    String value = jsonObject.optString(key, defaultValue);
                    if (!TextUtils.isEmpty(value)) {
                        return value;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static boolean isJson(String str) {
        try {
            JSONObject j = new JSONObject(str);
            return true;
        } catch (JSONException e) {
            //e.printStackTrace();
            PL.i("str is not json");
        }
        return false;
    }

    public static String map2jsonString(Map<String, String> map) throws JSONException {
        if (map == null || map.isEmpty()) {
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey())) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }

        return jsonObject.toString();
    }
}
