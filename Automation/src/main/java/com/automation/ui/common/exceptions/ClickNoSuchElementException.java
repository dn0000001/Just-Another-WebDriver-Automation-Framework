package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class ClickNoSuchElementException extends RuntimeException {
	/**
	 * Exception to use when cannot find the element to click
	 * 
	 * @param sError - Error Message
	 */
	public ClickNoSuchElementException(String sError)
	{
		super(sError);
	}
}