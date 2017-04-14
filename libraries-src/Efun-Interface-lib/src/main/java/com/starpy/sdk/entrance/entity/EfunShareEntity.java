package com.starpy.sdk.entrance.entity;

import android.graphics.Bitmap;

import com.starpy.sdk.entrance.constant.EfunShareType;

public final class EfunShareEntity extends EfunBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EfunShareType shareType;
	
	private String shareLinkName;
	private String shareLinkUrl;
	private String shareDescrition;
	private String sharePictureUrl;
	private String shareCaption;

	private boolean isLocalPicture;
	private String shareLocalPictureUrl;

	private String shareDownloadTxt;
	
	private Bitmap localPictureBitmap;
	
	private Bitmap localPictureBitmaps[];
	
	
	/**
	 * @return the shareType
	 */
	public EfunShareType getShareType() {
		return shareType;
	}
	/**
	 * @param shareType the shareType to setScrollDuration
	 */
	public void setShareType(EfunShareType shareType) {
		this.shareType = shareType;
	}
	/**
	 * @return the shareLinkName
	 */
	public String getShareLinkName() {
		return shareLinkName;
	}
	/**
	 * @param shareLinkName the shareLinkName to setScrollDuration
	 */
	public void setShareLinkName(String shareLinkName) {
		this.shareLinkName = shareLinkName;
	}
	/**
	 * @return the shareLinkUrl
	 */
	public String getShareLinkUrl() {
		return shareLinkUrl;
	}
	/**
	 * @param shareLinkUrl the shareLinkUrl to setScrollDuration
	 */
	public void setShareLinkUrl(String shareLinkUrl) {
		this.shareLinkUrl = shareLinkUrl;
	}
	/**
	 * @return the shareDescrition
	 */
	public String getShareDescrition() {
		return shareDescrition;
	}
	/**
	 * @param shareDescrition the shareDescrition to setScrollDuration
	 */
	public void setShareDescrition(String shareDescrition) {
		this.shareDescrition = shareDescrition;
	}
	/**
	 * @return the sharePictureUrl
	 */
	public String getSharePictureUrl() {
		return sharePictureUrl;
	}
	/**
	 * @param sharePictureUrl the sharePictureUrl to setScrollDuration
	 */
	public void setSharePictureUrl(String sharePictureUrl) {
		this.sharePictureUrl = sharePictureUrl;
	}
	/**
	 * @return the shareCaption
	 */
	public String getShareCaption() {
		return shareCaption;
	}
	/**
	 * @param shareCaption the shareCaption to setScrollDuration
	 */
	public void setShareCaption(String shareCaption) {
		this.shareCaption = shareCaption;
	}
	/**
	 * @return the isLocalPicture
	 */
	public boolean isLocalPicture() {
		return isLocalPicture;
	}
	/**
	 * @param isLocalPicture the isLocalPicture to setScrollDuration
	 */
	public void setLocalPicture(boolean isLocalPicture) {
		this.isLocalPicture = isLocalPicture;
	}
	public String getShareLocalPictureUrl() {
		return shareLocalPictureUrl;
	}
	public void setShareLocalPictureUrl(String shareLocalPictureUrl) {
		this.shareLocalPictureUrl = shareLocalPictureUrl;
	}
	
	public Bitmap getLocalPictureBitmap() {
		return localPictureBitmap;
	}
	public void setLocalPictureBitmap(Bitmap localPictureBitmap) {
		this.localPictureBitmap = localPictureBitmap;
	}
	
	public Bitmap[] getLocalPictureBitmaps() {
		return localPictureBitmaps;
	}
	public void setLocalPictureBitmaps(Bitmap localPictureBitmaps[]) {
		this.localPictureBitmaps = localPictureBitmaps;
	}
	public String getShareDownloadTxt() {
		return shareDownloadTxt;
	}
	public void setShareDownloadTxt(String shareDownloadTxt) {
		this.shareDownloadTxt = shareDownloadTxt;
	}
	
}
