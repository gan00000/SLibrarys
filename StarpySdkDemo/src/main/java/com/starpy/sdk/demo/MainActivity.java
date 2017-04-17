package com.starpy.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.core.base.utils.FileUtil;
import com.core.base.utils.PL;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SPayType;
import com.starpy.base.utils.SLog;
import com.starpy.base.utils.StarPyUtil;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.sdk.out.ISdkCallBack;
import com.starpy.sdk.out.IStarpy;
import com.starpy.sdk.out.StarpyFactory;

public class MainActivity extends Activity {

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

        SLog.enableInfo(true);
        SLog.enableDebug(true);

        iStarpy = StarpyFactory.create();

        iStarpy.setGameLanguage(this, SGameLanguage.zh_TW);

        //初始化sdk
        iStarpy.initSDK(this);

        //在游戏Activity的onCreate生命周期中调用
        iStarpy.onCreate(this);

        //在游戏获得角色信息的时候调用
        iStarpy.registerRoleInfo(this, "roleid_1", "roleName", "rolelevel", "1", "serverName");

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

                        }
                    }
                });

                String s = StarPyUtil.encryptDyUrl(getApplicationContext(),FileUtil.readAssetsTxtFile(getApplicationContext(),"s_sdk_config.txt"));
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
                iStarpy.pay(MainActivity.this, SPayType.GOOGLE, "" + System.currentTimeMillis(), "py.brmmd.29.99", "roleLevel", "customize");

            }
        });


        csButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStarpy.cs(MainActivity.this,"level","vipLevel");
            }
        });



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //下面的参数请按照实际传值
                String title = "hello world";
                String message = "hello world message";
                String shareUrl = "http://www.starb168.com/brmmd_201703171125.html";
                String picUrl = "https://lh3.googleusercontent.com/mOgnBSExg8wbssGwPGj-rscvNEklCvV3mGVqXuViUqROUok0P6P3JTo6Hmho0LRXoC8=w300-rw";

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


}
