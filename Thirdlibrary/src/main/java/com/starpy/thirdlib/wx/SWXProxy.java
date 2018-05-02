package com.starpy.thirdlib.wx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import com.core.base.http.HttpRequest;
import com.core.base.request.SRequestAsyncTask;
import com.core.base.utils.PL;
import com.core.base.utils.SPUtil;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.SignatureUtil;
import com.core.base.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 */
public class SWXProxy{

	public static final String SHARE_TEXT = "SHARE_TEXT";
	public static final String SHARE_WEB_PAGE = "SHARE_WEB_PAGE";

	private Activity activity;
	private IWXAPI iwxapi;
	private String appId;

	private ProgressDialog progressDialog;

	private String appSecret = "";//f9868ffbb04292411161c76cbde90664

	private final static String wx_file_name = "wx_file_name.xml";
	private final static String wxResult = "wx_wxResult";

	private WxCallback wxCallback;
	private WxShareCallback wxShareCallback;

	private WxUserInfo wxUserInfo;

	public WxUserInfo getWxUserInfo() {
		return wxUserInfo;
	}

	private static SWXProxy swxProxy;

	public static SWXProxy getSWXProxy(){

		if (swxProxy == null){
			swxProxy = new SWXProxy();
		}

		return swxProxy;
	}

	private SWXProxy() {

		//init(activity, appId, appSecret);

	}

	public IWXAPI createNewIWXAPI(Activity mActivity){
		return WXAPIFactory.createWXAPI(mActivity,appId,true);
	}

	public void init(Activity activity, String appId, String appSecret) {
		this.activity = activity;
		this.appId = appId;
		this.appSecret = appSecret;

		if (SStringUtil.isEmpty(appId) || SStringUtil.isEmpty(appSecret)){
			PL.w("wx appId or appSecret is empty");
			return;
		}

		iwxapi = WXAPIFactory.createWXAPI(activity,appId,true);

		iwxapi.registerApp(appId);

		progressDialog = new ProgressDialog(activity);
		// progressDialog.setMessage("Loading...");
		// progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	public void setWxCallback(WxCallback wxCallback) {
		this.wxCallback = wxCallback;
	}

	public void handleIntent(Intent intent, IWXAPIEventHandler handler){
		if (iwxapi != null){
			iwxapi.handleIntent(intent, handler);
		}
	}


	public void wxLogin(){

		String loginString = SPUtil.getSimpleString(activity,wx_file_name,wxResult);

		if (SStringUtil.isEmpty(loginString)){
			sendToWx();
			return;
		}
		WxLoginResult wxLoginResult = parseResult(loginString);

		if (wxLoginResult.accessTokenIsOk()){
			//直接请求获取
			getUserInfo(wxLoginResult);
		}else if (wxLoginResult.refreshTokenIsOk()){
			refreshAccessToken(wxLoginResult);
		}else {
			sendToWx();
		}

	}


	private void sendToWx(){

		if (iwxapi == null){
			return;
		}
		// send oauth request
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = activity.getPackageName() + "_" + SignatureUtil.getSignatureMD5(activity,activity.getPackageName());
		iwxapi.sendReq(req);

	}

	public void onResp(BaseResp baseResp){
		int errCode = baseResp.errCode;
		String transaction = baseResp.transaction;
		PL.d("baseResp type:" + baseResp.getType() + "  transaction:" + transaction);
		switch (errCode) {

			case BaseResp.ErrCode.ERR_OK:


				if (SStringUtil.isNotEmpty(transaction) && transaction.contains(SHARE_TEXT)){
					if (wxShareCallback != null){
						wxShareCallback.success();
					}
				}else if (SStringUtil.isNotEmpty(transaction) && transaction.contains(SHARE_WEB_PAGE)){
					if (wxShareCallback != null){
						wxShareCallback.success();
					}
				}else {
					SendAuth.Resp resp = (SendAuth.Resp)baseResp;
					String code = resp.code;
					if (SStringUtil.isNotEmpty(code)){
						codeExchangeAccessToken(code);
					}
				}

				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				if (SStringUtil.isNotEmpty(transaction) && transaction.contains(SHARE_TEXT)){
					if (wxShareCallback != null){
						wxShareCallback.failure();
					}
				}else if (SStringUtil.isNotEmpty(transaction) && transaction.contains(SHARE_WEB_PAGE)){
					if (wxShareCallback != null){
						wxShareCallback.failure();
					}
				}else {

					wxFiinshCallBack(null);
				}
				break;

			default:

				break;
		}
	}

	public void codeExchangeAccessToken(final String code){



		@SuppressLint("StaticFieldLeak") SRequestAsyncTask sRequestAsyncTask = new SRequestAsyncTask(){

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... strings) {

				String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
				String mUrl = String.format(url,appId,appSecret,code);
				String result = HttpRequest.get(mUrl);

				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				progressDialog.dismiss();

				PL.d("result:" + result);

				String str = saveTokenString(result);
				wxAccessTokenCallBack(str);
			}
		};

		sRequestAsyncTask.asyncExcute();

	}


	public void refreshAccessToken(final WxLoginResult wxLoginResult){

		@SuppressLint("StaticFieldLeak") SRequestAsyncTask sRequestAsyncTask = new SRequestAsyncTask(){

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... strings) {

				String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
				String mUrl = String.format(url,appId,wxLoginResult.getRefresh_token());
				String result = HttpRequest.get(mUrl);

				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				progressDialog.dismiss();

				PL.d("result:" + result);

				String str = saveTokenString(result);

				wxAccessTokenCallBack(str);
			}
		};

		sRequestAsyncTask.asyncExcute();

	}

	public void getUserInfo(final WxLoginResult wxLoginResult){

		@SuppressLint("StaticFieldLeak") SRequestAsyncTask sRequestAsyncTask = new SRequestAsyncTask(){

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... strings) {

				String url = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
				String mUrl = String.format(url,wxLoginResult.getAccess_token(),wxLoginResult.getOpenid());
				String result = HttpRequest.get(mUrl);

				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				progressDialog.dismiss();

				PL.d("result:" + result);

				WxUserInfo wxUserInfo = parseUserInfo(result);
				wxFiinshCallBack(wxUserInfo);
			}
		};

		sRequestAsyncTask.asyncExcute();

	}

	private void wxAccessTokenCallBack(String raw){
		WxLoginResult wxLoginResult = parseResult(raw);
		if (wxLoginResult != null){
			if (wxLoginResult.accessTokenIsOk()){
				getUserInfo(wxLoginResult);
				return;
			}else {
				if (SStringUtil.isNotEmpty(wxLoginResult.getErrmsg())){

					ToastUtils.toast(activity,wxLoginResult.getErrmsg());
				}
			}
		}

		wxFiinshCallBack(null);

	}

	public void wxFiinshCallBack(WxUserInfo wxUserInfo){
		this.wxUserInfo = wxUserInfo;
		if (wxCallback != null){
			wxCallback.callback(wxUserInfo);
		}
	}

	private String saveTokenString(String result) {
		if (SStringUtil.isNotEmpty(result)){
            try {
				JSONObject jsonObject = new JSONObject(result);
				jsonObject.put("getAccessTokenTime",System.currentTimeMillis());
				//parseResult(jsonObject.toString());
				SPUtil.saveSimpleInfo(activity,wx_file_name,wxResult,jsonObject.toString());
				return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "";
	}

	private WxLoginResult parseResult(String result) {

		WxLoginResult wxLoginResult = new WxLoginResult();
		try {
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.optString("access_token","");//接口调用凭证
            String refresh_token = jsonObject.optString("refresh_token","");//用户刷新access_token
            String openid = jsonObject.optString("openid","");//
            String scope = jsonObject.optString("scope","");//用户授权的作用域，使用逗号（,）分隔
            String unionid = jsonObject.optString("unionid","");//授权用户唯一标识
            int expires_in = jsonObject.optInt("expires_in",0);//access_token接口调用凭证超时时间，单位（秒）
            long getAccessTokenTime = jsonObject.optLong("getAccessTokenTime",0);//请求当前时间

			int errCode = jsonObject.optInt("errcode",0);
			String errmsg = jsonObject.optString("errmsg","");

			wxLoginResult.setAccess_token(access_token);
			wxLoginResult.setRefresh_token(refresh_token);
			wxLoginResult.setOpenid(openid);
			wxLoginResult.setScope(scope);
			wxLoginResult.setUnionid(unionid);
			wxLoginResult.setExpires_in(expires_in);
			wxLoginResult.setGetAccessTokenTime(getAccessTokenTime);

			wxLoginResult.setErrcode(errCode);
			wxLoginResult.setErrmsg(errmsg);

			return wxLoginResult;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}

	private WxUserInfo parseUserInfo(String result) {

		WxUserInfo wxUserInfo = new WxUserInfo();
		try {
            JSONObject jsonObject = new JSONObject(result);
			String openid = jsonObject.optString("openid","");//
			String nickname = jsonObject.optString("nickname","");
			String province = jsonObject.optString("province","");
            String city = jsonObject.optString("city","");//
            String country = jsonObject.optString("country","");//
            String headimgurl = jsonObject.optString("headimgurl","");//
            String unionid = jsonObject.optString("unionid","");//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
            int sex = jsonObject.optInt("sex",-1);//access_token接口调用凭证超时时间，单位（秒）

			int errCode = jsonObject.optInt("errcode",0);
			String errmsg = jsonObject.optString("errmsg","");

			wxUserInfo.setOpenid(openid);
			wxUserInfo.setNickname(nickname);
			wxUserInfo.setProvince(province);
			wxUserInfo.setCity(city);
			wxUserInfo.setCountry(country);
			wxUserInfo.setHeadimgurl(headimgurl);
			wxUserInfo.setUnionid(unionid);

			wxUserInfo.setErrcode(errCode);
			wxUserInfo.setErrmsg(errmsg);

			return wxUserInfo;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}


	public void shareText(String text,int mTargetScene,WxShareCallback wxShareCallback){

		this.wxShareCallback = wxShareCallback;

		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.description = text;

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction(SHARE_TEXT);
		req.message = msg;
		if (mTargetScene == 0){
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}else {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		iwxapi.sendReq(req);
	}

	//图片不能大于32K
	public void shareWebPage(String url, String title, String text, Bitmap thumbBitmap,int mTargetScene,WxShareCallback wxShareCallback){

		this.wxShareCallback = wxShareCallback;

		WXWebpageObject wxWebpageObject = new WXWebpageObject();
		wxWebpageObject.webpageUrl = url;

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = wxWebpageObject;
		msg.title = title;
		msg.description = text;

		if (thumbBitmap != null){
			msg.thumbData = Util.bmpToByteArray(thumbBitmap,true);//检查发送时的缩略图大小是否超过32k
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction(SHARE_WEB_PAGE);
		req.message = msg;
		if (mTargetScene == 0){
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}else {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		iwxapi.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
