package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class ElementNotDisplayedException extends RuntimeException {
	/**
	 * Exception to use when an element (field/drop down) is not displayed
	 * 
	 * @param sError - Error Message
	 */
	public ElementNotDisplayedException(String sError)
	{
		super(sError);
	}
}