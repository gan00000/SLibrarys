package com.starpy.sdk.demo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.core.base.SBaseActivity;
import com.core.base.http.HttpRequestCore;
import com.core.base.utils.FileUtil;
import com.core.base.utils.ObbUtil;
import com.core.base.utils.PL;
import com.core.base.utils.PermissionUtil;
import com.core.base.utils.ZipUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SLoginType;
import com.starpy.base.bean.SPayType;
import com.starpy.base.utils.SLog;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.out.ISdkCallBack;
import com.starpy.sdk.out.IStarpy;
import com.starpy.sdk.out.StarpyFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MainActivity extends SBaseActivity {

    private Button loginButton, othersPayButton,googlePayBtn,csButton,shareButton;

    private IStarpy iStarpy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.demo_login);
        othersPayButton = (Button) findViewById(R.id.demo_pay);
        googlePayBtn = (Button) findViewById(R.id.demo_pay_google);
        csButton = (Button) findViewById(R.id.demo_cs);
        shareButton = (Button) findViewById(R.id.demo_share);

        Fresco.initialize(this);
        SLog.enableInfo(true);
        SLog.enableDebug(true);

        iStarpy = StarpyFactory.create();

        iStarpy.setGameLanguage(this, SGameLanguage.en_US);

        //初始化sdk
        iStarpy.initSDK(this);

        //在游戏Activity的onCreate生命周期中调用
        iStarpy.onCreate(this);


        /**
         * 在游戏获得角色信息的时候调用
         * roleId 角色id
         * roleName  角色名
         * rolelevel 角色等级
         * severCode 角色伺服器id
         * serverName 角色伺服器名称
         */
        iStarpy.registerRoleInfo(this, "roleid_1", "gan", "10", "386", "serverName");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登陆接口 ILoginCallBack为登录成功后的回调
                iStarpy.login(MainActivity.this, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null){
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();

                            PL.i("uid:" + uid);
                            if (sLoginResponse.getLoginType().equals(SLoginType.LOGIN_TYPE_GOOGLE)){
                                PL.i("google login");
                            }

                        }
                    }
                });

                String s = StarPyUtil.encryptDyUrl(getApplicationContext(),FileUtil.readAssetsTxtFile(getApplicationContext(),"s_sdk_config_gl.txt"));
//
                PL.i(s);

                PL.i(StarPyUtil.decryptDyUrl(getApplicationContext(),s));
                Resources resources = getResources();//获得res资源对象
                Configuration config = resources.getConfiguration();//获得设置对象
                PL.i("onConfigurationChanged:" + config.toString());
            }
        });


        othersPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
                充值接口
                SPayType SPayType.OTHERS为第三方储值，SPayType.GOOGLE为Google储值
                cpOrderId cp订单号，请保持每次的值都是不会重复的
                productId 充值的商品id
                roleLevel 觉得等级
                customize 自定义透传字段（从服务端回调到cp）
                */
                iStarpy.pay(MainActivity.this, SPayType.OTHERS, "" + System.currentTimeMillis(), "payone", "roleLevel", "customize");


            }
        });

        googlePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                充值接口
                SPayType SPayType.OTHERS为第三方储值，SPayType.GOOGLE为Google储值
                cpOrderId cp订单号，请保持每次的值都是不会重复的
                productId 充值的商品id
                roleLevel 觉得等级
                customize 自定义透传字段（从服务端回调到cp）
                */
                iStarpy.pay(MainActivity.this, SPayType.GOOGLE, "" + System.currentTimeMillis(), "tw.zjld.2usd", "10", "customize");

            }
        });

        http://localhost:8086/player_entrance?gameCode=brmmd&packageName=web&userId=2&accessToken=123&loginTimestamp=234&serverCode=1&roleName=%E9%A9%AC%E7%BA%A2%E5%86%9B
        csButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 打开客服接口
                 * level：游戏等级
                 * vipLevel：vip等级，没有就选""
                 */
                iStarpy.cs(MainActivity.this,"level","vipLevel");
//                testOBBCanRead();

            }
        });


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //下面的参数请按照实际传值
                String title = "hello world";
                String message = "hello world message";
                String shareUrl = "http://ads.starb168.com/ads_scanner?gameCode=mthxtw&adsPlatForm=star_event&advertiser=share";
                String picUrl = "https://lh3.googleusercontent.com/mOgnBSExg8wbssGwPGj-rscvNEklCvV3mGVqXuViUqROUok0P6P3JTo6Hmho0LRXoC8=w300-rw";
                //分享回调
                PL.i(shareUrl);
                ISdkCallBack iSdkCallBack = new ISdkCallBack() {
                    @Override
                    public void success() {
                        PL.i("share  success");
                    }

                    @Override
                    public void failure() {
                        PL.i("share  failure");
                    }
                };

                iStarpy.share(MainActivity.this,iSdkCallBack,title, message, shareUrl, picUrl);

            }
        });

        findViewById(R.id.open_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 打开一个活动页面接口
                 * level：游戏等级
                 * vipLevel：vip等级，没有就写""
                 */
                iStarpy.openWebview(MainActivity.this,"roleLevel","10");
            }
        });

        findViewById(R.id.open_plat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.openPlatform(MainActivity.this,"","");

            }
        });

        findViewById(R.id.demo_google_unlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.unlockAchievement("CgkI75qesq4WEAIQAA");
            }
        });
        findViewById(R.id.demo_dis_cj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.displayingAchievements();
            }
        });
        findViewById(R.id.open_sumitScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.submitScore("CggI-uz7vhQQAhAC",10l);
            }
        });
        findViewById(R.id.open_dis_phb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.displayLeaderboard("CggI-uz7vhQQAhAC");
            }
        });

    }

    public static String postData(String urlStr, String data) {
        HttpRequestCore requestCore = new HttpRequestCore();
        requestCore.setPostByteData(data.getBytes());
        requestCore.setRequestUrl(urlStr);
        Map<String,String> m = requestCore.getHeaderMap();
        m.put("Host","ep2.ite2.com.tw");
        m.put("Content-Type","text/xml; charset=utf-8");
        m.put("SOAPAction","http://tempuri.org/SendSMS");
        return requestCore.doPost().getResult();
    }


    @Override
    protected void onResume() {
        super.onResume();
        PL.i("activity onResume");
        iStarpy.onResume(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iStarpy.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iStarpy.onPause(this);
        PL.i("activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PL.i("activity onStop");
        iStarpy.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PL.i("activity onDestroy");
        iStarpy.onDestroy(this);
    }


    /**
     *
     * 6.0以上系统权限授权回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PL.i("activity onRequestPermissionsResult");
        iStarpy.onRequestPermissionsResult(this,requestCode,permissions,grantResults);

        if (PermissionUtil.verifyPermissions(grantResults)){
            //同意授权，继续游戏
            Log.i("Starpy","user agaree permission");
        }else {
            //玩家拒绝
            Log.i("Starpy","user refuser permission");
            refuserPermissionTips(this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        iStarpy.onWindowFocusChanged(this,hasFocus);
    }

    private void refuserPermissionTips(final Activity activity){
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage("應用需要讀寫權限才能進行更新和數據存儲，不授權無法進行遊戲，是否開啟讀寫權限?")
                .setNegativeButton("開啟權限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Starpy","requestPermissions_STORAGE");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Log.i("Starpy","shouldShowRequestPermissionRationale true");
                            //如果没勾选“不再询问”，向用户发起权限请求
                            PermissionUtil.requestPermissions_STORAGE(activity,1001);
                        }else {
                            Log.i("Starpy","shouldShowRequestPermissionRationale false");

                            //之前点击了“不再询问”，无法再次弹出权限申请框。引导去开启相应权限

                            // 去应用信息
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                localIntent.setAction(Intent.ACTION_VIEW);
                                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
                            }
                            startActivity(localIntent);

                        }
                    }
                })
                .setPositiveButton("退出遊戲", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出遊戲
                        Log.i("Starpy","exit the game");
                    }
                }).create();
        alertDialog.show();
    }


    public void testOBBCanRead(){
        File obb = ObbUtil.getObbFile(this);
        if (!obb.exists()){
            PL.i("obb file not exists");
            return;
        }
        boolean open_failed = false;
        PL.i("Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT);
        try {
            BufferedReader br = new BufferedReader(new FileReader(obb));
            open_failed = false;
//            ReadObbFile(br);
            ZipUtil.upZipFile(obb,getFilesDir() + "/kdka/");
            FileUtil.copyFile(obb.getAbsolutePath(),getExternalCacheDir() + "/testobbcanread.obb");
            PL.i("ReadObbFile OK");
        } catch (IOException e) {
            open_failed = true;
            PL.i("ReadObbFile NOT OK");
            PL.i("ReadObbFile error msg -- " + e.getMessage());
        }

        if (open_failed) {
            // request READ_EXTERNAL_STORAGE permission before reading OBB file
//            ReadObbFileWithPermission();
            PL.i("ReadObbFile NEED READ_EXTERNAL_STORAGE permission");
        }
    }
}
