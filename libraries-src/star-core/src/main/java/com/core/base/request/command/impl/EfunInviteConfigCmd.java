/*
package com.core.base.request.command.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

import com.core.base.beans.InviteConfigBean;
import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.request.command.abstracts.EfunCommonCmd;
import com.core.base.utils.PL;

public class EfunInviteConfigCmd extends EfunCommonCmd<InviteConfigBean> {
	private InviteConfigBean inviteConfigBean = null;
	private String inviteFileUrl = null;
	private String saveFilePath = null;
	private String inviteFileCdnUrl;
	private String serverTime;
	private String result; 
	*/
/**
	 * 
	 *//*

	private static final long serialVersionUID = 1L;

	public void setInviteFileCdnUrl(String inviteFileCdnUrl) {
		this.inviteFileCdnUrl = inviteFileCdnUrl;
	}
	public String getInviteFileUrl() {
		return inviteFileUrl;
	}

	public void setInviteFileUrl(String inviteFileUrl) {
		this.inviteFileUrl = inviteFileUrl;
	}

	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	@Override
	public InviteConfigBean getResult() {
		return inviteConfigBean;
	}
	
	public void setResult(InviteConfigBean i){
		this.inviteConfigBean = i;
	}

	@Override
	public String getResponse() {
		return result;
	}
	
	public void setRespone(String mResult){
		this.result = mResult;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute() throws Exception {
		if(inviteFileUrl==null||"".equals(inviteFileUrl.trim())){
			PL.i( "inviteFileUrl is empty");
			return;
		}
		PL.i("开始下载："+inviteFileUrl);
		//result = getHttpResult(inviteFileCdnUrl, inviteFileUrl);
		
		HttpRequest httpRequest = new HttpRequest();
		HttpResponse httpResponse = httpRequest.getReuqestIn2Url(inviteFileCdnUrl, inviteFileUrl);
		result = httpResponse.getResult();
		serverTime = httpResponse.getServerTime();

		PL.i( "inviteNotice result:" + result);
			if(result==null||"".equals(result.trim())){
				Log.i("efun", "服务器返回空");
				return;
			}
			JSONObject resultJsonObject = new JSONObject(result);
			inviteConfigBean = new InviteConfigBean();
			inviteConfigBean.setRawRespone(result);
			inviteConfigBean.setAppPlatform(resultJsonObject.optString("appPlatform",""));
			inviteConfigBean.setVersion(resultJsonObject.optString("version",""));
			inviteConfigBean.setPackageName(resultJsonObject.optString("packageName",""));
			inviteConfigBean.setStartTime(resultJsonObject.optLong("startTime", 0l));
			inviteConfigBean.setEndTime(resultJsonObject.optLong("endTime", 0l));
			inviteConfigBean.setWhiteListNames(resultJsonObject.optString("whiteListNames", ""));
			inviteConfigBean.setJumpUrl(resultJsonObject.optString("jumpUrl", ""));
			inviteConfigBean.setFbLikeUrl(resultJsonObject.optString("fbLikeUrl", ""));
			inviteConfigBean.setFbShareUrl(resultJsonObject.optString("fbShareUrl", ""));//分享链接
			inviteConfigBean.setFbIconUrl(resultJsonObject.optString("fbIconUrl", ""));
			inviteConfigBean.setExplainUrl(resultJsonObject.optString("explainUrl", ""));
			inviteConfigBean.setFbShareContent(URLDecoder.decode(resultJsonObject.optString("fbShareContent", ""), "UTF-8"));//分享内容
			inviteConfigBean.setFbInviteContent(URLDecoder.decode(resultJsonObject.optString("fbInviteContent", ""), "UTF-8"));//邀请内容
			inviteConfigBean.setActivityName(resultJsonObject.optString("activityName", ""));
			long currentTime = System.currentTimeMillis();
			try {
				currentTime = new Date(serverTime).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			inviteConfigBean.setCurrentTime(currentTime);
			if(saveFilePath!=null&&!"".equals(result.trim())){
				Log.i("efun", "inviteConfigBean saveFilePath: "+saveFilePath+ File.separator+"inviteConfig.cf");
				File inviteFile = new File(saveFilePath);
				if(!inviteFile.exists()){
					inviteFile.mkdirs();
				}	
				FileOutputStream fos = new FileOutputStream(saveFilePath + File.separator+"inviteConfig.cf");
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
		        try {
		            oos.writeObject(inviteConfigBean);
		            oos.close();
		            fos.close();
		        } catch (IOException e) {   
		            System.out.println(e);   
		        }   
			}
	}

}
*/
