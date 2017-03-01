//package com.starpy.google;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.View;
//
//public class MainActivity extends FragmentActivity {
//
//    SGoogleSignIn gs;
//    public static final String TAG = "Google";
//    int googleServicesCode1;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        gs = new SGoogleSignIn(this);
//
//        EfunGoogleProxy.onCreateMainActivity(this);
//
//
//        EfunGoogleProxy.initPush(this.getApplicationContext());
//
//        findViewById(R.id.googleSignIn).setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                gs = new SGoogleSignIn(MainActivity.this);
//                gs.startSignIn(new SGoogleSignIn.GoogleSignInCallBack() {
//                    @Override
//                    public void success(String id, String mFullName, String mEmail) {
//                        Log.d(TAG,"ID:" + id);
//
//                    }
//
//                    @Override
//                    public void failure() {
//
//                    }
//                });
//            }
//        });
//
//        findViewById(R.id.showError).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(10,MainActivity.this,11);
//                //  errorDialog.show();
//                EfunGoogleProxy.isGooglePlayServicesAvailable(MainActivity.this);
//
//                EfunGoogleProxy.showGoogleServicesErrorDialog(MainActivity.this);
//
//                EfunGoogleProxy.getAdvertisingId(MainActivity.this);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        gs.handleActivityResult(this,requestCode,resultCode,data);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        gs.handleActivityDestroy(this);
//    }
//}
