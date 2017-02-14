package com.core.base.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.core.base.callback.ISReqCallBack;
import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.request.bean.BaseResponseModel;
import com.core.base.utils.SStringUtil;

import java.net.HttpURLConnection;


public class CfgFileRequest implements ISRqeust {

    private HttpResponse coreHttpResponse;

    private Dialog loadDialog;

    private ISReqCallBack reqCallBack;

    private BaseReqeustBean baseReqeustBean;

    private Context context;

    public CfgFileRequest(Context context) {
        this.context = context;
    }

    public void setBaseReqeustBean(BaseReqeustBean baseReqeustBean) {
        this.baseReqeustBean = baseReqeustBean;
    }

    public void setReqCallBack(ISReqCallBack reqCallBack) {
        this.reqCallBack = reqCallBack;
    }

    @Override
    public void excute() {
        excute(BaseResponseModel.class);
    }

    @Override
    public <T> void excute(final Class<T> classOfT) {

        SRequestAsyncTask asyncTask = new SRequestAsyncTask() {

            T responseModule = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (loadDialog != null && !loadDialog.isShowing()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && loadDialog.getOwnerActivity() != null && !loadDialog.getOwnerActivity().isDestroyed()) {
                        loadDialog.show();
                    }else if (loadDialog.getOwnerActivity() != null && !loadDialog.getOwnerActivity().isFinishing()){
                        loadDialog.show();
                    }
                }

            }

            @Override
            protected String doInBackground(String... params) {

                if (baseReqeustBean == null){
                    return "";
                }

                String rawResponse = doRequest(baseReqeustBean);

                //解析json数据
                if (!TextUtils.isEmpty(rawResponse) && classOfT != null) {
                    try {
                        Gson gson = new Gson();
                        responseModule = gson.fromJson(rawResponse, classOfT);
                        if (responseModule != null && (responseModule instanceof BaseResponseModel)) {
                            ((BaseResponseModel) responseModule).setRawResponse(rawResponse);
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                }
                return rawResponse;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (loadDialog != null && loadDialog.isShowing()){
                    loadDialog.dismiss();
                }

                if (coreHttpResponse != null) {
                    if (coreHttpResponse.getHttpResponseCode() != HttpURLConnection.HTTP_OK) {
                        onTimeout(coreHttpResponse.getHttpResponseCode() + "");
                    } else if (TextUtils.isEmpty(result)) {
                        onNoData(coreHttpResponse.getRequestCompleteUrl());
                    } else {
                        onHttpSucceess(responseModule);
                        if (reqCallBack != null){
                            reqCallBack.callBack(responseModule,result);
                        }
                    }
                }

            }
        };

        asyncTask.asyncExcute();
    }

    /**
     * <p>Title: doRequest</p> <p>Description: 实际网络请求</p>
     */
    public String doRequest(BaseReqeustBean baseReqeustBean) {
        if (SStringUtil.isNotEmpty(baseReqeustBean.getCompleteUrl())) {
            HttpRequest httpRequest = new HttpRequest();
            coreHttpResponse = httpRequest.getReuqest(baseReqeustBean.getCompleteUrl());
            return coreHttpResponse.getResult();
        }
        return "";
    }

    @Override
    public <T> void onHttpSucceess(T responseModel) {

    }

    @Override
    public void onNoData(String result) {

    }

    @Override
    public void onTimeout(String result) {

    }

    @Override
    public BaseReqeustBean onHttpRequest() {
        return null;
    }

    public void setLoadDialog(Dialog loadDialog) {
        this.loadDialog = loadDialog;
    }
}
