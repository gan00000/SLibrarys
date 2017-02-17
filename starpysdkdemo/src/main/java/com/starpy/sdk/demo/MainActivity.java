package com.starpy.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.core.base.utils.SStringUtil;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.data.pay.PayType;
import com.starpy.sdk.out.IStarpy;
import com.starpy.sdk.out.StarpyFactory;

public class MainActivity extends Activity {

    private Button loginButton, payButton;

    private IStarpy iStarpy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.demo_login);
        payButton = (Button) findViewById(R.id.demo_pay);

        iStarpy = StarpyFactory.create();

        //初始化sdk
        iStarpy.initSDK(this);

        //在游戏Activity的onCreate生命周期中调用
        iStarpy.onCreate(this);

        //在游戏获得角色信息的时候调用
        iStarpy.registerRoleInfo(this, "roleid", "roleName", "role level", "serverCode", "serverName");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登陆接口，登陆成功的回调在onActivityResult
                iStarpy.login(MainActivity.this, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null){
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();
                            Log.i("IStarpy","uid:" + uid + "  accessToken:" + accessToken + "  yanz:" +  SStringUtil.toMd5("DF7D80A64433C90E263F146315E17A79" +
                                    uid + sLoginResponse.getGameCode() + timestamp));

                        }
                    }
                });
            }
        });


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
                充值接口
                cpOrderId cp订单号，请保持值唯一
                productId 充值的商品id
                roleLevel 觉得等级
                customize 自定义透传字段（从服务端回调到cp）
                */
                iStarpy.pay(MainActivity.this, PayType.GOOGLE, "cpOrderId", "productId", "roleLevel", "customize");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        iStarpy.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iStarpy.onDestroy(this);
    }
}
