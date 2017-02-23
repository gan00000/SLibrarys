package com.starpy.pay.gp.task;

public class EndFlag {
	/**
	 * endFlag 标志该次购买流程是否完成
	 */
	private volatile static boolean endFlag = true;
	private volatile static boolean canPurchase = true;

	public static boolean isEndFlag() {
		return endFlag;
	}

	public static void setEndFlag(boolean endFlag) {
		EndFlag.endFlag = endFlag;
	}

	public static boolean isCanPurchase() {
		return canPurchase;
	}

	public static void setCanPurchase(boolean canPurchase) {
		EndFlag.canPurchase = canPurchase;
	}
	
}
