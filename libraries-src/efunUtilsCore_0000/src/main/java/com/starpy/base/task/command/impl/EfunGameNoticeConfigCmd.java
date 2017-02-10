package com.starpy.base.task.command.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

import com.starpy.base.beans.GameNoticeConfigBean;
import com.starpy.base.http.HttpRequest;
import com.starpy.base.http.HttpResponse;
import com.starpy.base.task.command.abstracts.EfunCommonCmd;

public class EfunGameNoticeConfigCmd extends EfunCommonCmd<GameNoticeConfigBean> {
	private GameNoticeConfigBean gameNoticeConfigBean = null;
	private String gameNoticeFileUrl = null;
	private String saveFilePath = null;
	private String gameNoticeFileCdnUrl;
	private String serverTime;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getGameNoticeFileUrl() {
		return gameNoticeFileUrl;
	}

	public void setGameNoticeFileUrl(String gameNoticeFileUrl) {
		this.gameNoticeFileUrl = gameNoticeFileUrl;
	}
	public void setGameNoticeFileCdnUrl(String gameNoticeFileCdnUrl) {
		this.gameNoticeFileCdnUrl=gameNoticeFileCdnUrl;
	}
	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	@Override
	public GameNoticeConfigBean getResult() {
		return gameNoticeConfigBean;
	}

	@Override
	public String getResponse() {
		return "";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute() throws Exception {
		if(gameNoticeFileUrl==null||"".equals(gameNoticeFileUrl.trim())){
			Log.e("efun", "gameNoticeFileUrl is empty");
			return;
		}
		Log.i("efun", "开始下载："+gameNoticeFileUrl);
//		String result = getHttpResult(gameNoticeFileCdnUrl, gameNoticeFileUrl);
		HttpRequest httpRequest = new HttpRequest();
		HttpResponse httpResponse = httpRequest.getReuqestIn2Url(gameNoticeFileCdnUrl, gameNoticeFileUrl);
		String result = httpResponse.getResult();
		serverTime = httpResponse.getServerTime();
			Log.i("efun", "gameNotice result:" + result);
			if(result==null||"".equals(result.trim())){
				Log.i("efun", "服务器返回空");
				return;
			}
			
			JSONObject resultJsonObject = new JSONObject(result);
			gameNoticeConfigBean = new GameNoticeConfigBean();
			gameNoticeConfigBean.setRawRespone(result);
			gameNoticeConfigBean.setAppPlatform(resultJsonObject.optString("appPlatform",""));
			gameNoticeConfigBean.setVersion(resultJsonObject.optString("version",""));
			gameNoticeConfigBean.setPackageName(resultJsonObject.optString("packageName",""));
			gameNoticeConfigBean.setStartTime(resultJsonObject.optLong("startTime", 0l));
			gameNoticeConfigBean.setEndTime(resultJsonObject.optLong("endTime", 0l));
			gameNoticeConfigBean.setWhiteListNames(resultJsonObject.optString("whiteListNames", ""));
			gameNoticeConfigBean.setSnsUrl(resultJsonObject.optString("snsUrl", ""));
			gameNoticeConfigBean.setServiceUrl(resultJsonObject.optString("serviceUrl", ""));
			gameNoticeConfigBean.setGameUrl(resultJsonObject.optString("gameUrl", ""));
			gameNoticeConfigBean.setPayUrl(resultJsonObject.optString("payUrl", ""));
			gameNoticeConfigBean.setConsultUrl(resultJsonObject.optString("consultUrl", ""));
			
			gameNoticeConfigBean.setNoticeUrl(resultJsonObject.optString("notice", ""));
			
			gameNoticeConfigBean.setUserSwitchEnable(resultJsonObject.optString("userSwitchEnable", ""));
			
			long currentTime = System.currentTimeMillis();
			try {
				currentTime = new Date(serverTime).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gameNoticeConfigBean.setCurrentTime(currentTime);
			if(saveFilePath!=null&&!"".equals(result.trim())){
				Log.i("efun", "gameNoticeConfigBean saveFilePath: "+saveFilePath+ File.separator+"gameNoticeConfig.cf");
				File gameNoticeFile = new File(saveFilePath);
				if(!gameNoticeFile.exists()){
					gameNoticeFile.mkdirs();
				}				
				FileOutputStream fos = new FileOutputStream(saveFilePath + File.separator+"gameNoticeConfig.cf");
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
		        try {
		            oos.writeObject(gameNoticeConfigBean);
		            oos.close();
		            fos.close();
		        } catch (IOException e) {   
		            System.out.println(e);   
		        }   
			}
		}

/*	public String getHttpResult(String reqUrl, String reqUrl2){
		String res = "";
		HttpURLConnection httpUrlConnection = null;
		try {
			httpUrlConnection  = getHttpConnection(reqUrl);
		}catch (Exception e) {
			try {
				httpUrlConnection = getHttpConnection(reqUrl2);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try {
			int httpCode = httpUrlConnection.getResponseCode();
			if(httpCode!=200){
				httpUrlConnection = getHttpConnection(reqUrl2);
			}
			serverTime = httpUrlConnection.getHeaderField("Date");
			InputStream inStream = httpUrlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			StringBuffer stringBuffer = new StringBuffer();
			int count;
			while ((count = inStream.read(buffer))>0) {
				stringBuffer.append(new String(buffer, 0, count));
			}
			res = stringBuffer.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}*/

//	private HttpURLConnection getHttpConnection(String reqUrl)
//			throws MalformedURLException, IOException, ProtocolException {
//		Log.i("efun", "当前访问地址："+reqUrl);
//		URL url = new URL(reqUrl);
//		URLConnection rulConnection = url.openConnection();
//		HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
//		httpUrlConnection.setConnectTimeout(15000);
//		httpUrlConnection.setReadTimeout(15000);
//		httpUrlConnection.setDoOutput(false);
//		httpUrlConnection.setDoInput(true);
//		httpUrlConnection.setUseCaches(false);
//		httpUrlConnection.setRequestProperty("accept", "*/*");
//		httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
//		// 进行编码
//		httpUrlConnection.setRequestProperty("Content-Type",
//				"application/x-www-form-urlencoded");
//		// 设定请求的方法为"POST"，默认是GET
//		httpUrlConnection.setRequestMethod("GET");
//		// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
//		httpUrlConnection.connect();
//		return httpUrlConnection;
//	}
	
}
