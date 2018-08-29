package com.starpy.sdk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.core.base.utils.PermissionUtil;

public class StarpyPermissionActivity extends SBaseSdkActivity {

    private boolean goToAppSetting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        1.启动app请求权限
        goToAppSetting = false;
        if (PermissionUtil.hasSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            continueGame();
        }else {

            PermissionUtil.requestPermissions_STORAGE(this,401);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (goToAppSetting){

//            2.假如进入了app设置界面授权，返回app的时候重新检查
            if (PermissionUtil.hasSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                continueGame();
            }else {
                refuserPermissionTips(this);
            }
        }

    }

    private void continueGame() {
        //跳转游戏Activity或者继续游戏
        // TODO: 2018/8/28

        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        3.权限全部进行了授权
        if (PermissionUtil.verifyPermissions(grantResults)){
            //跳转游戏Activity或者继续游戏
            continueGame();

        }else {
            refuserPermissionTips(this);
        }


    }


    private void refuserPermissionTips(final Activity activity){
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(R.string.py_permission_tips)
                .setNegativeButton(R.string.py_permission_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


//                        根据测试shouldShowRequestPermissionRationale的返回值主要以下几种情况 ：
//
//                        第一次打开App时	false
//                        上次弹出权限点击了禁止（但没有勾选“下次不在询问”）	true
//                        上次选择禁止并勾选：下次不在询问	false
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                            //如果没勾选“不再询问”，向用户发起权限请求
                            PermissionUtil.requestPermissions_STORAGE(activity,401);
                        }else {

                            //之前点击了“不再询问”，无法再次弹出权限申请框。引导去开启相应权限

                            // 跳去去应用信息

                            goToAppSetting = true;

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
                            dialog.dismiss();

                        }
                    }
                })
                .setPositiveButton(R.string.py_permission_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出遊戲，厂商按照自己的处理退出游戏

                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

}
