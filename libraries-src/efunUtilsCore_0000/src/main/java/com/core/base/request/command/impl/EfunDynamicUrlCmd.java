/*
package com.core.base.request.command.impl;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.core.base.beans.DynamicVersionBean;
import com.core.base.beans.UrlBean;
import com.core.base.beans.UrlFileContent;
import com.core.base.utils.SPUtil;
import com.core.base.http.HttpRequest;
import com.core.base.request.command.abstracts.EfunCommonCmd;
import com.core.base.utils.FileUtil;
import com.starpy.base.SLogUtil;
import com.core.base.utils.SStringUtil;

public class EfunDynamicUrlCmd extends EfunCommonCmd<UrlBean> {
	
	
	*/
/**
	 * serialVersionUID
	 *//*

	private static final long serialVersionUID = 1L;
	
	public final static String PASSWORD = "efundynamicurl201407011141";

	*/
/**
	 * VersionCodeFileUrl 版本号文件在cdn的路径url
	 *//*

	private String versionCodeFileUrl;
	*/
/**
	 * contentFileUrl 域名文件在cdn的路径
	 *//*

	private String contentFileUrl;
	

	*/
/**
	 * 版本号文件在文件服务器的路径url
	 *//*

	private String versionCodeFileUrl_Low;

	*/
/**
	 * 域名文件在文件服务器的路径
	 *//*

	private String contentFileUrl_Low;
	*/
/**
	 * localContentFilePath 域名文件在本地保存的路径
	 *//*

	private String localContentFilePath;
	
	
	*/
/**
	 * dynamicVersionBean 保存版本号内容的bean
	 *//*

	private static DynamicVersionBean dynamicVersionBean = null;
	*/
/**
	 * 保存域名文件内容的bean
	 *//*

	private UrlFileContent urlFileContentBean = null;
	*/
/**
	 * 解析域名的对象BEAN
	 *//*

	private static UrlBean mUrlBean = null;
	
	
	private Context mContext = null;
	
	*/
/**
	 * 域名文件在本地保存的文件夹
	 *//*

	private String localContentFileDir;
	
	private boolean isPlatform;

	
	public EfunDynamicUrlCmd(Context mContext) {
		this.mContext = mContext;
	}


	@Override
	public void execute() throws Exception {
		
		if (null == mContext || TextUtils.isEmpty(versionCodeFileUrl)) {
			Log.e("efun", "Context or url is empty");
			return;
		}
		  
		requestDynamicUrl(1,versionCodeFileUrl,contentFileUrl);
	}

	*/
/**
	* <p>Title: requestDynamicUrl</p>
	* <p>Description: 请求动态域名</p>
	* @param time 请求次数标识
	* @param mVersionCodeFileUrl 版本号文件路径
	* @param mUrlFileUrl 域名内容文静路径
	*//*

	private void requestDynamicUrl(int time,String mVersionCodeFileUrl,String mUrlFileUrl) {
		// 获取cdn版本号文件内容
		if (TextUtils.isEmpty(mVersionCodeFileUrl) || TextUtils.isEmpty(mUrlFileUrl)) {
			return;
		}
//		String localContentFileDir = mContext.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		if (TextUtils.isEmpty(localContentFileDir) || TextUtils.isEmpty(localContentFileDir.trim())) {
			localContentFileDir = mContext.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator;
		}
		String contentFileName = FileUtil.getFileName(contentFileUrl);
		localContentFilePath = localContentFileDir + contentFileName;
		Log.d("efun", "读取本地文件内容");
		urlFileContentBean = readUrlFileContent(mContext,localContentFilePath);//先读取本地文件内容
		
//		String versionContent = requestVersionContent(mVersionCodeFileUrl);//读取远程服务器版本号文件内容
		String versionContent = HttpRequest.get(mVersionCodeFileUrl);//读取远程服务器版本号文件内容
		SLogUtil.logD("versionContent:" + versionContent);
		if (!TextUtils.isEmpty(versionContent)) {//保存一下
			if (isPlatform) {
				SPUtil.saveSimpleInfo(mContext, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_APP_PLATFORM_DYNAMIC_VERSION_CONTENT, versionContent);
			} else {
				SPUtil.saveSimpleInfo(mContext, SPUtil.STAR_PY_SP_FILE, SPUtil.EFUN_GAME_DYNAMIC_VERSION_CONTENT, versionContent);
			}
		}
		dynamicVersionBean = parseVersionContent(versionContent);//解析版本号内容
		if (dynamicVersionBean != null && !dynamicVersionBean.isHasNull()) {
			if (!TextUtils.isEmpty(urlFileContentBean.getUrlContent())
					&& checkVersionCode(urlFileContentBean.getVersionCode(), dynamicVersionBean.getVersionCode())
					&& checkMd5Sign(urlFileContentBean.getVersionContentMd5(), dynamicVersionBean.getSignValue())) {// 验证本地域名文件内容通过
				Log.d("efun", "dynamic url local check success");
				parseUrlContent(mContext, urlFileContentBean.getUrlContent(), false);
				return;
			} else {// 验证本地域名文件内容不通过，需要重新下载域名内容文件
				Log.d("efun", "dynamic url download " + time + " times");
//				boolean isFinish = downLoadUrlFile(mUrlFileUrl, localContentFilePath);// 下载域名内容文件
				boolean isFinish = HttpRequest.downLoadUrlFile(mUrlFileUrl, localContentFilePath);// 下载域名内容文件
				if (isFinish) {// 判断是否正常下载完成
					urlFileContentBean = readUrlFileContent(mContext, localContentFilePath);// 读取域名文件内容
					if (!urlFileContentBean.hasFiledEmpty() && dynamicVersionBean.getVersionCode().equals(urlFileContentBean.getVersionCode())// 判断域名内容信息并且检查版本号和md5加密信息
							&& dynamicVersionBean.getSignValue().equals(urlFileContentBean.getVersionContentMd5())) {
						Log.d("efun", "dynamic url download check success");
						parseUrlContent(mContext, urlFileContentBean.getUrlContent(), true);// 信息校验通过
						return;
					}
				}

			}
		}
		if (time == 2) {
			if (dynamicVersionBean != null && !dynamicVersionBean.isHasNull()) {
				parseUrlContent(mContext, "", false);
			}
			return;
		}
		requestDynamicUrl(2,this.versionCodeFileUrl_Low,this.contentFileUrl_Low);
	}
	
	*/
/**
	* <p>Title: readUrlFileContent</p>
	* <p>Description: 读取本地域名内容文件的内容</p>
	* @param context
	* @param filePath 本地域名内容的路径
	* @return
	*//*

	private UrlFileContent readUrlFileContent(Context context,String filePath){
		String localVersionCode = "";
		String localVersionContentMd5 = "";
		String localPlaintext = "";
		String localContent = "";
		UrlFileContent urlFileContent = new UrlFileContent();
		try {
			localContent = FileUtil.readFileByByte(context, filePath);
			SLogUtil.logD("密文:" + localContent);
			if (!TextUtils.isEmpty(localContent)) {
				localVersionContentMd5 = SStringUtil.toMd5(localContent, false);
				SLogUtil.logD("密文MD5:" + localVersionContentMd5);
				//localPlaintext  = EfunCipher.decrypt3DES(localContent, PASSWORD);
				localPlaintext = "";
				SLogUtil.logD("local content:" + localPlaintext);
				if (!TextUtils.isEmpty(localPlaintext)) {
					JSONObject contentJsonObject = new JSONObject(localPlaintext);
					localVersionCode = contentJsonObject.optString("efunVersionCode", "");
					SLogUtil.logD("域名文件 VersionCode:" + localVersionCode);
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

	private boolean checkVersionCode(String localVersionCode,String remoteVersionCode){
		return !TextUtils.isEmpty(localVersionCode) && localVersionCode.equals(remoteVersionCode);
	}
	private boolean checkMd5Sign(String localMd5Sign,String remoteMd5Sign){
		return !TextUtils.isEmpty(localMd5Sign) && localMd5Sign.equals(remoteMd5Sign);
	}
	
	*/
/**
	* <p>Title: requestVersionContent</p>
	* <p>Description: 获取cdn上面的版本号文件内容</p>
	* @return 返回文件内容
	*//*

*/
/*	private String requestVersionContent(String mUrl) {
		
		HttpURLConnection conn = null;
		StringBuilder versionContent = new StringBuilder();
		try {
			URL url = new URL(mUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.connect();
			int code = conn.getResponseCode();
			Log.d("efun", "version file http response code:" + code);
			if (200 == code) {
				BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String data = "";
				while ((data = is.readLine()) != null) {
					versionContent.append(data);
				}
				is.close();
			}
		} catch (IOException e) {
			Log.d("efun", "exception:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
			Log.d("efun", "版本号文件内容:" + versionContent.toString());
		}
		return versionContent.toString();
	}*//*


	*/
/**
	* <p>Title: downLoadUrlFile</p>
	* <p>Description: 下载文件</p>
	* @param downLoadFileUrl 远程文件的路径
	* @param savePath 下载之后文件保存的本地路径
	* @return
	*//*


	
	// content:﻿<?xml version="1.0" encoding="utf-8"?><resources>
	// <!-- version -->    <string name="efunVersionCode">6</string>	</resources>
	private DynamicVersionBean parseVersionContent(String content) {
		// content:﻿{"efunVersionCode":"61","sign":"xxxxxxxxxxxxxxxx"}
		try {
			DynamicVersionBean dynamicVersionBean = new DynamicVersionBean();
			JSONObject object = new JSONObject(content);
			dynamicVersionBean.setVersionCode(object.optString("efunVersionCode", null));
			dynamicVersionBean.setSignValue(object.optString("sign", null));
			dynamicVersionBean.setQxdlSwitch(object.optString("qxdlswitch", null));
			return dynamicVersionBean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	*/
/**
	* <p>Title: parseUrl</p>
	* <p>Description: 解析域名</p>
	* @param context
	* @param urlContent
	* @param newDownLoad
	* @return
	*//*

	public static UrlBean parseUrlContent(Context context, String urlContent, boolean newDownLoad) {
		if (TextUtils.isEmpty(urlContent)) {
			return null;
		}
		try {
			mUrlBean = new UrlBean();
			JSONObject contentJsonObject = new JSONObject(urlContent);
			mUrlBean.setEfunLoginPreferredUrl(contentJsonObject.optString("efunLoginPreferredUrl", ""));
			mUrlBean.setEfunLoginSpareUrl(contentJsonObject.optString("efunLoginSpareUrl", ""));
			mUrlBean.setEfunAdsPreferredUrl(contentJsonObject.optString("efunAdsPreferredUrl", ""));
			mUrlBean.setEfunAdsSpareUrl(contentJsonObject.optString("efunAdsSpareUrl", ""));
			mUrlBean.setEfunPayPreferredUrl(contentJsonObject.optString("efunPayPreferredUrl", ""));
			mUrlBean.setEfunPaySpareUrl(contentJsonObject.optString("efunPaySpareUrl", ""));
			mUrlBean.setEfunFbPreferredUrl(contentJsonObject.optString("efunFbPreferredUrl", ""));
			mUrlBean.setEfunFbSpareUrl(contentJsonObject.optString("efunFbSpareUrl", ""));
			mUrlBean.setEfunGamePreferredUrl(contentJsonObject.optString("efunGamePreferredUrl", ""));
			mUrlBean.setEfunGameSpareUrl(contentJsonObject.optString("efunGameSpareUrl", ""));
			mUrlBean.setEfunQuestionPreferredUrl(contentJsonObject.optString("efunQuestionPreferredUrl", ""));
			mUrlBean.setEfunPlatformPreferredUrl(contentJsonObject.optString("efunPlatformPreferredUrl", ""));
		//	mUrlBean.setEfunLuaSwitchUrl(contentJsonObject.optString("efunLuaSwitchUrl", ""));
			if (dynamicVersionBean != null && !TextUtils.isEmpty(dynamicVersionBean.getQxdlSwitch()) && dynamicVersionBean.getQxdlSwitch().equals("on")) {
				mUrlBean.setQxdlSwitch(true);
			} else {
				mUrlBean.setQxdlSwitch(false);
			}
			return mUrlBean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("efun", "mBean is null");
		return null;
	}
	

	@Override
	public UrlBean getResult() {
		return mUrlBean;
	}


	@Override
	public String getResponse() {
		return "";
	}


	public String getContentFileUrl() {
		return contentFileUrl;
	}


	public void setContentFileUrl(String contentFileUrl) {
		this.contentFileUrl = contentFileUrl;
	}
	
	public String getVersionCodeFileUrl() {
		return versionCodeFileUrl;
	}

	public void setVersionCodeFileUrl(String versionCodeFileUrl) {
		this.versionCodeFileUrl = versionCodeFileUrl;
	}


	public String getVersionCodeFileUrl_Low() {
		return versionCodeFileUrl_Low;
	}


	public void setVersionCodeFileUrl_Low(String versionCodeFileUrl_Low) {
		this.versionCodeFileUrl_Low = versionCodeFileUrl_Low;
	}


	public String getContentFileUrl_Low() {
		return contentFileUrl_Low;
	}


	public void setContentFileUrl_Low(String contentFileUrl_Low) {
		this.contentFileUrl_Low = contentFileUrl_Low;
	}


	public String getLocalContentFileDir() {
		return localContentFileDir;
	}


	public void setLocalContentFileDir(String localContentFileDir) {
		this.localContentFileDir = localContentFileDir;
	}


	public boolean isPlatform() {
		return isPlatform;
	}


	public void setPlatform(boolean isPlatform) {
		this.isPlatform = isPlatform;
	}

}
*/
