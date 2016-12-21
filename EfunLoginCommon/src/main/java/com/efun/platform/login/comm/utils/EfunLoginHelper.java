package com.efun.platform.login.comm.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.efun.core.db.EfunDatabase;
import com.efun.core.tools.EfunJSONUtil;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.bean.CmdResponse;
import com.efun.platform.login.comm.bean.EfunPerson;
import com.efun.platform.login.comm.bean.LoginParameters;
import com.efun.platform.login.comm.bean.QuestionParams;
import com.efun.platform.login.comm.callback.CallBackServer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

public class EfunLoginHelper {

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

	/**
	 * loginListener Method Method Description :返回給廠商
	 * 
	 * @param loginParameters
	 * @return void
	 * @author Joe
	 * @date 2013-4-17
	 */
	public static void loginListener(LoginParameters loginParameters) {
		if (null == loginParameters || null == loginParameters.getCode()) {
			EfunLogUtil.logI("at EfunHelper loginListener loginParameters is null.");
			throw new RuntimeException("at EfunHelper loginListener loginParameters is null.");
		}
		CallBackServer backServer = CallBackServer.getInstance();
		if (null == backServer.getOnEfunLoginListener()) {
			EfunLogUtil.logI("CallBackServer OnEfunLoginListener is null.");
			throw new RuntimeException("CallBackServer OnEfunLoginListener is null.");
		}
		if (EfunLoginHelper.ReturnCode.RETURN_SUCCESS.equals(loginParameters.getCode())
				|| EfunLoginHelper.ReturnCode.ALREADY_EXIST.equals(loginParameters.getCode()))
			backServer.getOnEfunLoginListener().onFinishLoginProcess(loginParameters);
	}

	public static void overrideReturn() {
		CallBackServer backServer = CallBackServer.getInstance();
		if (null == backServer.getOnEfunLoginListener()) {
			EfunLogUtil.logI("CallBackServer OnEfunLoginListener is null.");
			throw new RuntimeException("CallBackServer OnEfunLoginListener is null.");
		}
		LoginParameters loginParameters = new LoginParameters();
		loginParameters.setCode(EfunLoginHelper.ReturnCode.LOGIN_BACK);
		backServer.getOnEfunLoginListener().onFinishLoginProcess(loginParameters);
	}

	@Deprecated
	public static boolean verifyAccount(String userName, Area area) {
		if (LoinStringUtil.isEmpty(userName) || area == null) {
			return false;
		}
		if (Area.HK_TW == area) {
			if (userName.matches("^[\\w_]{0,50}")
					|| userName.matches("^(?=[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				if (userName.toLowerCase().matches("^(efun|yh)[\\w\\W]{1,46}$") || userName.matches("^09[\\d]{8,8}$")) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} else if (Area.Korea == area) {
			if (userName.toUpperCase().matches("^YHKR[\\w_]{0,46}")
					|| userName.matches("^(?=YHKR[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.MiddleEast == area) {
			if (userName.toUpperCase().matches("^YHEN[\\w_]{0,46}")
					|| userName.matches("^(?=YHEN[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.NorthAmerica == area) {
			if (userName.toUpperCase().matches("^YHSA[\\w_]{0,46}")
					|| userName.matches("^(?=YHSA[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.Japan == area) {
			if (userName.toUpperCase().matches("^YHJP[\\w_]{0,46}")
					|| userName.matches("^(?=YHJP[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.ChinaLand == area) {
			if (userName.toUpperCase().matches("^YHCN[\\w_]{0,46}")
					|| userName.matches("^(?=YHCN[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.Vietnam == area) {
			if (userName.toUpperCase().matches("^YHVN[\\w_]{0,46}")
					|| userName.matches("^(?=YHVN[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		} else if (Area.Thailand == area) {
			if (userName.toUpperCase().matches("^YHTH[\\w_]{0,46}")
					|| userName.matches("^(?=YHTH[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static String takeAdvertisersName(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(EfunLoginHelper.ADS_ADVERTISERS_NAME, "");
		}
		return sp.getString(EfunLoginHelper.ADS_ADVERTISERS_NAME, defValue);
	}

	public static String takeReferrer(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(EfunLoginHelper.ADS_EFUN_REFERRER, "");
		}
		return sp.getString(EfunLoginHelper.ADS_EFUN_REFERRER, defValue);
	}

	public static String takePartnerName(Context context, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(EFUN_FILE, Context.MODE_PRIVATE);
		if (defValue == null) {
			return sp.getString(EfunLoginHelper.ADS_PARTNER_NAME, "");
		}
		return sp.getString(EfunLoginHelper.ADS_PARTNER_NAME, defValue);
	}

	public static final String HK = "YHTW";

	public static boolean verifyAccount(String userName, String areaPrefix) {
		if (LoinStringUtil.isEmpty(areaPrefix)) {
			return false;
		} else if (areaPrefix.equals(HK)) {

			if (userName.matches("^[\\w_]{0,50}")
					|| userName.matches("^(?=[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$")) {
				if (userName.toLowerCase().matches("^(efun|yh)[\\w\\W]{1,46}$") || userName.matches("^09[\\d]{8,8}$")) {
					return false;
				}
				return true;
			} else {
				return false;
			}

		} else {
			areaPrefix = areaPrefix.toUpperCase();
			String expression_one = "^" + areaPrefix + "[\\w_]{0,46}";
			String expression_two = "^(?=" + areaPrefix + "[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+).{0,50}$";
			if (userName.toUpperCase().matches(expression_one) || userName.matches(expression_two)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static String buildQuestionUrl(Context context, QuestionParams questionParams) {

		String manufacturer = android.os.Build.MANUFACTURER;
		String modle = android.os.Build.MODEL;
		String osVersion = EfunLocalUtil.getOsVersion();

		String deviceinfo = "android_" + manufacturer + "_" + modle + "_" + osVersion;

		StringBuilder stringBuilder = new StringBuilder();
		if (EfunStringUtil.isEmpty(questionParams.getQuestionUrl())) {
			return null;
		}
		stringBuilder.append(questionParams.getQuestionUrl()).append("index.html?").append("gameType=")
				.append(questionParams.getGameType()).append("&uid=").append(questionParams.getUserId())
				.append("&playerid=").append(questionParams.getRoleId()).append("&playername=")
				.append(questionParams.getRoleName()).append("&gsid=").append(questionParams.getServerCode())
				.append("&servername=").append(questionParams.getServerName()).append("&viplvl=")
				.append(questionParams.getViplvl()).append("&deviceinfo=").append(deviceinfo).append("&language=")
				.append(questionParams.getLanguage()).append("&gameCode=").append(questionParams.getGameCode())
				.append("&remark=").append(questionParams.getRemark());

		return stringBuilder.toString();
	}

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
		Log.d("efun", "当前 version 4.3,修复EfunForgetPasswordCmd类构造函数删掉的问题");
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
		return EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, EfunDatabase.EFUN_APP_BUSINESS_IDS);
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
		EfunDatabase.saveSimpleInfo(context, EfunDatabase.EFUN_FILE, LOGIN_REPONSE_KEY, reponse);
	}
	
	public static String fetchLoginReponse(Context context){
		return EfunDatabase.getSimpleString(context, EfunDatabase.EFUN_FILE, LOGIN_REPONSE_KEY);
	}
	

}
