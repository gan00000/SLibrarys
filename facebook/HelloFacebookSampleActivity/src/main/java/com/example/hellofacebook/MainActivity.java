package com.example.hellofacebook;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.efun.hh.R;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.sfb.SFacebookProxy;
import com.facebook.sfb.SFacebookProxy.EfunFbBusinessIdCallBack;
import com.facebook.sfb.SFacebookProxy.EfunFbGetInviteFriendsCallBack;
import com.facebook.sfb.SFacebookProxy.EfunFbInviteFriendsCallBack;
import com.facebook.sfb.SFacebookProxy.EfunFbLoginCallBack;
import com.facebook.sfb.SFacebookProxy.EfunFbMyFriendsCallBack;
import com.facebook.sfb.SFacebookProxy.EfunFbShareCallBack;
import com.facebook.sfb.SFacebookProxy.User;
import com.facebook.sfb.InviteFriend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

			
				/*Intent i = new Intent(getApplicationContext(), FacebookActivity.class);
				startActivity(i);*/
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// 9.onDestroy调用onDestroy
		efp.onDestroy(this);
	}

	/**
	 * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}, for
	 * your activity to start interacting with the user. This is a good place to begin animations,
	 * open exclusive-access devices (such as the camera), etc.
	 *
	 * <p>Keep in mind that onResume is not the best indicator that your activity is visible to the
	 * user; a system window such as the keyguard may be in front.  Use {@link
	 * #onWindowFocusChanged} to know for certain that your activity is visible to the user (for
	 * example, to resume a game).
	 *
	 * <p><em>Derived classes must call through to the super class's implementation of this method.
	 * If they do not, an exception will be thrown.</em></p>
	 *
	 * @see #onRestoreInstanceState
	 * @see #onRestart
	 * @see #onPostResume
	 * @see #onPause
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Called as part of the activity lifecycle when an activity is going into the background, but
	 * has not (yet) been killed.  The counterpart to {@link #onResume}.
	 *
	 * <p>When activity B is launched in front of activity A, this callback will be invoked on A.  B
	 * will not be created until A's {@link #onPause} returns, so be sure to not do anything lengthy
	 * here.
	 *
	 * <p>This callback is mostly used for saving any persistent state the activity is editing, to
	 * present a "edit in place" model to the user and making sure nothing is lost if there are not
	 * enough resources to start the new activity without first killing this one.  This is also a
	 * good place to do things like stop animations and other things that consume a noticeable
	 * amount of CPU in order to make the switch to the next activity as fast as possible, or to
	 * close resources that are exclusive access such as the camera.
	 *
	 * <p>In situations where the system needs more memory it may kill paused processes to reclaim
	 * resources.  Because of this, you should be sure that all of your state is saved by the time
	 * you return from this function.  In general {@link #onSaveInstanceState} is used to save
	 * per-instance state in the activity and this method is used to store global persistent data
	 * (in content providers, files, etc.)
	 *
	 * <p>After receiving this call you will usually receive a following call to {@link #onStop}
	 * (after the next activity has been resumed and displayed), however in some cases there will be
	 * a direct call back to {@link #onResume} without going through the stopped state.
	 *
	 * <p><em>Derived classes must call through to the super class's implementation of this method.
	 * If they do not, an exception will be thrown.</em></p>
	 *
	 * @see #onResume
	 * @see #onSaveInstanceState
	 * @see #onStop
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Called when you are no longer visible to the user.  You will next receive either {@link
	 * #onRestart}, {@link #onDestroy}, or nothing, depending on later user activity.
	 *
	 * <p><em>Derived classes must call through to the super class's implementation of this method.
	 * If they do not, an exception will be thrown.</em></p>
	 *
	 * @see #onRestart
	 * @see #onResume
	 * @see #onSaveInstanceState
	 * @see #onDestroy
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}
}
