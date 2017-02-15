package com.core.base.request;

import com.google.gson.Gson;

import android.app.Dialog;
import android.text.TextUtils;

import com.core.base.callback.ISReqCallBack;
import com.core.base.http.HttpRequest;
import com.core.base.http.HttpResponse;
import com.core.base.request.bean.BaseReqeustBean;
import com.core.base.request.bean.BaseResponseModel;
import com.core.base.utils.JsonUtil;
import com.core.base.utils.SStringUtil;

import java.net.HttpURLConnection;


public abstract class AbsHttpRequest implements ISRqeust {

    private HttpResponse coreHttpResponse;

    private Dialog loadDialog;

    private ISReqCallBack reqCallBack;

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
                    loadDialog.show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                BaseReqeustBean baseReqeustBean = onHttpRequest();
                if (baseReqeustBean == null) {
                    return "";
                }
                String rawResponse = doRequest(baseReqeustBean);

                //解析json数据
                if (!TextUtils.isEmpty(rawResponse) && classOfT != null && JsonUtil.isJson(rawResponse)) {
                    Gson gson = new Gson();
                    responseModule = gson.fromJson(rawResponse, classOfT);
                    if (responseModule != null && (responseModule instanceof BaseResponseModel)) {
                        ((BaseResponseModel) responseModule).setRawResponse(rawResponse);
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
            coreHttpResponse = httpRequest.postReuqest(baseReqeustBean.getCompleteUrl(), baseReqeustBean.fieldValueToMap());
            return coreHttpResponse.getResult();
        }
        return "";
    }

    public void setLoadDialog(Dialog loadDialog) {
        this.loadDialog = loadDialog;
    }
}
