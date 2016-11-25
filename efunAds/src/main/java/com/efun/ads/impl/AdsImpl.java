package com.efun.ads.impl;

import com.efun.ads.base.BaseAds;
import com.efun.ads.util.EfunDomainSite;
import com.efun.ads.util.SPUtil;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunResourceUtil;
import com.efun.core.tools.EfunStringUtil;

public class AdsImpl extends BaseAds {

	
	@Override
	public String adsStatistics() {
		String mPreferredUrl = null;//首选域名
		String mSpareUrl = null;//备用域名
		String region = SPUtil.takeRegion(context, "");//Google分析区域标识
		
		if (EfunStringUtil.isNotEmpty(region) && null != context) {//根据Google分析获取区域
			//根据区域获取域名
			httpParams.setRegion(region);
			//mPreferredUrl = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunAdsPreferredUrl");
			//mSpareUrl = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunAdsSpareUrl");
			mPreferredUrl = EfunResConfiguration.efunGetConfigUrl(context,region.toLowerCase() + "_efunAdsPreferredUrl");
			mSpareUrl = EfunResConfiguration.efunGetConfigUrl(context,region.toLowerCase() + "_efunAdsSpareUrl");
			//根據區域獲取gamecode
			String gameCode = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunGameCode");
			httpParams.setGameCode(gameCode);
		}
		
		if (EfunStringUtil.isEmpty(mPreferredUrl) && null != httpParams) {//代码设置的域名
			mPreferredUrl = httpParams.getPreferredUrl();
			mSpareUrl = httpParams.getSpareUrl();
		}
		
		if (EfunStringUtil.isEmpty(mPreferredUrl) && null != context) {// 获取默认配置文件中的域名
			mPreferredUrl = EfunResConfiguration.getAdsPreferredUrl(this.context);
			mSpareUrl = EfunResConfiguration.getAdsSpareUrl(this.context);
		}
		
		//添加正式&备用域名的后缀
		if (EfunStringUtil.isNotEmpty(mPreferredUrl)) {
			if (EfunStringUtil.isEmpty(httpParams.getDomainSite())) {//判断有没有设置域名缀
				this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS;//默认是ads_installStatistics.shtml
			}else{
				this.preferredUrl = mPreferredUrl + httpParams.getDomainSite();
			}
		}
		if (EfunStringUtil.isNotEmpty(mSpareUrl)) {
			if (EfunStringUtil.isEmpty(httpParams.getDomainSite())) {
				this.spareUrl = mSpareUrl + EfunDomainSite.EFUN_ADS;
			}else{
				this.spareUrl = mSpareUrl + httpParams.getDomainSite();
			}
		}
		
		//开始发送请求
		String resutl = this.executePost();
		return resutl;
	}

	@Override
	public String installThirdplatStatistics() {
		
	/*	String mPreferredUrl = null;
		String mSpareUrl = null;
		//读取代码设置的首选和备用域名
		if (null != httpParams) {
			mPreferredUrl = httpParams.getPreferredUrl();
			mSpareUrl = httpParams.getSpareUrl();
		}
		//如果代码没有设置首选域名，即读取配置文件下面的首选域名
		if (EfunStringUtil.isEmpty(mPreferredUrl) && null != context) {
			try {
				mPreferredUrl = EfunResourceUtil.findStringByName(context, "efunFbPreferredUrl");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果代码没有设置备用域名，即读取配置文件下面的备用域名
		if (EfunStringUtil.isEmpty(mSpareUrl) && null != context) {
			try {
				mSpareUrl = EfunResourceUtil.findStringByName(context, "efunFbSpareUrl");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//拼接域名后缀
		if (EfunStringUtil.isNotEmpty(mPreferredUrl)) {
			this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		if (EfunStringUtil.isNotEmpty(mSpareUrl)) {
			this.spareUrl = mSpareUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		//执行发送请求
		String resutl = this.executePost();
		return resutl;*/
		
		

		String mPreferredUrl = null;//首选域名
		String mSpareUrl = null;//备用域名
		String region = SPUtil.takeRegion(context, "");//Google分析区域标识
		
		if (EfunStringUtil.isNotEmpty(region) && null != context) {//根据Google分析获取区域
			//根据区域获取域名
			httpParams.setRegion(region);
//			mPreferredUrl = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunFbPreferredUrl");
//			mSpareUrl = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunFbSpareUrl");
			mPreferredUrl = EfunResConfiguration.efunGetConfigUrl(context,region.toLowerCase() + "_efunFbPreferredUrl");
			mSpareUrl = EfunResConfiguration.efunGetConfigUrl(context,region.toLowerCase() + "_efunFbSpareUrl");
			//根據區域獲取gamecode
			String gameCode = EfunResourceUtil.findStringByName(context,region.toLowerCase() + "_efunGameCode");
			httpParams.setGameCode(gameCode);
		}
		
		if (EfunStringUtil.isEmpty(mPreferredUrl) && null != httpParams) {//代码设置的域名
			mPreferredUrl = httpParams.getPreferredUrl();
			mSpareUrl = httpParams.getSpareUrl();
		}
		
		if (EfunStringUtil.isEmpty(mPreferredUrl) && null != context) {// 获取默认配置文件中的域名
			try {
//				mPreferredUrl = EfunResourceUtil.findStringByName(context, "efunFbPreferredUrl");
//				mSpareUrl = EfunResourceUtil.findStringByName(context, "efunFbSpareUrl");
				mPreferredUrl = EfunResConfiguration.getFBPreferredUrl(context);
				mSpareUrl = EfunResConfiguration.getFBSpareUrl(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 添加正式&备用域名的后缀
		if (EfunStringUtil.isNotEmpty(mPreferredUrl)) {
			this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		if (EfunStringUtil.isNotEmpty(mSpareUrl)) {
			this.spareUrl = mSpareUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		
		//开始发送请求
		String resutl = this.executePost();
		return resutl;
	
	}
	

	
}
