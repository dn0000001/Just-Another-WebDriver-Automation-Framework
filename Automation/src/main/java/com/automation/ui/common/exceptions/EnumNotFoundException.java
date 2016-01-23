package com.automation.ui.common.exceptions;

@SuppressWarnings("serial")
public class EnumNotFoundException extends RuntimeException {
	/**
	 * Exception to use when an enumeration is missing from a HashMap
	 * 
	 * @param sError - Error Message
	 */
	public EnumNotFoundException(String sError)
	{
		super(sError);
	}
}