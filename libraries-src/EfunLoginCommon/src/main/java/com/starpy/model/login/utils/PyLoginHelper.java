package com.starpy.model.login.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.utils.EfunJSONUtil;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SPUtil;
import com.starpy.model.login.bean.CmdResponse;
import com.starpy.model.login.bean.EfunPerson;
import com.starpy.model.login.bean.LoginParameters;

import org.json.JSONException;
import org.json.JSONObject;

public class PyLoginHelper {

	private static final String EFUN_FILE = "Efun.db";
	private static final String ADS_ADVERTISERS_NAME = "ADS_ADVERTISERS_NAME";
	private static final String ADS_PARTNER_NAME = "ADS_PARTNER_NAME";
	private static final String ADS_EFUN_REFERRER = "ADS_EFUN_REFERRER";

	public static class ReturnCode {
		public static final String RETURN_SUCCESS = "1000";
		public static final String ALREADY_EXIST = "1006";
		public static final String LOGIN_BACK = "1101";
		public static final String YAHOO_WEB_CANNEL = "8888";
		public static final String YAHOO_WEB_ERROR = "8889";
		public static final String THIRDPLAT_LOGIN_FAILURE = "1002";
	}

	@Deprecated
	public static LoginParameters StrToLoginParameters(String request, Object... objects) {
		LoginParameters loginParameters = null;
		String currentPage = null;
		if (null == request || "".equals(request))
			return loginParameters;
		if (objects.length > 0) {
			currentPage = (String) objects[0];
		}
		if (null == currentPage) {
			try {
				JSONObject jsonObject = new JSONObject(request);
				if (EfunJSONUtil.efunVerificationRequest(jsonObject)) {
					loginParameters = new LoginParameters();
					loginParameters
							.setCode(jsonObject.optString("code", ""));
					loginParameters.setMessage(jsonObject.optString("message", ""));
					loginParameters
							.setSign(jsonObject.optString("sign", ""));
					loginParameters.setTimestamp(jsonObject.optLong("timestamp",-1));
					loginParameters.setUserId(jsonObject.optLong("userid",-1));
					loginParameters.setStatus(jsonObject.optInt("status", -1));
				}
			} catch (JSONException e) {
				EfunLogUtil.logI("jsonObject exception");
				e.printStackTrace();
			}
		}
		return loginParameters;
	}

	public static LoginParameters paresLoginResult(String response) {
		LoginParameters loginParameters = null;
			try {
				JSONObject jsonObject = new JSONObject(response);
					loginParameters = new LoginParameters();
					loginParameters
							.setCode(jsonObject.optString("code", ""));
					loginParameters.setMessage(jsonObject.optString("message", ""));
					loginParameters
							.setSign(jsonObject.optString("sign", ""));
					loginParameters.setTimestamp(jsonObject.optLong("timestamp",-1));
					loginParameters.setUserId(jsonObject.optLong("userid",-1));
			} catch (JSONException e) {
				EfunLogUtil.logI("jsonObject exception");
				e.printStackTrace();
			}
		return loginParameters;
	}

	

	public static EfunPerson parserPersion(String result) {
		EfunPerson efunPerson = new EfunPerson();
		if (!TextUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				efunPerson.setCode(jsonObject.optString("code", ""));
				efunPerson.setMessage(jsonObject.optString("message", ""));
				efunPerson.setSign(jsonObject.optString("sign", ""));
				efunPerson.setTimestamp(jsonObject.optString("timestamp", ""));
				efunPerson.setUserId(jsonObject.optString("userid", ""));
			} catch (JSONException e) {
				EfunLogUtil.logW("jsonObject exception");
				e.printStackTrace();
			}
		}
		return efunPerson;
	}



	public static String takeAdvertisersName(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(PyLoginHelper.ADS_ADVERTISERS_NAME, "");
		}
		return sp.getString(PyLoginHelper.ADS_ADVERTISERS_NAME, defValue);
	}

	public static String takeReferrer(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(PyLoginHelper.ADS_EFUN_REFERRER, "");
		}
		return sp.getString(PyLoginHelper.ADS_EFUN_REFERRER, defValue);
	}

	public static String takePartnerName(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(PyLoginHelper.ADS_PARTNER_NAME, "");
		}
		return sp.getString(PyLoginHelper.ADS_PARTNER_NAME, defValue);
	}

	public static final String HK = "YHTW";


	public static String getVersionCode(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			String version = String.valueOf(info.versionCode);
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void logCurrentVersion() {
		Log.d("efun", "1.当前 version 4.4,fb登录添加unionId参数，值为fb token_for_business id;2.第三方登录重载一个构造函数，添加coveredThirdId、coveredThirdPlate参数");
	}

	public static boolean existClass(String className) {
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.w("efun", "not include google-play-services.jar");
			return false;
		}
		return true;
	}

	public static String getBusinessIds(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_APP_BUSINESS_IDS);
	}

	public static CmdResponse parseResult(String josnString) {

		CmdResponse cr = new CmdResponse();

		if (TextUtils.isEmpty(josnString)) {
			return cr;
		}

		try {
			JSONObject jsonObject = new JSONObject(josnString);

			cr.setCode(jsonObject.optString("code", ""));
			cr.setMessage(jsonObject.optString("message", ""));
			cr.setSign(jsonObject.optString("sign", ""));
			cr.setTimestamp(jsonObject.optString("timestamp", ""));
			cr.setUserId(jsonObject.optString("userid", ""));
			cr.setLoginTimes(jsonObject.optString("loginTimes", ""));
			cr.setUserName(jsonObject.optString("userName", ""));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cr;

	}
	
	private static final String LOGIN_REPONSE_KEY = "LOGIN_REPONSE_KEY";
	public static void saveLoginReponse(Context context,String reponse){
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, LOGIN_REPONSE_KEY, reponse);
	}
	
	public static String fetchLoginReponse(Context context){
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, LOGIN_REPONSE_KEY);
	}
	

}
