package com.starpy.model.login.utils;

import android.content.Context;
import android.text.TextUtils;

import com.core.base.utils.SPUtil;

public class EfunSwitchHelper {	
	
	public static void savaSwitchTypeNames(Context context, String typeNames) {
		if (TextUtils.isEmpty(typeNames)) {
			return;
		}
		String oldTypeNames = getSwitchTypeNames(context);
		if (!TextUtils.isEmpty(oldTypeNames)) {
			typeNames = oldTypeNames + "*" + typeNames;
		}
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, "EFUN_SWITCH_TYPENAMES", typeNames);
	}

	public static void cleanSwitchTypeNames(Context context) {
		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, "EFUN_SWITCH_TYPENAMES", "");
	}

	public static String getSwitchTypeNames(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, "EFUN_SWITCH_TYPENAMES");
	}

	public static void cleanUnifySwitch(final Context context) {
		String typeNames = getSwitchTypeNames(context);
		if (TextUtils.isEmpty(typeNames)) {
			return;
		}
		String[] types = typeNames.split("\\*");
		for (int i = 0; i < types.length; i++) {
			if (!TextUtils.isEmpty(types[i])) {
				SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, types[i], "");
			}
		}
		cleanSwitchTypeNames(context);
		
//		switchInitByTypeNames(context, "application", new OnEfunSwitchCallBack() {
//			@Override
//			public void switchCallBack(SwitchParameters parameters) {
//				Log.i("efun Switch", "EfunSwitchHelper : get application at luanch :" );
//				if (parameters.getSwitchApplicationBean() != null) {
//					Log.i("efun Switch", "EfunSwitchHelper application:" + parameters.getSwitchApplicationBean().getRawdata());
//				}
//				savaUnifySwitch(context, parameters.getResponse());
//			}
//		});
		
	}

	
	/*
	 * ************************************************分割线******************
	*/

//	private static String requestCompleteUrl;
	
	

	/*
	 * public static SwitchParameters StrToSwitchParameters(String request) {
	 * SwitchParameters switchParameters = null; if (TextUtils.isEmpty(request))
	 * return switchParameters; try { JSONObject jsonObject = new
	 * JSONObject(request); switchParameters = new SwitchParameters();
	 * switchParameters.setCode(jsonObject.optString("code", ""));
	 * switchParameters.setMessage(jsonObject.optString("message", ""));
	 * switchParameters.setLogin(jsonObject.optString("login", ""));
	 * switchParameters.setManagement(jsonObject.optString("management", ""));
	 * switchParameters.setNotice(jsonObject.optString("notice", ""));
	 * switchParameters.setInvite(jsonObject.optString("invite", ""));
	 * switchParameters.setKakaoshare(jsonObject.optString("kakaoshare", ""));
	 * switchParameters.setLineshare(jsonObject.optString("lineshare", ""));
	 * switchParameters.setTwittershare(jsonObject.optString("twittershare",
	 * "")); switchParameters.setVkshare(jsonObject.optString("vkshare", ""));
	 * switchParameters.setPlug(jsonObject.optString("plug", ""));
	 * switchParameters.setCafe(jsonObject.optString("cafe", ""));
	 * switchParameters.setService(jsonObject.optString("service", ""));
	 * switchParameters.setSocial(jsonObject.optString("social", ""));
	 * switchParameters.setTransfer(jsonObject.optString("transfer", ""));
	 * switchParameters.setCheckversoin(jsonObject.optString("checkversoin",
	 * ""));
	 * 
	 * } catch (JSONException e) { EfunLogUtil.logI("jsonObject exception");
	 * e.printStackTrace(); }
	 * 
	 * return switchParameters;
	 * 
	 * }
	 */

	

//	public static String getRequestCompleteUrl() {
//		return requestCompleteUrl;
//	}

//	public static void appSwitchInit(final Context context, String typeName, String appPlatform, String gameCode,
//			String gameLanguage, final EfunCommandCallBack callBack) {
//		String unionInterfacePreferredUrl = EfunDynamicUrl.getDynamicUrl(context, "unionInterfacePreferredUrl",SConfig.getGamePreferredUrl(context));
//		String unionInterfaceSpareUrl = EfunDynamicUrl.getDynamicUrl(context, "unionInterfaceSpareUrl",SConfig.getGameSpareUrl(context));
//		final EfunSwitchCmd efunSwitchCmd = new EfunSwitchCmd(context, typeName);// app
//		efunSwitchCmd.setPreferredUrl(unionInterfacePreferredUrl);
//		efunSwitchCmd.setSparedUrl(unionInterfaceSpareUrl);
//		efunSwitchCmd.setAppPlatform(appPlatform);// app需要设置，SDK有配置文件，可以不设置
//		efunSwitchCmd.setGameCode(gameCode);
//		efunSwitchCmd.setGameLanguage(gameLanguage);
//		efunSwitchCmd.setCallback(new EfunCommandCallBack() {
//
//			@Override
//			public void cmdCallBack(EfunCommand paramEfunCommand) {
//				String response = paramEfunCommand.getResponse();// 统一开关的接口返回
//				Log.i("efun Switch", "response: " + response);
//				try {
//					JSONObject mJsonObject = new JSONObject(response);
//					String code = mJsonObject.optString("code", "");
//					if ("1000".equals(code) || "1001".equals(code)) {
//						savaUnifySwitch(context, response);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				
//				if (callBack != null) {
//					callBack.cmdCallBack(paramEfunCommand);
//				}
//				
//			}
//		});
//
//		STaskExecutor.getInstance().asynExecute(context, efunSwitchCmd);
//	}

//	public static void sdkSwitchInit(final Context context, final EfunCommandCallBack callBack) {
//		String unionInterfacePreferredUrl = EfunDynamicUrl.getDynamicUrl(context, "unionInterfacePreferredUrl",SConfig.getGamePreferredUrl(context));
//		String unionInterfaceSpareUrl = EfunDynamicUrl.getDynamicUrl(context, "unionInterfaceSpareUrl",SConfig.getGameSpareUrl(context));
////		savaUnifySwitch(context, "");// 清空本地数据
//		EfunSwitchCmd efunSwitchCmd = new EfunSwitchCmd(context, "sdk");// SDK
//		efunSwitchCmd.setPreferredUrl(unionInterfacePreferredUrl);
//		efunSwitchCmd.setSparedUrl(unionInterfaceSpareUrl);
//		efunSwitchCmd.setGameLanguage(SConfig.getGameLanguage(context));
//		efunSwitchCmd.setCallback(new EfunCommandCallBack() {
//			
//			@Override
//			public void cmdCallBack(EfunCommand paramEfunCommand) {
//				String response = paramEfunCommand.getResponse();// 统一开关的接口返回
//				Log.i("efun Switch", "response: " + response);
//				try {
//					JSONObject mJsonObject = new JSONObject(response);
//					String code = mJsonObject.optString("code", "");
//					if ("1000".equals(code) || "1001".equals(code)) {
//						savaUnifySwitch(context, response);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
////				savaUnifySwitch(context, response);
////				
////				String requestCompleteUrl = paramEfunCommand.getRequestCompleteUrl();
////				Log.i("efun Switch", "requestCompleteUrl: " + requestCompleteUrl);
//				
//				if (callBack != null) {
//					callBack.cmdCallBack(paramEfunCommand);
//				}
//			}
//		});
//
//		STaskExecutor.getInstance().asynExecute(context, efunSwitchCmd);
//	}

//	public static String getSwitchByType(Context context, String type) {
//		String allSwitch = getUnifySwitch(context);
//		// Log.i("efun Switch", "allSwitch " + allSwitch);
//		if (TextUtils.isEmpty(allSwitch) || TextUtils.isEmpty(type)) {
//			return null;
//		}
//		String result = "";
//		try {
//			JSONObject mJson = new JSONObject(allSwitch);
//			result = mJson.optString(type, "");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

//	public static void savaUnifySwitch(Context context, String uniSwitch) {
//		SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE, "EFUN_UNIFY_SWITCH", uniSwitch);
//	}

//	public static String getUnifySwitch(Context context) {
//		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, "EFUN_UNIFY_SWITCH");
//	}

	/*
	 * public static void saveNotice(Context context, String notice) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_NOTICE", notice); }
	 * 
	 * public static String getNotice(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_NOTICE"); }
	 */

//	public static SwitchNoticeBean getNoticeBean(Context context) {
//		String noti = getSwitchByType(context, "notice");
//		// Log.i("efun Switch", "noti " + noti);
//		if (TextUtils.isEmpty(noti)) {
//			return null;
//		}
//		SwitchNoticeBean bean = new SwitchNoticeBean(noti);
//		return bean;
//	}

	/*
	 * public static void saveInvite(Context context, String invite) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_INVITE", invite); }
	 * 
	 * public static String getInvite(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_INVITE"); }
	 */
	
//	public static SwitchApplicationBean getApplicationBeanByKey(Context context) {
//		 String application = getSwitchByType(context, "application");
//		 if (TextUtils.isEmpty(application)) {
//			return null;
//		}
//		
//		 SwitchApplicationBean bean  = new SwitchApplicationBean(application);
//		 
//		return bean;
//	}

//	public static Map<String, String> getApplicationReponse(Context context) {
//		
//		Map<String, String> appUrlsMap = new HashMap<String, String>();
//		String application = getSwitchByType(context, "application");
//		if (TextUtils.isEmpty(application)) {
//			return appUrlsMap;
//		}
//		try {
//			JSONObject appJSONObject = new JSONObject(application);
//			if (appJSONObject.optString("code","").equals("1000")) {
//				
//				Iterator<String> keys = appJSONObject.keys();
//				while (keys.hasNext()) {
//				
//					String key = (String) keys.next();
//					appUrlsMap.put(key, appJSONObject.optString(key, ""));
//				}
//				return appUrlsMap;
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return appUrlsMap;
//	}
	
//	public static String getApplicationUrls(Context context) {
//		
//		String application = getSwitchByType(context, "application");
//		if (TextUtils.isEmpty(application)) {
//			return "";
//		}
//		try {
//			JSONObject appJSONObject = new JSONObject(application);
//			if (appJSONObject.optString("code","").equals("1000")) {
//				return application;
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return "";
//	}
	
//	public static SwitchInviteBean getInviteBean(Context context) {
//
//		String inv = getSwitchByType(context, "invite");
//		if (TextUtils.isEmpty(inv)) {
//			return null;
//		}
//		SwitchInviteBean switchInviteBean = new SwitchInviteBean(inv);
//		return switchInviteBean;
//	}

	/*
	 * public static void saveKakaoShare(Context context, String kakaoShare) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_KAKAOSHARE", kakaoShare); }
	 * 
	 * public static String getKakaoShare(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_KAKAOSHARE"); }
	 */

//	public static SwitchShareBean getKakaoShareBean(Context context) {
//		String kakao = getSwitchByType(context, "kakaoshare");
//		if (TextUtils.isEmpty(kakao)) {
//			return null;
//		}
//		SwitchShareBean bean = new SwitchShareBean(kakao);
//		return bean;
//	}

	/*
	 * public static void saveTwitterShare(Context context, String twitterShare)
	 * { SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_TWITTERSHARE", twitterShare); }
	 * 
	 * public static String getTwitterShare(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_TWITTERSHARE"); }
	 */

//	public static SwitchShareBean getTwitterShareBean(Context context) {
//		String twitter = getSwitchByType(context, "twittershare");
//		if (TextUtils.isEmpty(twitter)) {
//			return null;
//		}
//		return new SwitchShareBean(twitter);
//	}

	/*
	 * public static void saveLineShare(Context context, String lineShare) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_LINESHARE", lineShare); }
	 * 
	 * public static String getLineShare(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_LINESHARE"); }
	 */

//	public static SwitchShareBean getLineShareBean(Context context) {
//		String line = getSwitchByType(context, "lineshare");
//		if (TextUtils.isEmpty(line)) {
//			return null;
//		}
//		SwitchShareBean bean = new SwitchShareBean(line);
//
//		return bean;
//	}

	/*
	 * public static void saveVKShare(Context context, String vkShare) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_VKSHARE", vkShare); }
	 * 
	 * public static String getVKShare(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_VKSHARE"); }
	 */

//	public static SwitchShareBean getVKShareBean(Context context) {
//		String vk = getSwitchByType(context, "vkshare");
//		if (TextUtils.isEmpty(vk)) {
//			return null;
//		}
//		SwitchShareBean bean = new SwitchShareBean(vk);
//		return bean;
//	}

	/*
	 * public static void saveLogin(Context context, String login) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_LOGIN", login); }
	 * 
	 * public static String getLogin(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_LOGIN"); }
	 */

//	public static SwitchLoginBean getLoginBean(Context context) {
//		String login = getSwitchByType(context, "login");
//		if (TextUtils.isEmpty(login)) {
//			return null;
//		}
//		SwitchLoginBean switchLoginBean = new SwitchLoginBean(login);
//		return switchLoginBean;
//	}

	/*
	 * public static void savePlug(Context context, String plug) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_PLUG", plug); }
	 * 
	 * public static String getPlug(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_PLUG"); }
	 */

//	public static SwitchPlugBean getPlugBean(Context context) {
//		String plug = getSwitchByType(context, "plug");
//		if (TextUtils.isEmpty(plug)) {
//			return null;
//		}
//		SwitchPlugBean bean = new SwitchPlugBean(plug);
//		return bean;
//	}

	/*
	 * public static void saveService(Context context, String service) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SERVICE", service); }
	 * 
	 * public static String getService(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SERVICE"); }
	 */

//	public static SwitchKRplatformBean getServiceBean(Context context) {
//		String service = getSwitchByType(context, "service");
//		if (TextUtils.isEmpty(service)) {
//			return null;
//		}
//		SwitchKRplatformBean bean = new SwitchKRplatformBean(service);
//		return bean;
//	}

	/*
	 * public static void saveSocial(Context context, String social) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SOCIAL", social); }
	 * 
	 * public static String getSocial(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SOCIAL"); }
	 */

//	public static SwitchKRplatformBean getSocialBean(Context context) {
//		String social = getSwitchByType(context, "social");
//		if (TextUtils.isEmpty(social)) {
//			return null;
//		}
//		SwitchKRplatformBean bean = new SwitchKRplatformBean(social);
//		return bean;
//	}

	/*
	 * public static void saveCafe(Context context, String cafe) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SOCIAL", cafe); }
	 * 
	 * public static String getCafe(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_SOCIAL"); }
	 */

//	public static SwitchKRplatformBean getCafeBean(Context context) {
//		String cafe = getSwitchByType(context, "cafe");
//		if (TextUtils.isEmpty(cafe)) {
//			return null;
//		}
//		SwitchKRplatformBean bean = new SwitchKRplatformBean(cafe);
//		return bean;
//	}

	/*
	 * public static void saveManagement(Context context, String management) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_MANAGEMENT", management); }
	 * 
	 * public static String getManagement(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_MANAGEMENT"); }
	 */

//	public static SwitchManagementBean getManagementBean(Context context) {
//		String mana = getSwitchByType(context, "management");
//		if (TextUtils.isEmpty(mana)) {
//			return null;
//		}
//		SwitchManagementBean bean = new SwitchManagementBean(mana);
//
//		return bean;
//	}

	/*
	 * public static void saveTransfer(Context context, String transfer) {
	 * SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_TRANSFER", transfer); }
	 * 
	 * public static String getTransfer(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_TRANSFER"); }
	 */

//	public static SwitchTransferBean getTransferBean(Context context) {
//		String trans = getSwitchByType(context, "transfer");
//		if (TextUtils.isEmpty(trans)) {
//			return null;
//		}
//		SwitchTransferBean bean = new SwitchTransferBean(trans);
//		return bean;
//	}

	/*
	 * public static void saveCheckversoin(Context context, String checkversoin)
	 * { SPUtil.saveSimpleInfo(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_CHECKVERSION", checkversoin); }
	 * 
	 * public static String getCheckversoin(Context context) { return
	 * SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE,
	 * "EFUN_SWITCH_CHECKVERSION"); }
	 */

//	public static SwitchCheckversoinBean getCheckversoinBean(Context context) {
//		String checkversoin = getSwitchByType(context, "checkversoin");
//		if (TextUtils.isEmpty(checkversoin)) {
//			return null;
//		}
//		SwitchCheckversoinBean bean = new SwitchCheckversoinBean(checkversoin);
//		return bean;
//	}

	public static String setUrlparams(String url, String sharetype, String uid, String serverCode) {
		if (TextUtils.isEmpty(url)) {
			return url;
		}
		if (!TextUtils.isEmpty(sharetype)) {
			url = url + "_" + sharetype;
		}
		if (!TextUtils.isEmpty(uid)) {
			url = url + "_" + uid;
		}
		url = url + System.currentTimeMillis();
		if (!TextUtils.isEmpty(serverCode)) {
			url = url + "_" + serverCode;
		}
		return url;
	}

}
