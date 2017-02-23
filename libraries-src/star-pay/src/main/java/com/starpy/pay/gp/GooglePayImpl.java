package com.starpy.pay.gp;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.core.base.callback.ISReqCallBack;
import com.core.base.utils.PL;
import com.starpy.base.utils.SLog;
import com.starpy.pay.IPay;
import com.starpy.pay.gp.bean.req.BasePayReqBean;
import com.starpy.pay.gp.bean.req.GoogleExchangeReqBean;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;
import com.starpy.pay.gp.bean.res.CreateOrderIdRes;
import com.starpy.pay.gp.constants.GooglePayContant;
import com.starpy.pay.gp.constants.GooglePayDomainSite;
import com.starpy.pay.gp.task.EndFlag;
import com.starpy.pay.gp.task.GoogleCreateOrderReqTask;
import com.starpy.pay.gp.task.GoogleExchangeReqTask;
import com.starpy.pay.gp.task.LoadingDialog;
import com.starpy.pay.gp.util.IabHelper;
import com.starpy.pay.gp.util.IabResult;
import com.starpy.pay.gp.util.Inventory;
import com.starpy.pay.gp.util.PayHelper;
import com.starpy.pay.gp.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Efun on 2017/2/23.
 */

public class GooglePayImpl implements IPay {

    // The helper object
    private IabHelper mHelper;

    private LoadingDialog loadingDialog;

    private GooglePayCreateOrderIdReqBean createOrderIdReqBean;

    private Activity activity;

    @Override
    public void startPay(Activity activity, BasePayReqBean basePayReqBean) {

        this.createOrderIdReqBean = null;

        if (activity == null) {
            PL.w("activity is null");
            return;
        }

        if (basePayReqBean == null) {
            PL.w("basePayReqBean is null");
            return;
        }

        this.activity = activity;

        this.createOrderIdReqBean = (GooglePayCreateOrderIdReqBean) basePayReqBean;
        this.createOrderIdReqBean.setRequestUrl(PayHelper.getPreferredUrl(activity));
        this.createOrderIdReqBean.setRequestMethod(GooglePayDomainSite.google_order_create);

        loadingDialog = new LoadingDialog(activity);

        if (mHelper == null) {
            mHelper = new IabHelper(activity);
        }else if (mHelper.isAsyncInProgress()){
            onDestroy(activity);
            mHelper = new IabHelper(activity);
        }


        googlePaySetUp();
    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        handlerActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        if (mHelper != null) {
            SLog.logI("mHelper.dispose");
            mHelper.dispose();
        }
        mHelper = null;

    }


    /**
     * <p>Title: googlePaySetUp</p> <p>Description: 启动远程服务</p>
     */
    private void googlePaySetUp() {

        if (null == mHelper) {
            return;
        }
        loadingDialog.showProgressDialog();
        if (!mHelper.isSetupDone()) {
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    SLog.logI("startSetup onIabSetupFinished.");
                    if (!result.isSuccess()) {
                        if (loadingDialog != null) {
                            loadingDialog.dismissProgressDialog();
                        }
                        //showGoogleStoreErrorMessage();
                        return;
                    }
                    mHelper.queryInventoryAsync(queryInventoryFinishedListener);

                }

                @Override
                public void onError(String message) {//发生错误时提示
                    SLog.logI("message:" + message);
                    if (loadingDialog != null) {
                        loadingDialog.dismissProgressDialog();
                    }
                }
            });
        } else {
            mHelper.queryInventoryAsync(queryInventoryFinishedListener);

        }
    }


    private void handlerActivityResult(int requestCode, int resultCode, Intent data) {
//        loadingDialog.dismissProgressDialog();
        if (mHelper == null) {
            return;
        }
        if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            SLog.logI("onActivityResult handled by IABUtil. the result was related to a purchase flow and was handled");
        } else {
            SLog.logI("onActivityResult handled by IABUtil.the result was not related to a purchase");
        }
    }

    /**
     * <p>Title: startPurchase</p> <p>Description: 开始购买商品（储值）</p>
     */
    protected synchronized void startPurchase() {

        GoogleCreateOrderReqTask googleCreateOrderReqTask = new GoogleCreateOrderReqTask(createOrderIdReqBean);
        googleCreateOrderReqTask.setReqCallBack(new ISReqCallBack<CreateOrderIdRes>() {

            @Override
            public void success(CreateOrderIdRes createOrderIdRes, String rawResult) {
                if (createOrderIdRes != null && createOrderIdRes.isRequestSuccess() && !TextUtils.isEmpty(createOrderIdRes.getOrderId())) {
                    launchPurchase(createOrderIdRes);
                } else {
                    if (loadingDialog != null) {
                        loadingDialog.dismissProgressDialog();
                    }
                }
            }

            @Override
            public void timeout(String code) {
                if (loadingDialog != null) {
                    loadingDialog.dismissProgressDialog();
                }
            }

            @Override
            public void noData() {
                if (loadingDialog != null) {
                    loadingDialog.dismissProgressDialog();
                }
            }
        });
        googleCreateOrderReqTask.excute(CreateOrderIdRes.class);

    }


    private void launchPurchase(CreateOrderIdRes createOrderIdRes) {

        JSONObject mjson = new JSONObject();
        try {
            mjson.put("orderId", createOrderIdRes.getOrderId());
            mjson.put("paygpid", createOrderIdRes.getPaygpid());

            mjson.put("cpOrderId", createOrderIdReqBean.getCpOrderId());
            mjson.put("userId", createOrderIdReqBean.getUserId());
            mjson.put("gameCode", createOrderIdReqBean.getGameCode());
//			mjson.put("productId", createOrderIdReqBean.getProductId());
            mjson.put("serverCode", createOrderIdReqBean.getServerCode());
            mjson.put("roleId", createOrderIdReqBean.getRoleId());

        } catch (JSONException e) {
            SLog.logI("JSONException异常");
            e.printStackTrace();
        }
        String developerPayload = mjson.toString();
        if (developerPayload.length() > 256) {
            PL.i("developerPayload.length() > 256");
        }

        SLog.logI("developerPayload: " + developerPayload + " developerPayload length:" + developerPayload.length());
        SLog.logI("开始google购买流程launchPurchaseFlow");
        //developerPayload: optional argument to be sent back with the purchase information,最大256 characters.否则报错code:"IAB-DPTL"
        mHelper.launchPurchaseFlow(activity, createOrderIdReqBean.getProductId(), GooglePayContant.RC_REQUEST,
                new IabHelper.OnIabPurchaseFinishedListener() {
                    public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {

                        SLog.logI("onIabPurchaseFinished");

                        if (purchase == null || result.isFailure()) {

                            if (loadingDialog != null) {
                                loadingDialog.dismissProgressDialog();
                            }
                            SLog.logI("purchase is null.");

                            if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
                                SLog.logI("info: " + result.getMessage());
                                return;
                            }
                            loadingDialog.complain(result.getMessage());
                            return;

                        } else {
                            GoogleExchangeReqBean exchangeReqBean = new GoogleExchangeReqBean(activity);
                            exchangeReqBean.setDataSignature(purchase.getSignature());
                            exchangeReqBean.setPurchaseData(purchase.getOriginalJson());

                            exchangeReqBean.setRequestUrl(PayHelper.getPreferredUrl(activity));
                            exchangeReqBean.setRequestMethod(GooglePayDomainSite.google_send);

                            /*SkuDetails skuDetails = activity.getSkuDetails();
                            if (skuDetails != null) {
                                exchangeReqBean.setPriceCurrencyCode(skuDetails.getPrice_currency_code());
                                exchangeReqBean.setPriceAmountMicros(skuDetails.getPrice_amount_micros());
                                exchangeReqBean.setProductPrice(skuDetails.getPrice());
                            }*/

                            GoogleExchangeReqTask googleExchangeReqTask = new GoogleExchangeReqTask(activity, exchangeReqBean);
                            googleExchangeReqTask.setReqCallBack(new ISReqCallBack() {
                                @Override
                                public void success(Object o, String rawResult) {

                                    PL.i("exchange callback");
                                    // 消费
                                    if (mHelper != null) {
                                        PL.i("google pay consumeAsync");
                                        mHelper.consumeAsync(purchase, mlaunchPurchaseConsumeFinishedListener);
                                    } else {

                                        if (loadingDialog != null) {
                                            loadingDialog.dismissProgressDialog();
                                        }

                                    }
                                }

                                @Override
                                public void timeout(String code) {

                                    if (loadingDialog != null) {
                                        loadingDialog.dismissProgressDialog();
                                    }
                                }

                                @Override
                                public void noData() {
                                    if (loadingDialog != null) {
                                        loadingDialog.dismissProgressDialog();
                                    }
                                }
                            });
                            googleExchangeReqTask.excute();

                        }

					/*
                        //服务端订单验证失败（公密googleKey进行数据验证失败）
						if (result != null && result.getResponse() == IabHelper.IABHELPER_VERIFICATION_FAILED && null == result.getmEfunState()) {
							SLog.logI("本次购买失败: " + result.getMessage());
							payDialog.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							payDialog.complainCloseAct(act.getEfunPayError().getGoogleBuyFailError());
							return;
						}
						//请求验证订单的时候服务器超时或者返回结果失败
						if (null != result.getmEfunState() && GooglePayContant.IAB_STATE .equals(result.getmEfunState()) && null != result.getMessage()) {
							SLog.logI("msg : " + result.getMessage());
							payDialog.dismissProgressDialog();
							EndFlag.setEndFlag(true);
							payDialog.complainCloseAct(result.getMessage());
							return;
						}
						//购买成功开始消费
						if (result != null && result.isSuccess() && purchase.getPurchaseState() == 0) {
							// 消费
							mHelper.consumeAsync(purchase, mConsumeFinishedListener);
						} else {
							SLog.logI("本次购买失败...");
							EndFlag.setEndFlag(true);
							payDialog.dismissProgressDialog();
							if (act != null) {
								act.finish();
							}
						}

						*/
                    }
                }, developerPayload);

    }

    private IabHelper.OnConsumeFinishedListener mlaunchPurchaseConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                SLog.logI("消费成功");
            } else {
                SLog.logI("消费失败");
            }
            EndFlag.setEndFlag(true);
            if (loadingDialog != null) {
                loadingDialog.dismissProgressDialog();
            }
        }
    };


    private IabHelper.QueryInventoryFinishedListener queryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {

            handleQueryResult(result, inv);

        }
    };

    private void handleQueryResult(IabResult result, Inventory inventory) {

        if (result.isFailure()) {
            loadingDialog.dismissProgressDialog();
            PL.i("query result:" + result.getMessage());
            SLog.logD("getQueryInventoryState is null");
        } else {

            SLog.logD("Query inventory was successful.");
            List<Purchase> purchaseList = inventory.getAllPurchases();

            if (null == purchaseList || purchaseList.isEmpty()) {

                loadingDialog.dismissProgressDialog();
                SLog.logD("purchases is empty");

            } else {

                SLog.logD("purchases size: " + purchaseList.size());
                for (Purchase mPurchase : purchaseList) {
                    SLog.logI("purchases sku: " + mPurchase.getSku());
                    if (mPurchase.getPurchaseState() == 2) {//退款订单
                        PL.i("refunded:属于退款订单");
                    } else {

                        GoogleExchangeReqBean exchangeReqBean = new GoogleExchangeReqBean(activity);
                        exchangeReqBean.setDataSignature(mPurchase.getSignature());
                        exchangeReqBean.setPurchaseData(mPurchase.getOriginalJson());

                        exchangeReqBean.setRequestUrl(PayHelper.getPreferredUrl(activity));
                        exchangeReqBean.setRequestMethod(GooglePayDomainSite.google_send);

                        GoogleExchangeReqTask googleExchangeReqTask = new GoogleExchangeReqTask(activity, exchangeReqBean);
                        googleExchangeReqTask.setReqCallBack(new ISReqCallBack() {
                            @Override
                            public void success(Object o, String rawResult) {
                                PL.i("exchange callback");
                                // 消费
						/*if (mHelper != null) {
							PL.i("google pay consumeAsync");
							mHelper.consumeAsync(mPurchase, mConsumeFinishedListener);
						}*/
                            }

                            @Override
                            public void timeout(String code) {

                            }

                            @Override
                            public void noData() {

                            }
                        });
                        googleExchangeReqTask.excute();
                    }

                }

                if (purchaseList.size() == 1) {
                    SLog.logD("mConsumeFinishedListener. 消费一个");
                    mHelper.consumeAsync(purchaseList.get(0), mConsumeFinishedListener);
                } else if (purchaseList.size() > 1) {
                    SLog.logD("mConsumeMultiFinishedListener.消费多个");
                    mHelper.consumeAsync(purchaseList, mConsumeMultiFinishedListener);
                }
            }
        }
        startPurchase();
    }


    /**
     * 多个未消费
     */
    private IabHelper.OnConsumeMultiFinishedListener mConsumeMultiFinishedListener = new IabHelper.OnConsumeMultiFinishedListener() {

        @Override
        public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
            SLog.logD("Consume Multiple finished.");
            if (loadingDialog != null) {
                loadingDialog.dismissProgressDialog();
            }
            for (int i = 0; i < purchases.size(); i++) {
                if (results.get(i).isSuccess()) {
                    SLog.logD("sku: " + purchases.get(i).getSku() + " Consume finished success");
                } else {
                    SLog.logD("sku: " + purchases.get(i).getSku() + " Consume finished fail");
                    SLog.logD(purchases.get(i).getSku() + "consumption is not success, yet to be consumed.");
                }
            }
            SLog.logD("End consumption flow.");
            startPurchase();
        }
    };

    /**
     * 只有一个未消费
     */
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            SLog.logD("Consumption finished");
            if (null != purchase) {
                SLog.logD("Purchase: " + purchase.toString() + ", result: " + result);
            } else {
                SLog.logD("Purchase is null");
            }
            if (loadingDialog != null) {
                loadingDialog.dismissProgressDialog();
            }
            if (result.isSuccess()) {
                SLog.logD("Consumption successful.");
                if (purchase != null) {
                    SLog.logD("sku: " + purchase.getSku() + " Consume finished success");
                }
            } else {
                SLog.logD("consumption is not success, yet to be consumed.");
            }
            SLog.logD("End consumption flow.");

            startPurchase();
        }
    };

}
