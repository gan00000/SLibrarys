package com.starpy.pay.gp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.pay.IPay;
import com.starpy.pay.IPayCallBack;
import com.starpy.pay.IPayFactory;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;

public class GooglePayActivity2 extends Activity {

	IPay iPay;
	GooglePayCreateOrderIdReqBean googlePayCreateOrderIdReqBean;

	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;

		iPay = IPayFactory.create(IPayFactory.PAY_GOOGLE);
		iPay.onCreate(this);

		iPay.setIPayCallBack(new IPayCallBack() {
			@Override
			public void success() {
				activity.finish();
			}

			@Override
			public void fail() {
				activity.finish();
			}
		});

		Intent intent = getIntent();
		if (intent != null){
			googlePayCreateOrderIdReqBean = (GooglePayCreateOrderIdReqBean) intent.getSerializableExtra(GooglePayActivity.GooglePayReqBean_Extra_Key);

			iPay.startPay(this,googlePayCreateOrderIdReqBean);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		iPay.onActivityResult(this,requestCode,resultCode,data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		iPay.onDestroy(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		iPay.onResume(this);
	}
}
