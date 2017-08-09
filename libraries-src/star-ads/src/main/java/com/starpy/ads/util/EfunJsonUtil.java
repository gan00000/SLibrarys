package com.starpy.sdk.ads.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.core.base.utils.SStringUtil;

public class EfunJsonUtil {
	
	public static String adsJsonCode(String json){
		if (SStringUtil.isEmpty(json)) {
			return null;
		}
		String code = "";
		try {
			JSONObject jsonObject = new JSONObject(json);
		    code = jsonObject.optString("code", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return code;
	}
}
