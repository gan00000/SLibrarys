package com.starpy.googlepay;

import android.content.Intent;
import android.os.Bundle;

import com.core.base.utils.ApkInfoUtil;
import com.starpy.googlepay.bean.GooglePayReqBean;
import com.starpy.googlepay.callback.QueryItemListener;

public class GooglePayActivity extends BaseGooglePayActivity {

	public static final String GooglePayReqBean_Extra_Key = "GooglePayReqBean_Extra_Key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!ApkInfoUtil.isNetworkAvaiable(this)) {
			prompt.complainCloseAct("Network is not avaiable");
			return;
		}
		if (getGoogleOrderBean() != null) {

			startPurchaseWithoutDialog(getGoogleOrderBean().getProductId());
			googlePaySetUp();

		}
	}

	@Override
	protected GooglePayReqBean initGoogleOrderBean() {

		Intent intent = getIntent();
		if (intent != null){
			return  (GooglePayReqBean) intent.getSerializableExtra(GooglePayReqBean_Extra_Key);
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