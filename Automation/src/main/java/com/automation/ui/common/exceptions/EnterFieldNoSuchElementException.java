package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class EnterFieldNoSuchElementException extends RuntimeException {
	/**
	 * Exception to use when cannot find the field element
	 * 
	 * @param sError - Error Message
	 */
	public EnterFieldNoSuchElementException(String sError)
	{
		super(sError);
	}
}
