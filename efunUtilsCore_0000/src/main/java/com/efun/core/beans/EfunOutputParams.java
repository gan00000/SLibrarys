package com.efun.core.beans;

import java.io.Serializable;


/**
 * Class Description：通过回调，传给厂商的参数
 * @author Joe
 * @date 2013-3-16
 * @version 1.0
 */
public class EfunOutputParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
