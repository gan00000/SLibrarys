package com.efun.core.task.command.abstracts;

public abstract class EfunCommonCmd<T> extends EfunCommand {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public abstract T getResult();
}
