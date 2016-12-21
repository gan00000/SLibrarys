package com.efun.googlepay.bean;

import java.io.Serializable;

public class EfunQueryInventoryState implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static final int SERVER_TIME_OUT = 20000;
	public static final int SEND_STONE_FAIL = 20001;
	
	
	private int queryFailState;

	public int getQueryFailState() {
		return queryFailState;
	}

	public void setQueryFailState(int queryFailState) {
		this.queryFailState = queryFailState;
	}
}
