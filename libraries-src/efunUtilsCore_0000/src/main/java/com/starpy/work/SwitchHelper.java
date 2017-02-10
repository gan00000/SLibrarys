package com.starpy.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import com.starpy.base.beans.GameNoticeConfigBean;
import com.starpy.base.beans.InviteConfigBean;
import com.starpy.base.task.EfunCommandCallBack;
import com.starpy.base.task.STaskExecutor;
import com.starpy.base.task.command.abstracts.EfunCommand;
import com.starpy.base.task.command.impl.EfunGameNoticeConfigCmd;
import com.starpy.base.task.command.impl.EfunInviteConfigCmd;
import com.starpy.base.utils.SStringUtil;
import com.starpy.base.url.EfunDynamicUrl;

import android.content.Context;

public class SwitchHelper {

	/*public static InviteConfigBean parseInviteResult2(String result){
		
		if (TextUtils.isEmpty(result)) {
			return null;
		}
		
		InviteConfigBean inviteConfigBean = new InviteConfigBean();
		try {
			JSONObject resultJsonObject = new JSONObject(result);
			inviteConfigBean.setCode(resultJsonObject.optString("code", ""));
			inviteConfigBean.setJumpUrl(resultJsonObject.optString("jumpUrl", ""));
			inviteConfigBean.setFbLikeUrl(resultJsonObject.optString("fbLikeUrl", ""));
			inviteConfigBean.setFbShareUrl(resultJsonObject.optString("fbShareUrl", ""));//分享链接
			inviteConfigBean.setFbIconUrl(resultJsonObject.optString("fbIconUrl", ""));
			inviteConfigBean.setExplainUrl(resultJsonObject.optString("explainUrl", ""));
			inviteConfigBean.setFbShareContent(URLDecoder.decode(resultJsonObject.optString("fbShareContent", ""), "UTF-8"));//分享内容
			inviteConfigBean.setFbInviteContent(URLDecoder.decode(resultJsonObject.optString("fbInviteContent", ""), "UTF-8"));//邀请内容
			return inviteConfigBean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static GameNoticeConfigBean parseNoticeResult2(String result){
		
		if (TextUtils.isEmpty(result)) {
			return null;
		}
		
		GameNoticeConfigBean gameNoticeConfigBean = new GameNoticeConfigBean();
		
		try {
			JSONObject resultJsonObject = new JSONObject(result);
			gameNoticeConfigBean.setCode(resultJsonObject.optString("code", ""));
			//推广墙
			gameNoticeConfigBean.setSnsUrl(resultJsonObject.optString("snsUrl", ""));
			gameNoticeConfigBean.setServiceUrl(resultJsonObject.optString("serviceUrl", ""));
			gameNoticeConfigBean.setGameUrl(resultJsonObject.optString("gameUrl", ""));
			gameNoticeConfigBean.setPayUrl(resultJsonObject.optString("payUrl", ""));
			gameNoticeConfigBean.setConsultUrl(resultJsonObject.optString("consultUrl", ""));
			gameNoticeConfigBean.setNoticeUrl(resultJsonObject.optString("notice", ""));
			gameNoticeConfigBean.setUserSwitchEnable(resultJsonObject.optString("userSwitchEnable", ""));
			return gameNoticeConfigBean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	public static void oldGameNoticeCfg(final EfunCommandCallBack callBack, final Context context, String gameCode,
										String cdnUrl, String serverUrl) {
		cdnUrl = SStringUtil.checkUrl(cdnUrl);
		serverUrl = SStringUtil.checkUrl(serverUrl);
		String gameNoticeFileCdnUrl = cdnUrl + "gameNotice/" + gameCode + "Notice.txt";
		String gameNoticeFileUrl = serverUrl + "gameNotice/" + gameCode + "Notice.txt";

		gameNoticeFileCdnUrl = EfunDynamicUrl.getDynamicUrl(context, "efunFbNoticeUrl", gameNoticeFileCdnUrl);

		final EfunGameNoticeConfigCmd efunGameNoticeConfigCmd = new EfunGameNoticeConfigCmd();
		efunGameNoticeConfigCmd.setGameNoticeFileUrl(gameNoticeFileUrl);
		efunGameNoticeConfigCmd.setGameNoticeFileCdnUrl(gameNoticeFileCdnUrl);
		efunGameNoticeConfigCmd.setSaveFilePath(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform");
		efunGameNoticeConfigCmd.setShowProgress(false);
		
		efunGameNoticeConfigCmd.setCallback(new EfunCommandCallBack() {
			public void cmdCallBack(EfunCommand command) {
				if (callBack != null) {
					// efunGameNoticeConfigCmd.getResult()
					callBack.cmdCallBack(efunGameNoticeConfigCmd);
				}
			}
		});
		
		STaskExecutor.getInstance().asynExecute(context, efunGameNoticeConfigCmd);
	}
	
	public static void oldInviteConfig(EfunCommandCallBack callBack, Context context, String gameCode, String cdnUrl, String serverUrl,
			String inviteType) {
		cdnUrl = SStringUtil.checkUrl(cdnUrl);
		serverUrl = SStringUtil.checkUrl(serverUrl);
		String inviteFileCdnUrl = cdnUrl + "Invite/" + gameCode + inviteType + "Invite.txt";
		String inviteFileUrl = serverUrl + "Invite/" + gameCode + inviteType + "Invite.txt";
		inviteFileCdnUrl = EfunDynamicUrl.getDynamicUrl(context, "efunFbInviteUrl", inviteFileCdnUrl);
		initoldInviteConfig(context, inviteFileCdnUrl, inviteFileUrl, callBack);
	}
	  
	private static void initoldInviteConfig(Context context, String inviteFileCdnUrl, String inviteFileUrl, final EfunCommandCallBack callBack) {
		EfunInviteConfigCmd efunInviteConfigCmd = new EfunInviteConfigCmd();
		efunInviteConfigCmd.setInviteFileUrl(inviteFileUrl);
		efunInviteConfigCmd.setInviteFileCdnUrl(inviteFileCdnUrl);
		efunInviteConfigCmd.setSaveFilePath(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform");
		efunInviteConfigCmd.setShowProgress(false);
		efunInviteConfigCmd.setCallback(new EfunCommandCallBack() {
			public void cmdCallBack(EfunCommand command) {
				if (callBack != null) {
					callBack.cmdCallBack(command);
				}
			}
		});
		STaskExecutor.getInstance().asynExecute(context, efunInviteConfigCmd);
	}
	
	public static GameNoticeConfigBean getGameNoticeConfigBean(Context context) {
		GameNoticeConfigBean gameNoticeConfigBean = null;
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

	public static InviteConfigBean getInviteConfigBean(Context context) {
		InviteConfigBean inviteConfigBean = null;
		try {
			FileInputStream fis = new FileInputStream(context.getFilesDir().getAbsolutePath() + File.separator + "efun" + File.separator + "platform"
					+ File.separator + "inviteConfig.cf");
			ObjectInputStream ois = new ObjectInputStream(fis);
			inviteConfigBean = (InviteConfigBean) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inviteConfigBean;
	}
}
