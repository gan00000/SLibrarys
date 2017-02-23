package com.starpy.pay.gp;

import android.content.Intent;
import android.os.Bundle;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.pay.gp.bean.req.GooglePayCreateOrderIdReqBean;

public class GooglePayActivity extends BaseGooglePayActivity {

	public static final String GooglePayReqBean_Extra_Key = "GooglePayReqBean_Extra_Key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!ApkInfoUtil.isNetworkAvaiable(this)) {
			payDialog.complainCloseAct("Network is not avaiable");
			return;
		}
		if (getGoogleOrderBean() != null) {

			startPurchaseWithoutDialog(getGoogleOrderBean().getProductId());
			googlePaySetUp();

		}
	}

	@Override
	protected GooglePayCreateOrderIdReqBean initGoogleOrderBean() {

		Intent intent = getIntent();
		if (intent != null){
			return  (GooglePayCreateOrderIdReqBean) intent.getSerializableExtra(GooglePayReqBean_Extra_Key);
		}

		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
