/*package com.efun.morepay.Paypal;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import com.efun.common.TaskRequest;
import com.efun.core.db.EfunDatabase;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.googlepay.constants.GooglePayContant;

*//**
* <p>Title: Paypal</p>
* <p>Description: paypal支付玩家客户端验证</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年9月25日
*//*
public class Paypal extends TaskRequest {
	
	public static final String PAYPAL_ORDER_ID_KEY = "PAYPAL_ORDER_ID";
	public static final String paypalUrlSuffix = "paypalCheck_checkPaypal.shtml";
	
	private Activity activity;
	
	public Paypal(Activity activity) {
		this.activity = activity;
	}

	*//**
	* <p>Title: paypalCheck</p>
	* <p>Description: 检查是否需要验证，是否有未验证订单</p>
	* @param userid
	* @param gameCode
	*//*
	public void paypalCheck(String userid, String gameCode){
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("gameCode", gameCode);
		final String efunLocalOrderId = EfunDatabase.getSimpleString(activity, GooglePayContant.EFUNFILENAME, PAYPAL_ORDER_ID_KEY);
		map.put("efunOrderId", efunLocalOrderId);
		this.setMap(map);
		if (EfunStringUtil.isEmpty(this.getPreferredUrl())) {
			this.setPreferredUrl(EfunResConfiguration.getEfunPayPreferredUrl(activity));
		}
		this.setUrlSuffix(paypalUrlSuffix);
		this.setRequestCallBack(new RequestCallBack() {
			
			@Override
			public void onRequestFinish(String result) {

				if (EfunStringUtil.isEmpty(result)) {
					return;
				}
				EfunLogUtil.logD("paypal result:" + result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String code = jsonObject.optString("result", "");
					String msg = jsonObject.optString("msg", "");
					final String efunOrderId = jsonObject.optString("efunOrderId", "");
					if ("0000".equals(code)) {
//						if (EfunStringUtil.isNotEmpty(efunOrderId)) {
//							EfunDatabase.saveSimpleInfo(activity, GooglePayContant.EFUNFILENAME, PAYPAL_ORDER_ID_KEY, efunOrderId);
//						}
						if (!efunLocalOrderId.equals(efunOrderId)) {
							showMessage(msg, efunOrderId);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onPreExecute() {
				
			}
		});
		this.asyncExecute();
	}

	*//**
	* <p>Title: showMessage</p>
	* <p>Description: 弹出提示，提示用户验证</p>
	* @param msg
	* @param efunOrderId
	*//*
	private void showMessage(String msg, final String efunOrderId) {
		
		if (EfunStringUtil.isNotEmpty(msg)) {

			AlertDialog.Builder ab = new Builder(activity);
			ab.setTitle("提示");
			ab.setMessage(msg);
			ab.setPositiveButton("不再提示", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					if (EfunStringUtil.isNotEmpty(efunOrderId)) {
						EfunDatabase.saveSimpleInfo(activity, GooglePayContant.EFUNFILENAME, PAYPAL_ORDER_ID_KEY, efunOrderId);
					}
				}
			});
			ab.setNeutralButton("確定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface, int paramInt) {
					
				}
			});
			ab.create().show();

		}
	}
}
*/