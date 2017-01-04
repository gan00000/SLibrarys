package com.efun.ads.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.efun.core.tools.EfunStringUtil;

public class EfunJsonUtil {
	
	public static String adsJsonCode(String json){
		if (EfunStringUtil.isEmpty(json)) {
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
