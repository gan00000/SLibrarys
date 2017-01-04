package com.efun.core.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.efun.core.http.HttpRequest;
import com.efun.core.res.EfunResConfiguration;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

/**
 * band等上报储值结果到efun服务器端
 * 
 */
public class EfunReportBilling {
	
	public static void efunBandReport(final Context ctx,final String efunOrderId,final String bandUserKey,final String markType,
			final String osType,final String fromType,final String param1,final String param2){
		Log.i("efunLog", "开始上报储值结果2");
		Runnable run =new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("efunLog", "开始上报储值结果3");
				String address1=EfunResConfiguration.getEfunPayPreferredUrl(ctx);
				
				String url=address1+"band_inform.shtml";
				try{
					Log.i("efunLog","储值上报Looper验证"+ Looper.getMainLooper()+"_"+Looper.myLooper());
				}catch(Exception e){
					
				}
				try{
					/*List<NameValuePair> walletParamsPay=new ArrayList<NameValuePair>();
					
					walletParamsPay.add(new BasicNameValuePair("efunOrderId", efunOrderId));
					walletParamsPay.add(new BasicNameValuePair("user_key", bandUserKey));
					walletParamsPay.add(new BasicNameValuePair("market_type", markType));
					walletParamsPay.add(new BasicNameValuePair("os_type", osType));
					walletParamsPay.add(new BasicNameValuePair("from_to", fromType));
					if(param1!=null)
						walletParamsPay.add(new BasicNameValuePair("param1",param1));
					if(param2!=null)
						walletParamsPay.add(new BasicNameValuePair("param2",param2));
					Log.i("efunLog", url+"?efunOrderId="+efunOrderId+"&user_key="+bandUserKey+"&market_type="
					+markType+"&os_type="+osType+"&from_to="+fromType+"&param1="+param1+"&param2="+param2);*/
					
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("efunOrderId", efunOrderId);
					dataMap.put("user_key", bandUserKey);
					dataMap.put("market_type", markType);
					dataMap.put("os_type", osType);
					dataMap.put("from_to", fromType);
					if(param1!=null)
					dataMap.put("param1", param1);
					if(param2!=null)
					dataMap.put("param2", param2);
					
					HttpRequest.post(url, dataMap);
					//EfunHttpUtil.efunPostRequest(url, walletParamsPay);
				}catch(Exception e){
					e.printStackTrace();
				}
			}};
		addThread(run);
		
	}
	
	final static ExecutorService pool = Executors. newSingleThreadExecutor();
	public static synchronized void addThread(Runnable runnable){
		pool.execute(runnable);
	}
	
}
