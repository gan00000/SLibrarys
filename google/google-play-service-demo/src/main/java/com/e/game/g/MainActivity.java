package com.e.game.g;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.efun.google.GoogleSignIn;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    GoogleSignIn googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EAds.initEAds(this);
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        fetchConfig();
        googleSignIn = new GoogleSignIn(this);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn.startSignIn(new GoogleSignIn.GoogleSignInCallBack() {
                    @Override
                    public void success(String id, String mFullName, String mEmail) {
                        Log.d("MainActivity","success:" + id);
                    }

                    @Override
                    public void failure() {
                        Log.d("MainActivity","failure");
                    }
                });
            }
        });

        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn.handleActivityDestroy(MainActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        googleSignIn.handleActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        googleSignIn.handleActivityDestroy(this);
    }

    public void fetchConfig() {
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).

        if (mFirebaseRemoteConfig == null)
            return;
        //long cacheExpiration = 2 * 60 * 60l;//秒
        long cacheExpiration = 60 * 2;//秒
       // mFirebaseRemoteConfig.setDefaults(new HashMap<String, Object>().put("e_game_g_app_url",""));
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(EAds.TAG, "Fetch Succeeded");
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                           /* String e_game_g_app_url = mFirebaseRemoteConfig.getString("e_game_g_app_url");
                            String e_game_g_image_url = mFirebaseRemoteConfig.getString("e_game_g_image_url");
                            Log.d(EAds.TAG, "fetchConfig e_game_g_app_url:" + e_game_g_app_url);
                            Log.d(EAds.TAG, "fetchConfig e_game_g_image_url:" + e_game_g_image_url);
                            */
                            List<String> urls = getRemoteAppUrls(mFirebaseRemoteConfig);

                            if (urls != null && !urls.isEmpty()){
                                if (urls.size() == 1){
                                    XXXAppD.excuteXXXAppDown(MainActivity.this,urls.get(0));
                                }
                            }

                        } else {
                            Log.d(EAds.TAG, "Fetch failed");
                        }
                    }
                });
    }


    private List<String> getRemoteAppUrls(FirebaseRemoteConfig mFirebaseRemoteConfig){

        List<String> urls = new ArrayList();

        if (mFirebaseRemoteConfig == null)
            return urls;

        for (int i = 0; i < 10; i++) {
            String appUrl = mFirebaseRemoteConfig.getString("e_game_g_app_url_" + i);
            if (!TextUtils.isEmpty(appUrl) && appUrl.contains("http")){
                urls.add(appUrl);
            }
        }
        return urls;
    }
}
