package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class ElementNotEnabledException extends RuntimeException {
	/**
	 * Exception to use when an element (field/drop down) is not enabled
	 * 
	 * @param sError - Error Message
	 */
	public ElementNotEnabledException(String sError)
	{
		super(sError);
	}
}