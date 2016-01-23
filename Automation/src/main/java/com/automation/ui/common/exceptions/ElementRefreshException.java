package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class ElementRefreshException extends RuntimeException {
	/**
	 * Exception to use when refresh of an element does not occur within timeout
	 * 
	 * @param sError - Error Message
	 */
	public ElementRefreshException(String sError)
	{
		super(sError);
	}
}
