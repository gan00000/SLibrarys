package com.starpy.googlepay;

import java.util.List;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.base.utils.SLogUtil;
import com.starpy.googlepay.bean.WebOrderBean;
import com.starpy.googlepay.efuntask.EfunPayUtil;

import android.content.Intent;
import android.os.Bundle;

public abstract class BaseBillActivity extends BasePayActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		List<String> mSkus = initSku();
		if (!ApkInfoUtil.isNetworkAvaiable(this)) {
			payPrompt.complainCloseAct("Network is not avaiable");
			return;
		}
		/*if(!IabHelper.isSupported()){
			payPrompt.complainCloseAct("initialize Google play server error!");
			return;
		}*/
		//启动Google 内购服务
		EfunGooglePay.setUpGooglePayByBasePayActivity(this);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SLogUtil.logI("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		handlerActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}


	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// very important:
		if (null != this.payPrompt) {
			payPrompt.dismissProgressDialog();
		}
		processPayCallBack();
	}

	/**
	* <p>Title: startWebClient</p>
	* <p>Description: 启动更多储值支付页面</p>
	*/
	protected void startWebClient() {
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startOtherWallet(this, webOrderBean, "");
		this.finish();
	}
	
	
	
	protected void startWebGW(){
		/*Intent GWPayIntent = new Intent(GooglePayContant.BillAction.EFUN_PAY_ACTIVITY_GW + _gameCode);
		startGW(GWPayIntent);*/
		WebOrderBean webOrderBean = this.initWebOrderBean();
		if (null == webOrderBean) {
			throw new RuntimeException("webOrderBean is null");
		}
		EfunPayUtil.startGWWallet(this, webOrderBean, "");
		this.finish();
	}
	
	
	@Override
	protected List<String> initSku() {
		// TODO Auto-generated method stub
		return null;
	}

}
