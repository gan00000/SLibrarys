/*
package com.example.hellofacebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.efun.hh.R;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.sfb.InviteFriend;
import com.facebook.sfb.SFacebookProxy;
import com.facebook.sfb.SFacebookProxy.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

	protected static final String TAG = "SFacebookProxy";
	Button fbButtonLogin;
	Button fbButtonLogout;
	Button fbButtonShare;
	Button fbButtonShareLocalImage;
	SFacebookProxy efp;
	protected EfunFbShareCallBack efunFbShareCallBack;
	List<InviteFriend> ifs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fbButtonLogin = (Button) findViewById(R.id.fbLogin);
		fbButtonLogout = (Button) findViewById(R.id.fbLogout);
		fbButtonShare = (Button) findViewById(R.id.fbShare);
		fbButtonShareLocalImage = (Button) findViewById(R.id.fbShareLocalImage);
		
		//广告
		SFacebookProxy.activateApp(this);
		// 1.实例EfunFacebookProxy
		efp = new SFacebookProxy(this);
		// 2.初始化fb sdk
		efp.initFbSdk(this);
		 Bundle b = new Bundle();
		efp.trackingEvent(this, AppEventsConstants.EVENT_NAME_PURCHASED,b);
		
		//efp.onCreate(this, savedInstanceState);

		fbButtonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 3.fb登陆
				efp.fbLogin(MainActivity.this, new EfunFbLoginCallBack() {

					@Override
					public void onSuccess(User user) {// 登陆成功
						Log.d(TAG, "onSuccess 1 :" + user.toString());

					}

					@Override
					public void onError(String message) {// 登陆出错
						Log.d(TAG, "message 1");

					}

					@Override
					public void onCancel() {// 登陆取消
						Log.d(TAG, "onCancel 1");

					}
				});
			}
		});

		fbButtonLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				efp.fbLogout(MainActivity.this);
			}
		});

		efunFbShareCallBack = new EfunFbShareCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.d(TAG, "share onSuccess");
			}

			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				Log.d(TAG, "share onError");
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Log.d(TAG, "share onCancel");
			}
		};

		fbButtonShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//efp.shareVideo(MainActivity.this);
				// 4.分享
				efp.fbShare(MainActivity.this, efunFbShareCallBack, 
						"like you", 
						"If your app share links to the iTunes or Google Play stores",
						"http://my.oschina.net/u/146011/blog/350172",
						"https://www.youtube.com/watch?v=9m6MoBM-sFI&list=PLWz5rJ2EKKc9ofd2f-_-xmUi07wIGZa1c&index=3");
			}
		});

		findViewById(R.id.InviteFiends).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("limit", "30");
				bundle.putString("fields", "name,picture.width(300)");
				// 5.获取可邀请好友
				efp.requestInviteFriends(MainActivity.this, bundle, new EfunFbGetInviteFriendsCallBack() {

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(JSONObject graphObject, List<InviteFriend> inviteFriends) {
						ifs = inviteFriends;
						Log.d(TAG, "requestInviteFriends:" + ifs.size());
						
					}
				});
			}
		});

		findViewById(R.id.MyFriends).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 6.获取我的好友
				efp.requestMyFriends(MainActivity.this, new EfunFbMyFriendsCallBack() {

					@Override
					public void onSuccess(JSONArray objects, JSONObject graphObject) {
						Log.d(TAG, "requestMyFriends:" + objects.toString());
						Log.d(TAG, "requestMyFriends:" + graphObject.toString());
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
			}
		});

		findViewById(R.id.business).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 7.获取busness id
				efp.requestBusinessId(MainActivity.this, new EfunFbBusinessIdCallBack() {

					@Override
					public void onSuccess(String businessId) {
						Log.d(TAG, "requestBusinessId :" + businessId);
					}

					@Override
					public void onError() {

					}
				});
			}
		});

		findViewById(R.id.invite).setOnClickListener(new OnClickListener() {
//			AVmyaU5Hypdp3524r9hmjXSdL-ljbzMn0J0U2Di0rrrrgYpcHUFkdnGlgMoJO9J4KOKLa_TCjh-mS27ii1pYpPz632_ZNDWk7S1p0l-8l9mcsg
			@Override
			public void onClick(View v) {
				efp.inviteFriends(MainActivity.this, ifs, "kdkds", new EfunFbInviteFriendsCallBack() {

					@Override
					public void onError(String message) {
						Log.d(TAG, "onError :");

					}

					@Override
					public void onCancel() {
						Log.d(TAG, "onCancel :");

					}

					@Override
					public void onSuccess(String requestId, List<String> arg1) {
						// TODO Auto-generated method stub
						Log.d(TAG, "inviteFriends :" + requestId);
						Log.d(TAG, "inviteFriendsId :" + arg1);
					}
				});

			
				*/
/*Intent i = new Intent(getApplicationContext(), FacebookActivity.class);
				startActivity(i);*//*

			}
		});
		
		fbButtonShareLocalImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String rootsdcard = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
				efp.shareLocalPhotos(MainActivity.this, efunFbShareCallBack,  BitmapFactory.decodeFile(rootsdcard + "fbshare2.png"),BitmapFactory.decodeFile(rootsdcard + "fbshare1.png"));
				
				//efp.shareLocalPhoto(MainActivity.this, efunFbShareCallBack, BitmapFactory.decodeResource(getResources(), R.drawable.fbshare1));
			}
		});

		findViewById(R.id.get_token_business).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				efp.requestTokenForBusines();
			}
		});

	}
	


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 8.onActivityResult调用onActivityResult
		efp.onActivityResult(this, requestCode, resultCode, data);
	}

	*/
/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 *//*

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// 9.onDestroy调用onDestroy
		efp.onDestroy(this);
	}

}
*/
