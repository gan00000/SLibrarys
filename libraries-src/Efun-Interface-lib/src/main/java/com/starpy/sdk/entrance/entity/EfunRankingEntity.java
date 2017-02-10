package com.starpy.sdk.entrance.entity;

import com.starpy.sdk.entrance.constant.EfunRankingType;

public final class EfunRankingEntity extends EfunSocialBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 排行榜类型
	 */
	private EfunRankingType rankingType;
	
	private String rankingTemplateId;
	
	/**
	 * 排行榜的标识（根据什么来排行）
	 */
	private String rankingTag;

	/**
	 * @return the rankingType
	 */
	public EfunRankingType getRankingType() {
		return rankingType;
	}

	/**
	 * @param rankingType the rankingType to set
	 */
	public void setRankingType(EfunRankingType rankingType) {
		this.rankingType = rankingType;
	}

	/**
	 * @return the rankingTemplateId
	 */
	public String getRankingTemplateId() {
		return rankingTemplateId;
	}

	/**
	 * @param rankingTemplateId the rankingTemplateId to set
	 */
	public void setRankingTemplateId(String rankingTemplateId) {
		this.rankingTemplateId = rankingTemplateId;
	}

	public String getRankingTag() {
		return rankingTag;
	}

	public void setRankingTag(String rankingTag) {
		this.rankingTag = rankingTag;
	}
	
	
	
}
