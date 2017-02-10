package com.starpy.base.exception;

/**
 * Class Description：EfunException
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public class EfunException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EfunException() {
		super();
	}

	public EfunException(String message, Throwable cause) {
		super(message, cause);
	}

	public EfunException(String message) {
		super(message);
	}

	public EfunException(Throwable cause) {
		super(cause);
	}

}
