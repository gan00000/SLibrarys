/*package com.efun.platform.login.comm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.efun.core.exception.EfunException;
import com.efun.core.http.EfunHttpUtil;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunStringUtil;
import com.efun.platform.login.comm.bean.ListenerParameters;
import com.efun.platform.login.comm.constant.DomainSuffix;
import com.efun.platform.login.comm.constant.HttpParamsKey;
import com.efun.platform.login.comm.dao.UserDao;
import com.efun.platform.login.comm.utils.LoinStringUtil;

@Deprecated
public class UserDaoImpl implements UserDao{
	
	private static boolean validate(String loginName, String password) {
		if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(password))
			return false;
		return true;
	}
	
	@Override
    public String efunLogin(ListenerParameters login,String url){
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
        if (!validate(login.getUserName(), login.getPassword())){
        	EfunLogUtil.logI( "Method efunLogin params userName or userPwd is null");
        	return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
        }
        Long currentTime = System.currentTimeMillis();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.mac, login.getMac()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.imei, login.getImei()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.androidid, login.getAndroidId()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.advertiser, login.getAdvertisersName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.partner, login.getPartner()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.referrer, login.getReferrer()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginName, login.getUserName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginPwd, login.getPassword()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		postParams.add(new BasicNameValuePair(HttpParamsKey.gameCode, login.getGameCode()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.systemVersion, login.getSystemVersion()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.deviceType, login.getDeviceType()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.signature, EfunStringUtil.toMd5(
													login.getAppKey() + 
													currentTime + 
													login.getUserName() + 
													login.getPassword() + 
													login.getGameCode(), false)));
		EfunLogUtil.logI( url + DomainSuffix.URL_LOGIN_CHECK + postParams.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_LOGIN_CHECK, postParams);
    }
    
	@Override
	public String efunRegister(ListenerParameters login,String url) {
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		
		if (!validate(login.getUserName(), login.getPassword())){
			EfunLogUtil.logI( "Method efunRegister params userName or userPwd is null");
			return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
		}
		Long currentTime = System.currentTimeMillis();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginName, login.getUserName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginPwd, login.getPassword()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.email, login.getEmail()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.gameCode, login.getGameCode()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.advertiser, login.getAdvertisersName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.mac, login.getMac()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.imei, login.getImei()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.androidid, login.getAndroidId()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.ip, login.getIp()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.systemVersion, login.getSystemVersion()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.deviceType, login.getDeviceType()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.params, login.getPartner()+","+login.getGameCode()+"," + login.getPlatForm()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		postParams.add(new BasicNameValuePair(HttpParamsKey.referrer, login.getReferrer()));
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + currentTime + login.getUserName() + login.getPassword() +
				login.getEmail() + login.getGameCode() + login.getAdvertisersName()+ login.getPartner()+","+ login.getGameCode() + "," + login.getPlatForm(), false)));
		EfunLogUtil.logI(url + DomainSuffix.URL_LOGIN_REGISTER + postParams.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_LOGIN_REGISTER, postParams);
	}

	@Override
	public String efunChangePwd(ListenerParameters login, String url) throws EfunException{
		
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		
		if (!validate(login.getUserName(), login.getPassword()) || TextUtils.isEmpty(login.getNewPassword())){
			EfunLogUtil.logI( "Method efunChangePwd params userName is null");
			return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
		}
		Long currentTime = System.currentTimeMillis();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginName, login.getUserName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginPwd, login.getPassword()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.newPwd, login.getNewPassword()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.gameCode, login.getGameCode()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		postParams.add(new BasicNameValuePair(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + currentTime + login.getUserName() + login.getPassword() + login.getNewPassword() + login.getGameCode(), false)));
		EfunLogUtil.logI( url + DomainSuffix.URL_CHANGE_PWD + postParams.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_CHANGE_PWD, postParams);
	}
	
	@Override
	public String efunForgetPwdByEmail(ListenerParameters login,String url) throws EfunException{
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		
		if(!validate(login.getUserName() , login.getEmail())){
    		EfunLogUtil.logI( "Method efunForgetPwdByEmail params userName is null");
    		return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
    	}
		Long currentTime = System.currentTimeMillis();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(HttpParamsKey.loginName, login.getUserName()));
		params.add(new BasicNameValuePair(HttpParamsKey.email, login.getEmail()));
		params.add(new BasicNameValuePair(HttpParamsKey.gameCode, login.getGameCode()));
		params.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		params.add(new BasicNameValuePair(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + currentTime + login.getUserName() + login.getEmail()+ login.getGameCode(), false)));
		EfunLogUtil.logI( url + DomainSuffix.URL_FINDPWD_EMAIL + params.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_FINDPWD_EMAIL, params);
	}
	
	@Override
	public String efunThirdPlatLoginOrReg(ListenerParameters login,String url) throws EfunException{
		
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		
		if (TextUtils.isEmpty(login.getThirdPlateId())){
			EfunLogUtil.logI( "Method efunThirdPlatLoginOrReg params userName is null");
			return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
		}
		Long currentTime = System.currentTimeMillis();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginid, login.getThirdPlateId()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.advertiser, login.getAdvertisersName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.mac, login.getMac()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.imei, login.getImei()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.androidid, login.getAndroidId()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.ip, login.getIp()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.systemVersion, login.getSystemVersion()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.deviceType, login.getDeviceType()));
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.referrer, login.getReferrer()));
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.params, login.getGameCode()+","+login.getPartner()+","
				+login.getGameShortName()+","+login.getThirdPlate()+","+login.getPlatForm()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + currentTime + 
						login.getThirdPlateId() + login.getAdvertisersName() + login.getGameCode()+","+login.getPartner()+","
						+login.getGameShortName()+","+login.getThirdPlate()+","+login.getPlatForm(), false)));
		EfunLogUtil.logI( url + DomainSuffix.URL_THIRD_PART + postParams.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_THIRD_PART, postParams);
	}

	@Override
	public String efunBind(ListenerParameters login,String url){
		
		if (LoinStringUtil.isEmpty(url)) {
			throw new RuntimeException("请先配置接口地址url");
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		
		if (!validate(login.getUserName(), login.getPassword())){
			EfunLogUtil.logI( "Method efunBind params userName or userPwd is null");
			return "{\"message\":\"傳遞參數異常\",\"code\":\"params_error\"}";
		}
		String _params = login.getGameShortName()+","+login.getThirdPlate()+"," + login.getPlatForm();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		Long currentTime = System.currentTimeMillis();
		postParams.add(new BasicNameValuePair(HttpParamsKey.timestamp, currentTime+""));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginid, login.getThirdPlateId()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginName, login.getUserName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.loginPwd, login.getPassword()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.gameCode, login.getGameCode()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.email, login.getEmail()));
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.advertiser, login.getAdvertisersName()));
		postParams.add(new BasicNameValuePair(HttpParamsKey.partner, login.getPartner()));
		
		postParams.add(new BasicNameValuePair(HttpParamsKey.params, _params));
		postParams.add(new BasicNameValuePair(HttpParamsKey.signature, 
				EfunStringUtil.toMd5(login.getAppKey() + currentTime + login.getThirdPlateId() + login.getUserName() +
						login.getPassword()+ login.getGameCode() +_params, false)));
		EfunLogUtil.logI( url + DomainSuffix.URL_BIND + postParams.toString());
		EfunLogUtil.logI( "AppKey:" + login.getAppKey());
		return EfunHttpUtil.efunPostRequest(url + DomainSuffix.URL_BIND, postParams);
	}
}
*/