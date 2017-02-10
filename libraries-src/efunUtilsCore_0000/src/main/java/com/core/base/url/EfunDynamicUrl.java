package com.core.base.url;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.json.JSONObject;

import com.core.base.beans.GameNoticeConfigBean;
import com.core.base.beans.InviteConfigBean;
import com.core.base.beans.UrlBean;
import com.core.base.beans.UrlFileContent;
import com.core.base.constant.InviteType;
import com.core.base.task.STaskExecutor;
import com.core.base.utils.SPUtil;
import com.core.base.task.EfunCommandCallBack;
import com.core.base.task.command.impl.EfunDynamicUrlCmd;
import com.core.base.task.command.impl.EfunReadFileConfigCmd;
import com.core.base.utils.EfunFileUtil;
import com.core.base.utils.EfunLogUtil;
import com.core.base.utils.SStringUtil;
import com.starpy.work.SwitchHelper;

import android.content.Context;
import android.text.TextUtils;

public class EfunDynamicUrl {

	private static UrlBean urlBean = null;
	private static GameNoticeConfigBean gameNoticeConfigBean = null;
	private static InviteConfigBean inviteConfigBean = null;

	/**
	 * 
	 * @param callBack
	 * @param context
	 * @param gameCode
	 * @param cdnUrl
	 * @param serverUrl
	 */
	public static void initGameNoticeConfig(final EfunCommandCallBack callBack, final Context context, String gameCode, String cdnUrl,
			String serverUrl) {

		SwitchHelper.oldGameNoticeCfg(callBack, context, gameCode, cdnUrl, serverUrl);
	}

	/**
	 * 
	 * @param callBack
	 * @param context
	 * @param gameCode
	 * @param cdnUrl
	 * @param serverUrl
	 * @param inviteType
	 *            Fb 、Kakao…..区分大写小；比如com.efun.core.constant.InviteType.Kakao
	 */
	public static void initInviteConfig(final EfunCommandCallBack callBack, final Context context, String gameCode, String cdnUrl, String serverUrl,
			String inviteType) {
		
		SwitchHelper.oldInviteConfig(callBack, context, gameCode, cdnUrl, serverUrl, inviteType);
	}

	/**
	 * 
	 * @param callBack
	 * @param context
	 * @param gameCode
	 * @param cdnUrl
	 * @param serverUrl
	 */
	public static void initFbInviteConfig(final EfunCommandCallBack callBack, final Context context, String gameCode, String cdnUrl,
			String serverUrl) {

		SwitchHelper.oldInviteConfig(callBack, context, gameCode, cdnUrl, serverUrl, InviteType.FB);
	}


	public synchronized static GameNoticeConfigBean getGameNoticeConfigBean(Context context) {
		try {
			FileInputStream fis = new FileInputStream(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform"
					+ File.separator + "gameNoticeConfig.cf");
			ObjectInputStream ois = new ObjectInputStream(fis);
			gameNoticeConfigBean = (GameNoticeConfigBean) ois.readObject();
			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameNoticeConfigBean;
	}

	public static String getGameNoticeConfigByKey(Context context,String key,String def){
		try {
			String result = "";
			FileInputStream fis = new FileInputStream(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform" + File.separator + "gameNoticeConfig.cf"); 
			ObjectInputStream ois = new ObjectInputStream(fis); 
			GameNoticeConfigBean g = (GameNoticeConfigBean) ois.readObject(); 
			ois.close();
			fis.close();
			if (g != null) {
				result = g.getRawRespone();
			}
			
			if (TextUtils.isEmpty(result)) {
				return def;
			}
			JSONObject resultJsonObject = new JSONObject(result);
			return resultJsonObject.optString(key, def);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return def;
	}
	
	
	public synchronized static InviteConfigBean getInviteConfigBean(Context context) {
		
		try {
			
			FileInputStream fis = new FileInputStream(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator
					+ "platform" + File.separator + "inviteConfig.cf");
			ObjectInputStream ois = new ObjectInputStream(fis);
			inviteConfigBean = (InviteConfigBean) ois.readObject();
			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return inviteConfigBean;
	}

	public static String getInviteConfigByKey(Context context,String key,String def){
		try {
			String result = "";
			
			FileInputStream fis = new FileInputStream(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator
					+ "platform" + File.separator + "inviteConfig.cf");
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			InviteConfigBean i = (InviteConfigBean) ois.readObject();
			ois.close();
			fis.close();
			
			if (i != null) {
				result = i.getRawRespone();
			}
		
			
			if (TextUtils.isEmpty(result)) {
				return def;
			}
			JSONObject resultJsonObject = new JSONObject(result);
			return resultJsonObject.optString(key, def);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return def;
	}
	
	
	public static void initDynamicUrl(Context context, String gameCode, String cdnUrl, String serverUrl, EfunCommandCallBack callBack) {
		cdnUrl = SStringUtil.checkUrl(cdnUrl);
		serverUrl = SStringUtil.checkUrl(serverUrl);
		String versionFileUrl_cdn = cdnUrl + gameCode + "/google/efunVersionCode.txt";
		String contentFileUrl_cdn = cdnUrl + gameCode + "/google/efunDomainInventory.txt";
		String versionFileUrl_server = serverUrl + gameCode + "/google/efunVersionCode.txt";
		String contentFileUrl_server = serverUrl + gameCode + "/google/efunDomainInventory.txt";
		initDynamicUrl(context, versionFileUrl_cdn, versionFileUrl_server, contentFileUrl_cdn, contentFileUrl_server, callBack);
	}

	public static void initDynamicUrl(Context context, String versionFileUrl_cdn, String versionFileUrl_server, String contentFileUrl_cdn,
			String contentFileUrl_server, EfunCommandCallBack callBack) {
		EfunDynamicUrlCmd dynamicUrlCmd = new EfunDynamicUrlCmd(context);
		dynamicUrlCmd.setShowProgress(false);
		dynamicUrlCmd.setVersionCodeFileUrl(versionFileUrl_cdn);
		dynamicUrlCmd.setVersionCodeFileUrl_Low(versionFileUrl_server);
		dynamicUrlCmd.setContentFileUrl(contentFileUrl_cdn);
		dynamicUrlCmd.setContentFileUrl_Low(contentFileUrl_server);
		dynamicUrlCmd.setCallback(callBack);
		STaskExecutor.getInstance().asynExecute(context, dynamicUrlCmd);
	}

	public static void getEfunFileConfigContent(Context context, String fileUrl, EfunCommandCallBack callBack) {
		fileUrl = getDynamicUrl(context, "efunLoginSwitchURL", fileUrl);
		if (TextUtils.isEmpty(fileUrl)) {
			return;
		}
		EfunReadFileConfigCmd readFileUrlCmd = new EfunReadFileConfigCmd(context);
		readFileUrlCmd.setShowProgress(false);
		readFileUrlCmd.setFileUrl(fileUrl);
		readFileUrlCmd.setCallback(callBack);
		STaskExecutor.getInstance().asynExecute(context, readFileUrlCmd);
	}

	public static void initPlatformDynamicUrl(Context context, String versionFileUrl_cdn, String versionFileUrl_server, String contentFileUrl_cdn,
			String contentFileUrl_server, EfunCommandCallBack callBack) {
		EfunDynamicUrlCmd dynamicUrlCmd = new EfunDynamicUrlCmd(context);
		dynamicUrlCmd.setShowProgress(false);
		dynamicUrlCmd.setVersionCodeFileUrl(versionFileUrl_cdn);
		dynamicUrlCmd.setVersionCodeFileUrl_Low(versionFileUrl_server);
		dynamicUrlCmd.setContentFileUrl(contentFileUrl_cdn);
		dynamicUrlCmd.setContentFileUrl_Low(contentFileUrl_server);
		dynamicUrlCmd.setPlatform(true);// 设置平台
		dynamicUrlCmd.setCallback(callBack);
		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform" + File.separator;
		dynamicUrlCmd.setLocalContentFileDir(localContentFileDir);
		STaskExecutor.getInstance().asynExecute(context, dynamicUrlCmd);
	}

	public synchronized static UrlBean getUrlBean(Context context) {
		if (null != urlBean && !urlBean.isEmpty()) {
			return urlBean;
		}
		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			return EfunDynamicUrlCmd.parseUrlContent(context, urlFileContent.getUrlContent(), false);
		}
		return null;
	}

	/**
	 * <p>
	 * Title: getDynamicUrls
	 * </p>
	 * <p>
	 * Description: 根据json键获取相应的域名值
	 * </p>
	 * 
	 * @param context
	 * @param urlKey
	 *            url键的数组
	 * @return url对应键的值
	 */
	public synchronized static String[] getDynamicUrls(Context context, String... urlKey) {

		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			String urlContent = urlFileContent.getUrlContent();
			if (!TextUtils.isEmpty(urlContent)) {
				try {
					JSONObject jsonObject = new JSONObject(urlContent);
					String[] urlValues = new String[urlKey.length];
					for (int i = 0; i < urlKey.length; i++) {
						urlValues[i] = jsonObject.optString(urlKey[i], "");
					}
					return urlValues;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public synchronized static String[] getDynamicUrls(Context context, String[] urlKey, String[] defaultValues) {
		if (urlKey.length != defaultValues.length) {
			throw new RuntimeException("urlKey与defaultValues长度不一致");
		}
		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			String urlContent = urlFileContent.getUrlContent();
			if (!TextUtils.isEmpty(urlContent)) {
				try {
					JSONObject jsonObject = new JSONObject(urlContent);
					String[] urlValues = new String[urlKey.length];
					for (int i = 0; i < urlKey.length; i++) {
						urlValues[i] = jsonObject.optString(urlKey[i], defaultValues[i]);
					}
					return urlValues;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return defaultValues;
	}

	public synchronized static String[] getPlatformDynamicUrls(Context context, String[] urlKey, String[] defaultValues) {
		if (urlKey.length != defaultValues.length) {
			throw new RuntimeException("urlKey与defaultValues长度不一致");
		}
		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform" + File.separator;
		// String localContentFileDir = context.getFilesDir().getAbsolutePath()
		// + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			String urlContent = urlFileContent.getUrlContent();
			if (!TextUtils.isEmpty(urlContent)) {
				try {
					JSONObject jsonObject = new JSONObject(urlContent);
					String[] urlValues = new String[urlKey.length];
					for (int i = 0; i < urlKey.length; i++) {
						urlValues[i] = jsonObject.optString(urlKey[i], defaultValues[i]);
					}
					return urlValues;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return defaultValues;
	}

	/**
	 * <p>
	 * Title: getDynamicUrls
	 * </p>
	 * <p>
	 * Description: 根据json键获取相应的域名值
	 * </p>
	 * 
	 * @param context
	 * @param urlKey
	 *            url键
	 * @return url对应键的值
	 */
	public synchronized static String getDynamicUrl(Context context, String urlKey) {

		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			String urlContent = urlFileContent.getUrlContent();
			if (!TextUtils.isEmpty(urlContent)) {
				try {
					JSONObject jsonObject = new JSONObject(urlContent);
					String urlValue = jsonObject.optString(urlKey, "");
					if (urlValue != null && !"".equals(urlValue.trim()) && !"null".equalsIgnoreCase(urlValue.trim())) {
						return urlValue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Title: getDynamicUrls
	 * </p>
	 * <p>
	 * Description: 根据json键获取相应的域名值
	 * </p>
	 * 
	 * @param context
	 * @param urlKey
	 *            url键
	 * @param defaultValue
	 *            defaultValue默认值
	 * @return url对应键的值
	 */
	public synchronized static String getDynamicUrl(Context context, String urlKey, String defaultValue) {

		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		if (null != urlFileContent && urlFileContent.getUrlContent() != null) {
			String urlContent = urlFileContent.getUrlContent();
			if (!TextUtils.isEmpty(urlContent)) {
				try {
					JSONObject jsonObject = new JSONObject(urlContent);
					String urlValue = jsonObject.optString(urlKey, defaultValue);
					if (urlValue != null && !"".equals(urlValue.trim()) && !"null".equalsIgnoreCase(urlValue.trim())) {
						return urlValue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return defaultValue;
	}

	public static String getDynamicContent(Context context) {
		String localContentFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		UrlFileContent urlFileContent = readUrlFileContent(context, localContentFileDir + "efunDomainInventory.txt");
		String urlContent = urlFileContent.getUrlContent();
		return urlContent;
	}

	/**
	 * <p>
	 * Title: readUrlFileContent
	 * </p>
	 * <p>
	 * Description: 读取本地域名内容文件的内容
	 * </p>
	 * 
	 * @param context
	 * @param filePath
	 *            本地域名内容的路径
	 * @return
	 */
	private static UrlFileContent readUrlFileContent(Context context, String filePath) {
		String localVersionCode = "";
		String localVersionContentMd5 = "";
		String localPlaintext = "";
		String localContent = "";
		UrlFileContent urlFileContent = new UrlFileContent();
		try {
			localContent = EfunFileUtil.readFile(context, filePath);
			if (!TextUtils.isEmpty(localContent)) {
				localVersionContentMd5 = SStringUtil.toMd5(localContent, false);
				//localPlaintext = EfunCipher.decrypt3DES(localContent, EfunDynamicUrlCmd.PASSWORD);
				// EfunLogUtil.logD("local content:" + localPlaintext);
				if (!TextUtils.isEmpty(localPlaintext)) {
					JSONObject contentJsonObject = new JSONObject(localPlaintext);
					localVersionCode = contentJsonObject.optString("efunVersionCode", "");
					EfunLogUtil.logD("local VersionCode:" + localVersionCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		urlFileContent.setUrlContent(localPlaintext);
		urlFileContent.setVersionCode(localVersionCode);
		urlFileContent.setVersionContentMd5(localVersionContentMd5);
		return urlFileContent;
	}


	public static String getDynamicPlatformVersionContent(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_APP_PLATFORM_DYNAMIC_VERSION_CONTENT);
	}

	public static String getDynamicGameVersionContent(Context context) {
		return SPUtil.getSimpleString(context, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GAME_DYNAMIC_VERSION_CONTENT);
	}

}
