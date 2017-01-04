/**
 * 
 */
package com.efun.ads.base;


public interface AdsInterface {
	
	/**
	* <p>Title: adsStatistics</p>
	* <p>Description: efun广告S2S安装统计上报接口</p>
	* @return 返回调用S2S安装返回的请求响应
	*/
	public String adsStatistics();
	
	/**
	* <p>Title: installThirdplatStatistics</p>
	* <p>Description: 更多分享（微信、line、简讯）安装统计上报接口</p>
	* @return 返回调用（微信、line、简讯）安装统计上报接口的请求响应
	*/
	public String installThirdplatStatistics();
}
