package com.starpy.ads.impl;

import com.core.base.res.SConfig;
import com.starpy.ads.base.BaseAds;
import com.starpy.ads.util.EfunDomainSite;
import com.starpy.ads.util.SPUtil;
import com.core.base.utils.ResUtil;
import com.core.base.utils.SStringUtil;

public class AdsImpl extends BaseAds {

	
	@Override
	public String adsStatistics() {
		String mPreferredUrl = null;//首选域名
		String mSpareUrl = null;//备用域名
		String region = SPUtil.takeRegion(context, "");//Google分析区域标识
		
		if (SStringUtil.isNotEmpty(region) && null != context) {//根据Google分析获取区域
			//根据区域获取域名
			httpParams.setRegion(region);
			//mPreferredUrl = ResUtil.findStringByName(context,region.toLowerCase() + "_efunAdsPreferredUrl");
			//mSpareUrl = ResUtil.findStringByName(context,region.toLowerCase() + "_efunAdsSpareUrl");
			mPreferredUrl = SConfig.efunGetConfigUrl(context,region.toLowerCase() + "_efunAdsPreferredUrl");
			mSpareUrl = SConfig.efunGetConfigUrl(context,region.toLowerCase() + "_efunAdsSpareUrl");
			//根據區域獲取gamecode
			String gameCode = ResUtil.findStringByName(context,region.toLowerCase() + "_efunGameCode");
			httpParams.setGameCode(gameCode);
		}
		
		if (SStringUtil.isEmpty(mPreferredUrl) && null != httpParams) {//代码设置的域名
			mPreferredUrl = httpParams.getPreferredUrl();
			mSpareUrl = httpParams.getSpareUrl();
		}
		
		if (SStringUtil.isEmpty(mPreferredUrl) && null != context) {// 获取默认配置文件中的域名
			mPreferredUrl = SConfig.getAdsPreferredUrl(this.context);
			mSpareUrl = SConfig.getAdsSpareUrl(this.context);
		}
		
		//添加正式&备用域名的后缀
		if (SStringUtil.isNotEmpty(mPreferredUrl)) {
			if (SStringUtil.isEmpty(httpParams.getDomainSite())) {//判断有没有设置域名缀
				this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS;//默认是ads_installStatistics.shtml
			}else{
				this.preferredUrl = mPreferredUrl + httpParams.getDomainSite();
			}
		}
		if (SStringUtil.isNotEmpty(mSpareUrl)) {
			if (SStringUtil.isEmpty(httpParams.getDomainSite())) {
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
		if (SStringUtil.isEmpty(mPreferredUrl) && null != context) {
			try {
				mPreferredUrl = ResUtil.findStringByName(context, "efunFbPreferredUrl");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果代码没有设置备用域名，即读取配置文件下面的备用域名
		if (SStringUtil.isEmpty(mSpareUrl) && null != context) {
			try {
				mSpareUrl = ResUtil.findStringByName(context, "efunFbSpareUrl");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//拼接域名后缀
		if (SStringUtil.isNotEmpty(mPreferredUrl)) {
			this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		if (SStringUtil.isNotEmpty(mSpareUrl)) {
			this.spareUrl = mSpareUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		//执行发送请求
		String resutl = this.executePost();
		return resutl;*/
		
		

		String mPreferredUrl = null;//首选域名
		String mSpareUrl = null;//备用域名
		String region = SPUtil.takeRegion(context, "");//Google分析区域标识
		
		if (SStringUtil.isNotEmpty(region) && null != context) {//根据Google分析获取区域
			//根据区域获取域名
			httpParams.setRegion(region);
//			mPreferredUrl = ResUtil.findStringByName(context,region.toLowerCase() + "_efunFbPreferredUrl");
//			mSpareUrl = ResUtil.findStringByName(context,region.toLowerCase() + "_efunFbSpareUrl");
			mPreferredUrl = SConfig.efunGetConfigUrl(context,region.toLowerCase() + "_efunFbPreferredUrl");
			mSpareUrl = SConfig.efunGetConfigUrl(context,region.toLowerCase() + "_efunFbSpareUrl");
			//根據區域獲取gamecode
			String gameCode = ResUtil.findStringByName(context,region.toLowerCase() + "_efunGameCode");
			httpParams.setGameCode(gameCode);
		}
		
		if (SStringUtil.isEmpty(mPreferredUrl) && null != httpParams) {//代码设置的域名
			mPreferredUrl = httpParams.getPreferredUrl();
			mSpareUrl = httpParams.getSpareUrl();
		}
		
		if (SStringUtil.isEmpty(mPreferredUrl) && null != context) {// 获取默认配置文件中的域名
			try {
//				mPreferredUrl = ResUtil.findStringByName(context, "efunFbPreferredUrl");
//				mSpareUrl = ResUtil.findStringByName(context, "efunFbSpareUrl");
				mPreferredUrl = SConfig.getFBPreferredUrl(context);
				mSpareUrl = SConfig.getFBSpareUrl(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 添加正式&备用域名的后缀
		if (SStringUtil.isNotEmpty(mPreferredUrl)) {
			this.preferredUrl = mPreferredUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		if (SStringUtil.isNotEmpty(mSpareUrl)) {
			this.spareUrl = mSpareUrl + EfunDomainSite.EFUN_ADS_THIRDPLAT;
		}
		
		//开始发送请求
		String resutl = this.executePost();
		return resutl;
	
	}
	

	
}
