package com.facebook.sfb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.FacebookSdk.InitializeCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONArrayCallback;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.messenger.ShareToMessengerParamsBuilder;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareLinkContent.Builder;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SFacebookProxy {

	protected static final String TAG = "SFacebookProxy";
	
	private static DefaultAudience defaultAudience = DefaultAudience.FRIENDS;
	private static LoginBehavior loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK;
	private static List<String> permissions = Arrays.asList(new String[]{"public_profile", "user_friends","email"});
	private static final int REQUEST_TOMESSENGER = 16;
	EfunFbShareCallBack shareCallBack;
	
	//final static List<String> PUBLISH_PERMISSIONS = Arrays.asList("publish_actions");

	
	private static LoginManager loginManager;
	private static CallbackManager callbackManager;
	
//	private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
	
	public SFacebookProxy(Context context) {
		FacebookSdk.sdkInitialize(context.getApplicationContext());
		// Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
        Log.d(TAG, "canPresentShareDialogWithPhotos:" + canPresentShareDialogWithPhotos);
	}
	
	public static void initFbSdk(Context context){
		FacebookSdk.sdkInitialize(context.getApplicationContext());
	}
	
	public static void activateApp(Context context){
		
		if (TextUtils.isEmpty(FbResUtil.findStringByName(context,"facebook_app_id"))) {
			Toast.makeText(context, "fb applicationId is empty", Toast.LENGTH_LONG).show();
		}else{
			activateApp(context, FbResUtil.findStringByName(context,"facebook_app_id"));
		}
		
	}
	
	public static void activateApp(final Context context,final String appId){
//		FacebookSdk.sdkInitialize(context.getApplicationContext(),new InitializeCallback() {
//
//			@Override
//			public void onInitialized() {
//				Log.d(TAG, "InitializeCallback");
//				AppEventsLogger.activateApp(context, appId);
//			}
//		});
		AppEventsLogger.activateApp(context, appId);
	}
	
	public static void trackingEvent(Activity activity,String eventName){
//		AppEventsLogger logger = AppEventsLogger.newLogger(activity);
//		logger.logEvent(eventName);
		trackingEvent(activity, eventName,null);
	}
	
	public static void trackingEvent(final Activity activity,final String eventName, final Bundle parameters){
		if (activity == null || TextUtils.isEmpty(eventName)) {
			return;
		}
		
		FacebookSdk.sdkInitialize(activity.getApplicationContext(), new InitializeCallback() {

			@Override
			public void onInitialized() {
				Log.d(TAG, "InitializeCallback");
				AppEventsLogger logger = AppEventsLogger.newLogger(activity);
				if (parameters != null) {
					logger.logEvent(eventName, parameters);
				}else{
					logger.logEvent(eventName);
				}
			}
		});
		
		
	}
	
	/**
	 * <p>Description: fb 登陆</p>
	 * @param activity
	 * @param fbLoginCallBack
	 * @date 2015年11月20日
	 */
	public void fbLogin(final Activity activity, final EfunFbLoginCallBack fbLoginCallBack) {

		if (loginManager == null) {
			loginManager = LoginManager.getInstance();
		}
		if (callbackManager == null) {
			callbackManager = CallbackManager.Factory.create();
		}
		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult result) {
				Log.d(TAG, "onSuccess");
			
				if (fbLoginCallBack == null){
					return;
				}
				Profile.fetchProfileForCurrentAccessToken();
				Profile p = Profile.getCurrentProfile();
				User user = new User();
				if (p != null) {
					user.setUserId(p.getId());
					user.setFirstName(p.getFirstName());
					user.setLastName(p.getLastName());
					user.setName(p.getName());
					user.setMiddleName(p.getMiddleName());
					user.setLinkUri(p.getLinkUri());

				}else{
					user.setUserId(result.getAccessToken().getUserId());
				}
				//fbLoginCallBack.onSuccess(user);
				FbSp.saveFbId(activity,user.getUserId());
				requestTokenForBusines(activity,user, fbLoginCallBack);
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "onError:" + error.getMessage());
				if (fbLoginCallBack != null) {
					fbLoginCallBack.onError(error.getMessage());
				}
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "onCancel");
				if (fbLoginCallBack != null) {
					fbLoginCallBack.onCancel();
				}
			}
		});

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken == null) {
			Log.d(TAG, "accessToken == null");
			loginManager.setDefaultAudience(defaultAudience);
			loginManager.setLoginBehavior(loginBehavior);
			// loginManager.logInWithReadPermissions(MainActivity.this,
			// permissions);
		
			//loginManager.logInWithPublishPermissions(activity, permissions);
			loginManager.logInWithReadPermissions(activity, permissions);

		} else {
			Profile p = Profile.getCurrentProfile();
			if (p != null) {
				if (fbLoginCallBack != null) {
					User user = new User();
					user.setUserId(p.getId());
					user.setFirstName(p.getFirstName());
					user.setLastName(p.getLastName());
					user.setName(p.getName());
					user.setMiddleName(p.getMiddleName());
					user.setLinkUri(p.getLinkUri());
//					fbLoginCallBack.onSuccess(user);
					FbSp.saveFbId(activity,user.getUserId());
					requestTokenForBusines(activity,user, fbLoginCallBack);
				}
			}
		}

	}

	/**
	 * 获取token_for_business,登陆完成后获取
	 * @param activity
	 * @param user
	 * @param fbLoginCallBack
	 */
	public void requestTokenForBusines(final Activity activity, final User user, final EfunFbLoginCallBack fbLoginCallBack){
		AccessToken accessToken =  AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d(TAG, "token_for_business:" + response.toString());
							if (response != null){
								JSONObject resJsonObject = response.getJSONObject();
								if (resJsonObject != null){
									String token_for_business = resJsonObject.optString("token_for_business","");
									FbSp.saveTokenForBusiness(activity,token_for_business);
									if (user != null){
										user.setTokenForBusiness(token_for_business);
									}
								}
							}
							if (fbLoginCallBack != null) {
								fbLoginCallBack.onSuccess(user);//回调登陆成功
							}

						}
                    });

			Bundle parameters = new Bundle();
			parameters.putString("fields", "token_for_business");
			request.setParameters(parameters);
			request.executeAsync();
		}else {
			if (fbLoginCallBack != null) {
				fbLoginCallBack.onSuccess(user);//回调登陆成功
			}
		}
	}


	public void fbLogout(Activity activity){
		if (loginManager != null) {
			loginManager.logOut();
		}else {
			LoginManager.getInstance().logOut();
		}
	}
	
	public void fbShare(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, String shareCaption, String shareDescrition,
			String shareLinkUrl, String sharePictureUrl) {
		fbShare(activity, efunFbShareCallBack, shareCaption, shareDescrition, shareLinkUrl, sharePictureUrl, "");
	}
	public void fbShare(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, String shareCaption, String shareDescrition,
			String shareLinkUrl, String sharePictureUrl,String shareLinkName) {
		
		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
			@Override
			public void onCancel() {
				Log.d(TAG, "onCancel");
				if (efunFbShareCallBack != null) {
					//efunFbShareCallBack.onCancel();
					efunFbShareCallBack.onSuccess();//由于ios不能判断取消为取消的情况，为了保持一致性，这里也回调成功
				}
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "onError");
				if (error != null) {
					Log.d(TAG, "onError:" + error.getMessage());
				}
				if (efunFbShareCallBack != null) {
					efunFbShareCallBack.onError(error.getMessage());
				}
			}

			@Override
			public void onSuccess(Sharer.Result result) {
				Log.d(TAG, "onSuccess");
				if (result != null) {
					String id = result.getPostId();
					Log.d(TAG, "onSuccess,post id:" + id);
				}
				
				if (efunFbShareCallBack != null) {
					efunFbShareCallBack.onSuccess();
				}
				
			}

		};
		
		ShareDialog shareDialog = new ShareDialog(activity);
		if (callbackManager == null) {
			callbackManager = CallbackManager.Factory.create();
		}
		shareDialog.registerCallback(callbackManager, shareCallback);
		
		/*
		 * Note: If your app share links to the iTunes or Google Play stores, we
		 * do not post any images or descriptions that you specify in the share.
		 * Instead we post some app information we scrape from the app store
		 * directly with the Webcrawler. This may not include images.
		 */
		Builder builder = new ShareLinkContent.Builder().setContentTitle(shareCaption).setContentDescription(
				shareDescrition);
		if (!TextUtils.isEmpty(shareLinkUrl)) {
			builder.setContentUrl(Uri.parse(shareLinkUrl));
		}
		if (!TextUtils.isEmpty(sharePictureUrl)) {
			builder.setImageUrl(Uri.parse(sharePictureUrl));
		}
		ShareLinkContent linkContent = builder.build();

		shareDialog.show(linkContent);

	}
	
	
	
	public void shareLocalPhotos(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, Bitmap ...images) {
		if (images != null) {
			List<SharePhoto> sharePhotos = new ArrayList<>();
			for (Bitmap bitmap : images) {
				SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
				sharePhotos.add(photo);
			}
			
			SharePhotoContent contents = new SharePhotoContent.Builder().addPhotos(sharePhotos).build();
			shareLocalPhotoImpl(activity, efunFbShareCallBack, contents);
		}else{
			shareLocalPhotoImpl(activity, efunFbShareCallBack, null);
		}
	}
	
	public void shareLocalPhoto(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, Bitmap bitmap) {
		if (bitmap != null) {
			SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
			SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
			shareLocalPhotoImpl(activity, efunFbShareCallBack, content);
		}else{
			shareLocalPhotoImpl(activity, efunFbShareCallBack, null);
		}
		
	}
	
	public void shareLocalPhoto(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, String imagePath) {
		Bitmap image = BitmapFactory.decodeFile(imagePath);
		if (image != null) {
			SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
			SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
			shareLocalPhotoImpl(activity, efunFbShareCallBack, content);
		}else{
			shareLocalPhotoImpl(activity, efunFbShareCallBack, null);
		}
		
	}
	
	public void shareLocalPhoto(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, Uri picUri) {
		if (picUri != null) {
			SharePhoto photo = new SharePhoto.Builder().setImageUrl(picUri).build();
			SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
			shareLocalPhotoImpl(activity, efunFbShareCallBack, content);
		}else{
			shareLocalPhotoImpl(activity, efunFbShareCallBack, null);
		}
		
	}
	
/*	public void shareLocalPhoto(Activity activity, final EfunFbShareCallBack efunFbShareCallBack, Uri imageUrl){
		SharePhoto photo = new SharePhoto.Builder().setImageUrl(imageUrl).build();
		shareLocalPhoto(activity, efunFbShareCallBack, photo);
	}*/
	
	private void shareLocalPhotoImpl(Activity activity, final EfunFbShareCallBack efunFbShareCallBack,SharePhotoContent content) {
	
		if (content == null) {
			if (efunFbShareCallBack != null) {
				efunFbShareCallBack.onError("SharePhotoContent is null");
			}
			return;
		}
		
		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
			@Override
			public void onCancel() {
				Log.d(TAG, "onCancel");
				if (efunFbShareCallBack != null) {
					efunFbShareCallBack.onCancel();
					//efunFbShareCallBack.onSuccess();//由于ios不能判断取消为取消的情况，为了保持一致性，这里也回调成功
				}
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "onError");
				if (error != null) {
					Log.d(TAG, "onError:" + error.getMessage());
				}
				if (efunFbShareCallBack != null) {
					efunFbShareCallBack.onError(error.getMessage());
				}
			}

			@Override
			public void onSuccess(Sharer.Result result) {
				Log.d(TAG, "onSuccess");
				if (result != null) {
					String id = result.getPostId();
					Log.d(TAG, "postId:" + id);
				}
				
				if (efunFbShareCallBack != null) {
					efunFbShareCallBack.onSuccess();
				}
				
			}

		};
		
//		SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
		//SharePhoto photo = new SharePhoto.Builder().setImageUrl(imageUrl).build();
	//	SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
		
		ShareDialog shareDialog = new ShareDialog(activity);
		if (callbackManager == null) {
			callbackManager = CallbackManager.Factory.create();
		}
		shareDialog.registerCallback(callbackManager, shareCallback);
		if (canPresentShareDialogWithPhotos) {
			shareDialog.show(content);
		}else if(hasPublishPermission()){
			 ShareApi.share(content, shareCallback);
		}else{
			Toast.makeText(activity, "facebook app is not installed", Toast.LENGTH_SHORT).show();
			if (efunFbShareCallBack != null) {
				efunFbShareCallBack.onError("facebook app is not installed and no publish_actions permission");
			}
		}
	}
	
	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null && accessToken.getPermissions().contains("publish_actions");
	}
/*	public void shareVideohh(){
		Bundle params = new Bundle();
		params.putString("source", "{video-data}");
		 make the API call 
		new GraphRequest(
		    AccessToken.getCurrentAccessToken(),
		    "/{page-id}/videos",
		    params,
		    HttpMethod.POST,
		    new GraphRequest.Callback() {
		        public void onCompleted(GraphResponse response) {
		             handle the result 
		        }
		    }
		).executeAsync();
	}
	
	public void shareVideo(Activity activity){
		if (!AccessToken.getCurrentAccessToken().getPermissions().contains(PUBLISH_PERMISSIONS)) {
			Log.d(TAG, "not publish_actions");
		}
		FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
			@Override
			public void onCancel() {
				Log.d(TAG, "onCancel");
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "onError");
				error.printStackTrace();
			}

			@Override
			public void onSuccess(Sharer.Result result) {
				Log.d(TAG, "onSuccess");
				if (result.getPostId() != null) {
					String id = result.getPostId();
				}
				
			}

		};
		
		ShareDialog shareDialog = new ShareDialog(activity);
		if (callbackManager == null) {
			callbackManager = CallbackManager.Factory.create();
		}
		shareDialog.registerCallback(callbackManager, shareCallback);
			
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mmmmmm.mp4");
		if (f.exists() && f.isFile()) {
			if (ShareDialog.canShow(ShareVideoContent.class)) {
				
				Uri videoUrl = Uri.fromFile(f);
				ShareVideo video = new ShareVideo.Builder()
						.setLocalUrl(videoUrl)
						.build();
				ShareVideoContent content = new ShareVideoContent.Builder()
						.setVideo(video)
						.setContentTitle("Video Title")
						.setContentDescription("Video description")
						.build();
				//ShareDialog.show(activity, content);
				ShareApi.share(content, shareCallback);
			}else{
				Log.d(TAG, "you cannot share videos");
			}
		}
		
	}*/
	
	public void requestInviteFriends(Activity activity,final Bundle b, final EfunFbGetInviteFriendsCallBack efunFbGetInviteFriendsCallBack){
		
		fbLogin(activity, new EfunFbLoginCallBack() {
			
			
			@Override
			public void onSuccess(User user) {
				
				Bundle bundle = b;
				AccessToken accessToken = AccessToken.getCurrentAccessToken();
				if (bundle == null) {
					bundle = new Bundle();
					bundle.putString("limit", "2000");
					bundle.putString("fields", "name,picture.width(300)");
				}
				if (accessToken != null) {
					new GraphRequest(accessToken, "/me/invitable_friends", bundle, HttpMethod.GET, new GraphRequest.Callback() {
						public void onCompleted(GraphResponse response) {
							Log.d(TAG, "invite:" + response.toString());
							if (efunFbGetInviteFriendsCallBack != null && response != null) {
								List<InviteFriend>  friends = JsonUtil.parseInviteFriendsJson(response.getJSONObject());
								efunFbGetInviteFriendsCallBack.onSuccess(response.getJSONObject(),friends);
							}
						}
					}).executeAsync();
				}else{
					if (efunFbGetInviteFriendsCallBack != null) {
						efunFbGetInviteFriendsCallBack.onError();
					}
				}
				
			}
			
			@Override
			public void onError(String message) {
				Log.d(TAG, "onError:" + message);
				if (efunFbGetInviteFriendsCallBack != null) {
					efunFbGetInviteFriendsCallBack.onError();
				}
				
			}
			
			@Override
			public void onCancel() {
				if (efunFbGetInviteFriendsCallBack != null) {
					efunFbGetInviteFriendsCallBack.onError();
				}	
			}
		});
		
		
	}
	
	
	public void inviteFriends(Activity activity,List<InviteFriend> inviteFriendIdsList,String message,final EfunFbInviteFriendsCallBack efunFbInviteFriendsCallBack) {
		
		if (inviteFriendIdsList != null && !inviteFriendIdsList.isEmpty()) {
//			StringBuilder stringBuilder = new StringBuilder();
//			for (InviteFriend inviteFriend : inviteFriendIdsList) {
//				stringBuilder.append(inviteFriend.getId()).append(",");
//			}
//		//	inviteFriendIdsString = inviteFriendIdsString.substring(0, inviteFriendIdsString.length()-1);
//			inviteFriends(activity, stringBuilder.toString(), message, efunFbInviteFriendsCallBack);
			List<InviteFriend> invitingList = new ArrayList<>();
			for (InviteFriend friend : inviteFriendIdsList){
				if(!TextUtils.isEmpty(friend.getId())){
					invitingList.add(friend);
				}
			}

//			List<InviteFriend> invitingList = inviteFriendIdsList.subList(0, inviteFriendIdsList.size());
			List<String> fbids = new ArrayList<String>();

			inviteFriendinBatches(activity,invitingList,"",fbids,message,efunFbInviteFriendsCallBack);
		}
	}

	/* 处理分批次邀请的逻辑*/
	private void inviteFriendinBatches(final Activity activity, final List<InviteFriend>
			invitingList,final String requestId, final List<String> fbids, final String message, final EfunFbInviteFriendsCallBack callback) {

		final List<InviteFriend> currentList = new ArrayList<InviteFriend>();
		while (currentList.size() < 40 && invitingList.size() > 0) {
			currentList.add(invitingList.remove(0));
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (InviteFriend inviteFriend : currentList) {
			stringBuilder.append(inviteFriend.getId()).append(",");
		}

		inviteFriends(activity, stringBuilder.toString(), message, new SFacebookProxy
				.EfunFbInviteFriendsCallBack() {


			@Override
			public void onCancel() {
				if (!TextUtils.isEmpty(requestId)) {
					callback.onSuccess(requestId,fbids);
				} else {
					callback.onCancel();
				}
			}

			@Override
			public void onError(String s) {
				Log.e(TAG,"inviteFriendinBatches " + s);
				if (!TextUtils.isEmpty(requestId)) {
					callback.onSuccess(requestId,fbids);
				} else {
					callback.onError(s);
				}
			}

			@Override
			public void onSuccess(String id, List<String> currentfbids) {
				Log.e(TAG,"inviteFriendsinBatches onSuccess" + id +
						"currentfbids size " + currentfbids.size());
				fbids.addAll(currentfbids);

				if (invitingList.size() > 0) {
					inviteFriendinBatches(activity, invitingList, id, fbids, message,
							callback);
				} else {
					callback.onSuccess(id, fbids);
				}

			}
		});

	}
	
	
	public void inviteFriends(final Activity activity,final String inviteFriendIds,final String message,final EfunFbInviteFriendsCallBack efunFbInviteFriendsCallBack) {
		if (TextUtils.isEmpty(message)) {
			Toast.makeText(activity, "request message is empty", Toast.LENGTH_SHORT).show();
			return;
		}
		fbLogin(activity, new EfunFbLoginCallBack() {
			
			@Override
			public void onSuccess(User user) {
				GameRequestDialog requestDialog = new GameRequestDialog(activity);
				if (callbackManager == null) {
					callbackManager = CallbackManager.Factory.create();
				}
				requestDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
					public void onSuccess(GameRequestDialog.Result result) {
						String id = result.getRequestId();
						List<String> requestRecipients = result.getRequestRecipients();
						Log.d(TAG, "inviteFriends:" + id);
						if (efunFbInviteFriendsCallBack != null) {
							efunFbInviteFriendsCallBack.onSuccess(id,requestRecipients);
						}
					}

					public void onCancel() {
						Log.d(TAG, "inviteFriends onCancel");
						if (efunFbInviteFriendsCallBack != null) {
							efunFbInviteFriendsCallBack.onCancel();
						}
					}

					public void onError(FacebookException error) {
						Log.d(TAG, "inviteFriends onError:" + error.getMessage());
						if (efunFbInviteFriendsCallBack != null) {
							efunFbInviteFriendsCallBack.onError(error.getMessage());
						}
					}
				});
				
				 GameRequestContent content = new GameRequestContent.Builder()
			                .setMessage(message)
			                .setTo(inviteFriendIds)
			                
			                //.setActionType(ActionType.SEND)
			                .build();
			     requestDialog.show(content);
				
			}
			
			@Override
			public void onError(String message) {
				if (efunFbInviteFriendsCallBack != null) {
					efunFbInviteFriendsCallBack.onError(message);
				}
				
			}
			
			@Override
			public void onCancel() {
				if (efunFbInviteFriendsCallBack != null) {
					efunFbInviteFriendsCallBack.onCancel();
				}
			}
		});
		
	}
	
	public void requestMyFriends(Activity activity,final EfunFbMyFriendsCallBack efunFbMyFiendsCallBack){
		
		fbLogin(activity, new EfunFbLoginCallBack() {
			
			@Override
			public void onSuccess(User user) {

				AccessToken accessToken =  AccessToken.getCurrentAccessToken();
				
				if (accessToken != null) {
					GraphRequest.newMyFriendsRequest(accessToken, new GraphJSONArrayCallback() {

						@Override
						public void onCompleted(JSONArray objects, GraphResponse response) {
							Log.d(TAG, "objects:" + objects.toString());
							Log.d(TAG, "response:" + response.toString());
							if (efunFbMyFiendsCallBack != null) {
								efunFbMyFiendsCallBack.onSuccess(objects, response.getJSONObject());
							}
						}
					}).executeAsync();
				}else{
					if (efunFbMyFiendsCallBack != null) {
						efunFbMyFiendsCallBack.onError();
					}
				}
				
			}
			
			@Override
			public void onError(String message) {
				Log.d(TAG, "onError:" + message);
				if (efunFbMyFiendsCallBack != null) {
					efunFbMyFiendsCallBack.onError();
				}
				
			}
			
			@Override
			public void onCancel() {
				if (efunFbMyFiendsCallBack != null) {
					efunFbMyFiendsCallBack.onError();
				}
			}
		});
		
	}


	public void requestBusinessId(final Activity activity,final EfunFbBusinessIdCallBack efunFbBusinessIdCallBack){
		
		AccessToken accessToken =  AccessToken.getCurrentAccessToken();
		Bundle b = new Bundle();
		b.putString("limit", "300");
	//	b.putString("fields", "300");
		if (accessToken != null) {
			new GraphRequest(accessToken, "/me/ids_for_business", b, HttpMethod.GET, new GraphRequest.Callback() {
				public void onCompleted(GraphResponse response) {
					Log.d(TAG, "requestBusinessId:" + response.toString());
					String apps = "";
					try {
						if (response.getError() == null) {
						//	JSONObject bussesIdsGraphObject = response.getJSONObject();
							JSONObject InnerJSONObject = response.getJSONObject();
							JSONArray dataArr = InnerJSONObject.optJSONArray("data");
							if (dataArr != null && dataArr.length() != 0) {
								StringBuilder stringBuilder = new StringBuilder();
								for (int i = 0; i < dataArr.length(); i++) {
									JSONObject mJsonObject = dataArr.optJSONObject(i);
									String scopeId = mJsonObject.optString("id", "");
									if (!TextUtils.isEmpty(scopeId)) {
										String appId = mJsonObject.optJSONObject("app") == null ? "" : mJsonObject.optJSONObject("app").optString("id", "");
										if (!TextUtils.isEmpty(appId)) {
											stringBuilder.append(scopeId + "_" + appId + ",");// 拼接scopeId和appId
										}
									}
								}
								apps = stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
//								activity.getSharedPreferences(FbSp.S_FACEBOOK_FILE, Activity.MODE_PRIVATE).edit().putString(FbSp.S_FB_APP_BUSINESS_IDS, apps).commit();
								FbSp.saveAppsBusinessId(activity,apps);
								if (efunFbBusinessIdCallBack != null) {
									efunFbBusinessIdCallBack.onSuccess(apps);
								}
							}
						}else if(efunFbBusinessIdCallBack != null){
							efunFbBusinessIdCallBack.onError();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			}).executeAsync();
		}else if(efunFbBusinessIdCallBack != null){
			efunFbBusinessIdCallBack.onError();
		}
	}
	
	
	public void getMyProfile(Activity activity, final EfunFbLoginCallBack callBack) {
		// me?fields=name,id,first_name,last_name
		Bundle b = new Bundle();
		b.putString("fields", "name,first_name,last_name,id,gender,link,middle_name,picture.width(300)");
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		new GraphRequest(accessToken, "/me", b, HttpMethod.GET, new GraphRequest.Callback() {

			@Override
			public void onCompleted(GraphResponse response) {
				if (response == null) {
					if (callBack != null) {
						callBack.onError("response null");
					}
				} else if (response.getError() != null) {
					if (callBack != null) {
						callBack.onError(response.getError().getErrorMessage() + "");
					}
				} else {
					Log.d("efun", "获取信息-- 》 " + response.toString());
					JSONObject userInfo = response.getJSONObject();
					if(userInfo == null) {
						if (callBack != null) {
							callBack.onError("userInfo null");
						}
						return;
					}
					String id = userInfo.optString("id", "");
					String name = userInfo.optString("name", "");
					String first_name = userInfo.optString("first_name", "");
					String last_name = userInfo.optString("last_name", "");
					String gender = userInfo.optString("gender", "");
					String link = userInfo.optString("link", "");
					String middle_name = userInfo.optString("middle_name", "");
					String picture = "";
					if(userInfo.optJSONObject("picture") != null && userInfo.optJSONObject("picture").optJSONObject("data") != null) {
						picture = userInfo.optJSONObject("picture").optJSONObject("data").optString("url", "");
					}
					User user = new User();
					user.setUserId(id);
					user.setFirstName(first_name);
					user.setLastName(last_name);
					user.setName(name);
					user.setGender(gender);
					user.setMiddleName(middle_name);
					user.setLinkUri(Uri.parse(link));
					user.setPictureUri(Uri.parse(picture));
					if (callBack != null) {
						callBack.onSuccess(user);
					}
				}
			}
		}).executeAsync();
	}
	
	public void requestMyFriends(Activity activity, Bundle bundle, final EfunFbMyFriendsCallBack efunFbMyFiendsCallBack) {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			if (bundle == null) {
				bundle = new Bundle();
				bundle.putString("fields", "friends.limit(2000){name,gender,picture.width(300)}");
			}
			new GraphRequest(accessToken, "/me", bundle, HttpMethod.GET, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {
					if (response == null) {
						if (efunFbMyFiendsCallBack != null) {
							efunFbMyFiendsCallBack.onError();
						}
					} else if (response.getError() != null) {
						Log.e("efun", response.getError().getErrorMessage() + "");
						if (efunFbMyFiendsCallBack != null) {
							efunFbMyFiendsCallBack.onError();
						}
					} else {
						Log.d(TAG, "response:" + response.toString());
						if (efunFbMyFiendsCallBack != null) {
							efunFbMyFiendsCallBack.onSuccess(null, response.getJSONObject());
						}
					}

				}
			}).executeAsync();
		} else {
			if (efunFbMyFiendsCallBack != null) {
				efunFbMyFiendsCallBack.onError();
			}
		}
	}
	
	public void shareToMessenger(Activity activity, String picPath, EfunFbShareCallBack efunFbShareCallBack) {
		if(TextUtils.isEmpty(picPath)) {
			Log.e(TAG, "shareToMessenger 图片路径为空");
			return;
		}
		File tempFile = new File(picPath);
		Uri picUri = Uri.fromFile(tempFile);
		shareToMessenger(activity, picUri, efunFbShareCallBack);
	}
	
	public void shareToMessenger(Activity activity, Uri picUri, EfunFbShareCallBack efunFbShareCallBack) {
		if(picUri == null) {
			Log.e(TAG, "shareToMessenger 图片Uri为空");
			return;
		}
		this.shareCallBack = efunFbShareCallBack;
		String mimeType = "image/*";
		ShareToMessengerParamsBuilder newBuilder = ShareToMessengerParams.newBuilder(picUri, mimeType);
		ShareToMessengerParams shareToMessengerParams = newBuilder.build();
		
		MessengerUtils.shareToMessenger(activity, REQUEST_TOMESSENGER, shareToMessengerParams);
	}

	
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		 if (callbackManager != null) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
		 if(requestCode == REQUEST_TOMESSENGER) {
			 Log.d(TAG, "to messenger 回调");
			 if(shareCallBack != null) {
				 shareCallBack.onSuccess();
			 }
		 }
	}
	
	public void onDestroy(Activity activity) {
		fbLogout(activity);
	}
	

	public interface EfunFbShareCallBack{
		public void onCancel();
		public void onError(String message);
		public void onSuccess();
	}
	
	public interface EfunFbLoginCallBack{
		public void onCancel();
		public void onError(String message);
		public void onSuccess(User user);
	}
	
	/**
	* <p>Title: EfunFbInviteFriendsCallBack</p>
	* <p>Description: 请求好友的回调接口</p>
	* <p>Company: EFun</p> 
	* @author GanYuanrong
	* @date 2015年11月23日
	*/
	public interface EfunFbInviteFriendsCallBack{
		public void onCancel();
		public void onError(String message);
		public void onSuccess(String requestId,List<String> requestRecipients);
	}
	
	/**
	* <p>Title: EfunFbGetInviteFriendsCallBack</p>
	* <p>Description: 获取好友的回调接口</p>
	* <p>Company: EFun</p> 
	* @author GanYuanrong
	* @date 2015年11月23日
	*/
	public interface EfunFbGetInviteFriendsCallBack{
		public void onError();
		public void onSuccess(JSONObject graphObject,List<InviteFriend> inviteFriends);
	}
	
	public interface EfunFbMyFriendsCallBack{
		public void onError();
		public void onSuccess(JSONArray objects, JSONObject graphObject);
	}
	public interface EfunFbBusinessIdCallBack{
		public void onError();
		public void onSuccess(String businessId);
	}
	
	
	
	public class User{
		
		String userId;

		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}

		/**
		 * @param userId the userId to set
		 */
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	    private  String firstName;
	    private  String middleName;
	    private  String lastName;
	    private  String name;
	    private  Uri linkUri;
	    
	    private String gender;
		private Uri pictureUri;

		private String tokenForBusiness;

		/**
		 * @return the firstName
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * @param firstName the firstName to set
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		/**
		 * @return the middleName
		 */
		public String getMiddleName() {
			return middleName;
		}

		/**
		 * @param middleName the middleName to set
		 */
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		/**
		 * @return the lastName
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * @param lastName the lastName to set
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the linkUri
		 */
		public Uri getLinkUri() {
			return linkUri;
		}

		/**
		 * @param linkUri the linkUri to set
		 */
		public void setLinkUri(Uri linkUri) {
			this.linkUri = linkUri;
		}
	    

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Uri getPictureUri() {
			return pictureUri;
		}

		public void setPictureUri(Uri pictureUri) {
			this.pictureUri = pictureUri;
		}

		@Override
		public String toString() {
			return "User{" +
					"userId='" + userId + '\'' +
					", firstName='" + firstName + '\'' +
					", middleName='" + middleName + '\'' +
					", lastName='" + lastName + '\'' +
					", name='" + name + '\'' +
					", linkUri=" + linkUri +
					", gender='" + gender + '\'' +
					", pictureUri=" + pictureUri +
					", tokenForBusiness='" + tokenForBusiness + '\'' +
					'}';
		}

		public String getTokenForBusiness() {
			return tokenForBusiness;
		}

		public void setTokenForBusiness(String tokenForBusiness) {
			this.tokenForBusiness = tokenForBusiness;
		}
	}

}
