package com.starpy.plugin;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.core.base.callback.ISReqCallBack;
import com.core.base.http.DownloadManager;
import com.core.base.request.SimpleHttpRequest;
import com.core.base.utils.ApkInstallUtil;
import com.core.base.utils.PL;
import com.core.base.utils.SStringUtil;
import com.core.base.utils.ToastUtils;
import com.starpy.base.bean.SSdkBaseRequestBean;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.plugin.bean.CheckPluginResultModel;
import com.starpy.sdk.utils.DialogUtil;

import okhttp3.Request;

public class PayPluginManger {

    private Activity activity;
    private Dialog dialog;
    private PluginCallBack pluginCallBack;

    public static final String PayPluginAction = "com.qqgame.PayPluginAction";

    public void setPluginCallBack(PluginCallBack pluginCallBack) {
        this.pluginCallBack = pluginCallBack;
    }

    public PayPluginManger(Activity activity) {
        this.activity = activity;
        dialog = DialogUtil.createLoadingDialog(activity,"loading");
    }

    public void checkPlugin(){

        dialog.show();
        SSdkBaseRequestBean sSdkBaseRequestBean = new SSdkBaseRequestBean(activity);
        sSdkBaseRequestBean.setRequestUrl(PayHelper.getPreferredUrl(activity));
        sSdkBaseRequestBean.setRequestSpaUrl(PayHelper.getSpareUrl(activity));
        sSdkBaseRequestBean.setRequestMethod("dynamic_swc");

        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest();
        simpleHttpRequest.setBaseReqeustBean(sSdkBaseRequestBean);

        simpleHttpRequest.setReqCallBack(new ISReqCallBack<CheckPluginResultModel>() {
            @Override
            public void success(CheckPluginResultModel pluginResultModel, String rawResult) {

                dialog.dismiss();

                if (pluginResultModel==null || !pluginResultModel.isRequestSuccess() || !pluginResultModel.isOpen()
                        || SStringUtil.isEmpty(pluginResultModel.getPluginPackageName())
                        || SStringUtil.isEmpty(pluginResultModel.getVersion())){

                    //关闭插件，使用包内充值
                    if (pluginCallBack != null){
                        pluginCallBack.payInapp("");
                    }
                    return;
                }

                // 获取packagemanager的实例
                PackageManager packageManager = activity.getPackageManager();
                try {
                    PackageInfo packInfo = packageManager.getPackageInfo(pluginResultModel.getPluginPackageName(), 0);
                    int versionCode = packInfo.versionCode;
                    PL.i("插件本地版本: " + versionCode);
                    if (String.valueOf(versionCode).equals(pluginResultModel.getVersion())){
                        //启动插件充值
                        if (pluginCallBack != null){
                            pluginCallBack.payInPlugin(pluginResultModel.getPluginPackageName());
                        }

                    }else {
                        PL.i("插件本地版本 " + versionCode + "，需要下载更新");
                        //下载更新
                        downLoadPlugin(pluginResultModel.getDownLoadUrl());
                    }

                } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
                    PL.i("插件 " + pluginResultModel.getPluginPackageName() + " 没有安装");
                    //下载更新
                    downLoadPlugin(pluginResultModel.getDownLoadUrl());
                }

            }

            @Override
            public void timeout(String code) {
                //关闭插件，使用包内充值
                dialog.dismiss();
                if (pluginCallBack != null){
                    pluginCallBack.payInapp("");
                }
            }

            @Override
            public void noData() {
                //关闭插件，使用包内充值
                dialog.dismiss();
                if (pluginCallBack != null){
                    pluginCallBack.payInapp("");
                }
            }
        });

        simpleHttpRequest.excute(CheckPluginResultModel.class);
    }


    private void downLoadPlugin(String downloadurl){

        dialog.show();
//        ToastUtils.toast(activity,"正在為您下載安裝充值插件，安裝完成后請重新點擊即可充值",Toast.LENGTH_SHORT);
        DownloadManager.downloadFile(downloadurl, activity.getExternalCacheDir().getAbsolutePath(), new DownloadManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
//                ToastUtils.toast(activity,"download error");
                dialog.dismiss();
                //关闭插件，使用包内充值
                if (pluginCallBack != null){
                    pluginCallBack.payInapp("");
                }
            }

            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (SStringUtil.isNotEmpty(response) && response.endsWith(".apk")) {
                    ToastUtils.toast(activity,"請安裝充值插件後再進行充值");
                    ApkInstallUtil.installApk(activity,response);
                }else {
//                    ToastUtils.toast(activity,"download error");
                    //关闭插件，使用包内充值
                    if (pluginCallBack != null){
                        pluginCallBack.payInapp("");
                    }
                }

            }

            @Override
            public void onProgress(double total, double current) {

            }
        });
    }

}
