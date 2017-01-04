package com.efun.platform.login.comm.bean;

public class SwitchParameters{
	
	/**
	 * 游戏链接
	 */
	private SwitchApplicationBean switchApplicationBean;
	/**
	 * 邀请开关
	 */
	private SwitchInviteBean switchInviteBean;
	/**
	 * 登录方式控制
	 */
	private SwitchLoginBean switchLoginBean;
	/**
	 * 账号管理
	 */
	private SwitchManagementBean switchManagementBean;
	/**
	 * Line分享
	 */
	private SwitchShareBean lineShareBean;
	/**
	 * Twitter分享
	 */
	private SwitchShareBean twitterShareBean;
	/**
	 * kakao 分享
	 */
	private SwitchShareBean kakaoShareBean;
	/**
	 * vk分享
	 */
	private SwitchShareBean vkShareBean;
	/**
	 * 联运账号转移
	 */
	private SwitchTransferBean switchTransferBean;
	/**
	 * 游戏版本管理（平台）
	 */
	private SwitchCheckversoinBean switchCheckversoinBean;
	/**
	 * 系统公告
	 */
	private SwitchNoticeBean switchNoticeBean;
	/**
	 * 钱包插件
	 */
	private SwitchPlugBean switchPlugBean;
	/**
	 * 韩国 个人中心 客服
	 */
	private SwitchKRplatformBean switchServiceBean;
	/**
	 * 韩国 个人中心 社群
	 */
	private SwitchKRplatformBean switchSocialBean;
	/**
	 * 韩国 个人中心 论坛
	 */
	private SwitchKRplatformBean switchCafeBean;
	
	/**
	 * 元数据
	 */
	private String response;
	/**
	 * 请求链接
	 */
	private String requestCompleteUrl;
	private String code;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getRequestCompleteUrl() {
		return requestCompleteUrl;
	}
	public void setRequestCompleteUrl(String requestCompleteUrl) {
		this.requestCompleteUrl = requestCompleteUrl;
	}
	public SwitchApplicationBean getSwitchApplicationBean() {
		return switchApplicationBean;
	}
	public void setSwitchApplicationBean(SwitchApplicationBean switchApplicationBean) {
		this.switchApplicationBean = switchApplicationBean;
	}
	public SwitchInviteBean getSwitchInviteBean() {
		return switchInviteBean;
	}
	public void setSwitchInviteBean(SwitchInviteBean switchInviteBean) {
		this.switchInviteBean = switchInviteBean;
	}
	public SwitchLoginBean getSwitchLoginBean() {
		return switchLoginBean;
	}
	public void setSwitchLoginBean(SwitchLoginBean switchLoginBean) {
		this.switchLoginBean = switchLoginBean;
	}
	public SwitchManagementBean getSwitchManagementBean() {
		return switchManagementBean;
	}
	public void setSwitchManagementBean(SwitchManagementBean switchManagementBean) {
		this.switchManagementBean = switchManagementBean;
	}
	public SwitchShareBean getLineShareBean() {
		return lineShareBean;
	}
	public void setLineShareBean(SwitchShareBean lineShareBean) {
		this.lineShareBean = lineShareBean;
	}
	public SwitchShareBean getTwitterShareBean() {
		return twitterShareBean;
	}
	public void setTwitterShareBean(SwitchShareBean twitterShareBean) {
		this.twitterShareBean = twitterShareBean;
	}
	public SwitchShareBean getKakaoShareBean() {
		return kakaoShareBean;
	}
	public void setKakaoShareBean(SwitchShareBean kakaoShareBean) {
		this.kakaoShareBean = kakaoShareBean;
	}
	public SwitchShareBean getVkShareBean() {
		return vkShareBean;
	}
	public void setVkShareBean(SwitchShareBean vkShareBean) {
		this.vkShareBean = vkShareBean;
	}
	public SwitchTransferBean getSwitchTransferBean() {
		return switchTransferBean;
	}
	public void setSwitchTransferBean(SwitchTransferBean switchTransferBean) {
		this.switchTransferBean = switchTransferBean;
	}
	public SwitchCheckversoinBean getSwitchCheckversoinBean() {
		return switchCheckversoinBean;
	}
	public void setSwitchCheckversoinBean(SwitchCheckversoinBean switchCheckversoinBean) {
		this.switchCheckversoinBean = switchCheckversoinBean;
	}
	public SwitchNoticeBean getSwitchNoticeBean() {
		return switchNoticeBean;
	}
	public void setSwitchNoticeBean(SwitchNoticeBean switchNoticeBean) {
		this.switchNoticeBean = switchNoticeBean;
	}
	public SwitchPlugBean getSwitchPlugBean() {
		return switchPlugBean;
	}
	public void setSwitchPlugBean(SwitchPlugBean switchPlugBean) {
		this.switchPlugBean = switchPlugBean;
	}
	public SwitchKRplatformBean getSwitchServiceBean() {
		return switchServiceBean;
	}
	public void setSwitchServiceBean(SwitchKRplatformBean switchServiceBean) {
		this.switchServiceBean = switchServiceBean;
	}
	public SwitchKRplatformBean getSwitchSocialBean() {
		return switchSocialBean;
	}
	public void setSwitchSocialBean(SwitchKRplatformBean switchSocialBean) {
		this.switchSocialBean = switchSocialBean;
	}
	public SwitchKRplatformBean getSwitchCafeBean() {
		return switchCafeBean;
	}
	public void setSwitchCafeBean(SwitchKRplatformBean switchCafeBean) {
		this.switchCafeBean = switchCafeBean;
	}
	
	
	
}
